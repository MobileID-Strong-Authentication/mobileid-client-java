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
package ch.swisscom.mid.client.rest;

import ch.swisscom.mid.client.config.TlsConfiguration;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.MimeType;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.swisscom.mid.client.MIDClient;
import ch.swisscom.mid.client.MIDFlowException;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.impl.MIDClientImpl;
import ch.swisscom.mid.client.model.*;

import static ch.swisscom.mid.client.rest.TestData.CUSTOM_AP_ID;
import static ch.swisscom.mid.client.rest.TestData.CUSTOM_AP_PASSWORD;
import static ch.swisscom.mid.client.rest.TestSupport.*;
import static ch.swisscom.mid.client.rest.TestSupport.fileToBytes;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;

public class SyncSignatureTest {

    private static WireMockServer server;
    private static MIDClient client;

    @BeforeAll
    public static void setUpThisClass() {
        server = new WireMockServer(options().port(8089));
        server.start();

        // client = new MIDClientImpl(buildConfig(buildTlsConfig("TLSv1.1")));
        client = new MIDClientImpl(buildConfig(buildTlsConfig(null)));
    }

    @AfterAll
    public static void tearDownThisClass() {
        client.close();
        server.stop();
    }

    // ----------------------------------------------------------------------------------------------------

    @Test
    public void testSignature_success() {
        server.stubFor(
            post(urlEqualTo(DefaultConfiguration.REST_ENDPOINT_SUB_URL))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", MimeType.JSON.toString())
                        .withBody(fileToString("/samples/rest-response-signature.json"))));

        SignatureRequest signatureRequest = buildSignatureRequest();
        SignatureResponse response = client.requestSyncSignature(signatureRequest);
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.SIGNATURE));
        assertThat(response.getStatus().getStatusCodeString(), is("500"));
        assertThat(response.getStatus().getStatusMessage(), is("SIGNATURE"));
        assertThat(response.getSignatureProfile(), is(TestData.CUSTOM_SIGNATURE_PROFILE));
        assertThat(response.getBase64Signature(), is(notNullValue()));
        assertThat(response.getBase64Signature().length(), is(TestData.BASE64_SIGNATURE_LENGTH));
    }

    @Test
    public void testSignature_success_overrideApIdAndApPassword() {
        server.stubFor(
            post(urlEqualTo(DefaultConfiguration.REST_ENDPOINT_SUB_URL))
                .withRequestBody(containing("\"" + CUSTOM_AP_ID + "\""))
                .withRequestBody(containing("\"" + CUSTOM_AP_PASSWORD + "\""))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", MimeType.JSON.toString())
                        .withBody(fileToString("/samples/rest-response-signature.json"))));

        SignatureRequest signatureRequest = buildSignatureRequest();
        signatureRequest.setOverrideApId(CUSTOM_AP_ID);
        signatureRequest.setOverrideApPassword(CUSTOM_AP_PASSWORD);

        SignatureResponse response = client.requestSyncSignature(signatureRequest);
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.SIGNATURE));
        assertThat(response.getStatus().getStatusCodeString(), is("500"));
        assertThat(response.getStatus().getStatusMessage(), is("SIGNATURE"));
        assertThat(response.getSignatureProfile(), is(TestData.CUSTOM_SIGNATURE_PROFILE));
        assertThat(response.getBase64Signature(), is(notNullValue()));
        assertThat(response.getBase64Signature().length(), is(TestData.BASE64_SIGNATURE_LENGTH));
        assertThat(response.getTracking().getOverrideApId(), is(CUSTOM_AP_ID));
        assertThat(response.getTracking().getOverrideApPassword(), is(CUSTOM_AP_PASSWORD));
    }

    @Test
    public void testSignature_userCancel() {
        server.stubFor(
            post(urlEqualTo("/rest/service"))
                .willReturn(
                    aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", MimeType.JSON.toString())
                        .withBody(fileToString("/samples/rest-response-fault-user-cancel.json"))));

        SignatureRequest signatureRequest = buildSignatureRequest();
        try {
            client.requestSyncSignature(signatureRequest);
            fail("A MIDFlowException was expected at this point");
        } catch (MIDFlowException exception) {
            Fault fault = exception.getFault();
            assertThat(fault.getFailureReason(), is(FailureReason.MID_SERVICE_FAILURE));
            assertThat(fault.getStatusCode(), is(StatusCode.USER_CANCEL));
            assertThat(fault.getStatusCodeString(), is("_401"));
        }
    }

    @Test
    public void testSignature_conFailure_responseTimeout() {
        server.stubFor(
            post(urlEqualTo("/rest/service"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody("")
                        .withFixedDelay(5000)));

        SignatureRequest signatureRequest = buildSignatureRequest();
        try {
            client.requestSyncSignature(signatureRequest);
            fail("A MIDFlowException was expected at this point");
        } catch (MIDFlowException exception) {
            Fault fault = exception.getFault();
            assertThat(fault.getFailureReason(), is(FailureReason.RESPONSE_TIMEOUT_FAILURE));
            assertThat(fault.getStatusCode(), is(StatusCode.INTERNAL_ERROR));
            assertThat(fault.getStatusCodeString(), is("INTERNAL_ERROR"));
        }
    }

    // ----------------------------------------------------------------------------------------------------

    private static SignatureRequest buildSignatureRequest() {
        SignatureRequest request = new SignatureRequest();
        request.setUserLanguage(UserLanguage.ENGLISH);
        request.getDataToBeSigned().setData("Test: Do you want to login?");
        request.getDataToBeSigned().setEncodingToUtf8();
        request.getDataToBeSigned().setMimeTypeToTextPlain();
        request.getMobileUser().setMsisdn(TrialNumbers.ONE_THAT_GIVES_MISSING_PARAM);
        request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
        request.addAdditionalService(new GeofencingAdditionalService());
        return request;
    }
}
