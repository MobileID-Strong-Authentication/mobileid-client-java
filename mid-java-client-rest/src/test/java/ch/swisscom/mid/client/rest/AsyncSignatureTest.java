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

import ch.swisscom.mid.client.MIDClient;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.impl.MIDClientImpl;
import ch.swisscom.mid.client.model.*;
import ch.swisscom.mid.client.model.service.*;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.MimeType;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.swisscom.mid.client.rest.TestData.*;
import static ch.swisscom.mid.client.rest.TestSupport.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class AsyncSignatureTest {

    private static WireMockServer server;
    private static MIDClient client;

    @BeforeAll
    public static void setUpThisClass() {
        server = new WireMockServer(options().port(8089));
        server.start();

        client = new MIDClientImpl(buildConfig(buildTlsConfig("TLSv1.2")));
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
    public void testAsyncSignature_success() {
        // given
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

        //when
        SignatureRequest signatureRequest = buildDefaultSignatureRequest();
        SignatureResponse response = client.requestAsyncSignature(signatureRequest);
        // then
        assertInitialDefaultStatus(response);
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
        assertThat(response.getAdditionalServiceResponses().size(), greaterThanOrEqualTo(2));

        final GeofencingAdditionalServiceResponse geofencingResponse = (GeofencingAdditionalServiceResponse) response.getAdditionalServiceResponses().get(0);
        assertOnGeofencingResponse(geofencingResponse, "RO", 10, "0.5", "1.0", "2021-01-01T11:00:00.000+01:00");

        final App2AppAdditionalServiceResponse app2appResponse = (App2AppAdditionalServiceResponse) response.getAdditionalServiceResponses().get(1);
        assertOnApp2AppResponse(app2appResponse, "mobileid://auth?mobile_auth_redirect_uri=myapp%3A%2F%2Fapp.open%23access_\ntoken%3DABCD&amp;session_token=32ffc297-0YLukZtgoRVz_wbJJfLnViViEhzk9aXM8\ndkHoOnhqf158&amp;user_id=32ffc297-ac33-4be2-b3f4-42588502ea0f");
    }

    @Test
    public void testAsyncSignature_success_overrideApIdAndPassword() {
        // given
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

        SignatureRequest signatureRequest = buildSignatureRequestWithApp2AppService();
        addServiceToRequest(signatureRequest, new GeofencingAdditionalService());

        signatureRequest.setOverrideApId(CUSTOM_AP_ID);
        signatureRequest.setOverrideApPassword(CUSTOM_AP_PASSWORD);

        SignatureResponse response = client.requestAsyncSignature(signatureRequest);
        assertInitialDefaultStatus(response);
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
        assertThat(response.getAdditionalServiceResponses().size(), is(2));
        assertThat(response.getTracking().getOverrideApId(), is(CUSTOM_AP_ID));
        assertThat(response.getTracking().getOverrideApPassword(), is(CUSTOM_AP_PASSWORD));

        assertThat(response.getAdditionalServiceResponses().size(), greaterThanOrEqualTo(2));

        final GeofencingAdditionalServiceResponse geofencingResponse = (GeofencingAdditionalServiceResponse) response.getAdditionalServiceResponses().get(0);
        assertOnGeofencingResponse(geofencingResponse, "RO", 10, "0.5", "1.0", "2021-01-01T11:00:00.000+01:00");

        final App2AppAdditionalServiceResponse app2appResponse = (App2AppAdditionalServiceResponse) response.getAdditionalServiceResponses().get(1);
        assertOnApp2AppResponse(app2appResponse, "mobileid://auth?mobile_auth_redirect_uri=myapp%3A%2F%2Fapp.open%23access_\ntoken%3DABCD&amp;session_token=32ffc297-0YLukZtgoRVz_wbJJfLnViViEhzk9aXM8\ndkHoOnhqf158&amp;user_id=32ffc297-ac33-4be2-b3f4-42588502ea0f");
    }

    // ----------------------------------------------------------------------------------------------------

    @Test
    public void testAsyncSignature_successWithOnlyApp2AppService() {
        // given
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
                                        .withBody(fileToString("/samples/rest-response-status-signature-app2app.json")))
                        .willSetStateTo("Signature finished")
        );

        SignatureRequest signatureRequest = buildSignatureRequestWithApp2AppService();
        SignatureResponse response = client.requestAsyncSignature(signatureRequest);
        assertInitialDefaultStatus(response);
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
        assertThat(response.getAdditionalServiceResponses().size(), greaterThanOrEqualTo(1));

        final App2AppAdditionalServiceResponse app2appResponse = (App2AppAdditionalServiceResponse) response.getAdditionalServiceResponses().get(0);
        assertOnApp2AppResponse(app2appResponse, "mobileid://auth?mobile_auth_redirect_uri=myauthapp%3A%2F%2Fapp.open%23access_\ntoken%3DABCD&amp;session_token=32ffc297-0YLukZtgoRVz_wbJJfLnViViEhzk9aXM8\ndkHoOnhqf158&amp;user_id=32ffc297-ac33-4be2-b3f4-42588502ea0f");
    }

    @Test
    public void testSignature_successWithLanguageAndApp2AppService() {
        // given
        server.stubFor(
                post(urlEqualTo(DefaultConfiguration.REST_ENDPOINT_SUB_URL))
                        .inScenario("Async signature")
                        .whenScenarioStateIs(Scenario.STARTED)
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MimeType.JSON.toString())
                                        .withBody(fileToString("/samples/rest-response-async-signature-app2app-EN-language.json")))
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
                                        .withBody(fileToString("/samples/rest-response-status-signature-app2app.json")))
                        .willSetStateTo("Signature finished")
        );
        // when
        SignatureRequest signatureRequest = buildSignatureRequestWithApp2AppService();
        SignatureResponse response = client.requestAsyncSignature(signatureRequest);
        // then
        assertInitialDefaultStatus(response);

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

        final App2AppAdditionalServiceResponse app2appResponse = (App2AppAdditionalServiceResponse) response.getAdditionalServiceResponses().get(0);
        assertOnApp2AppResponse(app2appResponse, "mobileid://auth?mobile_auth_redirect_uri=myauthapp%3A%2F%2Fapp.open%23access_\ntoken%3DABCD&amp;session_token=32ffc297-0YLukZtgoRVz_wbJJfLnViViEhzk9aXM8\ndkHoOnhqf158&amp;user_id=32ffc297-ac33-4be2-b3f4-42588502ea0f");
    }

    private static void assertInitialDefaultStatus(SignatureResponse response) {
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.REQUEST_OK));
        assertThat(response.getStatus().getStatusCodeString(), is("100"));
        assertThat(response.getStatus().getStatusMessage(), is("REQUEST_OK"));
    }

    private static SignatureRequest buildDefaultSignatureRequest() {
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

    private static SignatureRequest buildSignatureRequestWithApp2AppService() {
        SignatureRequest request = new SignatureRequest();
        request.setUserLanguage(UserLanguage.ENGLISH);
        request.getDataToBeSigned().setData("Test: Do you want to login?");
        request.getDataToBeSigned().setEncodingToUtf8();
        request.getDataToBeSigned().setMimeTypeToTextPlain();
        request.getMobileUser().setMsisdn(TrialNumbers.ONE_THAT_GIVES_MISSING_PARAM);
        request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
        request.addAdditionalService(new App2AppAdditionalService("myapp://example"));
        return request;
    }

    private static void addServiceToRequest(SignatureRequest request, AdditionalService additionalService) {
        request.addAdditionalService(additionalService);
    }

    private void assertOnGeofencingResponse(GeofencingAdditionalServiceResponse geoResp, String country, int accuracy, String deviceConfidence, String locationConfidence, String timestamp) {
        assertThat(geoResp.getCountry(), is(country));
        assertThat(geoResp.getAccuracy(), is(accuracy));
        assertThat(geoResp.getDeviceConfidence(), is(deviceConfidence));
        assertThat(geoResp.getLocationConfidence(), is(locationConfidence));
        assertThat(geoResp.getTimestamp(), is(timestamp));
        assertThat(geoResp.getErrorCode(), is(nullValue()));
        assertThat(geoResp.getErrorMessage(), is(nullValue()));
    }

    private void assertOnApp2AppResponse(App2AppAdditionalServiceResponse a2a, String authUri) {
        assertThat(a2a, is(notNullValue()));
        assertThat(a2a, instanceOf(App2AppAdditionalServiceResponse.class));
        assertThat(a2a.getAuthUri(), is(notNullValue()));
        assertThat(a2a.getAuthUri(), is(authUri));
    }
}
