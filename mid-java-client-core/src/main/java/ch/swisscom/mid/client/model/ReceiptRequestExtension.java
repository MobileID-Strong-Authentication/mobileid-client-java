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

import static ch.swisscom.mid.client.utils.Utils.dataNotNull;

public class ReceiptRequestExtension {

    private ReceiptMessagingMode messagingMode = ReceiptMessagingMode.SYNC;

    private ReceiptRequestProfile receiptProfile = new ReceiptRequestProfile();

    private boolean requestUserAck = true;

    // ----------------------------------------------------------------------------------------------------

    public ReceiptMessagingMode getMessagingMode() {
        return messagingMode;
    }

    public void setMessagingMode(ReceiptMessagingMode messagingMode) {
        this.messagingMode = messagingMode;
    }

    public ReceiptRequestProfile getReceiptProfile() {
        if (receiptProfile == null) {
            receiptProfile = new ReceiptRequestProfile();
        }
        return receiptProfile;
    }

    public void setReceiptProfile(ReceiptRequestProfile receiptProfile) {
        this.receiptProfile = receiptProfile;
    }

    public boolean isRequestUserAck() {
        return requestUserAck;
    }

    public void setRequestUserAck(boolean requestUserAck) {
        this.requestUserAck = requestUserAck;
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        dataNotNull(messagingMode, "The message mode cannot be null (the only option is SYNC)");
        dataNotNull(receiptProfile, "The receipt profile cannot be null (the only option is SYNC)");
        receiptProfile.validateYourself();
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ReceiptRequestExtension{" +
               "messagingMode=" + messagingMode +
               ", receiptProfile=" + receiptProfile +
               ", requestUserAck=" + requestUserAck +
               '}';
    }

}
