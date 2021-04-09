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
package ch.swisscom.mid.client.model;

import java.util.List;

public class ProfileMobileUserCertificate {

    private ProfileMobileUserCertificateState state;

    private String algorithm;

    private CertificateData userCertificate;

    private List<CertificateData> caCertificates;

    public ProfileMobileUserCertificateState getState() {
        return state;
    }

    public void setState(ProfileMobileUserCertificateState state) {
        this.state = state;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public CertificateData getUserCertificate() {
        return userCertificate;
    }

    public void setUserCertificate(CertificateData userCertificate) {
        this.userCertificate = userCertificate;
    }

    public List<CertificateData> getCaCertificates() {
        return caCertificates;
    }

    public void setCaCertificates(List<CertificateData> caCertificates) {
        this.caCertificates = caCertificates;
    }

    @Override
    public String toString() {
        return "ProfileMobileUserCertificate{" +
               "state=" + state +
               ", algorithm='" + algorithm + '\'' +
               ", userCertificate=" + userCertificate +
               ", caCertificates=" + caCertificates +
               '}';
    }
}
