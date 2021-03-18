
package ch.swisscom.mid.client.rest.model.profqresp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MobileUserCertificate",
    "PinStatus",
    "State"
})
public class App {

    @JsonProperty("MobileUserCertificate")
    private List<MobileUserCertificate> mobileUserCertificate = new ArrayList<MobileUserCertificate>();
    @JsonProperty("PinStatus")
    private PinStatus pinStatus;
    @JsonProperty("State")
    private String state;

    @JsonProperty("MobileUserCertificate")
    public List<MobileUserCertificate> getMobileUserCertificate() {
        return mobileUserCertificate;
    }

    @JsonProperty("MobileUserCertificate")
    public void setMobileUserCertificate(List<MobileUserCertificate> mobileUserCertificate) {
        this.mobileUserCertificate = mobileUserCertificate;
    }

    public App withMobileUserCertificate(List<MobileUserCertificate> mobileUserCertificate) {
        this.mobileUserCertificate = mobileUserCertificate;
        return this;
    }

    @JsonProperty("PinStatus")
    public PinStatus getPinStatus() {
        return pinStatus;
    }

    @JsonProperty("PinStatus")
    public void setPinStatus(PinStatus pinStatus) {
        this.pinStatus = pinStatus;
    }

    public App withPinStatus(PinStatus pinStatus) {
        this.pinStatus = pinStatus;
        return this;
    }

    @JsonProperty("State")
    public String getState() {
        return state;
    }

    @JsonProperty("State")
    public void setState(String state) {
        this.state = state;
    }

    public App withState(String state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(App.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mobileUserCertificate");
        sb.append('=');
        sb.append(((this.mobileUserCertificate == null)?"<null>":this.mobileUserCertificate));
        sb.append(',');
        sb.append("pinStatus");
        sb.append('=');
        sb.append(((this.pinStatus == null)?"<null>":this.pinStatus));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null)?"<null>":this.state));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
