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
package ch.swisscom.mid.client.model;

public class ReceiptResponse {

    private Status status;

    private ReceiptResponseExtension responseExtension;

    // ----------------------------------------------------------------------------------------------------

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ReceiptResponseExtension getResponseExtension() {
        return responseExtension;
    }

    public void setResponseExtension(ReceiptResponseExtension responseExtension) {
        this.responseExtension = responseExtension;
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ReceiptResponse{" +
               "status=" + status +
               ", responseExtension=" + responseExtension +
               '}';
    }

}
