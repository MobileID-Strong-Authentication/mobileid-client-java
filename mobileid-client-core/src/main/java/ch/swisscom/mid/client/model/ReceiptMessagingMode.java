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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.swisscom.mid.client.impl.Loggers;

public enum ReceiptMessagingMode implements DocumentedEnum {

    SYNC("synch",
         "The receipt is processed synchronously (on the thread of the calling client; the client has to wait).");

    private static final Logger log = LoggerFactory.getLogger(Loggers.LOGGER_CLIENT);

    private final String value;
    private final String description;

    ReceiptMessagingMode(String value, String description) {
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

    public static ReceiptMessagingMode getByValue(String value) {
        for (ReceiptMessagingMode mode : values()) {
            if (mode.getValue().equals(value)) {
                return mode;
            }
        }
        log.warn("Cannot find a valid " + ReceiptMessagingMode.class.getSimpleName() +
                 " for value [{}]. Returning NULL", value);
        return null;
    }

}
