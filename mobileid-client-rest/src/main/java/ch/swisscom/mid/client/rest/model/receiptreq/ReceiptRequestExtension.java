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
package ch.swisscom.mid.client.rest.model.receiptreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ReceiptMessagingMode",
        "ReceiptProfile",
        "UserAck"
})
public class ReceiptRequestExtension {

    @JsonProperty("ReceiptMessagingMode")
    private String receiptMessagingMode;
    @JsonProperty("ReceiptProfile")
    private ReceiptProfile receiptProfile;
    @JsonProperty("UserAck")
    private String userAck;

    @JsonProperty("ReceiptMessagingMode")
    public String getReceiptMessagingMode() {
        return receiptMessagingMode;
    }

    @JsonProperty("ReceiptMessagingMode")
    public void setReceiptMessagingMode(String receiptMessagingMode) {
        this.receiptMessagingMode = receiptMessagingMode;
    }

    public ReceiptRequestExtension withReceiptMessagingMode(String receiptMessagingMode) {
        this.receiptMessagingMode = receiptMessagingMode;
        return this;
    }

    @JsonProperty("ReceiptProfile")
    public ReceiptProfile getReceiptProfile() {
        return receiptProfile;
    }

    @JsonProperty("ReceiptProfile")
    public void setReceiptProfile(ReceiptProfile receiptProfile) {
        this.receiptProfile = receiptProfile;
    }

    public ReceiptRequestExtension withReceiptProfile(ReceiptProfile receiptProfile) {
        this.receiptProfile = receiptProfile;
        return this;
    }

    @JsonProperty("UserAck")
    public String getUserAck() {
        return userAck;
    }

    @JsonProperty("UserAck")
    public void setUserAck(String userAck) {
        this.userAck = userAck;
    }

    public ReceiptRequestExtension withUserAck(String userAck) {
        this.userAck = userAck;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(ReceiptRequestExtension.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("receiptMessagingMode");
        sb.append('=');
        sb.append(((this.receiptMessagingMode == null) ? "<null>" : this.receiptMessagingMode));
        sb.append(',');
        sb.append("receiptProfile");
        sb.append('=');
        sb.append(((this.receiptProfile == null) ? "<null>" : this.receiptProfile));
        sb.append(',');
        sb.append("userAck");
        sb.append('=');
        sb.append(((this.userAck == null) ? "<null>" : this.userAck));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
