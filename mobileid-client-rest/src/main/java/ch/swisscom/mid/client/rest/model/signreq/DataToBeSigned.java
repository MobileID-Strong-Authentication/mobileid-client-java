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
        "Data",
        "Encoding",
        "MimeType"
})
public class DataToBeSigned {

    @JsonProperty("Data")
    private String data;
    @JsonProperty("Encoding")
    private String encoding;
    @JsonProperty("MimeType")
    private String mimeType;

    @JsonProperty("Data")
    public String getData() {
        return data;
    }

    @JsonProperty("Data")
    public void setData(String data) {
        this.data = data;
    }

    public DataToBeSigned withData(String data) {
        this.data = data;
        return this;
    }

    @JsonProperty("Encoding")
    public String getEncoding() {
        return encoding;
    }

    @JsonProperty("Encoding")
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public DataToBeSigned withEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    @JsonProperty("MimeType")
    public String getMimeType() {
        return mimeType;
    }

    @JsonProperty("MimeType")
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public DataToBeSigned withMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(DataToBeSigned.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("data");
        sb.append('=');
        sb.append(((this.data == null) ? "<null>" : this.data));
        sb.append(',');
        sb.append("encoding");
        sb.append('=');
        sb.append(((this.encoding == null) ? "<null>" : this.encoding));
        sb.append(',');
        sb.append("mimeType");
        sb.append('=');
        sb.append(((this.mimeType == null) ? "<null>" : this.mimeType));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
