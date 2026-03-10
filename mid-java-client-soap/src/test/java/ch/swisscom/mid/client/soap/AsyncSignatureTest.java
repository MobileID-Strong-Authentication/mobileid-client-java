/*
 * Copyright 2026 Swisscom (Schweiz) AG
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
package ch.swisscom.mid.client.soap;

import ch.swisscom.mid.client.MIDClient;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.impl.MIDClientImpl;
import ch.swisscom.mid.client.model.*;
import ch.swisscom.mid.client.model.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static ch.swisscom.mid.client.soap.TestData.CONTENT_TYPE_SOAP_XML;
import static ch.swisscom.mid.client.soap.TestSupport.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * SOAP Async signature test class. Tests the flow of requesting an asynchronous signature and then polling for the status until the signature is ready.
 * Also tests the case where additional services are requested together with the signature,
 * and that the responses for those additional services are correctly parsed and returned in the response from the client.
 */
public class AsyncSignatureTest {

    private static WireMockServer server;
    private static MIDClient client;

    @BeforeAll
    public static void beforeAll() {
        client = new MIDClientImpl(buildConfig(buildTlsConfig("TLSv1.2")));
    }

    @AfterAll
    public static void afterAll() {
        client.close();
    }

    @BeforeEach
    public void setUp() {
        server = new WireMockServer(options().port(8089));
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    // ----------------------------------------------------------------------------------------------------

    @Test
    public void testSignature_success() throws JsonProcessingException {
        server.stubFor(
                post(urlEqualTo(DefaultConfiguration.SOAP_SIGNATURE_PORT_SUB_URL))
                        .inScenario("Async signature")
                        .whenScenarioStateIs(Scenario.STARTED)
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", CONTENT_TYPE_SOAP_XML)
                                        .withBody(fileToString("/samples/soap-response-async-signature.xml")))
                        .willSetStateTo("Signature running - poll 0"));

        server.stubFor(
                post(urlEqualTo(DefaultConfiguration.SOAP_STATUS_QUERY_PORT_SUB_URL))
                        .inScenario("Async signature")
                        .whenScenarioStateIs("Signature running - poll 0")
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", CONTENT_TYPE_SOAP_XML)
                                        .withBody(fileToString("/samples/soap-response-status-outstanding.xml")))
                        .willSetStateTo("Signature running - poll 1")
        );

