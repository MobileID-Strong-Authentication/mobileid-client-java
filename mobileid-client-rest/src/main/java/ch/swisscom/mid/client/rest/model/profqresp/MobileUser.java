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
package ch.swisscom.mid.client.rest.model.profqresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "AutoActivation",
        "RecoveryCodeCreated"
})
public class MobileUser {

    @JsonProperty("AutoActivation")
    private Boolean autoActivation;
    @JsonProperty("RecoveryCodeCreated")
    private Boolean recoveryCodeCreated;

    @JsonProperty("AutoActivation")
    public Boolean getAutoActivation() {
        return autoActivation;
    }

    @JsonProperty("AutoActivation")
    public void setAutoActivation(Boolean autoActivation) {
        this.autoActivation = autoActivation;
    }

    public MobileUser withAutoActivation(Boolean autoActivation) {
        this.autoActivation = autoActivation;
        return this;
    }

    @JsonProperty("RecoveryCodeCreated")
    public Boolean getRecoveryCodeCreated() {
        return recoveryCodeCreated;
    }

    @JsonProperty("RecoveryCodeCreated")
    public void setRecoveryCodeCreated(Boolean recoveryCodeCreated) {
        this.recoveryCodeCreated = recoveryCodeCreated;
    }

    public MobileUser withRecoveryCodeCreated(Boolean recoveryCodeCreated) {
        this.recoveryCodeCreated = recoveryCodeCreated;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append(MobileUser.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("autoActivation");
        sb.append('=');
        sb.append(((this.autoActivation == null) ? "<null>" : this.autoActivation));
        sb.append(',');
        sb.append("recoveryCodeCreated");
        sb.append('=');
        sb.append(((this.recoveryCodeCreated == null) ? "<null>" : this.recoveryCodeCreated));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
