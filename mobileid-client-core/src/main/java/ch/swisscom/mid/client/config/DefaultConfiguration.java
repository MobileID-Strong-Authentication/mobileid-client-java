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
package ch.swisscom.mid.client.config;

import ch.swisscom.mid.client.model.SignatureProfiles;

public class DefaultConfiguration {

    public static final String DEFAULT_INTERNET_BASE_URL = "https://mobileid.swisscom.com";

    public static final String REST_ENDPOINT_SUB_URL = "/rest/service";
    public static final String SOAP_SIGNATURE_PORT_SUB_URL = "/soap/services/MSS_SignaturePort";
    public static final String SOAP_STATUS_QUERY_PORT_SUB_URL = "/soap/services/MSS_StatusQueryPort";
    public static final String SOAP_RECEIPT_PORT_SUB_URL = "/soap/services/MSS_ReceiptPort";
    public static final String SOAP_PROFILE_QUERY_PORT_SUB_URL = "/soap/services/MSS_ProfileQueryPort";

    public static final String DEFAULT_MSSP_ID = "http://mid.swisscom.ch/";

    public static final String ADDITIONAL_SERVICE_USER_LANG_URI = "http://mss.ficom.fi/TS102204/v1.0.0#userLang";
    public static final String ADDITIONAL_SERVICE_SIGNATURE_VALIDATION_URI = "http://uri.etsi.org/TS102204/v1.1.2#validate";
    public static final String ADDITIONAL_SERVICE_GEOFENCING = "http://mid.swisscom.ch/as#geofencing";

    public static final String SIGNATURE_REQUEST_MAJOR_VERSION = "1";
    public static final String SIGNATURE_REQUEST_MINOR_VERSION = "2";
    public static final String SIGNATURE_MODE_SYNC = "synch";
    public static final String SIGNATURE_MODE_ASYNC = "asynch";
    public static final int SIGNATURE_DEFAULT_TIME_OUT_IN_SECONDS = 80;
    public static final int SIGNATURE_MINIMUM_TIME_OUT_IN_SECONDS = 15;
    public static final int SIGNATURE_MAXIMUM_TIME_OUT_IN_SECONDS = 300;
    public static final String SIGNATURE_DEFAULT_SIGNATURE_PROFILE = SignatureProfiles.DEFAULT_PROFILE;

    public static final String STATUS_QUERY_REQUEST_MAJOR_VERSION = "1";
    public static final String STATUS_QUERY_REQUEST_MINOR_VERSION = "1";

    public static final String PROFILE_REQUEST_MAJOR_VERSION = "2";
    public static final String PROFILE_REQUEST_MINOR_VERSION = "0";

    public static final String RECEIPT_REQUEST_MAJOR_VERSION = "1";
    public static final String RECEIPT_REQUEST_MINOR_VERSION = "1";
    public static final String RECEIPT_PROFILE_LANGUAGE = "LANGUAGE";
    public static final String RECEIPT_PROFILE_URI = "http://mss.swisscom.ch/synch";

    public static final int HTTP_CLIENT_MAX_TOTAL_CONNECTIONS = 20;
    public static final int HTTP_CLIENT_DEFAULT_CONNECTIONS_PER_ROUTE = 10;
    public static final int HTTP_CLIENT_DEFAULT_CONNECTION_TIMEOUT_IN_MS = 15 * 1000;
    public static final int HTTP_CLIENT_DEFAULT_SOCKET_READ_TIMEOUT_IN_MS = 120 * 1000;

}
