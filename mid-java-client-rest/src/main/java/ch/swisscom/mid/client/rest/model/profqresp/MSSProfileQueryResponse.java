
package ch.swisscom.mid.client.rest.model.profqresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MSS_ProfileResp"
})
public class MSSProfileQueryResponse {

    @JsonProperty("MSS_ProfileResp")
    private MSSProfileResp mSSProfileResp;

    @JsonProperty("MSS_ProfileResp")
    public MSSProfileResp getMSSProfileResp() {
        return mSSProfileResp;
    }

    @JsonProperty("MSS_ProfileResp")
    public void setMSSProfileResp(MSSProfileResp mSSProfileResp) {
        this.mSSProfileResp = mSSProfileResp;
    }

    public MSSProfileQueryResponse withMSSProfileResp(MSSProfileResp mSSProfileResp) {
        this.mSSProfileResp = mSSProfileResp;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSProfileQueryResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSProfileResp");
        sb.append('=');
        sb.append(((this.mSSProfileResp == null)?"<null>":this.mSSProfileResp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
