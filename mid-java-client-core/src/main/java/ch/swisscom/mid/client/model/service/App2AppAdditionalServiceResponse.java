/*
 *
 *  * Copyright 2021-2026 Swisscom (Schweiz) AG
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package ch.swisscom.mid.client.model.service;

import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.model.AdditionalServiceResponse;

/**
 * The information returned when the App2App additional service is requested to Mobile ID.
 */
public class App2AppAdditionalServiceResponse extends AdditionalServiceResponse {

    /**
     * The URI to which the user should be redirected to start the authentication process in the Mobile ID app.
     */
    private String authUri;

    public App2AppAdditionalServiceResponse() {
        super(DefaultConfiguration.ADDITIONAL_SERVICE_APP2APP);
    }

    public String getAuthUri() {
        return authUri;
    }

    public void setAuthUri(String authUri) {
        this.authUri = authUri;
    }

    @Override
    public String toString() {
        return "App2AppAdditionalServiceResponse{" +
                "authUri='" + authUri + '\'' +
                '}' + super.toString();
    }
}
