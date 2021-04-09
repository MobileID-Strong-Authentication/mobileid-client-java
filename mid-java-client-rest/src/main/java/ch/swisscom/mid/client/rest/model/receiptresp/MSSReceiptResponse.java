
package ch.swisscom.mid.client.rest.model.receiptresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MSS_ReceiptResp"
})
public class MSSReceiptResponse {

    @JsonProperty("MSS_ReceiptResp")
    private MSSReceiptResp mSSReceiptResp;

    @JsonProperty("MSS_ReceiptResp")
    public MSSReceiptResp getMSSReceiptResp() {
        return mSSReceiptResp;
    }

    @JsonProperty("MSS_ReceiptResp")
    public void setMSSReceiptResp(MSSReceiptResp mSSReceiptResp) {
        this.mSSReceiptResp = mSSReceiptResp;
    }

    public MSSReceiptResponse withMSSReceiptResp(MSSReceiptResp mSSReceiptResp) {
        this.mSSReceiptResp = mSSReceiptResp;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSReceiptResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSReceiptResp");
        sb.append('=');
        sb.append(((this.mSSReceiptResp == null)?"<null>":this.mSSReceiptResp));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
