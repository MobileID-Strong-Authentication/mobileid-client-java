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

import static ch.swisscom.mid.client.utils.Utils.dataNotEmpty;

public class MessageToBeDisplayed {

    private String data;

    private String encoding = "UTF-8";

    private String mimeType = "text/plain";

    // ----------------------------------------------------------------------------------------------------

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setEncodingToUtf8() {
        this.encoding = "UTF-8";
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setMimeTypeToTextPlain() {
        this.mimeType = "text/plain";
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        dataNotEmpty(data, "The data in the MessageToBeDisplayed cannot be null or empty");
        dataNotEmpty(encoding, "The encoding in the MessageToBeDisplayed cannot be null or empty (set it to \"UTF-8\", for example)");
        dataNotEmpty(mimeType, "The mime type in the MessageToBeDisplayed cannot be null or empty (set it to \"text/plain\", for example)");
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "MessageToBeDisplayed{" +
               "data='" + data + '\'' +
               ", encoding='" + encoding + '\'' +
               ", mimeType='" + mimeType + '\'' +
               '}';
    }
}
