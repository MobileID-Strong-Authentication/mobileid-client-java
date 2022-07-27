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
package ch.swisscom.mid.client.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import ch.swisscom.mid.client.config.ConfigurationException;
import ch.swisscom.mid.client.model.DataAssemblyException;
import ch.swisscom.mid.client.model.Traceable;

public class Utils {

    public static void configNotNull(Object target, String errorMessage) throws ConfigurationException {
        if (target == null) {
            throw new ConfigurationException(errorMessage);
        }
    }

    public static void configTrue(boolean condition, String errorMessage) throws ConfigurationException {
        if (!condition) {
            throw new ConfigurationException(errorMessage);
        }
    }

    public static void dataNotNull(Object target, String errorMessage) throws DataAssemblyException {
        if (target == null) {
            throw new DataAssemblyException(errorMessage);
        }
    }

    public static <T> void dataNotEmpty(List<T> list, String errorMessage) throws DataAssemblyException {
        if (list == null || list.size() == 0) {
            throw new DataAssemblyException(errorMessage);
        }
    }

    public static void dataNotEmpty(String value, String errorMessage) throws DataAssemblyException {
        if (value == null || value.trim().length() == 0) {
            throw new DataAssemblyException(errorMessage);
        }
    }

    public static void dataTrue(boolean condition, String errorMessage) throws DataAssemblyException {
        if (!condition) {
            throw new DataAssemblyException(errorMessage);
        }
    }

    public static <T> void dataGreaterThanZero(int value, String errorMessage) throws DataAssemblyException {
        if (value <= 0) {
            throw new DataAssemblyException(errorMessage);
        }
    }

    public static String generateTransId() {
        return "ID-" + UUID.randomUUID().toString();
    }

    public static XMLGregorianCalendar generateInstantAsXmlGregorianCalendar() {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            DatatypeFactory df = DatatypeFactory.newInstance();
            return df.newXMLGregorianCalendar(calendar);
        } catch (DatatypeConfigurationException e) {
            throw new DataAssemblyException("Cannot format the current date and time as xs:dateTime", e);
        }
    }

    public static String generateInstantAsString() {
        return generateInstantAsXmlGregorianCalendar().toString();
    }

    public static String stripInnerLargeBase64Content(String source, char leftBoundChar, char rightBoundChar) {
        String pattern = leftBoundChar + "[A-Za-z0-9+/=_-]{1000,}" + rightBoundChar;
        String replacement = leftBoundChar + "..." + rightBoundChar;
        return source.replaceAll(pattern, replacement);
    }

    public static String joinListOfStrings(List<String> theList, String separator) {
        if (theList == null) {
            return null;
        }
        if (theList.size() == 0) {
            return "";
        }
        return String.join(separator, theList);
    }

    public static void assertNotNull(Object object, String messageForException) {
        if (object == null) {
            throw new IllegalArgumentException(messageForException);
        }
    }

    public static void assertNotEmpty(String value, String messageForException) {
        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException(messageForException);
        }
    }

    public static String printTrace(Traceable trace) {
        if (trace == null) {
            return "";
        } else {
            return " - " + trace;
        }
    }

    public static byte[] bytesFromBase64_viaUTF8(String input) {
        return Base64.getDecoder().decode(input.getBytes(StandardCharsets.UTF_8));
    }

}
