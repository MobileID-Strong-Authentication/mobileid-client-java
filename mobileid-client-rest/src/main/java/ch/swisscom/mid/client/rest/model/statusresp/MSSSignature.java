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
package ch.swisscom.mid.client.rest.model.statusresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Base64Signature"
})
public class MSSSignature {

    @JsonProperty("Base64Signature")
    private String base64Signature;

    @JsonProperty("Base64Signature")
    public String getBase64Signature() {
        return base64Signature;
    }

    @JsonProperty("Base64Signature")
    public void setBase64Signature(String base64Signature) {
        this.base64Signature = base64Signature;
    }

    public MSSSignature withBase64Signature(String base64Signature) {
        this.base64Signature = base64Signature;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(200);
        sb.append(MSSSignature.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("base64Signature");
        sb.append('=');
        sb.append(((this.base64Signature == null) ? "<null>" : this.base64Signature));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
