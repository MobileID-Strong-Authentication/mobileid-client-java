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
        "ReceiptRequestExtension"
})
public class StatusDetail {

    @JsonProperty("ReceiptRequestExtension")
    private ReceiptRequestExtension receiptRequestExtension;

    @JsonProperty("ReceiptRequestExtension")
    public ReceiptRequestExtension getReceiptRequestExtension() {
        return receiptRequestExtension;
    }

    @JsonProperty("ReceiptRequestExtension")
    public void setReceiptRequestExtension(ReceiptRequestExtension receiptRequestExtension) {
        this.receiptRequestExtension = receiptRequestExtension;
    }

    public StatusDetail withReceiptRequestExtension(ReceiptRequestExtension receiptRequestExtension) {
        this.receiptRequestExtension = receiptRequestExtension;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(StatusDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("receiptRequestExtension");
        sb.append('=');
        sb.append(((this.receiptRequestExtension == null) ? "<null>" : this.receiptRequestExtension));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
