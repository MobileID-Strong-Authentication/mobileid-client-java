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

import ch.swisscom.mid.client.MIDClient;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.impl.MIDClientImpl;
import ch.swisscom.mid.client.model.ProfileRequest;
import ch.swisscom.mid.client.model.ProfileResponse;
import ch.swisscom.mid.client.model.SignatureProfiles;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static ch.swisscom.mid.client.soap.TestSupport.*;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class ProfileQueryTest {

    private static WireMockServer server;
    private static MIDClient client;

    @BeforeAll
    public static void setUpThisClass() {
        server = new WireMockServer(options().port(8089));
        server.start();

        client = new MIDClientImpl(buildConfig(buildTlsConfig("TLSv1.1")));
    }

    @AfterAll
    public static void tearDownThisClass() {
        client.close();
        server.stop();
    }

    // ----------------------------------------------------------------------------------------------------

    @Test
    public void testProfileQuery_success() throws JsonProcessingException {
        server.stubFor(
                post(urlEqualTo(DefaultConfiguration.SOAP_PROFILE_QUERY_PORT_SUB_URL))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", TestData.CONTENT_TYPE_SOAP_XML)
                                        .withBody(fileToString("/samples/soap-response-profile-query.xml"))));

        ProfileRequest request = new ProfileRequest();
        request.getMobileUser().setMsisdn(TestData.MSISDN);
        request.setExtensionParamsToAllValues();

        ProfileResponse response = client.requestProfile(request);
        assertThat(response.getSignatureProfiles(),
                contains(SignatureProfiles.ANY_LOA4, SignatureProfiles.DEFAULT_PROFILE, SignatureProfiles.STK_LOA4));
        assertResponseTo(response, "/samples/soap-response-profile-query-expected.json");
    }
}
