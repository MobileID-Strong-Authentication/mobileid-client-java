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
import ch.swisscom.mid.client.config.TrafficObserver;

import static ch.swisscom.mid.client.utils.Utils.dataNotEmpty;
import static ch.swisscom.mid.client.utils.Utils.dataNotNull;

public class ReceiptRequest {

    private String majorVersion = DefaultConfiguration.RECEIPT_REQUEST_MAJOR_VERSION;

    private String minorVersion = DefaultConfiguration.RECEIPT_REQUEST_MINOR_VERSION;

    private MessageToBeDisplayed messageToBeDisplayed;

    private StatusCode statusCode;

    private ReceiptRequestExtension requestExtension;

    private TrafficObserver trafficObserver;

    // ----------------------------------------------------------------------------------------------------

    public ReceiptRequestExtension addReceiptRequestExtension() {
        requestExtension = new ReceiptRequestExtension();
        return requestExtension;
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
    }

    public MessageToBeDisplayed getMessageToBeDisplayed() {
        if (messageToBeDisplayed == null) {
            messageToBeDisplayed = new MessageToBeDisplayed();
        }
        return messageToBeDisplayed;
    }

    public void setMessageToBeDisplayed(MessageToBeDisplayed messageToBeDisplayed) {
        this.messageToBeDisplayed = messageToBeDisplayed;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public ReceiptRequestExtension getRequestExtension() {
        if (requestExtension == null) {
            requestExtension = new ReceiptRequestExtension();
        }
        return requestExtension;
    }

    public void setRequestExtension(ReceiptRequestExtension requestExtension) {
        this.requestExtension = requestExtension;
    }

    public TrafficObserver getTrafficObserver() {
        return trafficObserver;
    }

    public void setTrafficObserver(TrafficObserver trafficObserver) {
        this.trafficObserver = trafficObserver;
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        dataNotEmpty(majorVersion, "The major version cannot be null or empty (see DefaultConfiguration for default values)");
        dataNotEmpty(minorVersion, "The minor version cannot be null or empty (see DefaultConfiguration for default values)");
        dataNotNull(messageToBeDisplayed, "The message to be displayed cannot be null (call setMessageToBeDisplayed)");
        messageToBeDisplayed.validateYourself();
        dataNotNull(statusCode, "The status code cannot be null");
        if (requestExtension != null) {
            requestExtension.validateYourself();
        }
    }

    @Override
    public String toString() {
        return "ReceiptRequest{" +
               "majorVersion='" + majorVersion + '\'' +
               ", minorVersion='" + minorVersion + '\'' +
               ", messageToBeDisplayed=" + messageToBeDisplayed +
               ", statusCode=" + statusCode +
               ", requestExtension=" + requestExtension +
               ", trafficObserver=" + trafficObserver +
               '}';
    }
}
