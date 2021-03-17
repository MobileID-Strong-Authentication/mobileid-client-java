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
package ch.swisscom.mid.client.rest.model.fault;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Code",
    "Detail",
    "Reason"
})
public class Fault {

    @JsonProperty("Code")
    private Code code;
    @JsonProperty("Detail")
    private String detail;
    @JsonProperty("Reason")
    private String reason;

    @JsonProperty("Code")
    public Code getCode() {
        return code;
    }

    @JsonProperty("Code")
    public void setCode(Code code) {
        this.code = code;
    }

    public Fault withCode(Code code) {
        this.code = code;
        return this;
    }

    @JsonProperty("Detail")
    public String getDetail() {
        return detail;
    }

    @JsonProperty("Detail")
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Fault withDetail(String detail) {
        this.detail = detail;
        return this;
    }

    @JsonProperty("Reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("Reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    public Fault withReason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(Fault.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("code");
        sb.append('=');
        sb.append(((this.code == null) ? "<null>" : this.code));
        sb.append(',');
        sb.append("detail");
        sb.append('=');
        sb.append(((this.detail == null) ? "<null>" : this.detail));
        sb.append(',');
        sb.append("reason");
        sb.append('=');
        sb.append(((this.reason == null) ? "<null>" : this.reason));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
