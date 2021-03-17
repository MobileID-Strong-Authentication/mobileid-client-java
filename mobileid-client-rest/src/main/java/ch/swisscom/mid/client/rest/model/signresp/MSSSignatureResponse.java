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
package ch.swisscom.mid.client.rest.model.signresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "MSS_SignatureResp"
})
public class MSSSignatureResponse {

    @JsonProperty("MSS_SignatureResp")
    private MSSSignatureResp mSSSignatureResp;

    @JsonProperty("MSS_SignatureResp")
    public MSSSignatureResp getMSSSignatureResp() {
        return mSSSignatureResp;
    }

    @JsonProperty("MSS_SignatureResp")
    public void setMSSSignatureResp(MSSSignatureResp mSSSignatureResp) {
        this.mSSSignatureResp = mSSSignatureResp;
    }

    public MSSSignatureResponse withMSSSignatureResp(MSSSignatureResp mSSSignatureResp) {
        this.mSSSignatureResp = mSSSignatureResp;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(250);
        sb.append(MSSSignatureResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mSSSignatureResp");
        sb.append('=');
        sb.append(((this.mSSSignatureResp == null) ? "<null>" : this.mSSSignatureResp));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
