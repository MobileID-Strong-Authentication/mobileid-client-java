
package ch.swisscom.mid.client.rest.model.signresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MSS_SignatureResp"
})
public class MSSSignatureResponse {

    @JsonProperty("MSS_SignatureResp")
    private MSSSignatureResp mSSSignatureResp;

    @JsonProperty("MSS_SignatureResp")
    public MSSSignatureResp getMSSSignatureResp() {
        return mSSSignatureResp;
    }

    @JsonProperty("MSS_SignatureResp")
    public void setMSSSignatureResp(MSSSignatureResp mSSSignatureResp) {
        this.mSSSignatureResp = mSSSignatureResp;
    }

    public MSSSignatureResponse withMSSSignatureResp(MSSSignatureResp mSSSignatureResp) {
        this.mSSSignatureResp = mSSSignatureResp;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSSignatureResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSSignatureResp");
        sb.append('=');
        sb.append(((this.mSSSignatureResp == null)?"<null>":this.mSSSignatureResp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
