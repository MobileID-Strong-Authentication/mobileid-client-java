package ch.swisscom.mid.client.rest.model.signreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "RedirectUri"
})
@ToString(includeFieldNames = true)
public class App2AppRequest {

    @JsonProperty("RedirectUri")
    private String redirectUri;

    @JsonProperty("RedirectUri")
    public String getRedirectUri() {
        return redirectUri;
    }

    @JsonProperty("RedirectUri")
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public App2AppRequest withRedirectUri(String value) {
        this.redirectUri = value;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(App2AppRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("RedirectUri");
        sb.append('=');
        sb.append(((this.redirectUri == null) ? "<null>" : this.redirectUri));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
