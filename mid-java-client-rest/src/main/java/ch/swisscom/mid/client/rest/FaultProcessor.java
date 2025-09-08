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
package ch.swisscom.mid.client.rest;

import ch.swisscom.mid.client.model.FailureReason;
import ch.swisscom.mid.client.model.Fault;
import ch.swisscom.mid.client.model.StatusCode;
import ch.swisscom.mid.client.rest.model.fault.Code;
import ch.swisscom.mid.client.rest.model.fault.MSSFault;
import ch.swisscom.mid.client.rest.model.fault.SubCode;
import org.apache.hc.client5.http.HttpHostConnectException;

import javax.net.ssl.SSLException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

public class FaultProcessor {

    public Fault processFailure(FailureReason failureReason) {
        Fault result = new Fault();
        result.setFailureReason(failureReason);
        return result;
    }

    public Fault processException(Exception e, FailureReason failureReason) {
        Fault result = new Fault();
        result.setStatusCode(StatusCode.INTERNAL_ERROR);
        result.setStatusCodeString(StatusCode.INTERNAL_ERROR.name());

        if (e != null) {
            result.setFailureDetail(e.getMessage());
            if (HttpHostConnectException.class.isAssignableFrom(e.getClass())) {
                result.setFailureReason(FailureReason.HOST_CONNECTION_FAILURE);
            } else if (ConnectException.class.isAssignableFrom(e.getClass())) {
                result.setFailureReason(FailureReason.HOST_CONNECTION_FAILURE);
            } else if (SSLException.class.isAssignableFrom(e.getClass())) {
                SSLException sslException = (SSLException) e;
                if (sslException.getCause() != null && SocketTimeoutException.class.isAssignableFrom(sslException.getCause().getClass())) {
                    result.setFailureReason(FailureReason.HOST_CONNECT_TIMEOUT_FAILURE);
                } else {
                    result.setFailureReason(FailureReason.TLS_CONNECTION_FAILURE);
                }
            } else if (SocketTimeoutException.class.isAssignableFrom(e.getClass())) {
                result.setFailureReason(FailureReason.RESPONSE_TIMEOUT_FAILURE);
            } else if (IOException.class.isAssignableFrom(e.getClass())) {
                result.setFailureReason(FailureReason.HTTP_COMMUNICATION_FAILURE);
            } else {
                result.setFailureReason(FailureReason.UNKNOWN_FAILURE);
            }
        }

        if (failureReason != null) {
            result.setFailureReason(failureReason);
        }

        return result;
    }

    public Fault processFaultResponse(MSSFault faultWrapper) {
        Fault result = new Fault();
        result.setFailureReason(FailureReason.MID_SERVICE_FAILURE);

        if (faultWrapper == null || faultWrapper.getFault() == null) {
            return result;
        }

        ch.swisscom.mid.client.rest.model.fault.Fault fault = faultWrapper.getFault();
        result.setStatusDetail(fault.getDetail());
        result.setStatusFaultReason(fault.getReason());

        Code faultCode = fault.getCode();
        if (faultCode != null) {
            SubCode faultSubCode = faultCode.getSubCode();
            if (faultSubCode != null) {
                String statusCodeString = faultSubCode.getValue();
                result.setStatusCodeString(statusCodeString);
                result.setStatusCode(StatusCode.getByStatusCodeString(statusCodeString));
            }
        }

        return result;
    }

}
