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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import ch.swisscom.mid.client.MIDFlowException;
import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.config.TrafficObserver;
import ch.swisscom.mid.client.impl.Loggers;
import ch.swisscom.mid.client.model.*;
import ch.swisscom.mid.ts102204.as.v1.GeoFencing;
import ch.swisscom.ts102204.ext.v1_0.ReceiptExtensionType;
import fi.ficom.mss.ts102204.v1_0.ServiceResponses;
import fi.methics.ts102204.ext.v1_0.CertificateType;
import fi.methics.ts102204.ext.v1_0.MobileUserType;
import fi.methics.ts102204.ext.v1_0.PinStatusType;
import fi.methics.ts102204.ext.v1_0.ProfileQueryExtension;
import fi.methics.ts102204.ext.v1_0.SscdListType;
import fi.methics.ts102204.ext.v1_0.SscdType;

public class MssResponseProcessor {

    private static final Logger logProtocol = LoggerFactory.getLogger(Loggers.LOGGER_CLIENT_PROTOCOL);

    public static SignatureResponse processMssSignatureResponse(MSSSignatureRespType mssResponse) {
        SignatureResponse signatureResponse = new SignatureResponse();
        if (mssResponse != null) {
            MssURIType mssSigProfileUri = mssResponse.getSignatureProfile();
            if (mssSigProfileUri != null) {
                signatureResponse.setSignatureProfile(mssSigProfileUri.getMssURI());
            }
            SignatureType mssSignature = mssResponse.getMSSSignature();
            if (mssSignature != null) {
                signatureResponse.setBase64Signature(new String(mssSignature.getBase64Signature(), StandardCharsets.UTF_8));
            }
            StatusType mssResponseStatus = mssResponse.getStatus();
            signatureResponse.setStatus(processStatus(mssResponseStatus));
            signatureResponse.setAdditionalServiceResponses(processAdditionalServiceResponses(mssResponseStatus));
        } else {
            signatureResponse.setStatus(getGenericErrorStatus("Invalid MSS Signature response (it was NULL)"));
        }
        return signatureResponse;
    }


    public static ProfileResponse processMssProfileQueryResponse(MSSProfileRespType mssResponse) {
        ProfileResponse response = new ProfileResponse();
        if (mssResponse.getSignatureProfile() != null) {
            response.setSignatureProfiles(
                mssResponse.getSignatureProfile().stream().map(MssURIType::getMssURI).collect(Collectors.toList()));
        }
        if (mssResponse.getStatus() != null &&
            mssResponse.getStatus().getStatusDetail() != null &&
            mssResponse.getStatus().getStatusDetail().getRegistrationOutputOrEncryptedRegistrationOutputOrEncryptionCertificates() != null) {
            List<Object> profileQueryExtensions = mssResponse
                .getStatus()
                .getStatusDetail()
                .getRegistrationOutputOrEncryptedRegistrationOutputOrEncryptionCertificates();
            if (profileQueryExtensions.size() == 1) {
                ProfileQueryExtension mssPQExt = (ProfileQueryExtension) profileQueryExtensions.get(0);
                if (mssPQExt.getMobileUser() != null) {
                    ProfileMobileUserInfo mobileUserInfo = new ProfileMobileUserInfo();
                    MobileUserType mssPQMobileUser = mssPQExt.getMobileUser();
                    if (mssPQMobileUser != null) {
                        mobileUserInfo.setRecoveryCodeCreated(mssPQMobileUser.isRecoveryCodeCreated());
                        mobileUserInfo.setAutoActivation(Boolean.parseBoolean(
                            mssPQMobileUser.getOtherAttributes().get(new QName("AutoActivation"))));
                    }
                    response.setMobileUser(mobileUserInfo);
                }
                if (mssPQExt.getSscds() != null) {
                    response.setSimDevices(new ArrayList<>());
                    response.setAppDevices(new ArrayList<>());
                    SscdListType mssSscds = mssPQExt.getSscds();
                    if (mssSscds.getSim() != null) {
                        SscdType mssSim = mssSscds.getSim();
                        response.getSimDevices().add(processDeviceInfo(mssSim::getState,
                                                                       mssSim::getPinStatus,
                                                                       mssSim::getMobileUserCertificate));
                    }
                    if (mssSscds.getApp() != null && mssSscds.getApp().size() > 0) {
                        List<SscdType> mssAppList = mssSscds.getApp();
                        for (SscdType mssApp : mssAppList) {
                            response.getAppDevices().add(processDeviceInfo(mssApp::getState,
                                                                           mssApp::getPinStatus,
                                                                           mssApp::getMobileUserCertificate));
                        }
                    }
                }
            }
        }
        return response;
    }

