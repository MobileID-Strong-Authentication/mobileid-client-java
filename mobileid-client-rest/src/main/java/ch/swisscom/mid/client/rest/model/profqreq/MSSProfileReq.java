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
package ch.swisscom.mid.client.rest.model.profqreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "AP_Info",
        "MSSP_Info",
        "MajorVersion",
        "MinorVersion",
        "MobileUser",
        "Params"
})
public class MSSProfileReq {

    @JsonProperty("AP_Info")
    private APInfo aPInfo;
    @JsonProperty("MSSP_Info")
    private MSSPInfo mSSPInfo;
    @JsonProperty("MajorVersion")
    private String majorVersion;
    @JsonProperty("MinorVersion")
    private String minorVersion;
    @JsonProperty("MobileUser")
    private MobileUser mobileUser;
    @JsonProperty("Params")
    private String params;

    @JsonProperty("AP_Info")
    public APInfo getAPInfo() {
        return aPInfo;
    }

    @JsonProperty("AP_Info")
    public void setAPInfo(APInfo aPInfo) {
        this.aPInfo = aPInfo;
    }

    public MSSProfileReq withAPInfo(APInfo aPInfo) {
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

    public MSSProfileReq withMSSPInfo(MSSPInfo mSSPInfo) {
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

    public MSSProfileReq withMajorVersion(String majorVersion) {
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

    public MSSProfileReq withMinorVersion(String minorVersion) {
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

    public MSSProfileReq withMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
        return this;
    }

    @JsonProperty("Params")
    public String getParams() {
        return params;
    }

    @JsonProperty("Params")
    public void setParams(String params) {
        this.params = params;
    }

    public MSSProfileReq withParams(String params) {
        this.params = params;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(MSSProfileReq.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("aPInfo");
        sb.append('=');
        sb.append(((this.aPInfo == null) ? "<null>" : this.aPInfo));
        sb.append(',');
        sb.append("mSSPInfo");
        sb.append('=');
        sb.append(((this.mSSPInfo == null) ? "<null>" : this.mSSPInfo));
        sb.append(',');
        sb.append("majorVersion");
        sb.append('=');
        sb.append(((this.majorVersion == null) ? "<null>" : this.majorVersion));
        sb.append(',');
        sb.append("minorVersion");
        sb.append('=');
        sb.append(((this.minorVersion == null) ? "<null>" : this.minorVersion));
        sb.append(',');
        sb.append("mobileUser");
        sb.append('=');
        sb.append(((this.mobileUser == null) ? "<null>" : this.mobileUser));
        sb.append(',');
        sb.append("params");
        sb.append('=');
        sb.append(((this.params == null) ? "<null>" : this.params));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
