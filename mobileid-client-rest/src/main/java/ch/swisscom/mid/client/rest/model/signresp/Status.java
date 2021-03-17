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
        "StatusCode",
        "StatusMessage"
})
public class Status {

    @JsonProperty("StatusCode")
    private StatusCode statusCode;
    @JsonProperty("StatusMessage")
    private String statusMessage;

    @JsonProperty("StatusCode")
    public StatusCode getStatusCode() {
        return statusCode;
    }

    @JsonProperty("StatusCode")
    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Status withStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @JsonProperty("StatusMessage")
    public String getStatusMessage() {
        return statusMessage;
    }

    @JsonProperty("StatusMessage")
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Status withStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append(Status.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("statusCode");
        sb.append('=');
        sb.append(((this.statusCode == null) ? "<null>" : this.statusCode));
        sb.append(',');
        sb.append("statusMessage");
        sb.append('=');
        sb.append(((this.statusMessage == null) ? "<null>" : this.statusMessage));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