    public static SignatureTracking createSignatureTracking(MSSSignatureRespType mssResponse, TrafficObserver trafficObserver,
                                                            String overrideApId, String overrideApPassword) {
        if (mssResponse != null) {
            SignatureTracking result = new SignatureTracking();
            result.setOverrideApId(overrideApId);
            result.setOverrideApPassword(overrideApPassword);
            result.setTrafficObserver(trafficObserver);
            result.setTransactionId(mssResponse.getMSSPTransID());
            result.setMobileUserMsisdn(mssResponse.getMobileUser().getMSISDN());
            return result;
        } else {
            throw new MIDFlowException("Invalid MSS response received. " +
                                       "Cannot parse it and convert it to a valid " +
                                       SignatureTracking.class.getSimpleName(),
                                       MssFaultProcessor.processFailure(FailureReason.MID_INVALID_RESPONSE_FAILURE));
        }
    }

    public static ReceiptResponse processReceiptResponse(MSSReceiptRespType mssResponse) {
        Status status = new Status();
        ReceiptResponseExtension receiptResponseExtension = null;
        if (mssResponse.getStatus() != null) {
            StatusType mssStatus = mssResponse.getStatus();
            StatusCodeType mssStatusCode = mssStatus.getStatusCode();
            if (mssStatusCode != null) {
                BigInteger statusCode = mssStatusCode.getValue();
                if (statusCode != null) {
                    String statusCodeString = String.valueOf(statusCode);
                    status.setStatusCodeString(statusCodeString);
                    status.setStatusCode(StatusCode.getByStatusCodeString(statusCodeString));
                    status.setStatusMessage(status.getStatusCode().name());
                }
            }
            if (mssStatus.getStatusDetail() != null &&
                mssStatus.getStatusDetail().getRegistrationOutputOrEncryptedRegistrationOutputOrEncryptionCertificates() != null) {
                List<Object> mssResponseExtensionList = mssStatus
                    .getStatusDetail()
                    .getRegistrationOutputOrEncryptedRegistrationOutputOrEncryptionCertificates();
                if (mssResponseExtensionList.size() == 1) {
                    @SuppressWarnings("unchecked")
                    JAXBElement<ReceiptExtensionType> receiptExtensionTypeElement =
                        (JAXBElement<ReceiptExtensionType>) mssStatus.getStatusDetail()
                            .getRegistrationOutputOrEncryptedRegistrationOutputOrEncryptionCertificates()
                            .get(0);
                    receiptResponseExtension = processReceiptRespExtension(receiptExtensionTypeElement.getValue());
                } else {
                    logProtocol.warn("Expected one MSS receipt extension in the MSS Receipt response. " +
                                     "Instead found [{}] extensions. " +
                                     "Skipping the receipt extension processing altogether", mssResponseExtensionList.size());
                }
            }
        }

        ReceiptResponse response = new ReceiptResponse();
        response.setStatus(status);
        response.setResponseExtension(receiptResponseExtension);

        return response;
    }

