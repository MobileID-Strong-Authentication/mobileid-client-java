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
package ch.swisscom.mid.client.rest.model.receiptresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ClientAck",
        "NetworkAck",
        "ReceiptMessagingMode",
        "UserAck",
        "UserResponse"
})
public class ReceiptResponseExtension {

    @JsonProperty("ClientAck")
    private String clientAck;
    @JsonProperty("NetworkAck")
    private String networkAck;
    @JsonProperty("ReceiptMessagingMode")
    private String receiptMessagingMode;
    @JsonProperty("UserAck")
    private String userAck;
    @JsonProperty("UserResponse")
    private String userResponse;

    @JsonProperty("ClientAck")
    public String getClientAck() {
        return clientAck;
    }

    @JsonProperty("ClientAck")
    public void setClientAck(String clientAck) {
        this.clientAck = clientAck;
    }

    public ReceiptResponseExtension withClientAck(String clientAck) {
        this.clientAck = clientAck;
        return this;
    }

    @JsonProperty("NetworkAck")
    public String getNetworkAck() {
        return networkAck;
    }

    @JsonProperty("NetworkAck")
    public void setNetworkAck(String networkAck) {
        this.networkAck = networkAck;
    }

    public ReceiptResponseExtension withNetworkAck(String networkAck) {
        this.networkAck = networkAck;
        return this;
    }

    @JsonProperty("ReceiptMessagingMode")
    public String getReceiptMessagingMode() {
        return receiptMessagingMode;
    }

    @JsonProperty("ReceiptMessagingMode")
    public void setReceiptMessagingMode(String receiptMessagingMode) {
        this.receiptMessagingMode = receiptMessagingMode;
    }

    public ReceiptResponseExtension withReceiptMessagingMode(String receiptMessagingMode) {
        this.receiptMessagingMode = receiptMessagingMode;
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

    public ReceiptResponseExtension withUserAck(String userAck) {
        this.userAck = userAck;
        return this;
    }

    @JsonProperty("UserResponse")
    public String getUserResponse() {
        return userResponse;
    }

    @JsonProperty("UserResponse")
    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    public ReceiptResponseExtension withUserResponse(String userResponse) {
        this.userResponse = userResponse;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(ReceiptResponseExtension.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("clientAck");
        sb.append('=');
        sb.append(((this.clientAck == null) ? "<null>" : this.clientAck));
        sb.append(',');
        sb.append("networkAck");
        sb.append('=');
        sb.append(((this.networkAck == null) ? "<null>" : this.networkAck));
        sb.append(',');
        sb.append("receiptMessagingMode");
        sb.append('=');
        sb.append(((this.receiptMessagingMode == null) ? "<null>" : this.receiptMessagingMode));
        sb.append(',');
        sb.append("userAck");
        sb.append('=');
        sb.append(((this.userAck == null) ? "<null>" : this.userAck));
        sb.append(',');
        sb.append("userResponse");
        sb.append('=');
        sb.append(((this.userResponse == null) ? "<null>" : this.userResponse));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
