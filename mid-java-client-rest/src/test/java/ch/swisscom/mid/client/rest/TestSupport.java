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

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.config.HttpConfiguration;
import ch.swisscom.mid.client.config.TlsConfiguration;
import ch.swisscom.mid.client.config.UrlsConfiguration;

public class TestSupport {

    public static ClientConfiguration buildConfig() {
        ClientConfiguration config = new ClientConfiguration();
        config.setProtocolToRest();
        config.setApId("mid://test.swisscom.ch");
        config.setApPassword("TEST_ID");

        UrlsConfiguration urls = config.getUrls();
        urls.setAllServiceUrlsTo("http://localhost:8089" + DefaultConfiguration.REST_ENDPOINT_SUB_URL);

        TlsConfiguration tls = config.getTls();
        tls.setKeyStoreBytes(fileToBytes("/empty-store.jks"));
        tls.setKeyStorePassword("secret");
        tls.setKeyStoreKeyPassword("secret");
        tls.setKeyStoreCertificateAlias("alias");
        tls.setTrustStoreBytes(fileToBytes("/empty-store.jks"));
        tls.setTrustStorePassword("secret");
        tls.setHostnameVerification(false);

        HttpConfiguration http = config.getHttp();
        http.setConnectionTimeoutInMs(2 * 1000);
        http.setResponseTimeoutInMs(2 * 1000);

        return config;
    }

    public static String fileToString(String fileName) {
        try (InputStream is = SyncSignatureTest.class.getResourceAsStream(fileName)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load payload from file [" + fileName + "]", e);
        }
    }

    public static byte[] fileToBytes(String fileName) {
        try (InputStream is = SyncSignatureTest.class.getResourceAsStream(fileName)) {
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load payload from file [" + fileName + "]", e);
        }
    }


}
