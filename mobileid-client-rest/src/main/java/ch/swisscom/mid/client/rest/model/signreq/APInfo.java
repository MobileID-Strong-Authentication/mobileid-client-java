
package ch.swisscom.mid.client.rest.model.signreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "AP_ID",
    "AP_PWD",
    "AP_TransID",
    "Instant"
})
public class APInfo {

    @JsonProperty("AP_ID")
    private String apId;
    @JsonProperty("AP_PWD")
    private String apPwd;
    @JsonProperty("AP_TransID")
    private String aPTransID;
    @JsonProperty("Instant")
    private String instant;

    @JsonProperty("AP_ID")
    public String getApId() {
        return apId;
    }

    @JsonProperty("AP_ID")
    public void setApId(String apId) {
        this.apId = apId;
    }

    public APInfo withApId(String apId) {
        this.apId = apId;
        return this;
    }

    @JsonProperty("AP_PWD")
    public String getApPwd() {
        return apPwd;
    }

    @JsonProperty("AP_PWD")
    public void setApPwd(String apPwd) {
        this.apPwd = apPwd;
    }

    public APInfo withApPwd(String apPwd) {
        this.apPwd = apPwd;
        return this;
    }

    @JsonProperty("AP_TransID")
    public String getAPTransID() {
        return aPTransID;
    }

    @JsonProperty("AP_TransID")
    public void setAPTransID(String aPTransID) {
        this.aPTransID = aPTransID;
    }

    public APInfo withAPTransID(String aPTransID) {
        this.aPTransID = aPTransID;
        return this;
    }

    @JsonProperty("Instant")
    public String getInstant() {
        return instant;
    }

    @JsonProperty("Instant")
    public void setInstant(String instant) {
        this.instant = instant;
    }

    public APInfo withInstant(String instant) {
        this.instant = instant;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(APInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("apId");
        sb.append('=');
        sb.append(((this.apId == null)?"<null>":this.apId));
        sb.append(',');
        sb.append("apPwd");
        sb.append('=');
        sb.append(((this.apPwd == null)?"<null>":this.apPwd));
        sb.append(',');
        sb.append("aPTransID");
        sb.append('=');
        sb.append(((this.aPTransID == null)?"<null>":this.aPTransID));
        sb.append(',');
        sb.append("instant");
        sb.append('=');
        sb.append(((this.instant == null)?"<null>":this.instant));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
