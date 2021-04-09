package ch.swisscom.mid.client.model;

/**
 * Enumeration with possible Geofencing error codes.
 * See Mobile ID Reference Guide, section 3.2.4.2.4.
 */
public enum GeofencingErrorCode implements DocumentedEnum {

    FEATURE_DISABLED(100, "The geofencing feature option in the “More” menu is currently disabled."),
    CANNOT_RETRIEVE_LOCATION_INSUFFICIENT_RESOURCES(101,
                                                    "The app failed to retrieve the user's location possibly due to insufficient resources (network, GPS, etc.) or a timeout."),
    NO_GRANT_RESPONSE(102, "The user has not yet responded to the dialog that grants the app permission to access location services."),
    USER_DENIED_APP_PERMISSION(103, "The user has explicitly denied the app permission to access location services."),
    USER_CANNOT_ENABLE_LOCATION(104,
                                "This is on iOS only. The user cannot enable location services possibly due to active restrictions such as parental controls, corporate policy etc. being in place."),
    USER_TURNED_OFF_LOCATION(105, "The user has turned off location services device-wide (for all apps) from the system settings."),
    DEVICE_IN_AIRPLANE_MODE(106, "Location services are unavailable because the device is in Airplane mode."),
    CANNOT_RETRIEVE_LOCATION_UNKNOWN_CAUSE(120, "Location failed to the app for an unspecified reason."),
    INTERNAL_ERROR(121, "MSSP internal error (misconfiguration etc.)."),
    AP_NOT_AUTHORIZED_TO_REQUEST_GEOFENCING(122, "AP is not authorized to use Geofencing additional service."),
    USER_HAS_NON_SWISSCOM_SIM(123, "User has a non-Swisscom SIM card."),
    CANNOT_RETRIEVE_LOCATION_EMPTY_RESPONSE(200, "No location returned from mobile app."),
    USER_HAS_OUTDATED_APP(201, "App outdated, geofencing not supported.");

    private int code;

    private String description;

    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    GeofencingErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static GeofencingErrorCode getByCodeAsString(String code) {
        if (code == null) {
            return null;
        }

        int inputCode = Integer.parseInt(code);
        for (GeofencingErrorCode value : values()) {
            if (value.getCode() == inputCode) {
                return value;
            }
        }
        throw new IllegalArgumentException("Invalid Geofencing error code: [" + code + "]");
    }

}
