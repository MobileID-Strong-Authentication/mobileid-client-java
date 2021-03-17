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
package ch.swisscom.mid.client.rest.model.statusresp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "AP_Info",
    "MSSP_Info",
    "MSS_Signature",
    "MajorVersion",
    "MinorVersion",
    "MobileUser",
    "ServiceResponses",
    "Status"
})
public class MSSStatusResp {

    @JsonProperty("AP_Info")
    private APInfo aPInfo;
    @JsonProperty("MSSP_Info")
    private MSSPInfo mSSPInfo;
    @JsonProperty("MSS_Signature")
    private MSSSignature mSSSignature;
    @JsonProperty("MajorVersion")
    private String majorVersion;
    @JsonProperty("MinorVersion")
    private String minorVersion;
    @JsonProperty("MobileUser")
    private MobileUser mobileUser;
    @JsonProperty("ServiceResponses")
    private List<ServiceResponse> serviceResponses = new ArrayList<ServiceResponse>();
    @JsonProperty("Status")
    private Status status;

    @JsonProperty("AP_Info")
    public APInfo getAPInfo() {
        return aPInfo;
    }

    @JsonProperty("AP_Info")
    public void setAPInfo(APInfo aPInfo) {
        this.aPInfo = aPInfo;
    }

    public MSSStatusResp withAPInfo(APInfo aPInfo) {
        this.aPInfo = aPInfo;
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

    public MSSStatusResp withMSSPInfo(MSSPInfo mSSPInfo) {
        this.mSSPInfo = mSSPInfo;
        return this;
    }

    @JsonProperty("MSS_Signature")
    public MSSSignature getMSSSignature() {
        return mSSSignature;
    }

    @JsonProperty("MSS_Signature")
    public void setMSSSignature(MSSSignature mSSSignature) {
        this.mSSSignature = mSSSignature;
    }

    public MSSStatusResp withMSSSignature(MSSSignature mSSSignature) {
        this.mSSSignature = mSSSignature;
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

    public MSSStatusResp withMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
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

    public MSSStatusResp withMinorVersion(String minorVersion) {
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

    public MSSStatusResp withMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
        return this;
    }

    @JsonProperty("ServiceResponses")
    public List<ServiceResponse> getServiceResponses() {
        return serviceResponses;
    }

    @JsonProperty("ServiceResponses")
    public void setServiceResponses(List<ServiceResponse> serviceResponses) {
        this.serviceResponses = serviceResponses;
    }

    public MSSStatusResp withServiceResponses(List<ServiceResponse> serviceResponses) {
        this.serviceResponses = serviceResponses;
        return this;
    }

    @JsonProperty("Status")
    public Status getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(Status status) {
        this.status = status;
    }

    public MSSStatusResp withStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSStatusResp.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("aPInfo");
        sb.append('=');
        sb.append(((this.aPInfo == null)?"<null>":this.aPInfo));
        sb.append(',');
        sb.append("mSSPInfo");
        sb.append('=');
        sb.append(((this.mSSPInfo == null)?"<null>":this.mSSPInfo));
        sb.append(',');
        sb.append("mSSSignature");
        sb.append('=');
        sb.append(((this.mSSSignature == null)?"<null>":this.mSSSignature));
        sb.append(',');
        sb.append("majorVersion");
        sb.append('=');
        sb.append(((this.majorVersion == null)?"<null>":this.majorVersion));
        sb.append(',');
        sb.append("minorVersion");
        sb.append('=');
        sb.append(((this.minorVersion == null)?"<null>":this.minorVersion));
        sb.append(',');
        sb.append("mobileUser");
        sb.append('=');
        sb.append(((this.mobileUser == null)?"<null>":this.mobileUser));
        sb.append(',');
        sb.append("serviceResponses");
        sb.append('=');
        sb.append(((this.serviceResponses == null)?"<null>":this.serviceResponses));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
