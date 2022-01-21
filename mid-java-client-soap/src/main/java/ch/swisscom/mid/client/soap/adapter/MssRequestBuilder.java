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

import org.etsi.uri.ts102204.v1_1.*;

import java.math.BigInteger;
import java.util.List;

import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.model.*;
import ch.swisscom.mid.client.utils.Utils;
import ch.swisscom.ts102204.ext.v1_0.ReceiptExtensionType;
import ch.swisscom.ts102204.ext.v1_0.ReceiptMessagingModeType;
import ch.swisscom.ts102204.ext.v1_0.ReceiptProfileType;
import fi.ficom.mss.ts102204.v1_0.ObjectFactory;

public class MssRequestBuilder {

    public static MSSSignatureReqType createSignatureReq(ClientConfiguration config,
                                                         SignatureRequest signatureRequest,
                                                         boolean sync) {
        MSSSignatureReqType request = new MSSSignatureReqType();

        // Set required MinorVersion and MajorVersion
        request.setMajorVersion(longToBigInteger(signatureRequest.getMajorVersion()));
        request.setMinorVersion(longToBigInteger(signatureRequest.getMinorVersion()));
        request.setTimeOut(BigInteger.valueOf(signatureRequest.getUserResponseTimeOutInSeconds()));
        // Set the messaging mode
        request.setMessagingMode(sync ? MessagingModeType.SYNCH : MessagingModeType.ASYNCH_CLIENT_SERVER);
        /* now set the elements */
        // Set the AP info
        request.setAPInfo(createApInfo(config, signatureRequest.getOverrideApId(), signatureRequest.getOverrideApPassword()));
        // Set the MSSP info
        request.setMSSPInfo(createMsspInfo(config));
        // Set the MobileUser
        request.setMobileUser(createMobileUser(signatureRequest.getMobileUser().getMsisdn()));
        // Set the DTBS
        request.setDataToBeSigned(createDtbs(signatureRequest.getDataToBeSigned()));
        // Set the MSS_Format
        request.setMSSFormat(null);
        // Set the SignatureProfile
        request.setSignatureProfile(createSignatureProfile(signatureRequest.getSignatureProfile()));
        // Set the Additional Services
        request.setAdditionalServices(createAdditionalServiceList(signatureRequest));

        return request;
    }

    public static MSSStatusReqType createStatusQueryReq(ClientConfiguration config, SignatureTracking signatureTracking) {
        MSSStatusReqType mssReq = new MSSStatusReqType();
        mssReq.setAPInfo(createApInfo(config, signatureTracking.getOverrideApId(), signatureTracking.getOverrideApPassword()));
        mssReq.setMSSPInfo(createMsspInfo(config));
        mssReq.setMajorVersion(longToBigInteger(signatureTracking.getMajorVersion()));
        mssReq.setMinorVersion(longToBigInteger(signatureTracking.getMinorVersion()));
        mssReq.setMSSPTransID(signatureTracking.getTransactionId());
        mssReq.setMobileUser(createMobileUser(signatureTracking.getMobileUserMsisdn()));
        return mssReq;
    }

    public static MSSReceiptReqType createReceiptReq(ClientConfiguration config,
                                                     SignatureTracking signatureTracking,
                                                     ReceiptRequest clientRequest) {
        StatusCodeType mssStatusCode = new StatusCodeType();
        mssStatusCode.setValue(BigInteger.valueOf(clientRequest.getStatusCode().getCode()));

        StatusType mssStatus = new StatusType();
        mssStatus.setStatusCode(mssStatusCode);
        if (clientRequest.getRequestExtension() != null) {
            mssStatus.setStatusDetail(createStatusDetail(clientRequest.getRequestExtension()));
        }

        MSSReceiptReqType mssReq = new MSSReceiptReqType();
        mssReq.setMajorVersion(longToBigInteger(clientRequest.getMajorVersion()));
        mssReq.setMinorVersion(longToBigInteger(clientRequest.getMinorVersion()));
        mssReq.setAPInfo(createApInfo(config, clientRequest.getOverrideApId(), clientRequest.getOverrideApPassword()));
        mssReq.setMSSPInfo(createMsspInfo(config));
        mssReq.setMobileUser(createMobileUser(signatureTracking.getMobileUserMsisdn()));
        mssReq.setMSSPTransID(signatureTracking.getTransactionId());
        mssReq.setMessage(createMessage(clientRequest.getMessageToBeDisplayed()));
        mssReq.setStatus(mssStatus);

        return mssReq;
    }

