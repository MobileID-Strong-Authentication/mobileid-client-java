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
package ch.swisscom.mid.client.samples;

import ch.swisscom.mid.client.MIDClient;
import ch.swisscom.mid.client.config.ClientConfiguration;
import ch.swisscom.mid.client.impl.MIDClientImpl;
import ch.swisscom.mid.client.model.*;

import static ch.swisscom.mid.client.samples.Utils.prettyPrintTheException;

public class SyncSignatureWithReceipt {

    public static void main(String[] args) {
        // edit the config here with your data
        ClientConfiguration clientConfig = Utils.buildClientConfig();
        MIDClient client = new MIDClientImpl(clientConfig);

        SignatureRequest request = new SignatureRequest();
        request.setUserLanguage(UserLanguage.ENGLISH);
        request.getDataToBeSigned().setData("ApplicationProvider.com: Please sign this document");
        // edit this value accordingly
        request.getMobileUser().setMsisdn("41790000000");
        request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
        request.addAdditionalService(new SubscriberInfoAdditionalService());

        try {
            SignatureResponse signatureResponse = client.requestSyncSignature(request);
            System.out.println(signatureResponse.toString());

            ReceiptRequest receiptRequest = new ReceiptRequest();
            receiptRequest.getMessageToBeDisplayed().setData("Test receipt message");
            receiptRequest.setStatusCode(StatusCode.REQUEST_OK);
            receiptRequest.addReceiptRequestExtension();

            ReceiptResponse receiptResponse = client.requestSyncReceipt(signatureResponse.getTracking(), receiptRequest);
            System.out.println(receiptResponse.toString());

        } catch (Exception e) {
            System.out.println(prettyPrintTheException(e));
        } finally {
            client.close();
        }
    }

}
