
package ch.swisscom.mid.client.rest.model.signresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Accuracy",
    "Country",
    "DeviceConfidence",
    "LocationConfidence",
    "Timestamp",
    "ErrorCode",
    "ErrorMessage"
})
public class Geofencing {

    @JsonProperty("Accuracy")
    private String accuracy;
    @JsonProperty("Country")
    private String country;
    @JsonProperty("DeviceConfidence")
    private String deviceConfidence;
    @JsonProperty("LocationConfidence")
    private String locationConfidence;
    @JsonProperty("Timestamp")
    private String timestamp;
    @JsonProperty("ErrorCode")
    private String errorCode;
    @JsonProperty("ErrorMessage")
    private String errorMessage;

    @JsonProperty("Accuracy")
    public String getAccuracy() {
        return accuracy;
    }

    @JsonProperty("Accuracy")
    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public Geofencing withAccuracy(String accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    @JsonProperty("Country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("Country")
    public void setCountry(String country) {
        this.country = country;
    }

    public Geofencing withCountry(String country) {
        this.country = country;
        return this;
    }

    @JsonProperty("DeviceConfidence")
    public String getDeviceConfidence() {
        return deviceConfidence;
    }

    @JsonProperty("DeviceConfidence")
    public void setDeviceConfidence(String deviceConfidence) {
        this.deviceConfidence = deviceConfidence;
    }

    public Geofencing withDeviceConfidence(String deviceConfidence) {
        this.deviceConfidence = deviceConfidence;
        return this;
    }

    @JsonProperty("LocationConfidence")
    public String getLocationConfidence() {
        return locationConfidence;
    }

    @JsonProperty("LocationConfidence")
    public void setLocationConfidence(String locationConfidence) {
        this.locationConfidence = locationConfidence;
    }

    public Geofencing withLocationConfidence(String locationConfidence) {
        this.locationConfidence = locationConfidence;
        return this;
    }

    @JsonProperty("Timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("Timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Geofencing withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @JsonProperty("ErrorCode")
    public String getErrorCode() {
        return errorCode;
    }

    @JsonProperty("ErrorCode")
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public Geofencing withErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    @JsonProperty("ErrorMessage")
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty("ErrorMessage")
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Geofencing withErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Geofencing.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("accuracy");
        sb.append('=');
        sb.append(((this.accuracy == null)?"<null>":this.accuracy));
        sb.append(',');
        sb.append("country");
        sb.append('=');
        sb.append(((this.country == null)?"<null>":this.country));
        sb.append(',');
        sb.append("deviceConfidence");
        sb.append('=');
        sb.append(((this.deviceConfidence == null)?"<null>":this.deviceConfidence));
        sb.append(',');
        sb.append("locationConfidence");
        sb.append('=');
        sb.append(((this.locationConfidence == null)?"<null>":this.locationConfidence));
        sb.append(',');
        sb.append("timestamp");
        sb.append('=');
        sb.append(((this.timestamp == null)?"<null>":this.timestamp));
        sb.append(',');
        sb.append("errorCode");
        sb.append('=');
        sb.append(((this.errorCode == null)?"<null>":this.errorCode));
        sb.append(',');
        sb.append("errorMessage");
        sb.append('=');
        sb.append(((this.errorMessage == null)?"<null>":this.errorMessage));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
