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

import java.util.ArrayList;
import java.util.List;

/**
 * Specific class to use when requesting the Geofencing additional service to Mobile ID. Please also see the
 * {@link GeofencingAdditionalServiceResponse} class for the content that is returned from Mobile ID when this additional service
 * is requested.
 */
public class GeofencingAdditionalService extends AdditionalService {

    public GeofencingAdditionalService() {
        super(DefaultConfiguration.ADDITIONAL_SERVICE_GEOFENCING);
    }

    private List<String> countryWhiteList = new ArrayList<>();
    private List<String> countryBlackList = new ArrayList<>();
    private String minDeviceConfidence;
    private String minLocationConfidence;
    private String maxTimestampMinutes;
    private String maxAccuracyMeters;

    public List<String> getCountryWhiteList() {
        return countryWhiteList;
    }

    public void setCountryWhiteList(List<String> countryWhiteList) {
        this.countryWhiteList = countryWhiteList;
    }

    public List<String> getCountryBlackList() {
        return countryBlackList;
    }

    public void setCountryBlackList(List<String> countryBlackList) {
        this.countryBlackList = countryBlackList;
    }

    public String getMinDeviceConfidence() {
        return minDeviceConfidence;
    }

    public void setMinDeviceConfidence(String minDeviceConfidence) {
        this.minDeviceConfidence = minDeviceConfidence;
    }

    public String getMinLocationConfidence() {
        return minLocationConfidence;
    }

    public void setMinLocationConfidence(String minLocationConfidence) {
        this.minLocationConfidence = minLocationConfidence;
    }

    public String getMaxTimestampMinutes() {
        return maxTimestampMinutes;
    }

    public void setMaxTimestampMinutes(String maxTimestampMinutes) {
        this.maxTimestampMinutes = maxTimestampMinutes;
    }

    public String getMaxAccuracyMeters() {
        return maxAccuracyMeters;
    }

    public void setMaxAccuracyMeters(String maxAccuracyMeters) {
        this.maxAccuracyMeters = maxAccuracyMeters;
    }


    public boolean isDefined() {
        if (countryWhiteList != null && !countryWhiteList.isEmpty()) return true;
        if(countryBlackList!=null && !countryBlackList.isEmpty()) return true;
        if (minDeviceConfidence != null && !minDeviceConfidence.isEmpty() && !minDeviceConfidence.equalsIgnoreCase("0")) return true;
        if (minLocationConfidence != null && !minLocationConfidence.isEmpty() && !minLocationConfidence.equalsIgnoreCase("0")) return true;
        if (maxAccuracyMeters != null && !maxAccuracyMeters.isEmpty()) return true;
        if (maxTimestampMinutes != null && !maxTimestampMinutes.isEmpty()) return true;
        return false;
    }
}

