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

import java.util.ArrayList;
import java.util.List;

import ch.swisscom.mid.client.MIDFlowException;
import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.model.*;
import ch.swisscom.mid.client.rest.model.statusreq.MSSStatusReq;
import ch.swisscom.mid.client.rest.model.statusreq.MSSStatusRequest;
import ch.swisscom.mid.client.rest.model.statusresp.Geofencing;
import ch.swisscom.mid.client.rest.model.statusresp.MSSStatusResp;
import ch.swisscom.mid.client.rest.model.statusresp.MSSStatusResponse;
import ch.swisscom.mid.client.rest.model.statusresp.ServiceResponse;

import static ch.swisscom.mid.client.utils.Utils.generateInstantAsString;
import static ch.swisscom.mid.client.utils.Utils.generateTransId;

public class StatusQueryModelUtils {

    public static MSSStatusRequest createStatusQueryRequest(ClientConfiguration config,
                                                            SignatureTracking signatureTracking) {
        ch.swisscom.mid.client.rest.model.statusreq.APInfo apInfo = new ch.swisscom.mid.client.rest.model.statusreq.APInfo();
        apInfo.setApId(config.getApId());
        apInfo.setApPwd(config.getApPassword());
        apInfo.setAPTransID(generateTransId());
        apInfo.setInstant(generateInstantAsString());

        ch.swisscom.mid.client.rest.model.statusreq.MsspId msspId = new ch.swisscom.mid.client.rest.model.statusreq.MsspId();
        msspId.setUri(config.getMsspId());

        ch.swisscom.mid.client.rest.model.statusreq.MSSPInfo msspInfo = new ch.swisscom.mid.client.rest.model.statusreq.MSSPInfo();
        msspInfo.setMsspId(msspId);

        MSSStatusReq statusReq = new MSSStatusReq();
        statusReq.setAPInfo(apInfo);
        statusReq.setMSSPInfo(msspInfo);
        statusReq.setMajorVersion(signatureTracking.getMajorVersion());
        statusReq.setMinorVersion(signatureTracking.getMinorVersion());
        statusReq.setMSSPTransID(signatureTracking.getTransactionId());

        MSSStatusRequest requestWrapper = new MSSStatusRequest();
        requestWrapper.setMSSStatusReq(statusReq);
        return requestWrapper;
    }

    public static SignatureResponse processStatusQueryResponse(MSSStatusResponse responseWrapper, SignatureTracking originalTracking) {
        MSSStatusResp response = responseWrapper.getMSSStatusResp();
        if (response != null) {
            SignatureResponse result = new SignatureResponse();
            result.setMajorVersion(response.getMajorVersion());
            result.setMinorVersion(response.getMinorVersion());
            result.setTracking(originalTracking);
            ch.swisscom.mid.client.rest.model.statusresp.MSSSignature signature = response.getMSSSignature();
            if (signature != null) {
                result.setBase64Signature(signature.getBase64Signature());
            }
            result.setStatus(processStatusRespStatus(response));
            result.setAdditionalServiceResponses(processAdditionalServiceResponses(response));
            return result;
        } else {
            throw new MIDFlowException("Invalid MSS status response received. " +
                                       "Cannot parse it and convert it to a valid " +
                                       SignatureResponse.class.getSimpleName(),
                                       new FaultProcessor().processFailure(FailureReason.MID_INVALID_RESPONSE_FAILURE));
        }
    }

    // ----------------------------------------------------------------------------------------------------

    private static Status processStatusRespStatus(MSSStatusResp response) {
        Status result = new Status();
        ch.swisscom.mid.client.rest.model.statusresp.Status status = response.getStatus();
        if (status != null) {
            result.setStatusMessage(status.getStatusMessage());
            ch.swisscom.mid.client.rest.model.statusresp.StatusCode statusCode = status.getStatusCode();
            if (statusCode != null) {
                String statusCodeString = statusCode.getValue();
                result.setStatusCodeString(statusCodeString);
                result.setStatusCode(StatusCode.getByStatusCodeString(statusCodeString));
            }
        }
        return result;
    }

    private static List<AdditionalServiceResponse> processAdditionalServiceResponses(MSSStatusResp response) {
        List<AdditionalServiceResponse> resultList = new ArrayList<>();
        List<ServiceResponse> serviceResponseList = response.getServiceResponses();
        if (serviceResponseList != null) {
            for (ServiceResponse serviceResponse : serviceResponseList) {
                if (DefaultConfiguration.ADDITIONAL_SERVICE_GEOFENCING.equals(serviceResponse.getDescription())
                    && serviceResponse.getGeofencing() != null) {

                    Geofencing geofencing = serviceResponse.getGeofencing();
                    GeofencingAdditionalServiceResponse geoResponse = new GeofencingAdditionalServiceResponse();
                    if (geofencing.getErrorCode() == null) {
                        geoResponse.setCountry(geofencing.getCountry());
                        geoResponse.setAccuracy(geofencing.getAccuracy() == null ? 0 : Integer.parseInt(geofencing.getAccuracy()));
                        geoResponse.setTimestamp(geofencing.getTimestamp());
                        geoResponse.setDeviceConfidence(geofencing.getDeviceConfidence());
                        geoResponse.setLocationConfidence(geofencing.getLocationConfidence());
                    } else {
                        geoResponse.setErrorCode(GeofencingErrorCode.getByCodeAsString(geofencing.getErrorCode()));
                        geoResponse.setErrorMessage(geofencing.getErrorMessage());
                    }
                    resultList.add(geoResponse);
                }
            }
        }
        return resultList;
    }

}
