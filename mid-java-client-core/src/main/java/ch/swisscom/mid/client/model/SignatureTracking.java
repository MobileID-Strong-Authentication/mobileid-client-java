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

import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.config.TrafficObserver;

import static ch.swisscom.mid.client.utils.Utils.dataNotEmpty;

public class SignatureTracking {

    private String majorVersion = DefaultConfiguration.STATUS_QUERY_REQUEST_MAJOR_VERSION;

    private String minorVersion = DefaultConfiguration.STATUS_QUERY_REQUEST_MINOR_VERSION;

    private String transactionId;

    private String mobileUserMsisdn;

    private TrafficObserver trafficObserver;

    // ----------------------------------------------------------------------------------------------------

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public String getMobileUserMsisdn() {
        return mobileUserMsisdn;
    }

    public void setMobileUserMsisdn(String mobileUserMsisdn) {
        this.mobileUserMsisdn = mobileUserMsisdn;
    }

    public TrafficObserver getTrafficObserver() {
        return trafficObserver;
    }

    public void setTrafficObserver(TrafficObserver trafficObserver) {
        this.trafficObserver = trafficObserver;
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        dataNotEmpty(majorVersion, "The major version field cannot be null or empty");
        dataNotEmpty(minorVersion, "The minor version field cannot be null or empty");
        dataNotEmpty(transactionId, "The transaction ID cannot be null or empty");
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "SignatureTracking{" +
               "majorVersion='" + majorVersion + '\'' +
               ", minorVersion='" + minorVersion + '\'' +
               ", transactionId='" + transactionId + '\'' +
               ", mobileUserMsisdn='" + mobileUserMsisdn + '\'' +
               '}';
    }
}
