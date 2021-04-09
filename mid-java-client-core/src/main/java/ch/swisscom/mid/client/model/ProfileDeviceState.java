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

public enum ProfileDeviceState implements DocumentedEnum {

    REGISTERED("REGISTERED", "The device is registered in the system but it is not activated yet"),
    ACTIVE("ACTIVE", "The device is active and ready to be used"),
    INACTIVE("INACTIVE", "The device is registered in the system, was activated but at the moment it cannot be used " +
                         "(certificate invalid, PIN blocked, etc)");

    private final String value;
    private final String description;

    ProfileDeviceState(String value, String description) {
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

    public static ProfileDeviceState getByStateString(String value) {
        for (ProfileDeviceState state : values()) {
            if (state.getValue().equalsIgnoreCase(value)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid profile device state: " + value);
    }

}
