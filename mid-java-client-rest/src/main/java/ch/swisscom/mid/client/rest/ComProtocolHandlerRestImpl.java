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
package ch.swisscom.mid.client.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.CharEncoding;
import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.ssl.PrivateKeyStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import ch.swisscom.mid.client.MIDFlowException;
import ch.swisscom.mid.client.config.*;
import ch.swisscom.mid.client.impl.ComProtocolHandler;
import ch.swisscom.mid.client.impl.Loggers;
import ch.swisscom.mid.client.model.*;
import ch.swisscom.mid.client.rest.model.fault.MSSFault;
import ch.swisscom.mid.client.rest.model.profqreq.MSSProfileQueryRequest;
import ch.swisscom.mid.client.rest.model.profqresp.MSSProfileQueryResponse;
import ch.swisscom.mid.client.rest.model.receiptreq.MSSReceiptRequest;
import ch.swisscom.mid.client.rest.model.receiptresp.MSSReceiptResponse;
import ch.swisscom.mid.client.rest.model.signreq.MSSSignatureRequest;
import ch.swisscom.mid.client.rest.model.signresp.MSSSignatureResponse;
import ch.swisscom.mid.client.rest.model.statusreq.MSSStatusRequest;
import ch.swisscom.mid.client.rest.model.statusresp.MSSStatusResponse;
import ch.swisscom.mid.client.utils.Utils;

public class ComProtocolHandlerRestImpl implements ComProtocolHandler {

    private static final Logger logConfig = LoggerFactory.getLogger(Loggers.LOGGER_CONFIG);
    private static final Logger logProtocol = LoggerFactory.getLogger(Loggers.LOGGER_CLIENT_PROTOCOL);
    private static final Logger logReqResp = LoggerFactory.getLogger(Loggers.LOGGER_REQUEST_RESPONSE);
    private static final Logger logFullReqResp = LoggerFactory.getLogger(Loggers.LOGGER_FULL_REQUEST_RESPONSE);

    private ClientConfiguration config;

    private ObjectMapper jacksonMapper;

    private CloseableHttpClient httpClient;
    private RequestConfig httpRequestConfig;

    @Override
    public ComProtocol getImplementedComProtocol() {
        return ComProtocol.REST;
    }

