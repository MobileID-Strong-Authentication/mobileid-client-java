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

public class ReceiptResponseExtension {

    private ReceiptMessagingMode messagingMode;

    private Boolean clientAck;

    private Boolean networkAck;

    private Boolean userAck;

    private String userResponse;

    // ----------------------------------------------------------------------------------------------------

    public ReceiptMessagingMode getMessagingMode() {
        return messagingMode;
    }

    public void setMessagingMode(ReceiptMessagingMode messagingMode) {
        this.messagingMode = messagingMode;
    }

    public Boolean isClientAck() {
        return clientAck;
    }

    public void setClientAck(Boolean clientAck) {
        this.clientAck = clientAck;
    }

    public Boolean isNetworkAck() {
        return networkAck;
    }

    public void setNetworkAck(Boolean networkAck) {
        this.networkAck = networkAck;
    }

    public Boolean isUserAck() {
        return userAck;
    }

    public void setUserAck(Boolean userAck) {
        this.userAck = userAck;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ReceiptResponseExtension{" +
               "messagingMode=" + messagingMode +
               ", clientAck=" + clientAck +
               ", networkAck=" + networkAck +
               ", userAck=" + userAck +
               ", userResponse='" + userResponse + '\'' +
               '}';
    }
}
