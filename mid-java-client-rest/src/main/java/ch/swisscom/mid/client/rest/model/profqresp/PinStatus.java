
package ch.swisscom.mid.client.rest.model.profqresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Blocked"
})
public class PinStatus {

    @JsonProperty("Blocked")
    private Boolean blocked;

    @JsonProperty("Blocked")
    public Boolean getBlocked() {
        return blocked;
    }

    @JsonProperty("Blocked")
    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public PinStatus withBlocked(Boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PinStatus.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("blocked");
        sb.append('=');
        sb.append(((this.blocked == null)?"<null>":this.blocked));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
