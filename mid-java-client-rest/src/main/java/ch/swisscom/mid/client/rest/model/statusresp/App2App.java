package ch.swisscom.mid.client.rest.model.statusresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "AuthUri",
})
public class App2App {

    @JsonProperty("AuthUri")
    private String authUri;


    @JsonProperty("AuthUri")
    public String getAuthUri() {
        return authUri;
    }

    @JsonProperty("AuthUri")
    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    public App2App withAuthUri(String authUri) {
        this.authUri = authUri;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(App2App.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("authUri");
        sb.append('=');
        sb.append(((this.authUri == null) ? "<null>" : this.authUri));
        sb.append(']');

        return sb.toString();
    }

}
