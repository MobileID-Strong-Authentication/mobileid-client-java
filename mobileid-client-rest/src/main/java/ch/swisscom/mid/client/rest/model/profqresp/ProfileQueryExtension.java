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
        "MobileUser",
        "Sscds"
})
public class ProfileQueryExtension {

    @JsonProperty("MobileUser")
    private MobileUser mobileUser;
    @JsonProperty("Sscds")
    private Sscds sscds;

    @JsonProperty("MobileUser")
    public MobileUser getMobileUser() {
        return mobileUser;
    }

    @JsonProperty("MobileUser")
    public void setMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
    }

    public ProfileQueryExtension withMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
        return this;
    }

    @JsonProperty("Sscds")
    public Sscds getSscds() {
        return sscds;
    }

    @JsonProperty("Sscds")
    public void setSscds(Sscds sscds) {
        this.sscds = sscds;
    }

    public ProfileQueryExtension withSscds(Sscds sscds) {
        this.sscds = sscds;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append(ProfileQueryExtension.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mobileUser");
        sb.append('=');
        sb.append(((this.mobileUser == null) ? "<null>" : this.mobileUser));
        sb.append(',');
        sb.append("sscds");
        sb.append('=');
        sb.append(((this.sscds == null) ? "<null>" : this.sscds));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
