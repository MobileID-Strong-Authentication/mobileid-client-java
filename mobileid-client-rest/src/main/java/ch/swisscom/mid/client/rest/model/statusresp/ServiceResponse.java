
package ch.swisscom.mid.client.rest.model.statusresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Description",
    "Geofencing"
})
public class ServiceResponse {

    @JsonProperty("Description")
    private String description;
    @JsonProperty("Geofencing")
    private Geofencing geofencing;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ServiceResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
        sb.append(',');
        sb.append("geofencing");
        sb.append('=');
        sb.append(((this.geofencing == null)?"<null>":this.geofencing));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
