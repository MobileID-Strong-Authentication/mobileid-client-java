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

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.MimeType;
import com.github.tomakehurst.wiremock.stubbing.Scenario;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.swisscom.mid.client.MIDClient;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.impl.MIDClientImpl;
import ch.swisscom.mid.client.model.*;

import static ch.swisscom.mid.client.rest.TestData.*;
import static ch.swisscom.mid.client.rest.TestSupport.buildConfig;
import static ch.swisscom.mid.client.rest.TestSupport.fileToString;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class AsyncSignatureTest {

    private static WireMockServer server;
    private static MIDClient client;

    @BeforeAll
    public static void setUpThisClass() {
        server = new WireMockServer(options().port(8089));
        server.start();

        client = new MIDClientImpl(buildConfig());
    }

    @BeforeEach
    public void setUp() {
        server.resetAll();
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
                .inScenario("Async signature")
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", MimeType.JSON.toString())
                        .withBody(fileToString("/samples/rest-response-async-signature.json")))
                .willSetStateTo("Signature running - poll 0"));

        server.stubFor(
            post(urlEqualTo("/rest/service"))
                .inScenario("Async signature")
                .whenScenarioStateIs("Signature running - poll 0")
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", MimeType.JSON.toString())
                        .withBody(fileToString("/samples/rest-response-status-outstanding.json")))
                .willSetStateTo("Signature running - poll 1")
        );

        server.stubFor(
            post(urlEqualTo("/rest/service"))
                .inScenario("Async signature")
                .whenScenarioStateIs("Signature running - poll 1")
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", MimeType.JSON.toString())
                        .withBody(fileToString("/samples/rest-response-status-signature.json")))
                .willSetStateTo("Signature finished")
        );

        SignatureRequest signatureRequest = buildSignatureRequest();
        SignatureResponse response = client.requestAsyncSignature(signatureRequest);
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.REQUEST_OK));
        assertThat(response.getStatus().getStatusCodeString(), is("100"));
        assertThat(response.getStatus().getStatusMessage(), is("REQUEST_OK"));
        assertThat(response.getSignatureProfile(), is(CUSTOM_SIGNATURE_PROFILE));
        assertThat(response.getTracking(), is(notNullValue()));
        assertThat(response.getTracking().getMobileUserMsisdn(), is(TrialNumbers.ONE_THAT_GIVES_MISSING_PARAM));
        assertThat(response.getTracking().getTransactionId(), is(CUSTOM_TRANS_ID));

        response = client.pollForSignatureStatus(response.getTracking());
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.OUTSTANDING_TRANSACTION));
        assertThat(response.getStatus().getStatusCodeString(), is("504"));

        response = client.pollForSignatureStatus(response.getTracking());
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.SIGNATURE));
        assertThat(response.getStatus().getStatusCodeString(), is("500"));
        assertThat(response.getBase64Signature(), is(notNullValue()));
        assertThat(response.getBase64Signature().length(), is(BASE64_SIGNATURE_LENGTH));
        assertThat(response.getAdditionalServiceResponses().size(), is(1));

        GeofencingAdditionalServiceResponse geofencingResponse =
            (GeofencingAdditionalServiceResponse) response.getAdditionalServiceResponses().get(0);
        assertThat(geofencingResponse.getCountry(), is("RO"));
        assertThat(geofencingResponse.getAccuracy(), is(10));
        assertThat(geofencingResponse.getDeviceConfidence(), is("0.5"));
        assertThat(geofencingResponse.getLocationConfidence(), is("1.0"));
        assertThat(geofencingResponse.getTimestamp(), is("2021-01-01T11:00:00.000+01:00"));
        assertThat(geofencingResponse.getErrorCode(), is(nullValue()));
        assertThat(geofencingResponse.getErrorMessage(), is(nullValue()));
    }

    @Test
    public void testSignature_success_overrideApIdAndPassword() {
        server.stubFor(
            post(urlEqualTo(DefaultConfiguration.REST_ENDPOINT_SUB_URL))
                .withRequestBody(containing("\"" + CUSTOM_AP_ID + "\""))
                .withRequestBody(containing("\"" + CUSTOM_AP_PASSWORD + "\""))
                .inScenario("Async signature")
                .whenScenarioStateIs(Scenario.STARTED)
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", MimeType.JSON.toString())
                        .withBody(fileToString("/samples/rest-response-async-signature.json")))
                .willSetStateTo("Signature running - poll 0"));

        server.stubFor(
            post(urlEqualTo("/rest/service"))
                .withRequestBody(containing("\"" + CUSTOM_AP_ID + "\""))
                .withRequestBody(containing("\"" + CUSTOM_AP_PASSWORD + "\""))
                .inScenario("Async signature")
                .whenScenarioStateIs("Signature running - poll 0")
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", MimeType.JSON.toString())
                        .withBody(fileToString("/samples/rest-response-status-outstanding.json")))
                .willSetStateTo("Signature running - poll 1")
        );

        server.stubFor(
            post(urlEqualTo("/rest/service"))
                .withRequestBody(containing("\"" + CUSTOM_AP_ID + "\""))
                .withRequestBody(containing("\"" + CUSTOM_AP_PASSWORD + "\""))
                .inScenario("Async signature")
                .whenScenarioStateIs("Signature running - poll 1")
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", MimeType.JSON.toString())
                        .withBody(fileToString("/samples/rest-response-status-signature.json")))
                .willSetStateTo("Signature finished")
        );

        SignatureRequest signatureRequest = buildSignatureRequest();
        signatureRequest.setOverrideApId(CUSTOM_AP_ID);
        signatureRequest.setOverrideApPassword(CUSTOM_AP_PASSWORD);

        SignatureResponse response = client.requestAsyncSignature(signatureRequest);
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.REQUEST_OK));
        assertThat(response.getStatus().getStatusCodeString(), is("100"));
        assertThat(response.getStatus().getStatusMessage(), is("REQUEST_OK"));
        assertThat(response.getSignatureProfile(), is(CUSTOM_SIGNATURE_PROFILE));
        assertThat(response.getTracking(), is(notNullValue()));
        assertThat(response.getTracking().getMobileUserMsisdn(), is(TrialNumbers.ONE_THAT_GIVES_MISSING_PARAM));
        assertThat(response.getTracking().getTransactionId(), is(CUSTOM_TRANS_ID));
        assertThat(response.getTracking().getOverrideApId(), is(CUSTOM_AP_ID));
        assertThat(response.getTracking().getOverrideApPassword(), is(CUSTOM_AP_PASSWORD));

        response = client.pollForSignatureStatus(response.getTracking());
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.OUTSTANDING_TRANSACTION));
        assertThat(response.getStatus().getStatusCodeString(), is("504"));
        assertThat(response.getTracking().getOverrideApId(), is(CUSTOM_AP_ID));
        assertThat(response.getTracking().getOverrideApPassword(), is(CUSTOM_AP_PASSWORD));

        response = client.pollForSignatureStatus(response.getTracking());
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.SIGNATURE));
        assertThat(response.getStatus().getStatusCodeString(), is("500"));
        assertThat(response.getBase64Signature(), is(notNullValue()));
        assertThat(response.getBase64Signature().length(), is(BASE64_SIGNATURE_LENGTH));
        assertThat(response.getAdditionalServiceResponses().size(), is(1));
        assertThat(response.getTracking().getOverrideApId(), is(CUSTOM_AP_ID));
        assertThat(response.getTracking().getOverrideApPassword(), is(CUSTOM_AP_PASSWORD));

        GeofencingAdditionalServiceResponse geofencingResponse =
            (GeofencingAdditionalServiceResponse) response.getAdditionalServiceResponses().get(0);
        assertThat(geofencingResponse.getCountry(), is("RO"));
        assertThat(geofencingResponse.getAccuracy(), is(10));
        assertThat(geofencingResponse.getDeviceConfidence(), is("0.5"));
        assertThat(geofencingResponse.getLocationConfidence(), is("1.0"));
        assertThat(geofencingResponse.getTimestamp(), is("2021-01-01T11:00:00.000+01:00"));
        assertThat(geofencingResponse.getErrorCode(), is(nullValue()));
        assertThat(geofencingResponse.getErrorMessage(), is(nullValue()));
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