    @Override
    public void initialize(ClientConfiguration config) {
        this.config = config;
        jacksonMapper = new ObjectMapper();
        jacksonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        TlsConfiguration tlsConfig = config.getTls();
        logTlsConfiguration(tlsConfig);
        SSLConnectionSocketFactory sslConnectionSocketFactory;
        try {
            SSLContextBuilder sslContextBuilder = SSLContexts.custom()
                .loadKeyMaterial(produceAKeyStore(tlsConfig),
                                 tlsConfig.getKeyStoreKeyPassword() == null ? null : tlsConfig.getKeyStoreKeyPassword().toCharArray(),
                                 produceAPrivateKeyStrategy(tlsConfig));
            if (trustStoreIsConfigured(tlsConfig)) {
                sslContextBuilder.loadTrustMaterial(produceATrustStore(tlsConfig), null);
            }
            if (tlsConfig.isHostnameVerification()) {
                sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build());
            } else {
                sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
                                                                            NoopHostnameVerifier.INSTANCE);
            }
        } catch (Exception e) {
            throw new ConfigurationException("Failed to configure the TLS/SSL connection factory for the MID client", e);
        }

        BasicCredentialsProvider credentialsProvider = null;
        if (config.getProxy().isEnabled()) {
            ProxyConfiguration proxyConfig = config.getProxy();
            logProxyConfiguration(proxyConfig);
            String proxyHost = proxyConfig.getHost();
            int proxyPort = proxyConfig.getPort();

            if (proxyConfig.getUsername() != null) {
                credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(new AuthScope(proxyHost, proxyPort),
                                                   new UsernamePasswordCredentials(proxyConfig.getUsername().trim(),
                                                                                   proxyConfig.getPassword().trim().toCharArray()));
            }

            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
            httpRequestConfig = RequestConfig.custom().setProxy(proxy).build();
        }

        logHttpConnectionConfiguration(config);
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnTotal(config.getHttp().getMaxTotalConnections())
            .setMaxConnPerRoute(config.getHttp().getMaxConnectionsPerRoute())
            .setSSLSocketFactory(sslConnectionSocketFactory)
            .build();
        RequestConfig httpClientRequestConfig = RequestConfig.custom()
            .setConnectTimeout(config.getHttp().getConnectionTimeoutInMs(), TimeUnit.MILLISECONDS)
            .setResponseTimeout(config.getHttp().getResponseTimeoutInMs(), TimeUnit.MILLISECONDS)
            .build();

        httpClient = HttpClients.custom()
            .setDefaultCredentialsProvider(credentialsProvider)
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(httpClientRequestConfig)
            .build();
    }

    @Override
    public void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
    }

    @Override
    public SignatureResponse requestSyncSignature(SignatureRequest request) {
        MSSSignatureRequest requestWrapper = SignatureRequestModelUtils.createSignatureRequest(config, request, true);
        notifyTrafficObserverForApTransId(request.getTrafficObserver(), requestWrapper.getMSSSignatureReq().getAPInfo().getAPTransID());
        MSSSignatureResponse responseWrapper = sendAndReceive("MSS Signature (sync)",
                                                              config.getUrls().getSignatureServiceUrl(),
                                                              requestWrapper, MSSSignatureResponse.class, request.getTrafficObserver());
        SignatureResponse signatureResponse = SignatureRequestModelUtils.processSignatureResponse(responseWrapper);
        signatureResponse.setTracking(SignatureRequestModelUtils.createSignatureTracking(responseWrapper,
                                                                                         request.getTrafficObserver(),
                                                                                         request.getOverrideApId(),
                                                                                         request.getOverrideApPassword()));
        return signatureResponse;
    }

    @Override
    public SignatureResponse requestAsyncSignature(SignatureRequest request) {
        MSSSignatureRequest requestWrapper = SignatureRequestModelUtils.createSignatureRequest(config, request, false);
        notifyTrafficObserverForApTransId(request.getTrafficObserver(), requestWrapper.getMSSSignatureReq().getAPInfo().getAPTransID());
        MSSSignatureResponse responseWrapper = sendAndReceive("MSS Signature (async)",
                                                              config.getUrls().getSignatureServiceUrl(),
                                                              requestWrapper, MSSSignatureResponse.class, request.getTrafficObserver());
        SignatureResponse signatureResponse = SignatureRequestModelUtils.processSignatureResponse(responseWrapper);
        signatureResponse.setTracking(SignatureRequestModelUtils.createSignatureTracking(responseWrapper,
                                                                                         request.getTrafficObserver(),
                                                                                         request.getOverrideApId(),
                                                                                         request.getOverrideApPassword()));
        return signatureResponse;
    }

    @Override
    public SignatureResponse pollForSignatureStatus(SignatureTracking signatureTracking) {
        MSSStatusRequest requestWrapper = StatusQueryModelUtils.createStatusQueryRequest(config, signatureTracking);
        MSSStatusResponse responseWrapper = sendAndReceive("MSS Status Query",
                                                           config.getUrls().getStatusQueryServiceUrl(),
                                                           requestWrapper, MSSStatusResponse.class,
                                                           signatureTracking.getTrafficObserver());
        return StatusQueryModelUtils.processStatusQueryResponse(responseWrapper, signatureTracking);
    }

    @Override
    public ReceiptResponse requestSyncReceipt(SignatureTracking signatureTracking, ReceiptRequest request) {
        MSSReceiptRequest requestWrapper = ReceiptRequestModelUtils.createReceiptRequest(config, signatureTracking, request);
        String operationName;
        if (request.getRequestExtension() == null || request.getRequestExtension().getMessagingMode() == ReceiptMessagingMode.SYNC) {
            operationName = "MSS Receipt (sync)";
        } else {
            throw new UnsupportedOperationException("There is no support for non-sync MSS Receipt Request");
        }
        MSSReceiptResponse responseWrapper = sendAndReceive(operationName,
                                                            config.getUrls().getReceiptServiceUrl(),
                                                            requestWrapper, MSSReceiptResponse.class,
                                                            signatureTracking.getTrafficObserver());
        return ReceiptRequestModelUtils.processReceiptResponse(responseWrapper);
    }

    @Override
    public ProfileResponse requestProfile(ProfileRequest request) {
        MSSProfileQueryRequest requestWrapper = ProfileRequestModelUtils.createProfileQueryRequest(request, config);
        notifyTrafficObserverForApTransId(request.getTrafficObserver(), requestWrapper.getMSSProfileReq().getAPInfo().getAPTransID());
        MSSProfileQueryResponse responseWrapper = sendAndReceive("MSS Profile Query",
                                                                 config.getUrls().getProfileQueryServiceUrl(),
                                                                 requestWrapper, MSSProfileQueryResponse.class, request.getTrafficObserver());
        return ProfileRequestModelUtils.processProfileQueryResponse(responseWrapper);
    }

    // ----------------------------------------------------------------------------------------------------

    private void logHttpConnectionConfiguration(ClientConfiguration config) {
        logConfig.info("Configuring HTTP client: " +
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

    private PrivateKeyStrategy produceAPrivateKeyStrategy(TlsConfiguration tlsConfig) {
        return (aliases, sslParameters) -> tlsConfig.getKeyStoreCertificateAlias();
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

        logConfig.info("Configuring TLS connection factory for MID client: " +
                       "key store source: [{}], " +
                       "key store type: [{}], " +
                       "key store alias: [{}], " +
                       "trust store source: [{}], " +
                       "trust store type: [{}]",
                       keyStoreSource, tlsConfig.getKeyStoreType(), tlsConfig.getKeyStoreCertificateAlias(),
                       trustStoreSource, tlsConfig.getTrustStoreType());
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

    private <TReq, TResp> TResp sendAndReceive(String operationName,
                                               String serviceUrl,
                                               TReq requestObject,
                                               Class<TResp> responseClass,
                                               TrafficObserver trafficObserver) {
        logProtocol.debug("{}: Serializing object of type {} to JSON", operationName, requestObject.getClass().getSimpleName());
        FaultProcessor faultProcessor = new FaultProcessor();
        String requestJson;
        try {
            requestJson = jacksonMapper.writeValueAsString(requestObject);
        } catch (JsonProcessingException e) {
            throw new MIDFlowException("Failed to serialize request object to JSON, for operation " + operationName,
                                       e, faultProcessor.processException(e, FailureReason.REQUEST_PREPARATION_FAILURE));
        }
        notifyTrafficObserverForRequest(trafficObserver, requestJson);
        HttpPost httpPost = new HttpPost(serviceUrl);
        httpPost.setEntity(new StringEntity(requestJson, ContentType.APPLICATION_JSON, CharEncoding.UTF_8, false));
        httpPost.setConfig(httpRequestConfig);

        logProtocol.info("{}: Sending request to: [{}]", operationName, serviceUrl);
        logReqResp.info("{}: Sending JSON to: [{}], content: [{}]", operationName, serviceUrl, requestJson);
        logFullReqResp.info("{}: Sending JSON to: [{}], content: [{}]", operationName, serviceUrl, requestJson);

        TResp responseWrapper = null;
        MSSFault faultWrapper = null;
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            logProtocol.info("{}: Received HTTP status code: {}", operationName, response.getCode());
            String stringResponse;
            try {
                stringResponse = EntityUtils.toString(response.getEntity());
            } catch (ParseException e) {
                throw new MIDFlowException("Failed to interpret the HTTP response content as a string, " +
                                           "for operation " + operationName, e,
                                           faultProcessor.processException(e, FailureReason.HTTP_DATA_TRANSFER_FAILURE));
            }
            notifyTrafficObserverForResponse(trafficObserver, response, stringResponse);
            if (response.getCode() == 200) {
                if (logReqResp.isInfoEnabled()) {
                    String strippedResponse = Utils.stripInnerLargeBase64Content(stringResponse, '"', '"');
                    logReqResp.info("{}: Received JSON content: {}", operationName, strippedResponse);
                }
                if (logFullReqResp.isInfoEnabled()) {
                    logFullReqResp.info("{}: Received JSON content: {}", operationName, stringResponse);
                }
                logProtocol.debug("{}: Deserializing JSON to object of type {}", operationName, responseClass.getSimpleName());
                try {
                    responseWrapper = jacksonMapper.readValue(stringResponse, responseClass);
                } catch (JsonProcessingException e) {
                    throw new MIDFlowException("Failed to deserialize JSON content to object of type " +
                                               responseClass.getSimpleName() + " for operation " + operationName, e,
                                               faultProcessor.processException(e, FailureReason.RESPONSE_PARSING_FAILURE));
                }
            } else {
                logProtocol.debug("{}: Deserializing JSON to object of type {}", operationName, MSSFault.class.getSimpleName());
                try {
                    faultWrapper = jacksonMapper.readValue(stringResponse, MSSFault.class);
                } catch (JsonProcessingException e) {
                    throw new MIDFlowException("Failed to deserialize JSON content to object of type " +
                                               MSSFault.class.getSimpleName() + " for operation " + operationName,
                                               e, faultProcessor.processException(e, FailureReason.RESPONSE_PARSING_FAILURE));
                }
            }
        } catch (SSLException e) {
            throw new MIDFlowException("TLS/SSL connection failure for " + operationName, e, faultProcessor.processException(e, null));
        } catch (Exception e) {
            throw new MIDFlowException("Communication failure for " + operationName, e, faultProcessor.processException(e, null));
        }

        if (responseWrapper != null) {
            return responseWrapper;
        } else {
            throw new MIDFlowException("Fault response received from Mobile ID server. See embedded MIDFault",
                                       new FaultProcessor().processFaultResponse(faultWrapper));
        }
    }

    private void notifyTrafficObserverForRequest(TrafficObserver trafficObserver, String body) {
        if (trafficObserver == null) {
            return;
        }
        RequestTrace trace = new RequestTrace(body);
        trafficObserver.notifyOfOutgoingRequest(trace, ComProtocol.REST);
    }

    private void notifyTrafficObserverForResponse(TrafficObserver trafficObserver, HttpResponse response, String body) {
        if (trafficObserver == null) {
            return;
        }
        ResponseTrace trace = new ResponseTrace(response.getCode(), response.getReasonPhrase(), body);
        trafficObserver.notifyOfIncomingResponse(trace, ComProtocol.REST);
    }

    private void notifyTrafficObserverForApTransId(TrafficObserver trafficObserver, String apTransId) {
        if (trafficObserver == null) {
            return;
        }
        trafficObserver.notifyOfGeneratedApTransId(apTransId, ComProtocol.REST);
    }

}
