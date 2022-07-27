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
package ch.swisscom.mid.client.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Loggers {

    public static final String CLIENT = "ch.swisscom.mid.client";
    public static final String CONFIG = "ch.swisscom.mid.client.config";
    public static final String CLIENT_PROTOCOL = "ch.swisscom.mid.client.protocol";
    public static final String REQUEST_RESPONSE = "ch.swisscom.mid.client.requestResponse";
    public static final String FULL_REQUEST_RESPONSE = "ch.swisscom.mid.client.fullRequestResponse";
    public static final String SIGNATURE_VALIDATOR = "ch.swisscom.mid.signatureValidator";

    // ----------------------------------------------------------------------------------------------------

    public static final List<String> ALL_OF_THEM;

    static {
        Field[] allFields = Loggers.class.getDeclaredFields();
        ALL_OF_THEM = new ArrayList<>();
        for (Field field : allFields) {
            if (Modifier.isStatic(field.getModifiers()) && !field.getName().equals("ALL_OF_THEM")) {
                try {
                    ALL_OF_THEM.add((String) field.get(Loggers.class));
                } catch (IllegalAccessException ignored) {
                    // ignored exception
                }
            }
        }
        Collections.sort(ALL_OF_THEM);
    }

}
