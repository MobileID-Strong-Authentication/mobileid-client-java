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
package ch.swisscom.mid.client;

import ch.swisscom.mid.client.config.ConfigurationException;
import ch.swisscom.mid.client.model.*;

/**
 * Main interface for the Mobile ID client library. This provides access to the main Mobile ID services, as documented
 * in the Mobile ID Reference Guide (go to <a href="https://www.mobileid.ch/en/documents">Mobile ID documentation</a>, then
 * open the <i>Client Reference Guide</i>).
 *
 * <p>Operations implemented:
 * <ul>
 *     <li>MSS Signature (sync mode)</li>
 *     <li>MSS Signature (async mode) with status polling</li>
 *     <li>MSS Receipt (for a finished MSS Signature)</li>
 *     <li>MSS Profile Query</li>
 * </ul>
 *
 * <p>Technically wise, this MID Client library provides the following:
 * <ul>
 *     <li>Access to the Additional Services per MSS Signature</li>
 *     <li>Easy access to {@link ch.swisscom.mid.client.model.StatusCode Status Codes}, {@link ch.swisscom.mid.client.model.TrialNumbers Trial Numbers}</li>
 *     <li>Configurable validation of received signatures</li>
 *     <li>REST and SOAP implementation (depends on what you include in the classpath and select in the client configuration)</li>
 *     <li>Simple access and handling for network issues (connection timeout, response timeout, TLS handshake)</li>
 *     <li>Pluggable model, easy to swap out components and add your own</li>
 *     <li>Interfaces everywhere, easy to test and mock</li>
 *     <li>Thread-safe and connection-pooled default configuration; you can create one of these clients and use it for all you connections</li>
 * </ul>
 */
public interface MIDClient extends AutoCloseable {

    /**
     * Requests a MSS Signature to the connected Mobile ID service, using synchronous communication (the client's thread is
     * blocked until the final response is received; this could take a while, e.g. 80 seconds, so make sure that the HTTP response
     * timeout is properly set in the client configuration otherwise a timeout will occur). The response is returned to the
     * caller or, if something breaks during the communication, a {@link MIDClientException} is thrown.
     *
     * @param request the MSS Signature request to send
     * @return the response, as received from the Mobile ID service.
     * @throws ConfigurationException if something is missing from the client configuration, or is badly configuration
     * @throws MIDFlowException       if something fails during the communication with the backend services
     */
    SignatureResponse requestSyncSignature(SignatureRequest request);

    /**
     * Requests a MSS Signature to the connected Mobile ID service, using asynchronous communication (the method terminates
     * immediately, returning a signature object that is mostly empty, with a inner signature tracking object; the caller is
     * expected to poll for the status of this MSS Signature flow by repeatedly calling
     * {@link #pollForSignatureStatus(SignatureTracking)} with the aforementioned signature tracking
     * object). The method returns either with a response object and a tracking object to use in subsequent polls or by
     * throwing an exception is something is misaligned.
     *
     * @param request the MSS Signature request to send
     * @return a signature response object, containing the tracking element to use in subsequent polls.
     * @throws ConfigurationException if something is missing from the client configuration, or is badly configuration
     * @throws MIDFlowException       if something fails during the communication with the backend services
     */
    SignatureResponse requestAsyncSignature(SignatureRequest request);

    /**
     * Poll for the status of a previously requested asynchronous MSS Signature. The caller is expected to check the {@link
     * ch.swisscom.mid.client.model.Status} object from the returned response in order to learn about the current status
     * of the signature flow. If the signature is not finished, the caller needs to poll again, with the same input tracking
     * object.
     *
     * @param signatureTracking the signature tracking object received from the call to {@link #requestAsyncSignature(SignatureRequest)}
     * @return a response object that contains only the {@link ch.swisscom.mid.client.model.Status} populated with an intermediary
     *     status value or the final signature response
     * @throws MIDFlowException if something fails during the communication with the backend services
     */
    SignatureResponse pollForSignatureStatus(SignatureTracking signatureTracking);

    /**
     * A receipt can be sent to the mobile user that has just finished a digital signature. The call
     * is synchronous and optional. Making such a call allows the AP to send a confirmation message to
     * the mobile user (e.g. "Access granted").
     *
     * @param signatureTracking the signature tracking object received from the call to
     *                          {@link #requestAsyncSignature(SignatureRequest)} or {@link #requestSyncSignature(SignatureRequest)}
     * @param request           the request data additional to what the <code>signatureTracking</code> already provides
     * @return a response object containing the status of the operation.
     */
    ReceiptResponse requestSyncReceipt(SignatureTracking signatureTracking, ReceiptRequest request);

    /**
     * The profile of a mobile user can be queried using this method. No interaction with the mobile
     * user is performed. The backend just returns whatever data is available for the designated mobile user.
     * This operation is synchronous.
     *
     * @param request the profile request data
     * @return a response object containing the status outcome of the operation and the requested profile data.
     */
    ProfileResponse requestProfile(ProfileRequest request);

    /**
     * Disposes any internal resources allocated by this instance of the MID Client. This method should be used ONLY when the
     * MID Client cannot be used anymore (e.g. it is really not needed and needs to go away). The implementation of MID Client
     * should be thread-safe and resource pooled, so that it can be reused in as many threads and for as long as possible. Call
     * this method only when your app is shutting down or you really don't need the client anymore.
     */
    void close();

}
