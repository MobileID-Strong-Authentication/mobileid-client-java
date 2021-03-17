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

public enum ProfileDevicePinState implements DocumentedEnum {

    ACTIVE("ACTIVE", "The PIN is valid and it can be used in the next transaction"),
    BLOCKED("BLOCKED", "The PIN is blocked (forgotten, entered too many times) and " +
                       "the user cannot perform a new transaction with it");

    private final String value;
    private final String description;

    ProfileDevicePinState(String value, String description) {
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

    public static ProfileDevicePinState getByPinBlockedBooleanValue(Boolean value) {
        if (value != null) {
            return value ? BLOCKED : ACTIVE;
        } else {
            throw new IllegalArgumentException("Invalid PIN status value (null)");
        }
    }

}
