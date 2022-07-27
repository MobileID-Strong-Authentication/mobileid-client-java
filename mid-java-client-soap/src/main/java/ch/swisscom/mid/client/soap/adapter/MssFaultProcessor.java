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
package ch.swisscom.mid.client.soap.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import javax.net.ssl.SSLException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;

import ch.swisscom.mid.client.impl.Loggers;
import ch.swisscom.mid.client.model.FailureReason;
import ch.swisscom.mid.client.model.Fault;
import ch.swisscom.mid.client.model.StatusCode;

public class MssFaultProcessor {

    private static final Logger log = LoggerFactory.getLogger(Loggers.CLIENT_PROTOCOL);

    public static Fault processFailure(FailureReason failureReason) {
        Fault result = new Fault();
        result.setFailureReason(failureReason);
        return result;
    }

    public static Fault processSoapFaultException(SOAPFaultException exception) {
        Fault fault = null;
        if (exception != null) {
            SOAPFault soapFault = exception.getFault();
            if (soapFault != null) {
                try {
                    StatusCode faultStatusCode = StatusCode.INTERNAL_ERROR;
                    if (soapFault.getFaultSubcodes() != null &&
                        soapFault.getFaultSubcodes().hasNext()) {
                        QName element = (QName) soapFault.getFaultSubcodes().next();
                        String statusCodeString = element.getLocalPart();
                        faultStatusCode = StatusCode.getByStatusCodeString(statusCodeString);
                    }
                    String faultStatusDetail = soapFault.getDetail().getTextContent();

                    fault = new Fault();
                    fault.setStatusCode(faultStatusCode);
                    fault.setFailureDetail(faultStatusDetail);
                    if (faultStatusDetail != null && faultStatusDetail.contains(" SSL ")) {
                        fault.setFailureReason(FailureReason.TLS_CONNECTION_FAILURE);
                    } else {
                        fault.setFailureReason(FailureReason.MID_SERVICE_FAILURE);
                    }
                } catch (Exception e) {
                    log.warn("Failed to parse the received SOAP Fault. Assuming INTERNAL_ERROR + MID_SERVICE_FAILURE " +
                             "as default poll status results", e);
                }
            }
        }
        if (fault == null) {
            fault = new Fault();
            fault.setFailureReason(FailureReason.MID_SERVICE_FAILURE);
            fault.setFailureDetail("Invalid SOAP Fault received");
            fault.setStatusCode(StatusCode.INTERNAL_ERROR);
        }
        return fault;
    }

    public static Fault processException(Exception inputException, FailureReason potentialFailureReason) {
        Fault result = new Fault();
        result.setStatusCode(StatusCode.INTERNAL_ERROR);
        result.setStatusCodeString(StatusCode.INTERNAL_ERROR.name());

        FailureReason failureReason = null;
        String failureDetail = null;

        for (Throwable currentException = inputException;
             currentException != null && failureReason == null;
             currentException = currentException.getCause()) {

            Class currentExceptionClass = currentException.getClass();
            if (SSLException.class.isAssignableFrom(currentExceptionClass)) {
                SSLException sslException = (SSLException) currentException;
                Throwable sslExceptionCause = sslException.getCause();
                if (sslExceptionCause != null && SocketTimeoutException.class.isAssignableFrom(sslExceptionCause.getClass())) {
                    failureReason = FailureReason.HOST_CONNECT_TIMEOUT_FAILURE;
                    failureDetail = sslExceptionCause.getMessage();
                } else {
                    failureReason = FailureReason.TLS_CONNECTION_FAILURE;
                    failureDetail = sslException.getMessage();
                }
            } else if (SocketTimeoutException.class.isAssignableFrom(currentExceptionClass)) {
                failureReason = FailureReason.RESPONSE_TIMEOUT_FAILURE;
                failureDetail = currentException.getMessage();
            } else if (ConnectException.class.isAssignableFrom(currentExceptionClass)) {
                failureReason = FailureReason.HOST_CONNECTION_FAILURE;
                failureDetail = currentException.getMessage();
            } else if (IOException.class.isAssignableFrom(currentExceptionClass)) {
                failureReason = FailureReason.HTTP_COMMUNICATION_FAILURE;
                failureDetail = currentException.getMessage();
            }
        }

        if (failureReason == null) {
            failureReason = potentialFailureReason != null ? potentialFailureReason : FailureReason.UNKNOWN_FAILURE;
        }
        if (failureDetail == null) {
            failureDetail = "Internal error";
        }

        result.setFailureReason(failureReason);
        result.setFailureDetail(failureDetail);

        return result;
    }

}
