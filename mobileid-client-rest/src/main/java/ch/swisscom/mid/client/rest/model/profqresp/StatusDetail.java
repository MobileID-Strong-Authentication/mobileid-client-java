
package ch.swisscom.mid.client.rest.model.profqresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ProfileQueryExtension"
})
public class StatusDetail {

    @JsonProperty("ProfileQueryExtension")
    private ProfileQueryExtension profileQueryExtension;

    @JsonProperty("ProfileQueryExtension")
    public ProfileQueryExtension getProfileQueryExtension() {
        return profileQueryExtension;
    }

    @JsonProperty("ProfileQueryExtension")
    public void setProfileQueryExtension(ProfileQueryExtension profileQueryExtension) {
        this.profileQueryExtension = profileQueryExtension;
    }

    public StatusDetail withProfileQueryExtension(ProfileQueryExtension profileQueryExtension) {
        this.profileQueryExtension = profileQueryExtension;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StatusDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("profileQueryExtension");
        sb.append('=');
        sb.append(((this.profileQueryExtension == null)?"<null>":this.profileQueryExtension));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
