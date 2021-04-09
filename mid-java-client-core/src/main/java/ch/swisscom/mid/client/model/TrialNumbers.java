/*
 * Copyright 2021 Swisscom (Schweiz) AG
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

@SuppressWarnings("unused") // used sometimes by clients when they need to test various exceptional cases
public class TrialNumbers {

    /**
     * 101 Error among the arguments of the request
     */
    public static final String ONE_THAT_GIVES_WRONG_PARAM = "41000092101";

    /**
     * 102 An argument in the request is missing
     */
    public static final String ONE_THAT_GIVES_MISSING_PARAM = "41000092102";

    /**
     * 103 The DataToBeSigned are too large. Limitations are due to the
     * Mobile Signature technology implemented by the MSSP.
     */
    public static final String ONE_THAT_GIVES_WRONG_DATA_LENGTH = "41000092103";

    /**
     * 104 The AP is unknown, or the password is wrong, or the AP asks for an additional
     * service for which it has not subscribed.
     */
    public static final String ONE_THAT_GIVES_UNAUTHORIZED_ACCESS = "41000092104";

    /**
     * 105 MSISDN is unknown
     */
    public static final String ONE_THAT_GIVES_UNKNOWN_CLIENT = "41000092105";

    /**
     * 107 DTBD matching failed
     */
    public static final String ONE_THAT_GIVES_INAPPROPRIATE_DATA = "41000092107";

    /**
     * 108 The minor version and/or major version parameters are inappropriate for the receiver of the message.
     */
    public static final String ONE_THAT_GIVES_INCOMPATIBLE_INTERFACE = "41000092108";

    /**
     * 109 The user does not support this Mobile Signature Profile
     */
    public static final String ONE_THAT_GIVES_UNSUPPORTED_PROFILE = "41000092109";

    /**
     * 208 Transaction Expiry date has been reached or Time out has lapsed.
     */
    public static final String ONE_THAT_GIVES_EXPIRED_TRANSACTION = "41000092208";

    /**
     * 209 The MSSP has not succeeded to contact the end-user's mobile equipment Bad connection...)
     */
    public static final String ONE_THAT_GIVES_OTA_ERROR = "41000092209";

    /**
     * 401 User cancelled the request
     */
    public static final String ONE_THAT_GIVES_USER_CANCEL = "41000092401";

    /**
     * 402 PIN of the mobile user is blocked
     */
    public static final String ONE_THAT_GIVES_PIN_NR_BLOCKED = "41000092402";

    /**
     * 403 Mobile user account has state INACTIVE or no SIM assigned
     */
    public static final String ONE_THAT_GIVES_CARD_BLOCKED = "41000092403";

    /**
     * 404 Mobile user account needs to be activated
     */
    public static final String ONE_THAT_GIVES_NO_KEY_FOUND = "41000092404";

    /**
     * 406 Signature request already in progress.
     */
    public static final String ONE_THAT_GIVES_PB_SIGNATURE_PROCESS = "41000092406";

    /**
     * 422 Certificate is expired
     */
    public static final String ONE_THAT_GIVES_NO_CERT_FOUND = "41000092422";

    /**
     * 900 Unknown Error
     */
    public static final String ONE_THAT_GIVES_INTERNAL_ERROR = "41000092900";

}
