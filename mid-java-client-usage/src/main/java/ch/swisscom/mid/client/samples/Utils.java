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
package ch.swisscom.mid.client.samples;

import ch.swisscom.mid.client.MIDFlowException;
import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.config.HttpConfiguration;
import ch.swisscom.mid.client.config.ProxyConfiguration;
import ch.swisscom.mid.client.config.TlsConfiguration;
import ch.swisscom.mid.client.config.UrlsConfiguration;
import ch.swisscom.mid.client.model.Fault;

public class Utils {

    public static ClientConfiguration buildClientConfig() {
        ClientConfiguration config = new ClientConfiguration();
        config.setProtocolToRest();
        config.setApId("<My AP ID>");
        config.setApPassword("<My AP password>");

        UrlsConfiguration urls = config.getUrls();
        urls.setAllServiceUrlsTo(DefaultConfiguration.DEFAULT_INTERNET_BASE_URL + DefaultConfiguration.REST_ENDPOINT_SUB_URL);

        TlsConfiguration tls = config.getTls();
        tls.setKeyStoreFile("/home/localuser/mid_keystore.jks");
        tls.setKeyStorePassword("secret");
        tls.setKeyStoreKeyPassword("secret");
        tls.setKeyStoreCertificateAlias("my-cert-alias");
        tls.setTrustStoreFile("/home/localuser/truststore.jks");
        tls.setTrustStorePassword("secret");
        tls.setHostnameVerification(true);

        HttpConfiguration http = config.getHttp();
        http.setConnectionTimeoutInMs(20 * 1000);
        http.setResponseTimeoutInMs(100 * 1000);
        http.setMaxTotalConnections(20);
        http.setMaxConnectionsPerRoute(10);

        ProxyConfiguration proxy = config.getProxy();
        proxy.setHost("localhost");
        proxy.setPort(3128);
        proxy.setUsername("myuser");
        proxy.setPassword("mypass");

        return config;
    }

    public static String prettyPrintTheException(Exception e) {
        StringBuilder builder = new StringBuilder(200);

        if (MIDFlowException.class.isAssignableFrom(e.getClass())) {
            MIDFlowException midFlowException = (MIDFlowException) e;
            Fault fault = midFlowException.getFault();
            builder.append(fault.toString());
            builder.append("\n---\n");
        }

        builder.append("Exception stack:\n");
        Throwable exceptionCursor = e;
        for (int depth = 0; depth < 20 && exceptionCursor != null; ) {
            builder.append("  ");
            builder.append(exceptionCursor.getClass().getSimpleName());
            builder.append(" = ");
            builder.append(exceptionCursor.getLocalizedMessage());
            builder.append('\n');
            depth++;
            exceptionCursor = exceptionCursor.getCause();
        }

        return builder.toString();
    }

}
