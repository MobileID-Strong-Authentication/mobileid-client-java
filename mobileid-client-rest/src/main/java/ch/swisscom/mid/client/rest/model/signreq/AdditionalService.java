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
package ch.swisscom.mid.client.rest.model.signreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Description",
        "UserLang"
})
public class AdditionalService {

    @JsonProperty("Description")
    private String description;
    @JsonProperty("UserLang")
    private UserLang userLang;

    @JsonProperty("Description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    public AdditionalService withDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("UserLang")
    public UserLang getUserLang() {
        return userLang;
    }

    @JsonProperty("UserLang")
    public void setUserLang(UserLang userLang) {
        this.userLang = userLang;
    }

    public AdditionalService withUserLang(UserLang userLang) {
        this.userLang = userLang;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append(AdditionalService.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null) ? "<null>" : this.description));
        sb.append(',');
        sb.append("userLang");
        sb.append('=');
        sb.append(((this.userLang == null) ? "<null>" : this.userLang));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
