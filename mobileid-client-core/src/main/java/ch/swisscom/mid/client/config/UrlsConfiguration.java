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

public class UrlsConfiguration {

    private String signatureServiceUrl;

    private String statusQueryServiceUrl;

    private String receiptServiceUrl;

    private String profileQueryServiceUrl;

    public String getSignatureServiceUrl() {
        return signatureServiceUrl;
    }

    public void setSignatureServiceUrl(String signatureServiceUrl) {
        this.signatureServiceUrl = signatureServiceUrl;
    }

    public String getProfileQueryServiceUrl() {
        return profileQueryServiceUrl;
    }

    public void setProfileQueryServiceUrl(String profileQueryServiceUrl) {
        this.profileQueryServiceUrl = profileQueryServiceUrl;
    }

    public String getStatusQueryServiceUrl() {
        return statusQueryServiceUrl;
    }

    public void setStatusQueryServiceUrl(String statusQueryServiceUrl) {
        this.statusQueryServiceUrl = statusQueryServiceUrl;
    }

    public String getReceiptServiceUrl() {
        return receiptServiceUrl;
    }

    public void setReceiptServiceUrl(String receiptServiceUrl) {
        this.receiptServiceUrl = receiptServiceUrl;
    }

    // ----------------------------------------------------------------------------------------------------

    public void setAllServiceUrlsTo(String serviceUrl) {
        setSignatureServiceUrl(serviceUrl);
        setStatusQueryServiceUrl(serviceUrl);
        setReceiptServiceUrl(serviceUrl);
        setProfileQueryServiceUrl(serviceUrl);
    }

    public void setAllServiceUrlsToBase(String baseUrl) {
        String finalBaseUrl = baseUrl.trim();
        if (finalBaseUrl.endsWith("/")) {
            finalBaseUrl = finalBaseUrl.substring(0, finalBaseUrl.length() - 1);
        }
        setSignatureServiceUrl(finalBaseUrl + DefaultConfiguration.SOAP_SIGNATURE_PORT_SUB_URL);
        setStatusQueryServiceUrl(finalBaseUrl + DefaultConfiguration.SOAP_STATUS_QUERY_PORT_SUB_URL);
        setProfileQueryServiceUrl(finalBaseUrl + DefaultConfiguration.SOAP_PROFILE_QUERY_PORT_SUB_URL);
        setReceiptServiceUrl(finalBaseUrl + DefaultConfiguration.SOAP_RECEIPT_PORT_SUB_URL);
    }

    public void validateYourself() {
        configNotNull(signatureServiceUrl, "The signatureServiceUrl cannot be NULL");
        configNotNull(statusQueryServiceUrl, "The statusQueryServiceUrl cannot be NULL");
        configNotNull(receiptServiceUrl, "The receiptServiceUrl cannot be NULL");
        configNotNull(profileQueryServiceUrl, "The profileQueryServiceUrl cannot be NULL");
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "UrlsConfiguration{" +
               "signatureServiceUrl='" + signatureServiceUrl + '\'' +
               ", statusQueryServiceUrl='" + statusQueryServiceUrl + '\'' +
               ", profileQueryServiceUrl='" + profileQueryServiceUrl + '\'' +
               '}';
    }

}
