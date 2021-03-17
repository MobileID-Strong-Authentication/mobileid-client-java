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

import java.io.Closeable;

import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.config.ComProtocol;
import ch.swisscom.mid.client.model.*;

/**
 * Abstraction for the actual implementation of the Mobile ID protocol. Since Mobile ID can be
 * accessed via either SOAP or REST (as of Nov/28/2020), then each such variant must implement
 * this interface.
 */
public interface ComProtocolHandler extends Closeable {

    void initialize(ClientConfiguration config);

    ComProtocol getImplementedComProtocol();

    SignatureResponse requestSyncSignature(SignatureRequest request);

    SignatureResponse requestAsyncSignature(SignatureRequest request);

    SignatureResponse pollForSignatureStatus(SignatureTracking signatureTracking);

    ReceiptResponse requestSyncReceipt(SignatureTracking signatureTracking, ReceiptRequest request);

    ProfileResponse requestProfile(ProfileRequest request);

}
