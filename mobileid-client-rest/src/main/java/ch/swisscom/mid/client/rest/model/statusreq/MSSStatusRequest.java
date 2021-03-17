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
package ch.swisscom.mid.client.rest.model.statusreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "MSS_StatusReq"
})
public class MSSStatusRequest {

    @JsonProperty("MSS_StatusReq")
    private MSSStatusReq mSSStatusReq;

    @JsonProperty("MSS_StatusReq")
    public MSSStatusReq getMSSStatusReq() {
        return mSSStatusReq;
    }

    @JsonProperty("MSS_StatusReq")
    public void setMSSStatusReq(MSSStatusReq mSSStatusReq) {
        this.mSSStatusReq = mSSStatusReq;
    }

    public MSSStatusRequest withMSSStatusReq(MSSStatusReq mSSStatusReq) {
        this.mSSStatusReq = mSSStatusReq;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(130);
        sb.append(MSSStatusRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSStatusReq");
        sb.append('=');
        sb.append(((this.mSSStatusReq == null) ? "<null>" : this.mSSStatusReq));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
