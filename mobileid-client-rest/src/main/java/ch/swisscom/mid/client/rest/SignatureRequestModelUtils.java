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
import ch.swisscom.mid.client.config.TrafficObserver;
import ch.swisscom.mid.client.model.*;
import ch.swisscom.mid.client.rest.model.signreq.AdditionalService;
import ch.swisscom.mid.client.rest.model.signreq.DataToBeSigned;
import ch.swisscom.mid.client.rest.model.signreq.MobileUser;
import ch.swisscom.mid.client.rest.model.signreq.*;
import ch.swisscom.mid.client.rest.model.signresp.MSSSignature;
import ch.swisscom.mid.client.rest.model.signresp.MSSSignatureResp;
import ch.swisscom.mid.client.rest.model.signresp.MSSSignatureResponse;
import ch.swisscom.mid.client.rest.model.signresp.ServiceResponse;
import ch.swisscom.mid.client.rest.model.signresp.SubscriberInfoDetail;

import static ch.swisscom.mid.client.utils.Utils.generateInstantAsString;
import static ch.swisscom.mid.client.utils.Utils.generateTransId;

public class SignatureRequestModelUtils {

    public static MSSSignatureRequest createSignatureRequest(ClientConfiguration config,
                                                             SignatureRequest clientRequest,
                                                             boolean sync) {
        MSSSignatureReq signatureReq = new MSSSignatureReq();
        signatureReq.setAPInfo(createApInfo(config));
        signatureReq.setAdditionalServices(createAdditionalServices(clientRequest));
        signatureReq.setDataToBeSigned(createDataToBeSigned(clientRequest));
        signatureReq.setMSSPInfo(createMsspInfo(config));
        signatureReq.setMajorVersion(clientRequest.getMajorVersion());
        signatureReq.setMinorVersion(clientRequest.getMinorVersion());
        signatureReq.setMessagingMode(sync ?
                                      DefaultConfiguration.SIGNATURE_MODE_SYNC :
                                      DefaultConfiguration.SIGNATURE_MODE_ASYNC);
        signatureReq.setMobileUser(createMobileUser(clientRequest));
        signatureReq.setSignatureProfile(clientRequest.getSignatureProfile());
        signatureReq.setTimeOut(String.valueOf(clientRequest.getUserResponseTimeOutInSeconds()));

        MSSSignatureRequest requestWrapper = new MSSSignatureRequest();
        requestWrapper.setMSSSignatureReq(signatureReq);
        return requestWrapper;
    }

    public static SignatureResponse processSignatureResponse(MSSSignatureResponse responseWrapper) {
        MSSSignatureResp response = responseWrapper.getMSSSignatureResp();
        SignatureResponse result = new SignatureResponse();
        if (response != null) {
            result.setMajorVersion(response.getMajorVersion());
            result.setMinorVersion(response.getMinorVersion());
            result.setSignatureProfile(response.getSignatureProfile());
            MSSSignature signature = response.getMSSSignature();
            if (signature != null) {
                result.setBase64Signature(signature.getBase64Signature());
            }
            result.setStatus(processSignatureRespStatus(response));
            result.setAdditionalServiceResponses(processAdditionalServiceResponses(response));
        } else {
            throw new MIDFlowException("Invalid MSS response received. " +
                                       "Cannot parse it and convert it to a valid " +
                                       SignatureResponse.class.getSimpleName(),
                                       new FaultProcessor().processFailure(FailureReason.MID_INVALID_RESPONSE_FAILURE));
        }
        return result;
    }

    public static SignatureTracking createSignatureTracking(MSSSignatureResponse responseWrapper, TrafficObserver trafficObserver) {
        MSSSignatureResp response = responseWrapper.getMSSSignatureResp();
        if (response != null) {
            SignatureTracking result = new SignatureTracking();
            result.setMajorVersion(response.getMajorVersion());
            result.setMinorVersion(response.getMinorVersion());
            result.setTrafficObserver(trafficObserver);
            result.setTransactionId(response.getMSSPTransID());
            result.setMobileUserMsisdn(response.getMobileUser().getMsisdn());
            return result;
        } else {
            throw new MIDFlowException("Invalid MSS response received. " +
                                       "Cannot parse it and convert it to a valid " +
                                       SignatureTracking.class.getSimpleName(),
                                       new FaultProcessor().processFailure(FailureReason.MID_INVALID_RESPONSE_FAILURE));
        }
    }

