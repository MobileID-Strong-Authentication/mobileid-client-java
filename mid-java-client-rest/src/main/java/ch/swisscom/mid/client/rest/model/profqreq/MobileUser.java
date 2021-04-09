
package ch.swisscom.mid.client.rest.model.profqreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MSISDN"
})
public class MobileUser {

    @JsonProperty("MSISDN")
    private String msisdn;

    @JsonProperty("MSISDN")
    public String getMsisdn() {
        return msisdn;
    }

    @JsonProperty("MSISDN")
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public MobileUser withMsisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MobileUser.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("msisdn");
        sb.append('=');
        sb.append(((this.msisdn == null)?"<null>":this.msisdn));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
