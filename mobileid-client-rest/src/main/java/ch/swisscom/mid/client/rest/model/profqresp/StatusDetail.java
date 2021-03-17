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
        "ProfileQueryExtension"
})
public class StatusDetail {

    @JsonProperty("ProfileQueryExtension")
    private ProfileQueryExtension profileQueryExtension;

    @JsonProperty("ProfileQueryExtension")
    public ProfileQueryExtension getProfileQueryExtension() {
        return profileQueryExtension;
    }

    @JsonProperty("ProfileQueryExtension")
    public void setProfileQueryExtension(ProfileQueryExtension profileQueryExtension) {
        this.profileQueryExtension = profileQueryExtension;
    }

    public StatusDetail withProfileQueryExtension(ProfileQueryExtension profileQueryExtension) {
        this.profileQueryExtension = profileQueryExtension;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(30);
        sb.append(StatusDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("profileQueryExtension");
        sb.append('=');
        sb.append(((this.profileQueryExtension == null) ? "<null>" : this.profileQueryExtension));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
