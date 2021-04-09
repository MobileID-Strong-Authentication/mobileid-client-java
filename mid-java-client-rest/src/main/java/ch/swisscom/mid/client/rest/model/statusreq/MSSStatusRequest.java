
package ch.swisscom.mid.client.rest.model.statusreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MSS_StatusReq"
})
public class MSSStatusRequest {

    @JsonProperty("MSS_StatusReq")
    private MSSStatusReq mSSStatusReq;

    @JsonProperty("MSS_StatusReq")
    public MSSStatusReq getMSSStatusReq() {
        return mSSStatusReq;
    }

    @JsonProperty("MSS_StatusReq")
    public void setMSSStatusReq(MSSStatusReq mSSStatusReq) {
        this.mSSStatusReq = mSSStatusReq;
    }

    public MSSStatusRequest withMSSStatusReq(MSSStatusReq mSSStatusReq) {
        this.mSSStatusReq = mSSStatusReq;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSStatusRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSStatusReq");
        sb.append('=');
        sb.append(((this.mSSStatusReq == null)?"<null>":this.mSSStatusReq));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
