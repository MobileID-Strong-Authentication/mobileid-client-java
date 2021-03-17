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
package ch.swisscom.mid.client.rest.model.receiptreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Language",
        "ReceiptProfileURI"
})
public class ReceiptProfile {

    @JsonProperty("Language")
    private String language;
    @JsonProperty("ReceiptProfileURI")
    private String receiptProfileURI;

    @JsonProperty("Language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("Language")
    public void setLanguage(String language) {
        this.language = language;
    }

    public ReceiptProfile withLanguage(String language) {
        this.language = language;
        return this;
    }

    @JsonProperty("ReceiptProfileURI")
    public String getReceiptProfileURI() {
        return receiptProfileURI;
    }

    @JsonProperty("ReceiptProfileURI")
    public void setReceiptProfileURI(String receiptProfileURI) {
        this.receiptProfileURI = receiptProfileURI;
    }

    public ReceiptProfile withReceiptProfileURI(String receiptProfileURI) {
        this.receiptProfileURI = receiptProfileURI;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(ReceiptProfile.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("language");
        sb.append('=');
        sb.append(((this.language == null) ? "<null>" : this.language));
        sb.append(',');
        sb.append("receiptProfileURI");
        sb.append('=');
        sb.append(((this.receiptProfileURI == null) ? "<null>" : this.receiptProfileURI));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
