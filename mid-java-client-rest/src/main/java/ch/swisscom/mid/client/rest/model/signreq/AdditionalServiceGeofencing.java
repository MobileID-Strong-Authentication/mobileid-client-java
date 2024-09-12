
package ch.swisscom.mid.client.rest.model.signreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Description",
    "GeoFencingRequest"

})
public class AdditionalServiceGeofencing extends AdditionalService {

    @JsonProperty("GeoFencingRequest")
    private GeoFencingRequest geoFencingRequest;

    @JsonProperty("GeoFencingRequest")
    public GeoFencingRequest getGeoFencingRequest() {
        return geoFencingRequest;
    }


    public void setGeoFencingReqeust(GeoFencingRequest geoFencingRequest) {
        this.geoFencingRequest = geoFencingRequest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AdditionalServiceGeofencing.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("description");
        sb.append('=');
        sb.append(((this.getDescription() == null)?"<null>":this.getDescription()));
        sb.append(',');
        sb.append("GeoFencingRequest");
        sb.append('=');
        sb.append(((this.geoFencingRequest == null)?"<null>":this.geoFencingRequest));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
