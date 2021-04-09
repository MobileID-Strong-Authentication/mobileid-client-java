
package ch.swisscom.mid.client.rest.model.signreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MSS_SignatureReq"
})
public class MSSSignatureRequest {

    @JsonProperty("MSS_SignatureReq")
    private MSSSignatureReq mSSSignatureReq;

    @JsonProperty("MSS_SignatureReq")
    public MSSSignatureReq getMSSSignatureReq() {
        return mSSSignatureReq;
    }

    @JsonProperty("MSS_SignatureReq")
    public void setMSSSignatureReq(MSSSignatureReq mSSSignatureReq) {
        this.mSSSignatureReq = mSSSignatureReq;
    }

    public MSSSignatureRequest withMSSSignatureReq(MSSSignatureReq mSSSignatureReq) {
        this.mSSSignatureReq = mSSSignatureReq;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSSignatureRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSSignatureReq");
        sb.append('=');
        sb.append(((this.mSSSignatureReq == null)?"<null>":this.mSSSignatureReq));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
