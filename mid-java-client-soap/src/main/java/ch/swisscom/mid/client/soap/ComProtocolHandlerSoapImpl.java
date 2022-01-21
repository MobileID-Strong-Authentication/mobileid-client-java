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
package ch.swisscom.mid.client.soap;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.etsi.uri.ts102204.etsi204_kiuru.MSSProfileQueryType;
import org.etsi.uri.ts102204.etsi204_kiuru.MSSReceiptType;
import org.etsi.uri.ts102204.etsi204_kiuru.MSSSignaturePortType;
import org.etsi.uri.ts102204.etsi204_kiuru.MSSStatusQueryType;
import org.etsi.uri.ts102204.v1_1.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.soap.SOAPFaultException;

import ch.swisscom.mid.client.MIDFlowException;
import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.config.ComProtocol;
import ch.swisscom.mid.client.config.TrafficObserver;
import ch.swisscom.mid.client.impl.ComProtocolHandler;
import ch.swisscom.mid.client.impl.Loggers;
import ch.swisscom.mid.client.model.*;
import ch.swisscom.mid.client.soap.adapter.MssFaultProcessor;
import ch.swisscom.mid.client.soap.adapter.MssRequestBuilder;
import ch.swisscom.mid.client.soap.adapter.MssResponseProcessor;

public class ComProtocolHandlerSoapImpl implements ComProtocolHandler {

    private static final Logger logClient = LoggerFactory.getLogger(Loggers.LOGGER_CLIENT);
    private static final Logger logConfig = LoggerFactory.getLogger(Loggers.LOGGER_CONFIG);
    private static final Logger logProtocol = LoggerFactory.getLogger(Loggers.LOGGER_CLIENT_PROTOCOL);

    private ClientConfiguration config;
    private ObjectPool<MssService<MSSSignaturePortType>> mssSignatureServicePool;
    private ObjectPool<MssService<MSSStatusQueryType>> mssStatusQueryServicePool;
    private ObjectPool<MssService<MSSReceiptType>> mssReceiptServicePool;
    private ObjectPool<MssService<MSSProfileQueryType>> mssProfileQueryServicePool;

    @Override
    public ComProtocol getImplementedComProtocol() {
        return ComProtocol.SOAP;
    }

    @Override
    public void initialize(ClientConfiguration config) {
        this.config = config;
        mssSignatureServicePool = new GenericObjectPool<>(new MssServiceFactory<>(config,
                                                                                  MSSSignaturePortType.class,
                                                                                  config.getUrls()::getSignatureServiceUrl));
        mssStatusQueryServicePool = new GenericObjectPool<>(new MssServiceFactory<>(config,
                                                                                    MSSStatusQueryType.class,
                                                                                    config.getUrls()::getStatusQueryServiceUrl));
        mssReceiptServicePool = new GenericObjectPool<>(new MssServiceFactory<>(config,
                                                                                MSSReceiptType.class,
                                                                                config.getUrls()::getReceiptServiceUrl));
        mssProfileQueryServicePool = new GenericObjectPool<>(new MssServiceFactory<>(config,
                                                                                     MSSProfileQueryType.class,
                                                                                     config.getUrls()::getProfileQueryServiceUrl));
        logConfig.info("Initializing MID SOAP client with config: [{}]", config);
    }

