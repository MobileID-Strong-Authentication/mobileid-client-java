package ch.swisscom.mid.client.model;

import ch.swisscom.mid.client.config.DefaultConfiguration;

/**
 * The information returned when the Geofencing additional service is requested to Mobile ID.
 */
public class GeofencingAdditionalServiceResponse extends AdditionalServiceResponse {

    /**
     * The error code that might be returned if the Geofencing service failed to gather requested data. If this field
     * is <code>null</code>, things are OK. See {@link GeofencingErrorCode}.
     */
    private GeofencingErrorCode errorCode;

    /**
     * The error message description. This this is <code>null</code> and if also {@link #errorCode} is null, things are OK.
     */
    private String errorMessage;

    /**
     * Current device location as a two-letter code country code (ISO-3166-1 alpha-2).
     */
    private String country;

    /**
     * Location data accuracy, in meters.
     */
    private int accuracy;

    /**
     * Location acquiring timestamp (formatting of "yyyy-MM-dd'T'HH:mm:ss.SSSXXX").
     */
    private String timestamp;

    /**
     * Floating point value of the confidence in the security of the used device (e.g. jailbroken devices vs normal secured devices).
     * Values range from 0 (means location data is very likely from a rooted or jailbroken device) to 1 (no abnormalities found in relation
     * to rooted or jailbroken devices).
     */
    private String deviceConfidence;

    /**
     * Floating point value of the confidence in the location acquiring quality. Values range from 0 (= location data is very likely falsified)
     * to 1 (no abnormalities found in relation to falsified data).
     */
    private String locationConfidence;

    public GeofencingAdditionalServiceResponse() {
        super(DefaultConfiguration.ADDITIONAL_SERVICE_GEOFENCING);
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeviceConfidence() {
        return deviceConfidence;
    }

    public void setDeviceConfidence(String deviceConfidence) {
        this.deviceConfidence = deviceConfidence;
    }

    public String getLocationConfidence() {
        return locationConfidence;
    }

    public void setLocationConfidence(String locationConfidence) {
        this.locationConfidence = locationConfidence;
    }

    public GeofencingErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(GeofencingErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "GeofencingAdditionalServiceResponse{" +
               "errorCode=" + errorCode +
               ", errorMessage='" + errorMessage + '\'' +
               ", country='" + country + '\'' +
               ", accuracy=" + accuracy +
               ", timestamp='" + timestamp + '\'' +
               ", deviceConfidence='" + deviceConfidence + '\'' +
               ", locationConfidence='" + locationConfidence + '\'' +
               "} " + super.toString();
    }

}