    public static SignatureResponse processStatusQueryResponse(MSSStatusRespType mssResponse, SignatureTracking originalTracking) {
        if (mssResponse != null) {
            SignatureResponse result = new SignatureResponse();
            result.setMajorVersion(mssResponse.getMajorVersion().toString());
            result.setMinorVersion(mssResponse.getMinorVersion().toString());
            result.setTracking(originalTracking);
            SignatureType mssSignature = mssResponse.getMSSSignature();
            if (mssSignature != null) {
                result.setBase64Signature(Base64.getEncoder().encodeToString(mssSignature.getBase64Signature()));
            }
            result.setStatus(processStatus(mssResponse.getStatus()));
            result.setAdditionalServiceResponses(processAdditionalServiceResponses(mssResponse.getStatus()));
            return result;
        } else {
            throw new MIDFlowException("Invalid MSS status response received. " +
                                       "Cannot parse it and convert it to a valid " +
                                       SignatureResponse.class.getSimpleName(),
                                       MssFaultProcessor.processFailure(FailureReason.MID_INVALID_RESPONSE_FAILURE));
        }
    }

    // ----------------------------------------------------------------------------------------------------

    private static Status processStatus(StatusType mssStatus) {
        if (mssStatus != null) {
            Status result = new Status();
            result.setStatusMessage(mssStatus.getStatusMessage());
            StatusCodeType mssStatusCode = mssStatus.getStatusCode();
            if (mssStatusCode != null && mssStatusCode.getValue() != null) {
                int statusCodeValue = mssStatusCode.getValue().intValue();
                StatusCode statusCode = StatusCode.getByStatusCodeValue(statusCodeValue);
                if (statusCode != null) {
                    result.setStatusCode(statusCode);
                    result.setStatusCodeString(String.valueOf(statusCode.getCode()));
                }
            }
            return result;
        } else {
            return getGenericErrorStatus("Invalid MSS Signature response status");
        }
    }

