
package ch.swisscom.mid.client.rest.model.signresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Instant",
    "MSSP_ID"
})
public class MSSPInfo {

    @JsonProperty("Instant")
    private String instant;
    @JsonProperty("MSSP_ID")
    private MsspId msspId;

    @JsonProperty("Instant")
    public String getInstant() {
        return instant;
    }

    @JsonProperty("Instant")
    public void setInstant(String instant) {
        this.instant = instant;
    }

    public MSSPInfo withInstant(String instant) {
        this.instant = instant;
        return this;
    }

    @JsonProperty("MSSP_ID")
    public MsspId getMsspId() {
        return msspId;
    }

    @JsonProperty("MSSP_ID")
    public void setMsspId(MsspId msspId) {
        this.msspId = msspId;
    }

    public MSSPInfo withMsspId(MsspId msspId) {
        this.msspId = msspId;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSPInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("instant");
        sb.append('=');
        sb.append(((this.instant == null)?"<null>":this.instant));
        sb.append(',');
        sb.append("msspId");
        sb.append('=');
        sb.append(((this.msspId == null)?"<null>":this.msspId));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
