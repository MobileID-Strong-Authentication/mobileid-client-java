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
package ch.swisscom.mid.client.soap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.swisscom.mid.client.MIDClient;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.impl.MIDClientImpl;
import ch.swisscom.mid.client.model.*;

import static ch.swisscom.mid.client.soap.TestData.CONTENT_TYPE_SOAP_XML;
import static ch.swisscom.mid.client.soap.TestSupport.assertResponseTo;
import static ch.swisscom.mid.client.soap.TestSupport.buildConfig;
import static ch.swisscom.mid.client.soap.TestSupport.fileToString;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AsyncSignatureTest {

    private static WireMockServer server;
    private static MIDClient client;

    @BeforeAll
    public static void setUpThisClass() {
        server = new WireMockServer(options().port(8089));
        server.start();

        client = new MIDClientImpl(buildConfig());
    }

    @AfterAll
    public static void tearDownThisClass() {
        client.close();
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
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.REQUEST_OK));
        assertThat(response.getStatus().getStatusCodeString(), is("100"));
        assertThat(response.getStatus().getStatusMessage(), is("REQUEST_OK"));
        assertThat(response.getSignatureProfile(), is(TestData.CUSTOM_SIGNATURE_PROFILE));
        assertThat(response.getTracking(), is(notNullValue()));
        assertThat(response.getTracking().getMobileUserMsisdn(), is(TestData.MSISDN));
        assertThat(response.getTracking().getTransactionId(), is(TestData.CUSTOM_TRANS_ID));

        response = client.pollForSignatureStatus(response.getTracking());
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.OUTSTANDING_TRANSACTION));
        assertThat(response.getStatus().getStatusCodeString(), is("504"));

        response = client.pollForSignatureStatus(response.getTracking());
        assertThat(response.getStatus().getStatusCode(), is(StatusCode.SIGNATURE));
        assertThat(response.getStatus().getStatusCodeString(), is("500"));
        assertThat(response.getBase64Signature(), is(notNullValue()));
        assertThat(response.getBase64Signature().length(), is(TestData.BASE64_SIGNATURE_LENGTH));
        assertResponseTo(response, "/samples/soap-response-status-signature-expected.json");
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
        request.addAdditionalService(new SubscriberInfoAdditionalService());
        return request;
    }

}