    private static List<AdditionalServiceResponse> processAdditionalServiceResponses(StatusType mssStatus) {
        List<AdditionalServiceResponse> resultList = new ArrayList<>();
        if (mssStatus != null) {
            StatusDetailType mssStatusDetail = mssStatus.getStatusDetail();
            if (mssStatusDetail != null) {
                List<Object> mssResponseList = mssStatusDetail.getRegistrationOutputOrEncryptedRegistrationOutputOrEncryptionCertificates();
                for (Object mssResponse : mssResponseList) {
                    if (mssResponse instanceof ServiceResponses) {
                        ServiceResponses mssServiceResponses = (ServiceResponses) mssResponse;
                        if (mssServiceResponses.getServiceResponse() != null &&
                            mssServiceResponses.getServiceResponse().size() > 0) {
                            ServiceResponses.ServiceResponse mssServiceResponse = mssServiceResponses.getServiceResponse().get(0);
                            if (mssServiceResponse.getDescription() != null &&
                                mssServiceResponse.getDescription().getMssURI() != null) {
                                String mssServiceUri = mssServiceResponse.getDescription().getMssURI();

                                if (DefaultConfiguration.ADDITIONAL_SERVICE_GEOFENCING.equals(mssServiceUri)
                                    && mssServiceResponse.getGeoFencing() != null) {

                                    GeoFencing geofencing = mssServiceResponse.getGeoFencing();
                                    GeofencingAdditionalServiceResponse geoResponse = new GeofencingAdditionalServiceResponse();
                                    if (geofencing.getErrorcode() == null) {
                                        geoResponse.setCountry(geofencing.getCountry());
                                        geoResponse.setAccuracy(geofencing.getAccuracy() == null ? 0 : Integer.parseInt(geofencing.getAccuracy()));
                                        geoResponse.setTimestamp(gregorianCalendarToString(geofencing.getTimestamp()));
                                        geoResponse.setDeviceConfidence(geofencing.getDeviceconfidence());
                                        geoResponse.setLocationConfidence(geofencing.getLocationconfidence());
                                    } else {
                                        geoResponse.setErrorCode(GeofencingErrorCode.getByCodeAsString(
                                            geofencing.getErrorcode() == null ? null : geofencing.getErrorcode().toString()));
                                        geoResponse.setErrorMessage(geofencing.getErrormessage());
                                    }
                                    resultList.add(geoResponse);
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultList;
    }

    private static Status getGenericErrorStatus(String statusMessage) {
        Status status = new Status();
        status.setStatusCode(StatusCode.INTERNAL_ERROR);
        status.setStatusMessage(statusMessage);
        return status;
    }

    @SuppressWarnings("unchecked")
    private static ProfileDeviceInfo processDeviceInfo(Supplier<String> stateSupplier,
                                                       Supplier<PinStatusType> pinStatusSupplier,
                                                       Supplier<List<CertificateType>> certificateListSupplier) {
        ProfileDeviceInfo deviceInfo = new ProfileDeviceInfo();
        deviceInfo.setState(ProfileDeviceState.getByStateString(stateSupplier.get()));
        if (pinStatusSupplier.get() != null) {
            deviceInfo.setPinState(ProfileDevicePinState.getByPinBlockedBooleanValue(pinStatusSupplier.get().isBlocked()));
        }
        if (certificateListSupplier.get() != null && certificateListSupplier.get().size() > 0) {
            deviceInfo.setCertificates(new ArrayList<>());
            for (CertificateType mssCert : certificateListSupplier.get()) {
                ProfileMobileUserCertificate cert = new ProfileMobileUserCertificate();
                cert.setAlgorithm(mssCert.getAlgorithm());
                cert.setState(ProfileMobileUserCertificateState.getByStateString(mssCert.getState()));

                List<Object> mssCertElementList = mssCert.getX509IssuerSerialOrX509SKIOrX509SubjectName();
                if (mssCertElementList != null && mssCertElementList.size() > 0) {
                    CertificateData certificateData = new CertificateData();
                    certificateData.setCertificateAsBase64(
                        Base64.getEncoder().encodeToString(((JAXBElement<byte[]>) mssCertElementList.get(0)).getValue()));
                    if (mssCertElementList.size() > 1) {
                        certificateData.setSubjectName(((JAXBElement<String>) mssCertElementList.get(1)).getValue());
                    }
                    cert.setUserCertificate(certificateData);
                    List<CertificateData> caCertificates = new ArrayList<>();
                    if (mssCertElementList.size() > 2) {
                        for (int index = 2; index < mssCertElementList.size(); index += 2) {
                            certificateData = new CertificateData();
                            certificateData.setCertificateAsBase64(
                                Base64.getEncoder().encodeToString(((JAXBElement<byte[]>) mssCertElementList.get(0)).getValue()));
                            if (index + 1 < mssCertElementList.size()) {
                                certificateData.setSubjectName(((JAXBElement<String>) mssCertElementList.get(index + 1)).getValue());
                            }
                            caCertificates.add(certificateData);
                        }
                    }
                    cert.setCaCertificates(caCertificates);
                }
                deviceInfo.getCertificates().add(cert);
            }
        }
        return deviceInfo;
    }

    private static ReceiptResponseExtension processReceiptRespExtension(ReceiptExtensionType mssReceiptExtension) {
        if (mssReceiptExtension == null) {
            return null;
        }

        ReceiptResponseExtension extension = new ReceiptResponseExtension();
        extension.setMessagingMode(ReceiptMessagingMode.getByValue(mssReceiptExtension.getReceiptMessagingMode().value()));
        extension.setClientAck(mssReceiptExtension.isClientAck());
        extension.setNetworkAck(mssReceiptExtension.isNetworkAck());
        extension.setUserAck(mssReceiptExtension.isUserAck());
        extension.setUserResponse(mssReceiptExtension.getUserResponse());

        return extension;
    }

    private static String gregorianCalendarToString(XMLGregorianCalendar gregorianCalendar) {
        if (gregorianCalendar == null) {
            return null;
        }
        GregorianCalendar calendar = gregorianCalendar.toGregorianCalendar();
        String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat);
        sdf.setTimeZone(calendar.getTimeZone());
        return sdf.format(calendar.getTime());
    }

}
