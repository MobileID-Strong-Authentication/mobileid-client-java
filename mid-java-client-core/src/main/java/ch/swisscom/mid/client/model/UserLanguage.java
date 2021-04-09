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

public enum UserLanguage {

    ENGLISH("en"),
    GERMAN("de"),
    ITALIAN("it"),
    FRENCH("fr");

    private final String value;

    UserLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static UserLanguage getByValue(String value) {
        for (UserLanguage element : values()) {
            if (element.getValue().equals(value)) {
                return element;
            }
        }
        throw new IllegalArgumentException("Cannot find a valid " + UserLanguage.class.getSimpleName() + " for value [" + value + "]");
    }
}
