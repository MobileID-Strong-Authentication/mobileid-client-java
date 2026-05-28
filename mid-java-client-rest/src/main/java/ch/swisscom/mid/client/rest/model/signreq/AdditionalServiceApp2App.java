package ch.swisscom.mid.client.rest.model.signreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Description",
        "App2App"
})
public class AdditionalServiceApp2App extends AdditionalService {

    @JsonProperty("App2App")
    private App2AppRequest app2appRequest;

    @JsonProperty("App2App")
    public App2AppRequest getApp2appRequest() {
        return app2appRequest;
    }

    public void setApp2AppRequest(App2AppRequest app2appRequest) {
        this.app2appRequest = app2appRequest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AdditionalServiceApp2App.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("description");
        sb.append('=');
        sb.append(((this.getDescription() == null) ? "<null>" : this.getDescription()));
        sb.append(',');
        sb.append("app2app");
        sb.append('=');
        sb.append(((this.app2appRequest == null) ? "<null>" : this.app2appRequest));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