    @Override
    public void close() {
        // no code here
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public SignatureResponse requestSyncSignature(SignatureRequest request) {
        logProtocol.info("MSS Signature (sync): Sending request: [{}]", request);
        MSSSignatureReqType mssSignatureReq = MssRequestBuilder.createSignatureReq(config, request, true);
        notifyTrafficObserverForApTransId(request.getTrafficObserver(), mssSignatureReq.getAPInfo().getAPTransID());
        MSSSignatureRespType mssSignatureResp;
        MssService<MSSSignaturePortType> mssSignatureService = null;
        try {
            mssSignatureService = mssSignatureServicePool.borrowObject();
            mssSignatureService.registerTrafficObserverForThisRequest(request.getTrafficObserver());
            mssSignatureResp = mssSignatureService.getPort().mssSignature(mssSignatureReq);
            logClient.info("Received MSS (sync) signature response: [{}]", mssSignatureResp == null ? "null" : "not-null, looks OK");
        } catch (SOAPFaultException e) {
            throw new MIDFlowException("SOAP Fault received", e,
                                       MssFaultProcessor.processSoapFaultException(e));
        } catch (Exception e) {
            throw new MIDFlowException("Error in (sync) Signature operation.", e,
                                       MssFaultProcessor.processException(e, FailureReason.MID_SERVICE_FAILURE));
        } finally {
            if (mssSignatureService != null) {
                try {
                    mssSignatureService.clearTrafficObserver();
                    mssSignatureServicePool.returnObject(mssSignatureService);
                } catch (Exception e) {
                    logClient.error("Failed to return MSS Signature Port object back to the pool", e);
                }
            }
        }
        SignatureResponse signatureResponse = MssResponseProcessor.processMssSignatureResponse(mssSignatureResp);
        signatureResponse.setTracking(MssResponseProcessor.createSignatureTracking(mssSignatureResp,
                                                                                   request.getTrafficObserver(),
                                                                                   request.getOverrideApId(),
                                                                                   request.getOverrideApPassword()));
        return signatureResponse;
    }

    @Override
    public SignatureResponse requestAsyncSignature(SignatureRequest request) {
        logProtocol.info("MSS Signature (async): Sending request: [{}]", request);
        MSSSignatureReqType mssSignatureReq = MssRequestBuilder.createSignatureReq(config, request, false);
        notifyTrafficObserverForApTransId(request.getTrafficObserver(), mssSignatureReq.getAPInfo().getAPTransID());
        MSSSignatureRespType mssSignatureResp;
        MssService<MSSSignaturePortType> mssSignatureService = null;
        try {
            mssSignatureService = mssSignatureServicePool.borrowObject();
            mssSignatureService.registerTrafficObserverForThisRequest(request.getTrafficObserver());
            mssSignatureResp = mssSignatureService.getPort().mssSignature(mssSignatureReq);
            logClient.info("Received MSS (async) signature response: [{}]", mssSignatureResp == null ? "null" : "not-null, looks OK");
        } catch (SOAPFaultException e) {
            throw new MIDFlowException("SOAP Fault received", e,
                                       MssFaultProcessor.processSoapFaultException(e));
        } catch (Exception e) {
            throw new MIDFlowException("Error in (async) Signature operation.", e,
                                       MssFaultProcessor.processException(e, FailureReason.MID_SERVICE_FAILURE));
        } finally {
            if (mssSignatureService != null) {
                try {
                    mssSignatureService.clearTrafficObserver();
                    mssSignatureServicePool.returnObject(mssSignatureService);
                } catch (Exception e) {
                    logClient.error("Failed to return MSS Signature Port object back to the pool", e);
                }
            }
        }
        SignatureResponse signatureResponse = MssResponseProcessor.processMssSignatureResponse(mssSignatureResp);
        signatureResponse.setTracking(MssResponseProcessor.createSignatureTracking(mssSignatureResp,
                                                                                   request.getTrafficObserver(),
                                                                                   request.getOverrideApId(),
                                                                                   request.getOverrideApPassword()));
        return signatureResponse;
    }

    @Override
    public SignatureResponse pollForSignatureStatus(SignatureTracking signatureTracking) {
        logProtocol.info("MSS Status Query: Sending request for signature tracking object: [{}]", signatureTracking);
        MSSStatusReqType mssStatusReqType = MssRequestBuilder.createStatusQueryReq(config, signatureTracking);
        notifyTrafficObserverForApTransId(signatureTracking.getTrafficObserver(), mssStatusReqType.getAPInfo().getAPTransID());
        MSSStatusRespType mssStatusRespType;
        MssService<MSSStatusQueryType> mssStatusQueryService = null;
        try {
            mssStatusQueryService = mssStatusQueryServicePool.borrowObject();
            mssStatusQueryService.registerTrafficObserverForThisRequest(signatureTracking.getTrafficObserver());
            mssStatusRespType = mssStatusQueryService.getPort().mssStatusQuery(mssStatusReqType);
            logClient.info("Received MSS Status Query response: [{}]", mssStatusRespType == null ? "null" : "not-null, looks OK");
        } catch (SOAPFaultException e) {
            throw new MIDFlowException("SOAP Fault received", e,
                                       MssFaultProcessor.processSoapFaultException(e));
        } catch (Exception e) {
            throw new MIDFlowException("Error in Status Query operation.", e,
                                       MssFaultProcessor.processException(e, FailureReason.MID_SERVICE_FAILURE));
        } finally {
            if (mssStatusQueryService != null) {
                try {
                    mssStatusQueryService.clearTrafficObserver();
                    mssStatusQueryServicePool.returnObject(mssStatusQueryService);
                } catch (Exception e) {
                    logClient.error("Failed to return MSS Status Query object back to the pool", e);
                }
            }
        }
        return MssResponseProcessor.processStatusQueryResponse(mssStatusRespType, signatureTracking);
    }

    @Override
    public ReceiptResponse requestSyncReceipt(SignatureTracking signatureTracking, ReceiptRequest request) {
        logProtocol.info("MSS Receipt (sync): Sending request: [{}]", request);
        MSSReceiptReqType mssReceiptReq = MssRequestBuilder.createReceiptReq(config, signatureTracking, request);
        notifyTrafficObserverForApTransId(request.getTrafficObserver(), mssReceiptReq.getAPInfo().getAPTransID());
        MSSReceiptRespType mssReceiptResp;
        MssService<MSSReceiptType> mssReceiptService = null;
        try {
            mssReceiptService = mssReceiptServicePool.borrowObject();
            mssReceiptService.registerTrafficObserverForThisRequest(request.getTrafficObserver());
            mssReceiptResp = mssReceiptService.getPort().mssReceipt(mssReceiptReq);
            logClient.info("Received MSS Receipt response: [{}]", mssReceiptResp == null ? "null" : "not-null, looks OK");
        } catch (SOAPFaultException e) {
            throw new MIDFlowException("SOAP Fault received", e,
                                       MssFaultProcessor.processSoapFaultException(e));
        } catch (Exception e) {
            throw new MIDFlowException("Error in MSS Receipt operation.", e,
                                       MssFaultProcessor.processException(e, FailureReason.MID_SERVICE_FAILURE));
        } finally {
            if (mssReceiptService != null) {
                try {
                    mssReceiptService.clearTrafficObserver();
                    mssReceiptServicePool.returnObject(mssReceiptService);
                } catch (Exception e) {
                    logClient.error("Failed to return MSS Receipt Port object back to the pool", e);
                }
            }
        }
        return MssResponseProcessor.processReceiptResponse(mssReceiptResp);
    }

    @Override
    public ProfileResponse requestProfile(ProfileRequest request) {
        logProtocol.info("MSS Profile Query: Sending request: [{}]", request);
        MSSProfileReqType mssProfileReq = MssRequestBuilder.createProfileReq(config, request);
        notifyTrafficObserverForApTransId(request.getTrafficObserver(), mssProfileReq.getAPInfo().getAPTransID());
        MSSProfileRespType mssProfileResp;
        MssService<MSSProfileQueryType> mssProfileQueryService = null;
        try {
            mssProfileQueryService = mssProfileQueryServicePool.borrowObject();
            mssProfileQueryService.registerTrafficObserverForThisRequest(request.getTrafficObserver());
            mssProfileResp = mssProfileQueryService.getPort().mssProfileQuery(mssProfileReq);
            logClient.info("Received MSS Profile Query response: [{}]", mssProfileResp == null ? "null" : "not-null, looks OK");
        } catch (SOAPFaultException e) {
            throw new MIDFlowException("SOAP Fault received", e, MssFaultProcessor.processSoapFaultException(e));
        } catch (Exception e) {
            throw new MIDFlowException("Error in Profile Query operation.", e,
                                       MssFaultProcessor.processException(e, FailureReason.MID_SERVICE_FAILURE));
        } finally {
            if (mssProfileQueryService != null) {
                try {
                    mssProfileQueryService.clearTrafficObserver();
                    mssProfileQueryServicePool.returnObject(mssProfileQueryService);
                } catch (Exception e) {
                    logClient.error("Failed to return MSS Profile Query Port object back to the pool", e);
                }
            }
        }
        return MssResponseProcessor.processMssProfileQueryResponse(mssProfileResp);
    }

    // ----------------------------------------------------------------------------------------------------

    private void notifyTrafficObserverForApTransId(TrafficObserver trafficObserver, String apTransId) {
        if (trafficObserver == null) {
            return;
        }
        trafficObserver.notifyOfGeneratedApTransId(apTransId, ComProtocol.REST);
    }

}
