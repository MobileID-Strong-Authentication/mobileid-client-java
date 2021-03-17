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

import java.io.Serializable;

/**
 * Wrapper object for the details of a communication failure between the MID Client and the configured Mobile ID service.
 * The main field to look into is {@link FailureReason} which contains the root cause of what happened. For some of the
 * elements in this enum, the rest of the Fault's fields are also populated with additional info.
 */
public class Fault implements Serializable {

    private FailureReason failureReason;

    private String failureDetail;

    private String statusCodeString;

    private StatusCode statusCode;

    private String statusDetail;

    private String statusFaultReason;

    public String getFailureDetail() {
        return failureDetail;
    }

    public void setFailureDetail(String failureDetail) {
        this.failureDetail = failureDetail;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    public String getStatusCodeString() {
        return statusCodeString;
    }

    public void setStatusCodeString(String statusCodeString) {
        this.statusCodeString = statusCodeString;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getStatusFaultReason() {
        return statusFaultReason;
    }

    public void setStatusFaultReason(String statusFaultReason) {
        this.statusFaultReason = statusFaultReason;
    }

    @Override
    public String toString() {
        return "Fault{" +
               "failureReason=" + failureReason +
               ", failureDetail='" + failureDetail + '\'' +
               ", statusCodeString='" + statusCodeString + '\'' +
               ", statusCode=" + statusCode +
               ", statusDetail='" + statusDetail + '\'' +
               ", statusFaultReason='" + statusFaultReason + '\'' +
               '}';
    }
}
