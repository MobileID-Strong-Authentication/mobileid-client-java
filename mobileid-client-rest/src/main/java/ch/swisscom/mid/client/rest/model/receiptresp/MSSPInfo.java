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
        "Instant",
        "MSSP_ID"
})
public class MSSPInfo {

    @JsonProperty("Instant")
    private String instant;
    @JsonProperty("MSSP_ID")
    private MsspId msspId;

    @JsonProperty("Instant")
    public String getInstant() {
        return instant;
    }

    @JsonProperty("Instant")
    public void setInstant(String instant) {
        this.instant = instant;
    }

    public MSSPInfo withInstant(String instant) {
        this.instant = instant;
        return this;
    }

    @JsonProperty("MSSP_ID")
    public MsspId getMsspId() {
        return msspId;
    }

    @JsonProperty("MSSP_ID")
    public void setMsspId(MsspId msspId) {
        this.msspId = msspId;
    }

    public MSSPInfo withMsspId(MsspId msspId) {
        this.msspId = msspId;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append(MSSPInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("instant");
        sb.append('=');
        sb.append(((this.instant == null) ? "<null>" : this.instant));
        sb.append(',');
        sb.append("msspId");
        sb.append('=');
        sb.append(((this.msspId == null) ? "<null>" : this.msspId));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
