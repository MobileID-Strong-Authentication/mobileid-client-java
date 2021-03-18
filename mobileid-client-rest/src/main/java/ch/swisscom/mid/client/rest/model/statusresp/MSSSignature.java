
package ch.swisscom.mid.client.rest.model.statusresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Base64Signature"
})
public class MSSSignature {

    @JsonProperty("Base64Signature")
    private String base64Signature;

    @JsonProperty("Base64Signature")
    public String getBase64Signature() {
        return base64Signature;
    }

    @JsonProperty("Base64Signature")
    public void setBase64Signature(String base64Signature) {
        this.base64Signature = base64Signature;
    }

    public MSSSignature withBase64Signature(String base64Signature) {
        this.base64Signature = base64Signature;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSSignature.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("base64Signature");
        sb.append('=');
        sb.append(((this.base64Signature == null)?"<null>":this.base64Signature));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
