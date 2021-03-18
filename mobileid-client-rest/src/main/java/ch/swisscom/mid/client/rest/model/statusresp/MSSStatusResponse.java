
package ch.swisscom.mid.client.rest.model.statusresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MSS_StatusResp"
})
public class MSSStatusResponse {

    @JsonProperty("MSS_StatusResp")
    private MSSStatusResp mSSStatusResp;

    @JsonProperty("MSS_StatusResp")
    public MSSStatusResp getMSSStatusResp() {
        return mSSStatusResp;
    }

    @JsonProperty("MSS_StatusResp")
    public void setMSSStatusResp(MSSStatusResp mSSStatusResp) {
        this.mSSStatusResp = mSSStatusResp;
    }

    public MSSStatusResponse withMSSStatusResp(MSSStatusResp mSSStatusResp) {
        this.mSSStatusResp = mSSStatusResp;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSStatusResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSStatusResp");
        sb.append('=');
        sb.append(((this.mSSStatusResp == null)?"<null>":this.mSSStatusResp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
