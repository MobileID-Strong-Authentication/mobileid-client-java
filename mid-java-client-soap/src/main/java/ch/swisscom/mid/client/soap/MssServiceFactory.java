/*
 * Copyright 2021 Swisscom (Schweiz) AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.swisscom.mid.client.soap;

import com.sun.xml.ws.developer.JAXWSProperties;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.etsi.uri.ts102204.etsi204_kiuru.MSSSignatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.util.List;
import java.util.function.Supplier;

import javax.net.ssl.*;
import javax.xml.namespace.QName;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.handler.Handler;

import ch.swisscom.mid.client.MIDClientException;
import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.config.ConfigurationException;
import ch.swisscom.mid.client.config.ProxyConfiguration;
import ch.swisscom.mid.client.config.TlsConfiguration;
import ch.swisscom.mid.client.impl.Loggers;

public class MssServiceFactory<PortType> extends BasePooledObjectFactory<MssService<PortType>> {

    private static final Logger log = LoggerFactory.getLogger(Loggers.CLIENT_PROTOCOL);
    private static final Logger logConfig = LoggerFactory.getLogger(Loggers.CONFIG);

    /**
     * JDK JAX-WS properties
     * ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", timeout);
     * ((BindingProvider) port).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", timeout);
     *
     * JBOSS CXF JAX-WS properties, warning these might change in the future (CXF-5261)
     * ((BindingProvider) port).getRequestContext().put("javax.xml.ws.client.connectionTimeout", timeout);
     * ((BindingProvider) port).getRequestContext().put("javax.xml.ws.client.receiveTimeout", timeout);
     *
     * From here: https://github.com/javaee/metro-jax-ws/issues/1166
     * See also: https://issue.swisscom.ch/browse/SCSMINK-231
     */
    private static final String JDK_JAXWS_CONNECT_TIMEOUT = "com.sun.xml.internal.ws.connect.timeout";
    private static final String JBOSS_CXF_CONNECT_TIMEOUT = "javax.xml.ws.client.connectionTimeout";

    private static final String JDK_JAXWS_REQUEST_TIMEOUT = "com.sun.xml.internal.ws.request.timeout";
    private static final String JBOSS_CXF_REQUEST_TIMEOUT = "javax.xml.ws.client.receiveTimeout";

    private static final java.lang.String JAXWS_HOSTNAME_VERIFIER = "com.sun.xml.internal.ws.transport.https.client.hostname.verifier";
    private static final java.lang.String JAXWS_SSL_SOCKET_FACTORY = "com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory";

    private static final String MSSP_NAMESPACE = "http://uri.etsi.org/TS102204/etsi204-kiuru.wsdl";
    private static final String MSSP_SIGNATURE_SERVICE = "MSS_SignatureService";

    private static final String WSDL_CLASSPATH_LOCATION = "/wsdl/etsi204-kiuru.wsdl";

    private final ClientConfiguration clientConfiguration;
    private final Class<PortType> portTypeClass;
    private final Supplier<String> serviceUrlSupplier;

    // ----------------------------------------------------------------------------------------------------

    public MssServiceFactory(ClientConfiguration clientConfiguration,
                             Class<PortType> portTypeClass,
                             Supplier<String> serviceUrlSupplier) {
        this.clientConfiguration = clientConfiguration;
        this.portTypeClass = portTypeClass;
        this.serviceUrlSupplier = serviceUrlSupplier;
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public MssService<PortType> create() {
        return createMssService();
    }

    @Override
    public PooledObject<MssService<PortType>> wrap(MssService<PortType> mssService) {
        return new DefaultPooledObject<>(mssService);
    }

    // ----------------------------------------------------------------------------------------------------

    @SuppressWarnings("rawtypes")
    private MssService<PortType> createMssService() {
        URL wsdlLocation = this.getClass().getResource(WSDL_CLASSPATH_LOCATION);
        if (wsdlLocation == null) {
            throw new ConfigurationException("Cannot find the classpath resource for the MID Client SOAP WSDL: [" +
                                             WSDL_CLASSPATH_LOCATION +
                                             "]. This seems to be a library packaging issue, as this WSDL file should " +
                                             "be packaged in the MID client SOAP library by default");
        }

        try {
            // initialize the WS using the provided service URL
            log.info("Creating a new SOAP WS port for class: {}", portTypeClass.getSimpleName());
            Service ws = MSSSignatureService.create(wsdlLocation, new QName(MSSP_NAMESPACE, MSSP_SIGNATURE_SERVICE));
            PortType wsPort = ws.getPort(portTypeClass);

            // set logging handler
            SoapTrafficHandler soapTrafficHandler = new SoapTrafficHandler();
            BindingProvider bindingProvider = (BindingProvider) wsPort;

            Binding binding = bindingProvider.getBinding();
            List<Handler> handlerList = binding.getHandlerChain();
            handlerList.add(soapTrafficHandler);
            binding.setHandlerChain(handlerList);

            logHttpConnectionConfiguration(clientConfiguration);
            bindingProvider.getRequestContext().put(JAXWSProperties.CONNECT_TIMEOUT, clientConfiguration.getHttp().getConnectionTimeoutInMs());
            bindingProvider.getRequestContext().put(JDK_JAXWS_CONNECT_TIMEOUT, clientConfiguration.getHttp().getConnectionTimeoutInMs());
            bindingProvider.getRequestContext().put(JBOSS_CXF_CONNECT_TIMEOUT, clientConfiguration.getHttp().getConnectionTimeoutInMs());

            bindingProvider.getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, clientConfiguration.getHttp().getResponseTimeoutInMs());
            bindingProvider.getRequestContext().put(JDK_JAXWS_REQUEST_TIMEOUT, clientConfiguration.getHttp().getResponseTimeoutInMs());
            bindingProvider.getRequestContext().put(JBOSS_CXF_REQUEST_TIMEOUT, clientConfiguration.getHttp().getResponseTimeoutInMs());

            SSLSocketFactory sslSocketFactory = produceAnSslSocketFactory(clientConfiguration);
            ProxyConfiguration proxyConfig = clientConfiguration.getProxy();
            if (proxyConfig.isEnabled()) {
                logProxyConfiguration(proxyConfig);
                sslSocketFactory = new ProxyAwareSSLSocketFactory(proxyConfig, sslSocketFactory);
            }

            bindingProvider.getRequestContext().put(JAXWSProperties.SSL_SOCKET_FACTORY, sslSocketFactory);
            bindingProvider.getRequestContext().put(JAXWS_SSL_SOCKET_FACTORY, sslSocketFactory);

            if (!clientConfiguration.getTls().isHostnameVerification()) {
                NoopHostnameVerifier noopHostnameVerifier = new NoopHostnameVerifier();
                bindingProvider.getRequestContext().put(JAXWSProperties.HOSTNAME_VERIFIER, noopHostnameVerifier);
                bindingProvider.getRequestContext().put(JAXWS_HOSTNAME_VERIFIER, noopHostnameVerifier);
            }

            String serviceBaseUrl = serviceUrlSupplier.get();
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceBaseUrl);
            log.info("{} service initialised for {}", portTypeClass.getSimpleName(), serviceBaseUrl);

            return new MssService<>(wsPort, soapTrafficHandler);
        } catch (Exception e) {
            log.error("Failed to configure a new MID Client SOAP port", e);
            throw new MIDClientException("Failed to configure a new MID Client SOAP port", e);
        }
    }

    private void logHttpConnectionConfiguration(ClientConfiguration config) {
        logConfig.info("MSS Soap client: Configuring HTTP client: " +
                       "connection timeout [{}], response timeout [{}], " +
                       "max HTTP connections (total) [{}], HTTP connections per route [{}]",
                       config.getHttp().getConnectionTimeoutInMs(),
                       config.getHttp().getResponseTimeoutInMs(),
                       config.getHttp().getMaxTotalConnections(),
                       config.getHttp().getMaxConnectionsPerRoute());
    }

    private void logProxyConfiguration(ProxyConfiguration config) {
        logConfig.info("Configuring PROXY parameters: enabled [{}], host [{}], port [{}], username [{}], password [{}]",
                       config.isEnabled(), config.getHost(), config.getPort(),
                       config.getUsername(), config.getPassword() != null ? "(not-null)" : "null");
    }

    private SSLSocketFactory produceAnSslSocketFactory(ClientConfiguration config) {
        TlsConfiguration tlsConfig = config.getTls();
        logTlsConfiguration(tlsConfig);

        try {
            SSLContext sslContext;
            if (tlsConfig.getSslContext() == null) {
                sslContext = SSLContext.getInstance("Default");
            } else {
                sslContext = SSLContext.getInstance(tlsConfig.getSslContext());
            }

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(produceAKeyStore(tlsConfig), tlsConfig.getKeyStoreKeyPassword().toCharArray());
            KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();

            TrustManager[] trustManagers = null;
            if (trustStoreIsConfigured(tlsConfig)) {
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(produceATrustStore(tlsConfig));
                trustManagers = trustManagerFactory.getTrustManagers();
            }

            sslContext.init(keyManagers, trustManagers, null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new ConfigurationException("Failed to configure the SSL Socket factory for the MID SOAP client", e);
        }
    }

    private void logTlsConfiguration(TlsConfiguration tlsConfig) {
        String keyStoreSource;
        String trustStoreSource;

        if (tlsConfig.getKeyStoreFile() != null) {
            keyStoreSource = "file: [" + tlsConfig.getKeyStoreFile() + "]";
        } else if (tlsConfig.getKeyStoreClasspathFile() != null) {
            keyStoreSource = "classpath: [" + tlsConfig.getKeyStoreClasspathFile() + "]";
        } else {
            keyStoreSource = "input stream (byte content)";
        }

        if (tlsConfig.getTrustStoreFile() != null) {
            trustStoreSource = "file: [" + tlsConfig.getTrustStoreFile() + "]";
        } else if (tlsConfig.getTrustStoreClasspathFile() != null) {
            trustStoreSource = "classpath: [" + tlsConfig.getTrustStoreClasspathFile() + "]";
        } else {
            trustStoreSource = "input stream (byte content)";
        }

        logConfig.info("MSS Soap client: Configuring TLS connection factory for MID client: " +
                       "key store source: [{}], " +
                       "key store type: [{}], " +
                       "key store alias: [{}], " +
                       "trust store source: [{}], " +
                       "trust store type: [{}], " +
                       "tls ssl context: [{}]",
                       keyStoreSource, tlsConfig.getKeyStoreType(), tlsConfig.getKeyStoreCertificateAlias(),
                       trustStoreSource, tlsConfig.getTrustStoreType(), tlsConfig.getSslContext());
    }


    private KeyStore produceAKeyStore(TlsConfiguration tlsConfig) {
        try {
            KeyStore keyStore = KeyStore.getInstance(tlsConfig.getKeyStoreType());
            if (tlsConfig.getKeyStoreFile() != null) {
                try (InputStream is = new FileInputStream(tlsConfig.getKeyStoreFile())) {
                    keyStore.load(is, tlsConfig.getKeyStorePassword() == null ?
                                      null : tlsConfig.getKeyStorePassword().toCharArray());
                }
            } else if (tlsConfig.getKeyStoreClasspathFile() != null) {
                try (InputStream is = this.getClass().getResourceAsStream(tlsConfig.getKeyStoreClasspathFile())) {
                    keyStore.load(is, tlsConfig.getKeyStorePassword() == null ?
                                      null : tlsConfig.getKeyStorePassword().toCharArray());
                }
            } else {
                try (InputStream is = new ByteArrayInputStream(tlsConfig.getKeyStoreBytes())) {
                    keyStore.load(is, tlsConfig.getKeyStorePassword() == null ?
                                      null : tlsConfig.getKeyStorePassword().toCharArray());
                }
            }
            return keyStore;
        } catch (Exception e) {
            throw new ConfigurationException("Failed to initialize the TLS keystore", e);
        }
    }

    private boolean trustStoreIsConfigured(TlsConfiguration tlsConfig) {
        return tlsConfig.getTrustStoreFile() != null ||
               tlsConfig.getTrustStoreClasspathFile() != null ||
               tlsConfig.getTrustStoreBytes() != null;
    }

    private KeyStore produceATrustStore(TlsConfiguration tlsConfig) {
        try {
            KeyStore keyStore = KeyStore.getInstance(tlsConfig.getTrustStoreType());
            if (tlsConfig.getTrustStoreFile() != null) {
                try (InputStream is = new FileInputStream(tlsConfig.getTrustStoreFile())) {
                    keyStore.load(is, tlsConfig.getTrustStorePassword() == null ?
                                      null : tlsConfig.getTrustStorePassword().toCharArray());
                }
            } else if (tlsConfig.getTrustStoreClasspathFile() != null) {
                try (InputStream is = this.getClass().getResourceAsStream(tlsConfig.getTrustStoreClasspathFile())) {
                    keyStore.load(is, tlsConfig.getTrustStorePassword() == null ?
                                      null : tlsConfig.getTrustStorePassword().toCharArray());
                }
            } else {
                try (InputStream is = new ByteArrayInputStream(tlsConfig.getTrustStoreBytes())) {
                    keyStore.load(is, tlsConfig.getTrustStorePassword() == null ?
                                      null : tlsConfig.getTrustStorePassword().toCharArray());
                }
            }
            return keyStore;
        } catch (Exception e) {
            throw new ConfigurationException("Failed to initialize the TLS truststore", e);
        }
    }

    // ----------------------------------------------------------------------------------------------------

    private static class NoopHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostName, SSLSession session) {
            return true;
        }
    }

}
