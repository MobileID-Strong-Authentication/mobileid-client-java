/*
 * Copyright 2021-2025 Swisscom (Schweiz) AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.swisscom.mid.client.model;

import ch.swisscom.mid.client.impl.Loggers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum StatusCode implements DocumentedEnum {

    REQUEST_OK(100, false, "The request from the AP has been accepted."),
    WRONG_PARAM(101, true, "The AP’s request contains wrong parameters."),
    MISSING_PARAM(102, true, "The AP’s request has missing parameters."),
    WRONG_DATA_LENGTH(103, true, "The AP’s request contains a DTBD message "
            + "that is exceeding the max. allowed length."),
    UNAUTHORIZED_ACCESS(104, true, "AP is not authorized to access the Mobile ID API. "
            + "This is typically due to a wrong AP_ID value or missing X509 client certificate."),
    UNKNOWN_CLIENT(105, true, "The MSISDN value is unknown to the MID service. "
            + "There is no Mobile ID user with that MSISDN value."),
    INAPPROPRIATE_DATA(107, true, "The AP’s request was not accepted due to inappropriate data. "
            + "Typically, the DTBD message does not contain the mandatory prefix string "
            + "(see section 2.19 of the Reference Guide) that is a unique string for each AP."),
    INCOMPATIBLE_INTERFACE(108, true, "The AP’s request contains bad data. "
            + "Typically, a wrong MajorVersion or MinorVersion value has been set in the request."),
    UNSUPPORTED_PROFILE(109, true, "Either the AP has specified an MSS signature profile value "
            + "that the MSSP does not support or the AP is not authorized to use the "
            + "Signature Profile. See section 3.2.1 of the Reference Guide."),
    EXPIRED_TRANSACTION(208, true, "The transaction timed out. The AP may try again."),
    OTA_ERROR(209, true, "A Problem related to the MSSP internal Over-The-Air (OTA) communication "
            + "with the Mobile ID user’s SIM. "
            + "Typically, there is a temporary problem with SMS communication."),
    USER_CANCEL(401, true, "The user cancelled the request at the mobile phone."),
    PIN_NR_BLOCKED(402, true, "The Mobile ID PIN of the SIM method is blocked. "
            + "The user must reactivate the Mobile ID SIM card on the Mobile ID selfcare portal."),
    CARD_BLOCKED(403, true, "The Mobile ID user is currently suspended. "
            + "Please contact Swisscom Support."),
    NO_KEY_FOUND(404, true, "The Mobile ID user exists but is not in an active state. "
            + "The user must activate the account on the Mobile ID selfcare portal."),
    PB_SIGNATURE_PROCESS(406, true, "A signature transaction is already on-going. "
            + "Please try again later."),
    NO_CERT_FOUND(422, true, "The Mobile ID user exists but is not in an active state. "
            + "The user must activate the account on the Mobile ID selfcare portal."),
    GEOFENCING_POLICY_VIOLATION(450, true, "Geo policy for referenced AP ID was violated. "
            + "Please try again later or contact Swisscom Support, if the problem persists."),
    SIGNATURE(500, false, "The MSS Signature transaction was successful."),
    REVOKED_CERTIFICATE(501, false, "The Mobile ID user’s509 certificate has been revoked. "
            + "The user must re-activate the account on the Mobile ID selfcare portal."),
    VALID_SIGNATURE(502, false, "The MSS Signature transaction was successful."),
    INVALID_SIGNATURE(503, false, "The MSS Signature transaction failed due to invalid signature data. "
            + "The user must re-activate the account on the Mobile ID selfcare portal."),
    OUTSTANDING_TRANSACTION(504, false, "The MSS Signature transaction is outstanding. "
            + "The AP must try again later."),
    CONNECTION_REFUSED(780, true, "The connection to the service was refused. "
            + "The client did not present a valid TLS certificate"),
    INTERNAL_ERROR(900, true, "An internal error on MSSP has occurred. "
            + "Please try again later or contact Swisscom Support, if the problem persists.");

    private static final Logger log = LoggerFactory.getLogger(Loggers.CLIENT);

    private final int code;

    private final boolean fault;

    private final String description;

    StatusCode(int code, boolean fault, String description) {
        this.code = code;
        this.description = description;
        this.fault = fault;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public boolean isFault() {
        return fault;
    }

    public static StatusCode getByStatusCodeString(String statusCode) {
        if (statusCode == null || statusCode.isEmpty()) {
            return null;
        }
        int code;
        try {
            statusCode = statusCode.trim();
            if (statusCode.length() > 0 && statusCode.charAt(0) == '_') {
                statusCode = statusCode.substring(1);
            }
            code = Integer.parseInt(statusCode);
        } catch (NumberFormatException e) {
            log.warn("Cannot parse status code [{}] to an integer", statusCode, e);
            return null;
        }
        return getByStatusCodeValue(code);
    }

    public static StatusCode getByStatusCodeValue(int value) {
        for (StatusCode element : values()) {
            if (element.getCode() == value) {
                return element;
            }
        }
        log.warn("Cannot find a valid " + StatusCode.class.getSimpleName() + " for status code [{}]. Returning NULL", value);
        return null;
    }
}
