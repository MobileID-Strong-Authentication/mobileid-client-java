
package ch.swisscom.mid.client.rest.model.statusreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "URI"
})
public class MsspId {

    @JsonProperty("URI")
    private String uri;

    @JsonProperty("URI")
    public String getUri() {
        return uri;
    }

    @JsonProperty("URI")
    public void setUri(String uri) {
        this.uri = uri;
    }

    public MsspId withUri(String uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MsspId.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("uri");
        sb.append('=');
        sb.append(((this.uri == null)?"<null>":this.uri));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
