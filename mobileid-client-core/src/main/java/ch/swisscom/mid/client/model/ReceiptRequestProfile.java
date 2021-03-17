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

import ch.swisscom.mid.client.config.DefaultConfiguration;

import static ch.swisscom.mid.client.utils.Utils.dataNotEmpty;
import static ch.swisscom.mid.client.utils.Utils.dataNotNull;

public class ReceiptRequestProfile {

    private String language = DefaultConfiguration.RECEIPT_PROFILE_LANGUAGE;

    private String profileUri = DefaultConfiguration.RECEIPT_PROFILE_URI;

    // ----------------------------------------------------------------------------------------------------

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        dataNotEmpty(language, "The language cannot be null (see DefaultConfiguration for the default value)");
        dataNotNull(profileUri, "The profile URI cannot be null (see DefaultConfiguration for the default value)");
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ReceiptRequestProfile{" +
               "language='" + language + '\'' +
               ", profileUri='" + profileUri + '\'' +
               '}';
    }
}
