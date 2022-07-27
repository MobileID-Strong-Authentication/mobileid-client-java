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
package ch.swisscom.mid.client.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import ch.swisscom.mid.client.MIDClient;
import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.config.ConfigurationException;
import ch.swisscom.mid.client.model.*;

import static ch.swisscom.mid.client.utils.Utils.configNotNull;
import static ch.swisscom.mid.client.utils.Utils.configTrue;
import static ch.swisscom.mid.client.utils.Utils.dataNotNull;

public class MIDClientImpl implements MIDClient {

    private static final Logger logClient = LoggerFactory.getLogger(Loggers.CLIENT);
    private static final Logger logConfig = LoggerFactory.getLogger(Loggers.CONFIG);

    private final List<ComProtocolHandler> comProtocolHandlers;
    private final ComProtocolHandler selectedProtocolHandler;

    public MIDClientImpl(ClientConfiguration config) throws ConfigurationException {
        logClient.debug("Creating new instance of MIDClient");
        logConfig.debug("MIDClient configuration: {}", config);
        comProtocolHandlers = loadComProtocolHandlers();
        validateClientConfiguration(config);
        logConfig.debug("MID Client configuration successfully validated.");
        selectedProtocolHandler = selectProtocolHandler(config);
        logConfig.debug("MID Client selected the following protocol implementation: {}", selectedProtocolHandler.getImplementedComProtocol());
        selectedProtocolHandler.initialize(config);
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public SignatureResponse requestSyncSignature(SignatureRequest request) {
        dataNotNull(request, "The given signature request is NULL");
        request.validateYourself();
        return selectedProtocolHandler.requestSyncSignature(request);
    }

    @Override
    public SignatureResponse requestAsyncSignature(SignatureRequest request) {
        dataNotNull(request, "The given signature request is NULL");
        request.validateYourself();
        return selectedProtocolHandler.requestAsyncSignature(request);
    }

    @Override
    public SignatureResponse pollForSignatureStatus(SignatureTracking signatureTracking) {
        dataNotNull(signatureTracking, "The given signature tracking object is NULL");
        signatureTracking.validateYourself();
        return selectedProtocolHandler.pollForSignatureStatus(signatureTracking);
    }

    @Override
    public ReceiptResponse requestSyncReceipt(SignatureTracking signatureTracking, ReceiptRequest request) {
        dataNotNull(request, "The given receipt request object is NULL");
        signatureTracking.validateYourself();
        request.validateYourself();
        return selectedProtocolHandler.requestSyncReceipt(signatureTracking, request);
    }

    @Override
    public ProfileResponse requestProfile(ProfileRequest request) {
        dataNotNull(request, "The given profile request object is NULL");
        request.validateYourself();
        return selectedProtocolHandler.requestProfile(request);
    }

    @Override
    public void close() {
        if (selectedProtocolHandler != null) {
            try {
                selectedProtocolHandler.close();
            } catch (Exception e) {
                logClient.debug("ComProtocolHandler failed to close: {}: {}, cause: {}: {}",
                                e.getClass().getSimpleName(),
                                e.getLocalizedMessage(),
                                e.getCause().getClass(),
                                e.getCause().getLocalizedMessage());
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------

    private List<ComProtocolHandler> loadComProtocolHandlers() {
        ServiceLoader<ComProtocolHandler> loader = ServiceLoader.load(ComProtocolHandler.class);
        List<ComProtocolHandler> resultList = new ArrayList<>();
        loader.iterator().forEachRemaining(element -> {
            resultList.add(element);
            logClient.debug("Found MID client protocol implementation for: {}", element.getImplementedComProtocol());
        });
        if (resultList.size() == 0) {
            logClient.warn("Found 0 client protocol implementations for MID Client. "
                           + "This is most likely an error. "
                           + "Please check your library dependencies, and make sure at least one of "
                           + "mobileid-client-rest and mobileid-client-soap is in the classpath.");
        }
        return resultList;
    }

    private void validateClientConfiguration(ClientConfiguration config) throws ConfigurationException {
        configNotNull(config, "The configuration object cannot be NULL");
        config.validateYourself();
        int handlersFoundForChosenProtocol = 0;
        for (ComProtocolHandler handler : comProtocolHandlers) {
            if (handler.getImplementedComProtocol() == config.getProtocol()) {
                handlersFoundForChosenProtocol++;
            }
        }
        configTrue(handlersFoundForChosenProtocol > 0,
                   "No HTTP protocol implementation found for chosen protocol: " +
                   config.getProtocol() +
                   ". Are you missing a library dependency to one of mobileid-client-rest or mobileid-client-soap?");
        configTrue(handlersFoundForChosenProtocol < 2,
                   "More than one HTTP protocol implementation found for chosen protocol: " +
                   config.getProtocol() +
                   ". Please check your library dependencies.");
    }

    private ComProtocolHandler selectProtocolHandler(ClientConfiguration config) {
        for (ComProtocolHandler handler : comProtocolHandlers) {
            if (handler.getImplementedComProtocol() == config.getProtocol()) {
                return handler;
            }
        }
        throw new ConfigurationException("The selected communication protocol: " +
                                         config.getProtocol() +
                                         " is not supported by any existing implementation." +
                                         " Are you missing a library dependency?");
    }

}
