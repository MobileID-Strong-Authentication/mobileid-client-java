
package ch.swisscom.mid.client.rest.model.receiptresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ReceiptResponseExtension"
})
public class StatusDetail {

    @JsonProperty("ReceiptResponseExtension")
    private ReceiptResponseExtension receiptResponseExtension;

    @JsonProperty("ReceiptResponseExtension")
    public ReceiptResponseExtension getReceiptResponseExtension() {
        return receiptResponseExtension;
    }

    @JsonProperty("ReceiptResponseExtension")
    public void setReceiptResponseExtension(ReceiptResponseExtension receiptResponseExtension) {
        this.receiptResponseExtension = receiptResponseExtension;
    }

    public StatusDetail withReceiptResponseExtension(ReceiptResponseExtension receiptResponseExtension) {
        this.receiptResponseExtension = receiptResponseExtension;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StatusDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("receiptResponseExtension");
        sb.append('=');
        sb.append(((this.receiptResponseExtension == null)?"<null>":this.receiptResponseExtension));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
