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
package ch.swisscom.mid.client.model;

import java.util.ArrayList;
import java.util.List;

import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.config.TrafficObserver;

import static ch.swisscom.mid.client.utils.Utils.dataNotEmpty;
import static ch.swisscom.mid.client.utils.Utils.dataNotNull;

public class ProfileRequest {

    private String majorVersion = DefaultConfiguration.PROFILE_REQUEST_MAJOR_VERSION;

    private String minorVersion = DefaultConfiguration.PROFILE_REQUEST_MINOR_VERSION;

    /**
     * Optional custom AP ID that will override the AP ID configured via {@link ch.swisscom.mid.client.config.ClientConfiguration}.
     * If this is not set then the ID from {@link ch.swisscom.mid.client.config.ClientConfiguration} is used.
     */
    private String overrideApId;

    /**
     * Optional custom AP password that will override the AP password configured via {@link ch.swisscom.mid.client.config.ClientConfiguration}.
     * If this is not set then the password from {@link ch.swisscom.mid.client.config.ClientConfiguration} is used.
     */
    private String overrideApPassword;

    private MobileUser mobileUser;

    private List<String> extensionParams;

    private TrafficObserver trafficObserver;

    // ----------------------------------------------------------------------------------------------------

    public void addExtensionParam(String param) {
        getExtensionParams().add(param);
    }

    public List<String> getExtensionParams() {
        if (extensionParams == null) {
            extensionParams = new ArrayList<>();
        }
        return extensionParams;
    }

    public void setExtensionParamsToAllValues() {
        List<String> localExtensionParams = getExtensionParams();
        localExtensionParams.add(ProfileQueryExtensions.SSCDS);
        localExtensionParams.add(ProfileQueryExtensions.ACCOUNT_STATE);
        localExtensionParams.add(ProfileQueryExtensions.CERTIFICATES);
        localExtensionParams.add(ProfileQueryExtensions.PIN_STATUS);
        localExtensionParams.add(ProfileQueryExtensions.RECOVERY_CODE_STATUS);
        localExtensionParams.add(ProfileQueryExtensions.AUTO_ACTIVATION_STATUS);
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
    }

    public String getMinorVersion() {
        return minorVersion;
    }

    public void setMinorVersion(String minorVersion) {
        this.minorVersion = minorVersion;
    }

    public MobileUser getMobileUser() {
        if (mobileUser == null) {
            mobileUser = new MobileUser();
        }
        return mobileUser;
    }

    public void setMobileUser(MobileUser mobileUser) {
        this.mobileUser = mobileUser;
    }

    public TrafficObserver getTrafficObserver() {
        return trafficObserver;
    }

    public void setTrafficObserver(TrafficObserver trafficObserver) {
        this.trafficObserver = trafficObserver;
    }

    public String getOverrideApId() {
        return overrideApId;
    }

    public void setOverrideApId(String overrideApId) {
        this.overrideApId = overrideApId;
    }

    public String getOverrideApPassword() {
        return overrideApPassword;
    }

    public void setOverrideApPassword(String overrideApPassword) {
        this.overrideApPassword = overrideApPassword;
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        dataNotEmpty(majorVersion, "The major version cannot be null or empty (see DefaultConfiguration for default values)");
        dataNotEmpty(minorVersion, "The minor version cannot be null or empty (see DefaultConfiguration for default values)");
        dataNotNull(mobileUser, "The mobile user object cannot be null");
        dataNotEmpty(extensionParams, "The extension params list cannot be null or empty");
        mobileUser.validateYourself();
    }

    // ----------------------------------------------------------------------------------------------------

    // do not print the AP ID and AP password

    @Override
    public String toString() {
        return "ProfileRequest{" +
               "majorVersion='" + majorVersion + '\'' +
               ", minorVersion='" + minorVersion + '\'' +
               ", mobileUser=" + mobileUser +
               ", extensionParams=" + extensionParams +
               ", trafficObserver=" + trafficObserver +
               '}';
    }
}
