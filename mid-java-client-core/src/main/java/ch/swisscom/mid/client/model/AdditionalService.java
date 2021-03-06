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
 * Base class for requesting additional services via requests to the Mobile ID service. You can either make an instance of this class
 * and set the URI by yourself, or use one of the provided subclasses. There is also a corresponding {@link AdditionalServiceResponse}
 * that contains, as expected, the data that is returned as part of the requested additional service payload.
 *
 * @see UserLangAdditionalService
 * @see SignatureValidationAdditionalService
 * @see GeofencingAdditionalService
 */
public class AdditionalService {

    private final String uri;

    public AdditionalService(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "AdditionalService{" +
               "uri='" + uri + '\'' +
               '}';
    }
}
