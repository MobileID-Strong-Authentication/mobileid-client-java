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
import static ch.swisscom.mid.client.utils.Utils.dataTrue;

public class SignatureRequest {

    private String majorVersion = DefaultConfiguration.SIGNATURE_REQUEST_MAJOR_VERSION;
    private String minorVersion = DefaultConfiguration.SIGNATURE_REQUEST_MINOR_VERSION;

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

    private final List<AdditionalService> additionalServices = new ArrayList<>();

    private DataToBeSigned dataToBeSigned;

    private MobileUser mobileUser;

    private int userResponseTimeOutInSeconds = DefaultConfiguration.SIGNATURE_DEFAULT_TIME_OUT_IN_SECONDS;

    private String signatureProfile = DefaultConfiguration.SIGNATURE_DEFAULT_SIGNATURE_PROFILE;

    private TrafficObserver trafficObserver;

    // ----------------------------------------------------------------------------------------------------

    public void setUserLanguage(UserLanguage language) {
        additionalServices.add(new UserLangAdditionalService(language));
    }

    public void addAdditionalService(AdditionalService service) {
        additionalServices.add(service);
    }

    public List<AdditionalService> getAdditionalServices() {
        return additionalServices;
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

    public DataToBeSigned getDataToBeSigned() {
        if (dataToBeSigned == null) {
            dataToBeSigned = new DataToBeSigned();
        }
        return dataToBeSigned;
    }

    public void setDataToBeSigned(DataToBeSigned dataToBeSigned) {
        this.dataToBeSigned = dataToBeSigned;
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

    public int getUserResponseTimeOutInSeconds() {
        return userResponseTimeOutInSeconds;
    }

    public void setUserResponseTimeOutInSeconds(int userResponseTimeOutInSeconds) {
        this.userResponseTimeOutInSeconds = userResponseTimeOutInSeconds;
    }

    public String getSignatureProfile() {
        return signatureProfile;
    }

    public void setSignatureProfile(String signatureProfile) {
        this.signatureProfile = signatureProfile;
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
        dataNotEmpty(additionalServices, "Invalid signature request configuration. " +
                                         "At least the UserLang additional service needs to be configured (call setUserLanguage).");
        dataNotNull(dataToBeSigned, "The data to be signed cannot be null (call setDataToBeSigned)");
        dataToBeSigned.validateYourself();
        dataNotNull(mobileUser, "The target mobile user cannot be null");
        mobileUser.validateYourself();
        dataNotEmpty(signatureProfile, "The signature profile cannot be null or empty. See " +
                                       SignatureProfiles.class.getSimpleName() +
                                       " for a list of possible profiles to choose from");
        dataTrue(userResponseTimeOutInSeconds >= DefaultConfiguration.SIGNATURE_MINIMUM_TIME_OUT_IN_SECONDS,
                 "The user response timeout cannot be lower than " +
                 DefaultConfiguration.SIGNATURE_MINIMUM_TIME_OUT_IN_SECONDS +
                 " seconds");
        dataTrue(userResponseTimeOutInSeconds <= DefaultConfiguration.SIGNATURE_MAXIMUM_TIME_OUT_IN_SECONDS,
                 "The user response timeout cannot be higher than " +
                 DefaultConfiguration.SIGNATURE_MAXIMUM_TIME_OUT_IN_SECONDS +
                 " seconds");
    }

    // ----------------------------------------------------------------------------------------------------

    // do not print the AP ID and AP password

    @Override
    public String toString() {
        return "SignatureRequest{" +
               "majorVersion='" + majorVersion + '\'' +
               ", minorVersion='" + minorVersion + '\'' +
               ", additionalServices=" + additionalServices +
               ", dataToBeSigned=" + dataToBeSigned +
               ", mobileUser=" + mobileUser +
               ", userResponseTimeOutInSeconds=" + userResponseTimeOutInSeconds +
               ", signatureProfile='" + signatureProfile + '\'' +
               ", trafficObserver=" + trafficObserver +
               '}';
    }
}
