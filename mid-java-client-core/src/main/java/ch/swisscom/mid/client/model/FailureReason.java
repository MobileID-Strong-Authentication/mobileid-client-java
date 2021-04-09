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

/**
 * Enum with all the main reasons for which a communication between the MID Client and a configured Mobile ID service can
 * fail. A reason from this list is wrapped in a {@link Fault} and then wrapped again in a thrown exception of
 * type {@link ch.swisscom.mid.client.MIDFlowException}. Callers can inspect the Fault and one of these reasons and understand
 * what failed. Some of these reasons will also put some extra info the Fault's fields.
 */
public enum FailureReason implements DocumentedEnum {

    REQUEST_PREPARATION_FAILURE("Assembling the MSS request (signature, receipt, profile) failed. Please check "
                                + "the MID client configuration and the request data."),
    HOST_CONNECTION_FAILURE("Failed to connect to the endpoint's host. Is it really reachable or DNS resolvable "
                            + "from MID client's host environment?"),
    HOST_CONNECT_TIMEOUT_FAILURE("The host was found, it seems to be reachable but the Mobile ID server is not "
                                 + "responding to the TCP connection request. Please check that the port is correct and "
                                 + "that you can connect to the server."),
    HTTP_DATA_TRANSFER_FAILURE("The response received from the Mobile ID server is malformed or cannot be interpreted. "
                               + "Please check the logs and the exception message printed by the MID client. In some cases, this "
                               + "signals a bug in the MID client, which might not be prepared to handle a certain response from "
                               + "the server"),
    TLS_CONNECTION_FAILURE("Failed to do the TLS handshake with the Mobile ID server. Please check the configured "
                           + "keystore and truststore. Is the right certificate (plus private key) correctly configured in the "
                           + "keystore? Is the CA's certificate added to the truststore (if it is not already trusted on "
                           + "MID client's host)?"),
    HTTP_COMMUNICATION_FAILURE("The transfer of HTTP data from the client to the server failed at some point. "
                               + "Is the connection stable? Please try the request again."),
    RESPONSE_TIMEOUT_FAILURE("Failed to received a response from the Mobile ID server in a timely manner. This can happen "
                             + "if the user needs a lot of time to answer the signature request or if the timeouts configured for "
                             + "this flow are too tight. Please check the client configuration HTTP response timeout and the request "
                             + "response timeout"),
    RESPONSE_PARSING_FAILURE("The response received from the Mobile ID server is correct but cannot be parsed by the "
                             + "MID client and converted into a valid object model. This signals a mismatch between the "
                             + "data sent by the server and the data that the client expects to received. Are you using an "
                             + "older version of the MID client? Please check the logs of the client and the printed "
                             + "exception message"),
    MID_SERVICE_FAILURE("This class of errors covers normal exceptional cases that are part of the MSS flows and "
                        + "that relate to the request content (is there an invalid parameter set to the request?) or to the "
                        + "user response (user cancelled, user never answered the signature request, etc). Please check the "
                        + "Fault object embedded in this exception, as it contains more details about what failed and why"),
    MID_INVALID_RESPONSE_FAILURE("This class of errors are really troublesome, as their cause is not entirely clear. "
                                 + "Please check the exception message printed by the client"),
    UNKNOWN_FAILURE("Unknown failure occurred here. Please check the exception message printed by the client");

    private final String description;

    FailureReason(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return null;
    }

    // toString: omit the description, as it is quite long and perhaps you don't want to see it in each printed log message
    @Override
    public String toString() {
        return name();
    }
}
