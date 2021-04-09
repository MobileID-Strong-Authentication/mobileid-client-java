
package ch.swisscom.mid.client.rest.model.profqreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MSS_ProfileReq"
})
public class MSSProfileQueryRequest {

    @JsonProperty("MSS_ProfileReq")
    private MSSProfileReq mSSProfileReq;

    @JsonProperty("MSS_ProfileReq")
    public MSSProfileReq getMSSProfileReq() {
        return mSSProfileReq;
    }

    @JsonProperty("MSS_ProfileReq")
    public void setMSSProfileReq(MSSProfileReq mSSProfileReq) {
        this.mSSProfileReq = mSSProfileReq;
    }

    public MSSProfileQueryRequest withMSSProfileReq(MSSProfileReq mSSProfileReq) {
        this.mSSProfileReq = mSSProfileReq;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSProfileQueryRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSProfileReq");
        sb.append('=');
        sb.append(((this.mSSProfileReq == null)?"<null>":this.mSSProfileReq));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
