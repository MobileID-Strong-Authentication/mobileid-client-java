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

import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.model.MessageToBeDisplayed;
import ch.swisscom.mid.client.model.ReceiptMessagingMode;
import ch.swisscom.mid.client.model.ReceiptRequest;
import ch.swisscom.mid.client.model.ReceiptResponse;
import ch.swisscom.mid.client.model.ReceiptResponseExtension;
import ch.swisscom.mid.client.model.SignatureTracking;
import ch.swisscom.mid.client.rest.model.receiptreq.*;
import ch.swisscom.mid.client.rest.model.receiptresp.MSSReceiptResp;
import ch.swisscom.mid.client.rest.model.receiptresp.MSSReceiptResponse;

import static ch.swisscom.mid.client.utils.Utils.generateInstantAsString;
import static ch.swisscom.mid.client.utils.Utils.generateTransId;

public class ReceiptRequestModelUtils {

    public static MSSReceiptRequest createReceiptRequest(ClientConfiguration config,
                                                         SignatureTracking signatureTracking,
                                                         ReceiptRequest clientRequest) {
        StatusCode mssStatusCode = new StatusCode();
        mssStatusCode.setValue(String.valueOf(clientRequest.getStatusCode().getCode()));

        Status mssStatus = new Status();
        mssStatus.setStatusCode(mssStatusCode);
        if (clientRequest.getRequestExtension() != null) {
            mssStatus.setStatusDetail(createStatusDetail(clientRequest.getRequestExtension()));
        }

        MSSReceiptReq mssRequest = new MSSReceiptReq();
        mssRequest.setMajorVersion(clientRequest.getMajorVersion());
        mssRequest.setMinorVersion(clientRequest.getMinorVersion());
        mssRequest.setAPInfo(createApInfo(config));
        mssRequest.setMSSPInfo(createMsspInfo(config));
        mssRequest.setMobileUser(createMobileUser(signatureTracking));
        mssRequest.setMSSPTransID(signatureTracking.getTransactionId());
        mssRequest.setMessage(createMessage(clientRequest.getMessageToBeDisplayed()));
        mssRequest.setStatus(mssStatus);

        MSSReceiptRequest requestWrapper = new MSSReceiptRequest();
        requestWrapper.setMSSReceiptReq(mssRequest);
        return requestWrapper;
    }

    public static ReceiptResponse processReceiptResponse(MSSReceiptResponse responseWrapper) {
        MSSReceiptResp mssResponse = responseWrapper.getMSSReceiptResp();
        ch.swisscom.mid.client.rest.model.receiptresp.Status mssStatus = mssResponse.getStatus();

        ReceiptResponse response = new ReceiptResponse();
        response.setStatus(processReceiptRespStatus(mssStatus));
        if (mssStatus != null && mssStatus.getStatusDetail() != null) {
            response.setResponseExtension(processReceiptRespExtension(mssStatus.getStatusDetail().getReceiptResponseExtension()));
        }

        return response;
    }

    // ----------------------------------------------------------------------------------------------------

    private static APInfo createApInfo(ClientConfiguration config) {
        APInfo apInfo = new APInfo();
        apInfo.setApId(config.getApId());
        apInfo.setApPwd(config.getApPassword());
        apInfo.setAPTransID(generateTransId());
        apInfo.setInstant(generateInstantAsString());
        return apInfo;
    }

    private static MSSPInfo createMsspInfo(ClientConfiguration config) {
        MsspId msspId = new MsspId();
        msspId.setUri(config.getMsspId());

        MSSPInfo msspInfo = new MSSPInfo();
        msspInfo.setMsspId(msspId);
        return msspInfo;
    }

    private static MobileUser createMobileUser(SignatureTracking signatureTracking) {
        MobileUser mobileUser = new MobileUser();
        mobileUser.setMsisdn(signatureTracking.getMobileUserMsisdn());
        return mobileUser;
    }

    private static Message createMessage(MessageToBeDisplayed messageToBeDisplayed) {
        Message message = new Message();
        message.setData(messageToBeDisplayed.getData());
        message.setEncoding(messageToBeDisplayed.getEncoding());
        message.setMimeType(messageToBeDisplayed.getMimeType());
        return message;
    }

    private static StatusDetail createStatusDetail(ch.swisscom.mid.client.model.ReceiptRequestExtension extension) {
        ReceiptProfile mssReceiptProfile = new ReceiptProfile();
        mssReceiptProfile.setLanguage(extension.getReceiptProfile().getLanguage());
        mssReceiptProfile.setReceiptProfileURI(extension.getReceiptProfile().getProfileUri());

        ReceiptRequestExtension mssExtension = new ReceiptRequestExtension();
        mssExtension.setReceiptMessagingMode(extension.getMessagingMode().getValue());
        mssExtension.setUserAck(Boolean.toString(extension.isRequestUserAck()));
        mssExtension.setReceiptProfile(mssReceiptProfile);

        StatusDetail statusDetail = new StatusDetail();
        statusDetail.setReceiptRequestExtension(mssExtension);
        return statusDetail;
    }

    private static ch.swisscom.mid.client.model.Status processReceiptRespStatus(ch.swisscom.mid.client.rest.model.receiptresp.Status mssStatus) {
        ch.swisscom.mid.client.model.Status result = new ch.swisscom.mid.client.model.Status();
        if (mssStatus != null) {
            ch.swisscom.mid.client.rest.model.receiptresp.StatusCode mssStatusCode = mssStatus.getStatusCode();
            if (mssStatusCode != null) {
                String statusCodeString = mssStatusCode.getValue();
                result.setStatusCodeString(statusCodeString);
                result.setStatusCode(ch.swisscom.mid.client.model.StatusCode.getByStatusCodeString(statusCodeString));
                result.setStatusMessage(result.getStatusCode().name());
            }
        }
        return result;
    }

    private static ReceiptResponseExtension processReceiptRespExtension(
        ch.swisscom.mid.client.rest.model.receiptresp.ReceiptResponseExtension mssExtension) {
        if (mssExtension == null) {
            return null;
        }

        ReceiptResponseExtension extension = new ReceiptResponseExtension();
        extension.setMessagingMode(ReceiptMessagingMode.getByValue(mssExtension.getReceiptMessagingMode()));
        extension.setClientAck(Boolean.parseBoolean(mssExtension.getClientAck()));
        extension.setNetworkAck(Boolean.parseBoolean(mssExtension.getNetworkAck()));
        extension.setUserAck(Boolean.parseBoolean(mssExtension.getUserAck()));
        extension.setUserResponse(mssExtension.getUserResponse());

        return extension;
    }

}
