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

public enum ProfileMobileUserCertificateState implements DocumentedEnum {

    ACTIVE("ACTIVE", "The certificate is active for this device and can be used"),
    INACTIVE("INACTIVE", "The certificate is inactive/disabled. It cannot be used for a new transaction"),
    REVOKED("REVOKED", "The certificate has been revoked. It cannot be used anymore"),
    EXPIRED("EXPIRED", "The certificate has expired. It cannot be used anymore"),
    FUTURE("FUTURE", "The certificate's validity period has not yet started (it will start in the future). " +
                     "It cannot be used for a new transaction, for now"),
    UNKNOWN("UNKNOWN", "The certificate in in an unknown state.");

    private final String value;
    private final String description;

    ProfileMobileUserCertificateState(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public static ProfileMobileUserCertificateState getByStateString(String value) {
        for (ProfileMobileUserCertificateState state : values()) {
            if (state.getValue().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid mobile user device certificate state: " + value);
    }

}