    // ----------------------------------------------------------------------------------------------------

    private static Status processSignatureRespStatus(MSSSignatureResp response) {
        Status result = new Status();
        ch.swisscom.mid.client.rest.model.signresp.Status status = response.getStatus();
        if (status != null) {
            result.setStatusMessage(status.getStatusMessage());
            ch.swisscom.mid.client.rest.model.signresp.StatusCode statusCode = status.getStatusCode();
            if (statusCode != null) {
                String statusCodeString = statusCode.getValue();
                result.setStatusCodeString(statusCodeString);
                result.setStatusCode(StatusCode.getByStatusCodeString(statusCodeString));
            }
        }
        return result;
    }

    private static APInfo createApInfo(ClientConfiguration config) {
        APInfo apInfo = new APInfo();
        apInfo.setApId(config.getApId());
        apInfo.setApPwd(config.getApPassword());
        apInfo.setAPTransID(generateTransId());
        apInfo.setInstant(generateInstantAsString());
        return apInfo;
    }

    private static List<AdditionalService> createAdditionalServices(SignatureRequest clientRequest) {
        List<AdditionalService> processedAdditionalServices = new ArrayList<>();
        List<ch.swisscom.mid.client.model.AdditionalService> requestedAdditionalService = clientRequest.getAdditionalServices();
        for (ch.swisscom.mid.client.model.AdditionalService currentAS : requestedAdditionalService) {
            AdditionalService additionalService = new AdditionalService();
            if (currentAS instanceof UserLangAdditionalService) {
                UserLang userLang = new UserLang();
                userLang.setValue((((UserLangAdditionalService) currentAS).getUserLanguage().getValue()));
                additionalService.setDescription(currentAS.getUri());
                additionalService.setUserLang(userLang);
            } else {
                additionalService.setDescription(currentAS.getUri());
            }
            processedAdditionalServices.add(additionalService);
        }
        return processedAdditionalServices;
    }

    private static List<AdditionalServiceResponse> processAdditionalServiceResponses(MSSSignatureResp response) {
        List<AdditionalServiceResponse> resultList = new ArrayList<>();
        List<ServiceResponse> serviceResponseList = response.getServiceResponses();
        if (serviceResponseList != null) {
            for (ServiceResponse serviceResponse : serviceResponseList) {
                if (DefaultConfiguration.ADDITIONAL_SERVICE_SUBSCRIBER_INFO_URI.equals(serviceResponse.getDescription())
                    && serviceResponse.getSubscriberInfo() != null
                    && serviceResponse.getSubscriberInfo().getDetails() != null
                    && serviceResponse.getSubscriberInfo().getDetails().size() > 0) {

                    SubscriberInfoDetail subscriberInfoDetail = serviceResponse.getSubscriberInfo().getDetails().get(0);
                    SubscriberInfoAdditionalServiceResponse asResponse = new SubscriberInfoAdditionalServiceResponse();
                    asResponse.setResponseId(subscriberInfoDetail.getId());
                    asResponse.setResponseValue(subscriberInfoDetail.getValue());
                    resultList.add(asResponse);
                }
            }
        }
        return resultList;
    }

    private static MobileUser createMobileUser(SignatureRequest clientRequest) {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setMsisdn(clientRequest.getMobileUser().getMsisdn());
        return mobileUser;
    }

    private static MSSPInfo createMsspInfo(ClientConfiguration config) {
        MsspId msspId = new MsspId();
        msspId.setUri(config.getMsspId());

        MSSPInfo msspInfo = new MSSPInfo();
        msspInfo.setMsspId(msspId);
        return msspInfo;
    }

    private static DataToBeSigned createDataToBeSigned(SignatureRequest clientRequest) {
        DataToBeSigned dtbs = new DataToBeSigned();
        dtbs.setData(clientRequest.getDataToBeSigned().getData());
        dtbs.setEncoding(clientRequest.getDataToBeSigned().getEncoding());
        dtbs.setMimeType(clientRequest.getDataToBeSigned().getMimeType());
        return dtbs;
    }

}
