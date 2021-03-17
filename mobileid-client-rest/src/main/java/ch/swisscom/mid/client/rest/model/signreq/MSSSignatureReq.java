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
package ch.swisscom.mid.client.rest.model.signreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "AP_Info",
        "AdditionalServices",
        "DataToBeSigned",
        "MSSP_Info",
        "MajorVersion",
        "MessagingMode",
        "MinorVersion",
        "MobileUser",
        "SignatureProfile",
        "TimeOut"
})
public class MSSSignatureReq {

    @JsonProperty("AP_Info")
    private APInfo aPInfo;
    @JsonProperty("AdditionalServices")
    private List<AdditionalService> additionalServices = new ArrayList<AdditionalService>();
    @JsonProperty("DataToBeSigned")
    private DataToBeSigned dataToBeSigned;
    @JsonProperty("MSSP_Info")
    private MSSPInfo mSSPInfo;
    @JsonProperty("MajorVersion")
    private String majorVersion;
    @JsonProperty("MessagingMode")
    private String messagingMode;
    @JsonProperty("MinorVersion")
    private String minorVersion;
    @JsonProperty("MobileUser")
    private MobileUser mobileUser;
    @JsonProperty("SignatureProfile")
    private String signatureProfile;
    @JsonProperty("TimeOut")
    private String timeOut;

    @JsonProperty("AP_Info")
    public APInfo getAPInfo() {
        return aPInfo;
    }

    @JsonProperty("AP_Info")
    public void setAPInfo(APInfo aPInfo) {
        this.aPInfo = aPInfo;
    }

    public MSSSignatureReq withAPInfo(APInfo aPInfo) {
        this.aPInfo = aPInfo;
        return this;
    }

    @JsonProperty("AdditionalServices")
    public List<AdditionalService> getAdditionalServices() {
        return additionalServices;
    }

    @JsonProperty("AdditionalServices")
    public void setAdditionalServices(List<AdditionalService> additionalServices) {
        this.additionalServices = additionalServices;
    }

    public MSSSignatureReq withAdditionalServices(List<AdditionalService> additionalServices) {
        this.additionalServices = additionalServices;
        return this;
    }

    @JsonProperty("DataToBeSigned")
    public DataToBeSigned getDataToBeSigned() {
        return dataToBeSigned;
    }

    @JsonProperty("DataToBeSigned")
    public void setDataToBeSigned(DataToBeSigned dataToBeSigned) {
        this.dataToBeSigned = dataToBeSigned;
    }

    public MSSSignatureReq withDataToBeSigned(DataToBeSigned dataToBeSigned) {
        this.dataToBeSigned = dataToBeSigned;
        return this;
    }

    @JsonProperty("MSSP_Info")
    public MSSPInfo getMSSPInfo() {
        return mSSPInfo;
    }

    @JsonProperty("MSSP_Info")
    public void setMSSPInfo(MSSPInfo mSSPInfo) {
        this.mSSPInfo = mSSPInfo;
    }

    public MSSSignatureReq withMSSPInfo(MSSPInfo mSSPInfo) {
        this.mSSPInfo = mSSPInfo;
        return this;
    }

    @JsonProperty("MajorVersion")
    public String getMajorVersion() {
        return majorVersion;
    }

    @JsonProperty("MajorVersion")
    public void setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
    }

    public MSSSignatureReq withMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
        return this;
    }

    @JsonProperty("MessagingMode")
    public String getMessagingMode() {
        return messagingMode;
    }

    @JsonProperty("MessagingMode")
    public void setMessagingMode(String messagingMode) {
        this.messagingMode = messagingMode;
    }

    public MSSSignatureReq withMessagingMode(String messagingMode) {
        this.messagingMode = messagingMode;
        return this;
    }

    @JsonProperty("MinorVersion")
    public String getMinorVersion() {
        return minorVersion;
    }

    @JsonProperty("MinorVersion")
    public void setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
    }

    public MSSSignatureReq withMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
        return this;
    }

    @JsonProperty("MobileUser")
    public MobileUser getMobileUser() {
        return mobileUser;
    }

    @JsonProperty("MobileUser")
    public void setMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
    }

    public MSSSignatureReq withMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
        return this;
    }

    @JsonProperty("SignatureProfile")
    public String getSignatureProfile() {
        return signatureProfile;
    }

    @JsonProperty("SignatureProfile")
    public void setSignatureProfile(String signatureProfile) {
        this.signatureProfile = signatureProfile;
    }

    public MSSSignatureReq withSignatureProfile(String signatureProfile) {
        this.signatureProfile = signatureProfile;
        return this;
    }

    @JsonProperty("TimeOut")
    public String getTimeOut() {
        return timeOut;
    }

    @JsonProperty("TimeOut")
    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public MSSSignatureReq withTimeOut(String timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(MSSSignatureReq.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("aPInfo");
        sb.append('=');
        sb.append(((this.aPInfo == null) ? "<null>" : this.aPInfo));
        sb.append(',');
        sb.append("additionalServices");
        sb.append('=');
        sb.append(((this.additionalServices == null) ? "<null>" : this.additionalServices));
        sb.append(',');
        sb.append("dataToBeSigned");
        sb.append('=');
        sb.append(((this.dataToBeSigned == null) ? "<null>" : this.dataToBeSigned));
        sb.append(',');
        sb.append("mSSPInfo");
        sb.append('=');
        sb.append(((this.mSSPInfo == null) ? "<null>" : this.mSSPInfo));
        sb.append(',');
        sb.append("majorVersion");
        sb.append('=');
        sb.append(((this.majorVersion == null) ? "<null>" : this.majorVersion));
        sb.append(',');
        sb.append("messagingMode");
        sb.append('=');
        sb.append(((this.messagingMode == null) ? "<null>" : this.messagingMode));
        sb.append(',');
        sb.append("minorVersion");
        sb.append('=');
        sb.append(((this.minorVersion == null) ? "<null>" : this.minorVersion));
        sb.append(',');
        sb.append("mobileUser");
        sb.append('=');
        sb.append(((this.mobileUser == null) ? "<null>" : this.mobileUser));
        sb.append(',');
        sb.append("signatureProfile");
        sb.append('=');
        sb.append(((this.signatureProfile == null) ? "<null>" : this.signatureProfile));
        sb.append(',');
        sb.append("timeOut");
        sb.append('=');
        sb.append(((this.timeOut == null) ? "<null>" : this.timeOut));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