        server.stubFor(
                post(urlEqualTo(DefaultConfiguration.SOAP_STATUS_QUERY_PORT_SUB_URL))
                        .inScenario("Async signature")
                        .whenScenarioStateIs("Signature running - poll 1")
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", CONTENT_TYPE_SOAP_XML)
                                        .withBody(fileToString("/samples/soap-response-status-signature.xml")))
                        .willSetStateTo("Signature finished")
        );

        SignatureRequest signatureRequest = buildSignatureRequest();
        SignatureResponse response = client.requestAsyncSignature(signatureRequest);

        assertInitialDefaultStatusOk(response);

        assertThat(response.getSignatureProfile(), is(TestData.CUSTOM_SIGNATURE_PROFILE));
        assertThat(response.getTracking(), is(notNullValue()));
        assertThat(response.getTracking().getMobileUserMsisdn(), is(TestData.MSISDN));
        assertThat(response.getTracking().getTransactionId(), is(TestData.CUSTOM_TRANS_ID));

        response = client.pollForSignatureStatus(response.getTracking());
        assertStatus(response, StatusCode.OUTSTANDING_TRANSACTION, "504");

        response = client.pollForSignatureStatus(response.getTracking());
        assertStatus(response, StatusCode.SIGNATURE, "500");
        assertSignature(response, notNullValue(), TestData.BASE64_SIGNATURE_LENGTH);
        assertResponseTo(response, "/samples/soap-response-status-signature-expected.json");
    }

    @Test
    public void testSignatureWithApp2AppAndGeo_success() throws JsonProcessingException {
        server.stubFor(
                post(urlEqualTo(DefaultConfiguration.SOAP_SIGNATURE_PORT_SUB_URL))
                        .inScenario("Async signature")
                        .whenScenarioStateIs(Scenario.STARTED)
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", CONTENT_TYPE_SOAP_XML)
                                        // .withBody(fileToString("/samples/soap-response-async-signature.xml")))
                                        .withBody(fileToString("/samples/soap-response-async-signature-app2app-geo.xml")))
                        .willSetStateTo("Signature running - poll 0"));

        final List<AdditionalService> additionalSrv = Arrays.asList(
                new GeofencingAdditionalService(), new App2AppAdditionalService("myapp://example-soap-app"));

        SignatureRequest signatureRequest = buildSignatureRequestWithServices(additionalSrv);
        SignatureResponse response = client.requestAsyncSignature(signatureRequest);

        assertInitialDefaultStatusOk(response);
        assertThat(response.getSignatureProfile(), is(TestData.CUSTOM_SIGNATURE_PROFILE));
        assertThat(response.getTracking(), is(notNullValue()));
        assertThat(response.getTracking().getMobileUserMsisdn(), is(TestData.MSISDN));
        assertThat(response.getTracking().getTransactionId(), is(TestData.CUSTOM_TRANS_ID));

        assertThat(response.getAdditionalServiceResponses().size(), is(2));
        assertResponseTo(response, "/samples/soap-response-status-signature-expected-app2app-geo.json");

        final GeofencingAdditionalServiceResponse geofencingResponse = (GeofencingAdditionalServiceResponse) response.getAdditionalServiceResponses().get(0);
        assertOnGeofencingResponse(geofencingResponse, "CH", 10, "0.5", "1.0", "2026-03-10T11:00:00.000+01:00");

        final App2AppAdditionalServiceResponse app2appResponse = (App2AppAdditionalServiceResponse) response.getAdditionalServiceResponses().get(1);
        assertOnApp2AppResponse(app2appResponse, "mobileid://auth?mobile_auth_redirect_uri=myapp%3A%2F%2Fapp.open%23access_token%3DABCD&session_token=32ffc297-0YLukZtgoRVz_wbJJfLnViViEhzk9aXM8dkHoOnhqf158&user_id=32ffc297-ac33-4be2-b3f4-42588502ea0f");
    }

    @Test
    public void testSignatureWithApp2AppOnly_success() throws JsonProcessingException {
        server.stubFor(
                post(urlEqualTo(DefaultConfiguration.SOAP_SIGNATURE_PORT_SUB_URL))
                        .inScenario("Async signature")
                        .whenScenarioStateIs(Scenario.STARTED)
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", CONTENT_TYPE_SOAP_XML)
                                        .withBody(fileToString("/samples/soap-response-async-signature-app2app.xml")))
                        .willSetStateTo("Signature running - poll 0"));

        final List<AdditionalService> additionalSer = Arrays.asList(new App2AppAdditionalService("myapp://example-soap-app"));
        SignatureRequest signatureRequest = buildSignatureRequestWithServices(additionalSer);
        SignatureResponse response = client.requestAsyncSignature(signatureRequest);

        assertInitialDefaultStatusOk(response);
        assertThat(response.getSignatureProfile(), is(TestData.CUSTOM_SIGNATURE_PROFILE));
        assertThat(response.getTracking(), is(notNullValue()));
        assertThat(response.getTracking().getMobileUserMsisdn(), is(TestData.MSISDN));
        assertThat(response.getTracking().getTransactionId(), is(TestData.CUSTOM_TRANS_ID));

        assertThat(response.getAdditionalServiceResponses().size(), is(1));
        assertResponseTo(response, "/samples/soap-response-status-signature-expected-app2app-only.json");

        final App2AppAdditionalServiceResponse app2appResponse = (App2AppAdditionalServiceResponse)
                response.getAdditionalServiceResponses().get(0);
        assertOnApp2AppResponse(app2appResponse, "mobileid://auth?mobile_auth_redirect_uri=myapp%3A%2F%2Fapp.open%23access_token%3DABCD&session_token=32ffc297-0YLukZtgoRVz_wbJJfLnViViEhzk9aXM8dkHoOnhqf158&user_id=32ffc297-ac33-4be2-b3f4-42588502ea0f");
    }

    // ----------------------------------------------------------------------------------------------------

    private static SignatureRequest buildSignatureRequest() {
        SignatureRequest request = new SignatureRequest();
        request.setUserLanguage(UserLanguage.ENGLISH);
        request.getDataToBeSigned().setData("Test: Do you want to login?");
        request.getDataToBeSigned().setEncodingToUtf8();
        request.getDataToBeSigned().setMimeTypeToTextPlain();
        request.getMobileUser().setMsisdn(TestData.MSISDN);
        request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
        request.addAdditionalService(new GeofencingAdditionalService());
        return request;
    }

    private static SignatureRequest buildSignatureRequestWithServices(List<AdditionalService> services) {
        SignatureRequest request = new SignatureRequest();
        request.setUserLanguage(UserLanguage.ENGLISH);
        request.getDataToBeSigned().setData("Test: Do you want to login?");
        request.getDataToBeSigned().setEncodingToUtf8();
        request.getDataToBeSigned().setMimeTypeToTextPlain();
        request.getMobileUser().setMsisdn(TrialNumbers.ONE_THAT_GIVES_MISSING_PARAM);
        request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
        for (AdditionalService service : services) {
            request.addAdditionalService(service);
        }
        return request;
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

    private static void assertInitialDefaultStatusOk(SignatureResponse response) {
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.REQUEST_OK));
        assertThat(response.getStatus().getStatusCodeString(), is("100"));
        assertThat(response.getStatus().getStatusMessage(), is("REQUEST_OK"));
    }

    private static void assertStatus(SignatureResponse response, StatusCode status, String code) {
        assertThat(response.getStatus().getStatusCode(), is(status));
        assertThat(response.getStatus().getStatusCodeString(), is(code));
    }

    private static void assertSignature(SignatureResponse response, org.hamcrest.Matcher<java.lang.Object> matcher, int length) {
        assertThat(response.getBase64Signature(), is(matcher));
        assertThat(response.getBase64Signature().length(), is(length));
    }
}
