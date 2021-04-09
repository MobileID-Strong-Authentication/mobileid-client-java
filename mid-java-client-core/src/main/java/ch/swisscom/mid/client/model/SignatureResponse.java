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

public class SignatureResponse {

    private String majorVersion;

    private String minorVersion;

    private String signatureProfile;

    private String base64Signature;

    private Status status;

    private SignatureTracking tracking;

    // ----------------------------------------------------------------------------------------------------

    private List<AdditionalServiceResponse> additionalServiceResponses;

    public String getSignatureProfile() {
        return signatureProfile;
    }

    public void setSignatureProfile(String signatureProfile) {
        this.signatureProfile = signatureProfile;
    }

    public String getBase64Signature() {
        return base64Signature;
    }

    public void setBase64Signature(String base64Signature) {
        this.base64Signature = base64Signature;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<AdditionalServiceResponse> getAdditionalServiceResponses() {
        return additionalServiceResponses;
    }

    public void setAdditionalServiceResponses(List<AdditionalServiceResponse> additionalServiceResponses) {
        this.additionalServiceResponses = additionalServiceResponses;
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
    }

    public SignatureTracking getTracking() {
        return tracking;
    }

    public void setTracking(SignatureTracking tracking) {
        this.tracking = tracking;
    }

    @Override
    public String toString() {
        return "SignatureResponse{" +
               "signatureProfile='" + signatureProfile + '\'' +
               ", base64Signature='" + (base64Signature == null ? "null" : "(not-null)") + '\'' +
               ", status=" + status +
               ", additionalServiceResponse=" + additionalServiceResponses +
               ", majorVersion=" + majorVersion +
               ", minorVersion=" + minorVersion +
               ", tracking=" + tracking +
               '}';
    }
}