    public static MSSProfileReqType createProfileReq(ClientConfiguration config,
                                                     ProfileRequest profileRequest) {
        MSSProfileReqType mssProfileReq = new MSSProfileReqType();
        mssProfileReq.setAPInfo(createApInfo(config, profileRequest.getOverrideApId(), profileRequest.getOverrideApPassword()));
        mssProfileReq.setMajorVersion(longToBigInteger(profileRequest.getMajorVersion()));
        mssProfileReq.setMinorVersion(longToBigInteger(profileRequest.getMinorVersion()));
        mssProfileReq.setMSSPInfo(createMsspInfo(config));
        mssProfileReq.setMobileUser(createMobileUser(profileRequest.getMobileUser().getMsisdn()));
        mssProfileReq.setParams(Utils.joinListOfStrings(profileRequest.getExtensionParams(), " "));
        return mssProfileReq;
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * Constructs the Application Provider's information, generated transaction id
     * and timestamp issued by the AP for the request.
     *
     * @return the constructed {@link MessageAbstractType.APInfo} instance
     * @throws ch.swisscom.mid.client.model.DataAssemblyException in case errors are encountered while constructing the
     *                                                            {@link MessageAbstractType.APInfo} instance
     */
    private static MessageAbstractType.APInfo createApInfo(ClientConfiguration config, String overrideApId, String overrideApPassword) {
        MessageAbstractType.APInfo apInfoType = new MessageAbstractType.APInfo();
        apInfoType.setAPID(overrideApId != null ? overrideApId : config.getApId());
        apInfoType.setAPPWD(overrideApPassword != null ? overrideApPassword : config.getApPassword());
        apInfoType.setAPTransID(Utils.generateTransId());
        apInfoType.setInstant(Utils.generateInstantAsXmlGregorianCalendar());
        return apInfoType;
    }

    /**
     * Constructs the MSSP's information. According to the MSS standard, the Application Provider must enter the element
     * MSSP_Info and the element MSSP_ID for it in the signature request. However, the latter element can normally be left
     * blank in a signature request as the AP does not need to know the HMSSP to which the signature request is directed.
     *
     * @return the constructed {@link MessageAbstractType.MSSPInfo} instance
     */
    private static MessageAbstractType.MSSPInfo createMsspInfo(ClientConfiguration config) {
        MessageAbstractType.MSSPInfo msspInfo = new MessageAbstractType.MSSPInfo();
        MeshMemberType meshMemberType = new MeshMemberType();
        meshMemberType.setURI(config.getMsspId());
        msspInfo.setMSSPID(meshMemberType);
        return msspInfo;
    }

    /**
     * Constructs the end user's contact information, currently always an element in the format:
     * &lt;MSISDN&gt;+418123456789@&lt;/MSISDN&gt; in which the prefix +41 (in Switzerland) of the mobile phone number is
     * obligatory, i.e. the international number format is applied.
     *
     * @param msisdn the mobile phone number of the mobile user
     * @return the constructed {@link MobileUserType} instance
     */
    private static MobileUserType createMobileUser(String msisdn) {
        MobileUserType mobileUserType = new MobileUserType();
        mobileUserType.setMSISDN(msisdn);
        return mobileUserType;
    }

    /**
     * Constructs the data to be signed.
     *
     * @return the constructed {@link DataType} instance
     * @value the actual value of the DTBS
     */
    private static DataType createDtbs(DataToBeSigned dataToBeSigned) {
        DataType dataType = new DataType();
        dataType.setMimeType(dataToBeSigned.getMimeType());
        dataType.setEncoding(dataToBeSigned.getEncoding());
        dataType.setValue(dataToBeSigned.getData());
        return dataType;
    }

    /**
     * The signature profile to be used, i.e. the selected service.
     * <p>
     * Please check https://issuetracker.sicap.com/browse/MINKAPL-132 for the list of available profiles.
     *
     * @return the constructed {@link MssURIType} instance with the URI configured for the key
     */
    private static MssURIType createSignatureProfile(String signatureProfile) {
        MssURIType uriType = new MssURIType();
        uriType.setMssURI(signatureProfile);
        return uriType;
    }

    private static MSSSignatureReqType.AdditionalServices createAdditionalServiceList(SignatureRequest signatureRequest) {
        if (signatureRequest.getAdditionalServices().size() <= 0) {
            return null;
        }
        MSSSignatureReqType.AdditionalServices serviceList = new MSSSignatureReqType.AdditionalServices();
        List<ch.swisscom.mid.client.model.AdditionalService> requestedAdditionalService = signatureRequest.getAdditionalServices();
        for (ch.swisscom.mid.client.model.AdditionalService currentAS : requestedAdditionalService) {
            AdditionalServiceType additionalService = new AdditionalServiceType();
            if (currentAS instanceof UserLangAdditionalService) {
                MssURIType serviceDescription = new MssURIType();
                serviceDescription.setMssURI(currentAS.getUri());
                additionalService.setDescription(serviceDescription);
                fi.ficom.mss.ts102204.v1_0.ObjectFactory factory = new ObjectFactory();
                additionalService
                    .getSessionIDOrEventIDOrNoSpamCode()
                    .add(factory.createUserLang(((UserLangAdditionalService) currentAS).getUserLanguage().getValue()));
            } else {
                MssURIType serviceDescription = new MssURIType();
                serviceDescription.setMssURI(currentAS.getUri());
                additionalService.setDescription(serviceDescription);
            }
            serviceList.getService().add(additionalService);
        }
        return serviceList;
    }

    private static BigInteger longToBigInteger(String valueAsString) {
        return BigInteger.valueOf(Long.parseLong(valueAsString));
    }

    private static DataType createMessage(MessageToBeDisplayed messageToBeDisplayed) {
        DataType message = new DataType();
        message.setValue(messageToBeDisplayed.getData());
        message.setEncoding(messageToBeDisplayed.getEncoding());
        message.setMimeType(messageToBeDisplayed.getMimeType());
        return message;
    }

    private static StatusDetailType createStatusDetail(ch.swisscom.mid.client.model.ReceiptRequestExtension extension) {
        ReceiptProfileType mssReceiptProfile = new ReceiptProfileType();
        mssReceiptProfile.setLanguage(extension.getReceiptProfile().getLanguage());
        mssReceiptProfile.setReceiptProfileURI(extension.getReceiptProfile().getProfileUri());

        ReceiptExtensionType mssExtension = new ReceiptExtensionType();
        mssExtension.setReceiptMessagingMode(toMssModel(extension.getMessagingMode()));
        mssExtension.setUserAck(extension.isRequestUserAck());
        mssExtension.setReceiptProfile(mssReceiptProfile);

        ch.swisscom.ts102204.ext.v1_0.ObjectFactory objectFactory = new ch.swisscom.ts102204.ext.v1_0.ObjectFactory();

        StatusDetailType mssStatusDetail = new StatusDetailType();
        mssStatusDetail.getRegistrationOutputOrEncryptedRegistrationOutputOrEncryptionCertificates()
            .add(objectFactory.createReceiptRequestExtension(mssExtension));
        return mssStatusDetail;
    }

    private static ReceiptMessagingModeType toMssModel(ReceiptMessagingMode mode) {
        if (mode == null) {
            return null;
        }
        if (mode == ReceiptMessagingMode.SYNC) {
            return ReceiptMessagingModeType.SYNCH;
        }
        throw new IllegalArgumentException("Unknown receipt message mode: " + mode);
    }

}
