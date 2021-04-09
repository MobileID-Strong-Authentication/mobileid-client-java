
package ch.swisscom.mid.client.rest.model.receiptresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "AP_Info",
    "MSSP_Info",
    "MajorVersion",
    "MinorVersion",
    "Status"
})
public class MSSReceiptResp {

    @JsonProperty("AP_Info")
    private APInfo aPInfo;
    @JsonProperty("MSSP_Info")
    private MSSPInfo mSSPInfo;
    @JsonProperty("MajorVersion")
    private String majorVersion;
    @JsonProperty("MinorVersion")
    private String minorVersion;
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

    public MSSReceiptResp withAPInfo(APInfo aPInfo) {
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

    public MSSReceiptResp withMSSPInfo(MSSPInfo mSSPInfo) {
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

    public MSSReceiptResp withMajorVersion(String majorVersion) {
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

    public MSSReceiptResp withMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
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

    public MSSReceiptResp withStatus(Status status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSReceiptResp.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("aPInfo");
        sb.append('=');
        sb.append(((this.aPInfo == null)?"<null>":this.aPInfo));
        sb.append(',');
        sb.append("mSSPInfo");
        sb.append('=');
        sb.append(((this.mSSPInfo == null)?"<null>":this.mSSPInfo));
        sb.append(',');
        sb.append("majorVersion");
        sb.append('=');
        sb.append(((this.majorVersion == null)?"<null>":this.majorVersion));
        sb.append(',');
        sb.append("minorVersion");
        sb.append('=');
        sb.append(((this.minorVersion == null)?"<null>":this.minorVersion));
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
