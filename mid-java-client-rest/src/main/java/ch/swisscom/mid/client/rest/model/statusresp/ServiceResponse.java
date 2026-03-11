package ch.swisscom.mid.client.rest.model.statusresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Description",
        "Geofencing",
        "App2App"
})
public class ServiceResponse {

    public static final String NULL = "<null>";

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Geofencing")
    private Geofencing geofencing;

    @JsonProperty("App2App")
    private App2App app2app;

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    public ServiceResponse withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("Geofencing")
    public Geofencing getGeofencing() {
        return geofencing;
    }

    @JsonProperty("Geofencing")
    public void setGeofencing(Geofencing geofencing) {
        this.geofencing = geofencing;
    }

    public ServiceResponse withGeofencing(Geofencing geofencing) {
        this.geofencing = geofencing;
        return this;
    }

    @JsonProperty("App2App")
    public App2App getApp2app() {
        return app2app;
    }

    @JsonProperty("App2App")
    public void setApp2app(App2App app2app) {
        this.app2app = app2app;
    }

    public ServiceResponse withApp2App(App2App app2app) {
        this.app2app = app2app;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ServiceResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null) ? NULL : this.description));
        sb.append(',');
        sb.append("geofencing");
        sb.append('=');
        sb.append(((this.geofencing == null) ? NULL : this.geofencing));
        sb.append(',');
        sb.append("app2app");
        sb.append('=');
        sb.append(((this.app2app == null) ? NULL : this.app2app));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
