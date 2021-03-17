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
import java.util.function.Supplier;

import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.model.*;
import ch.swisscom.mid.client.rest.model.profqreq.APInfo;
import ch.swisscom.mid.client.rest.model.profqreq.MSSPInfo;
import ch.swisscom.mid.client.rest.model.profqreq.MSSProfileQueryRequest;
import ch.swisscom.mid.client.rest.model.profqreq.MSSProfileReq;
import ch.swisscom.mid.client.rest.model.profqreq.MsspId;
import ch.swisscom.mid.client.rest.model.profqresp.MobileUser;
import ch.swisscom.mid.client.rest.model.profqresp.*;
import ch.swisscom.mid.client.utils.Utils;

import static ch.swisscom.mid.client.utils.Utils.generateInstantAsString;
import static ch.swisscom.mid.client.utils.Utils.generateTransId;

public class ProfileRequestModelUtils {

    public static MSSProfileQueryRequest createProfileQueryRequest(ProfileRequest request, ClientConfiguration config) {
        MSSProfileReq mssRequest = new MSSProfileReq();
        mssRequest.setAPInfo(createApInfo(config));
        mssRequest.setMajorVersion(request.getMajorVersion());
        mssRequest.setMinorVersion(request.getMinorVersion());
        mssRequest.setMSSPInfo(createMsspInfo(config));
        mssRequest.setMobileUser(createMobileUser(request));
        mssRequest.setParams(Utils.joinListOfStrings(request.getExtensionParams(), " "));

        MSSProfileQueryRequest mssRequestWrapper = new MSSProfileQueryRequest();
        mssRequestWrapper.setMSSProfileReq(mssRequest);
        return mssRequestWrapper;
    }

    public static ProfileResponse processProfileQueryResponse(MSSProfileQueryResponse mssResponseWrapper) {
        ProfileResponse response = new ProfileResponse();
        MSSProfileResp mssResponse = mssResponseWrapper.getMSSProfileResp();
        response.setSignatureProfiles(mssResponse.getSignatureProfile());
        if (mssResponse.getStatus() != null &&
            mssResponse.getStatus().getStatusDetail() != null &&
            mssResponse.getStatus().getStatusDetail().getProfileQueryExtension() != null) {
            ProfileQueryExtension mssPQExt = mssResponse.getStatus().getStatusDetail().getProfileQueryExtension();
            if (mssPQExt.getMobileUser() != null) {
                ProfileMobileUserInfo mobileUserInfo = new ProfileMobileUserInfo();
                MobileUser mssPQMobileUser = mssPQExt.getMobileUser();
                if (mssPQMobileUser.getRecoveryCodeCreated() != null) {
                    mobileUserInfo.setRecoveryCodeCreated(mssPQMobileUser.getRecoveryCodeCreated());
                }
                if (mssPQMobileUser.getAutoActivation() != null) {
                    mobileUserInfo.setAutoActivation(mssPQMobileUser.getAutoActivation());
                }
                response.setMobileUser(mobileUserInfo);
            }
            if (mssPQExt.getSscds() != null) {
                response.setSimDevices(new ArrayList<>());
                response.setAppDevices(new ArrayList<>());
                Sscds mssSscds = mssPQExt.getSscds();
                if (mssSscds.getSim() != null) {
                    Sim mssSim = mssSscds.getSim();
                    response.getSimDevices().add(processDeviceInfo(mssSim::getState,
                                                                   mssSim::getPinStatus,
                                                                   mssSim::getMobileUserCertificate));
                }
                if (mssSscds.getApp() != null && mssSscds.getApp().size() > 0) {
                    List<App> mssAppList = mssSscds.getApp();
                    for (App mssApp : mssAppList) {
                        response.getAppDevices().add(processDeviceInfo(mssApp::getState,
                                                                       mssApp::getPinStatus,
                                                                       mssApp::getMobileUserCertificate));
                    }
                }
            }
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

    private static ch.swisscom.mid.client.rest.model.profqreq.MobileUser createMobileUser(ProfileRequest request) {
        ch.swisscom.mid.client.rest.model.profqreq.MobileUser mobileUser = new ch.swisscom.mid.client.rest.model.profqreq.MobileUser();
        mobileUser.setMsisdn(request.getMobileUser().getMsisdn());
        return mobileUser;
    }

    private static MSSPInfo createMsspInfo(ClientConfiguration config) {
        MsspId msspId = new MsspId();
        msspId.setUri(config.getMsspId());
        MSSPInfo msspInfo = new MSSPInfo();
        msspInfo.setMsspId(msspId);
        return msspInfo;
    }

    private static ProfileDeviceInfo processDeviceInfo(Supplier<String> stateSupplier,
                                                       Supplier<PinStatus> pinStatusSupplier,
                                                       Supplier<List<MobileUserCertificate>> certificateListSupplier) {
        ProfileDeviceInfo deviceInfo = new ProfileDeviceInfo();
        deviceInfo.setState(ProfileDeviceState.getByStateString(stateSupplier.get()));
        if (pinStatusSupplier.get() != null) {
            deviceInfo.setPinState(ProfileDevicePinState.getByPinBlockedBooleanValue(pinStatusSupplier.get().getBlocked()));
        }
        if (certificateListSupplier.get() != null && certificateListSupplier.get().size() > 0) {
            deviceInfo.setCertificates(new ArrayList<>());

            for (MobileUserCertificate mssCert : certificateListSupplier.get()) {
                ProfileMobileUserCertificate cert = new ProfileMobileUserCertificate();
                cert.setAlgorithm(mssCert.getAlgorithm());
                cert.setState(ProfileMobileUserCertificateState.getByStateString(mssCert.getState()));

                CertificateData userCertificate = new CertificateData();
                List<CertificateData> caCertificates = new ArrayList<>();

                if (mssCert.getX509Certificate() != null && mssCert.getX509Certificate().size() > 0) {
                    userCertificate.setCertificateAsBase64(mssCert.getX509Certificate().get(0));
                    for (int index = 1; index < mssCert.getX509Certificate().size(); index++) {
                        CertificateData certificateData = new CertificateData();
                        certificateData.setCertificateAsBase64(mssCert.getX509Certificate().get(index));
                        caCertificates.add(certificateData);
                    }
                }
                if (mssCert.getX509SubjectName() != null && mssCert.getX509SubjectName().size() > 0) {
                    userCertificate.setSubjectName(mssCert.getX509SubjectName().get(0));
                    for (int index = 1; index < mssCert.getX509SubjectName().size(); index++) {
                        CertificateData certificateData;
                        if (index - 1 < caCertificates.size()) {
                            certificateData = caCertificates.get(index - 1);
                        } else {
                            certificateData = new CertificateData();
                            caCertificates.add(certificateData);
                        }
                        certificateData.setSubjectName(mssCert.getX509SubjectName().get(index));
                    }
                }

                cert.setUserCertificate(userCertificate);
                cert.setCaCertificates(caCertificates);
                deviceInfo.getCertificates().add(cert);
            }
        }
        return deviceInfo;
    }

}
