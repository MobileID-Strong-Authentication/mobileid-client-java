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

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "MobileUserCertificate",
        "PinStatus",
        "State"
})
public class App {

    @JsonProperty("MobileUserCertificate")
    private List<MobileUserCertificate> mobileUserCertificate = new ArrayList<MobileUserCertificate>();
    @JsonProperty("PinStatus")
    private PinStatus pinStatus;
    @JsonProperty("State")
    private String state;

    @JsonProperty("MobileUserCertificate")
    public List<MobileUserCertificate> getMobileUserCertificate() {
        return mobileUserCertificate;
    }

    @JsonProperty("MobileUserCertificate")
    public void setMobileUserCertificate(List<MobileUserCertificate> mobileUserCertificate) {
        this.mobileUserCertificate = mobileUserCertificate;
    }

    public App withMobileUserCertificate(List<MobileUserCertificate> mobileUserCertificate) {
        this.mobileUserCertificate = mobileUserCertificate;
        return this;
    }

    @JsonProperty("PinStatus")
    public PinStatus getPinStatus() {
        return pinStatus;
    }

    @JsonProperty("PinStatus")
    public void setPinStatus(PinStatus pinStatus) {
        this.pinStatus = pinStatus;
    }

    public App withPinStatus(PinStatus pinStatus) {
        this.pinStatus = pinStatus;
        return this;
    }

    @JsonProperty("State")
    public String getState() {
        return state;
    }

    @JsonProperty("State")
    public void setState(String state) {
        this.state = state;
    }

    public App withState(String state) {
        this.state = state;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append(App.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("mobileUserCertificate");
        sb.append('=');
        sb.append(((this.mobileUserCertificate == null) ? "<null>" : this.mobileUserCertificate));
        sb.append(',');
        sb.append("pinStatus");
        sb.append('=');
        sb.append(((this.pinStatus == null) ? "<null>" : this.pinStatus));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null) ? "<null>" : this.state));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
