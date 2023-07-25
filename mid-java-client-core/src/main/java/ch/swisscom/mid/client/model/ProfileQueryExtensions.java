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

public class ProfileQueryExtensions {

    /**
     * A list of all available Secure Signature Creation Devices of the user.
     */
    public static final String SSCDS = "sscds";

    /**
     * User account state details.
     */
    public static final String ACCOUNT_STATE = "state";

    /**
     * X.509 mobile user certificate binary content and details, incl. subject name.
     * The subject name contains the user’s unique MID serial number value.
     */
    public static final String CERTIFICATES = "certs";

    /**
     * Mobile ID PIN status.
     */
    public static final String PIN_STATUS = "pinstatus";

    /**
     * Recovery Code status (has it been created: true or false)
     */
    public static final String RECOVERY_CODE_STATUS = "rcstatus";

    /**
     * Auto-Activation status (is it enabled: true or false), see section 5.
     */
    public static final String AUTO_ACTIVATION_STATUS = "aastatus";

    /**
     * Mobile ID SIM card details, for example the Mobile Network Operator
     */
    public static final String CARD_DETAILS = "carddetails";
}
