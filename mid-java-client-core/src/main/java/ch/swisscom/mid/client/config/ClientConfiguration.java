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
package ch.swisscom.mid.client.config;

import static ch.swisscom.mid.client.utils.Utils.configNotNull;

public class ClientConfiguration {

    private TlsConfiguration tls;
    private HttpConfiguration http;
    private ComProtocol protocol;
    private UrlsConfiguration urls;
    private ProxyConfiguration proxy;

    private String apId;
    private String apPassword;
    private String msspId = DefaultConfiguration.DEFAULT_MSSP_ID;

    // ----------------------------------------------------------------------------------------------------

    public void setProtocolToRest() {
        protocol = ComProtocol.REST;
    }

    public void setProtocolToSoap() {
        protocol = ComProtocol.SOAP;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getApPassword() {
        return apPassword;
    }

    public void setApPassword(String apPassword) {
        this.apPassword = apPassword;
    }

    public ComProtocol getProtocol() {
        return protocol;
    }

    public String getMsspId() {
        return msspId;
    }

    public void setMsspId(String msspId) {
        this.msspId = msspId;
    }

    public void setProtocol(ComProtocol protocol) {
        this.protocol = protocol;
    }

    public TlsConfiguration getTls() {
        if (tls == null) {
            tls = new TlsConfiguration();
        }
        return tls;
    }

    public void setTls(TlsConfiguration tls) {
        this.tls = tls;
    }

    public HttpConfiguration getHttp() {
        if (http == null) {
            http = new HttpConfiguration();
        }
        return http;
    }

    public void setHttp(HttpConfiguration http) {
        this.http = http;
    }

    public UrlsConfiguration getUrls() {
        if (urls == null) {
            urls = new UrlsConfiguration();
        }
        return urls;
    }

    public ProxyConfiguration getProxy() {
        if (proxy == null) {
            proxy = new ProxyConfiguration();
        }
        return proxy;
    }

    public void setProxy(ProxyConfiguration proxy) {
        this.proxy = proxy;
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ClientConfiguration{" +
               "tls=" + tls +
               ", http=" + http +
               ", protocol=" + protocol +
               ", apId='" + apId + '\'' +
               ", msspId='" + msspId + '\'' +
               ", urls='" + urls + '\'' +
               ", proxy='" + proxy + '\'' +
               '}';
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        configNotNull(protocol, "The configured client protocol cannot be NULL.");
        configNotNull(apId, "The configured AP_ID (apId) cannot be NULL.");
        configNotNull(apPassword, "The configured AP_PWD (apPassword) cannot be NULL.");
        configNotNull(msspId, "The configured MSSP_ID (msspId) cannot be NULL. " +
                              "Usually the best value is to leave it as it is already: " + DefaultConfiguration.DEFAULT_MSSP_ID);
        configNotNull(tls, "The TLS configuration cannot be NULL");
        tls.validateYourself();
        configNotNull(http, "The HTTP configuration cannot be NULL");
        http.validateYourself();
        configNotNull(urls, "The URLs configuration cannot be NULL");
        urls.validateYourself();
        // proxy is optional, validate only if configured
        if (proxy != null) {
            proxy.validateYourself();
        }
    }
}
