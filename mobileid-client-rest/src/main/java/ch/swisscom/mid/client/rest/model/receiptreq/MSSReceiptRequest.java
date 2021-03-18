
package ch.swisscom.mid.client.rest.model.receiptreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MSS_ReceiptReq"
})
public class MSSReceiptRequest {

    @JsonProperty("MSS_ReceiptReq")
    private MSSReceiptReq mSSReceiptReq;

    @JsonProperty("MSS_ReceiptReq")
    public MSSReceiptReq getMSSReceiptReq() {
        return mSSReceiptReq;
    }

    @JsonProperty("MSS_ReceiptReq")
    public void setMSSReceiptReq(MSSReceiptReq mSSReceiptReq) {
        this.mSSReceiptReq = mSSReceiptReq;
    }

    public MSSReceiptRequest withMSSReceiptReq(MSSReceiptReq mSSReceiptReq) {
        this.mSSReceiptReq = mSSReceiptReq;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSReceiptRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSReceiptReq");
        sb.append('=');
        sb.append(((this.mSSReceiptReq == null)?"<null>":this.mSSReceiptReq));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
