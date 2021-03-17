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
        "MSS_ReceiptReq"
})
public class MSSReceiptRequest {

    @JsonProperty("MSS_ReceiptReq")
    private MSSReceiptReq mSSReceiptReq;

    @JsonProperty("MSS_ReceiptReq")
    public MSSReceiptReq getMSSReceiptReq() {
        return mSSReceiptReq;
    }

    @JsonProperty("MSS_ReceiptReq")
    public void setMSSReceiptReq(MSSReceiptReq mSSReceiptReq) {
        this.mSSReceiptReq = mSSReceiptReq;
    }

    public MSSReceiptRequest withMSSReceiptReq(MSSReceiptReq mSSReceiptReq) {
        this.mSSReceiptReq = mSSReceiptReq;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(MSSReceiptRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSReceiptReq");
        sb.append('=');
        sb.append(((this.mSSReceiptReq == null) ? "<null>" : this.mSSReceiptReq));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
