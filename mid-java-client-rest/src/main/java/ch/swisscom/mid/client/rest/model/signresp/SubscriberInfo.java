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

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Details"
})
public class SubscriberInfo {

    @JsonProperty("Details")
    private List<SubscriberInfoDetail> details = new ArrayList<>();

    @JsonProperty("Details")
    public List<SubscriberInfoDetail> getDetails() {
        return details;
    }

    @JsonProperty("Details")
    public void setDetails(List<SubscriberInfoDetail> details) {
        this.details = details;
    }

    public SubscriberInfo withDetails(List<SubscriberInfoDetail> details) {
        this.details = details;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(30);
        sb.append(SubscriberInfo.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("details");
        sb.append('=');
        sb.append(((this.details == null) ? "<null>" : this.details));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
