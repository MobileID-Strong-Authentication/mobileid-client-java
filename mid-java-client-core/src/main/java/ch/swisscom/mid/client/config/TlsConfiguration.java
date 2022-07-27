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
package ch.swisscom.mid.client.config;

import static ch.swisscom.mid.client.utils.Utils.configNotNull;
import static ch.swisscom.mid.client.utils.Utils.configTrue;

public class TlsConfiguration {

    private String keyStoreFile;
    private String keyStoreClasspathFile;
    private byte[] keyStoreBytes;

    private String keyStoreType = "JKS";

    private String keyStorePassword;

    private String keyStoreCertificateAlias;

    private String keyStoreKeyPassword;

    private String trustStoreFile;
    private String trustStoreClasspathFile;
    private byte[] trustStoreBytes;

    private String trustStoreType = "JKS";

    private String trustStorePassword;

    private boolean hostnameVerification = true;

    // ----------------------------------------------------------------------------------------------------

    public String getKeyStoreFile() {
        return keyStoreFile;
    }

    public void setKeyStoreFile(String keyStoreFile) {
        this.keyStoreFile = keyStoreFile;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getKeyStoreCertificateAlias() {
        return keyStoreCertificateAlias;
    }

    public void setKeyStoreCertificateAlias(String keyStoreCertificateAlias) {
        this.keyStoreCertificateAlias = keyStoreCertificateAlias;
    }

    public String getKeyStoreKeyPassword() {
        return keyStoreKeyPassword;
    }

    public void setKeyStoreKeyPassword(String keyStoreKeyPassword) {
        this.keyStoreKeyPassword = keyStoreKeyPassword;
    }

    public String getTrustStoreFile() {
        return trustStoreFile;
    }

    public void setTrustStoreFile(String trustStoreFile) {
        this.trustStoreFile = trustStoreFile;
    }

    public String getTrustStoreType() {
        return trustStoreType;
    }

    public void setTrustStoreType(String trustStoreType) {
        this.trustStoreType = trustStoreType;
    }

    public String getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(String trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public String getKeyStoreClasspathFile() {
        return keyStoreClasspathFile;
    }

    public void setKeyStoreClasspathFile(String keyStoreClasspathFile) {
        this.keyStoreClasspathFile = keyStoreClasspathFile;
    }

    public byte[] getKeyStoreBytes() {
        return keyStoreBytes;
    }

    public void setKeyStoreBytes(byte[] keyStoreBytes) {
        this.keyStoreBytes = keyStoreBytes;
    }

    public byte[] getTrustStoreBytes() {
        return trustStoreBytes;
    }

    public void setTrustStoreBytes(byte[] trustStoreBytes) {
        this.trustStoreBytes = trustStoreBytes;
    }

    public String getTrustStoreClasspathFile() {
        return trustStoreClasspathFile;
    }

    public void setTrustStoreClasspathFile(String trustStoreClasspathFile) {
        this.trustStoreClasspathFile = trustStoreClasspathFile;
    }

    public boolean isHostnameVerification() {
        return hostnameVerification;
    }

    public void setHostnameVerification(boolean hostnameVerification) {
        this.hostnameVerification = hostnameVerification;
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        if (keyStoreFile == null && keyStoreClasspathFile == null && keyStoreBytes == null) {
            //noinspection ConstantConditions
            configTrue(false, "At least a key store material source (one of keyStoreFile, "
                              + "keyStoreClasspathFile or keyStoreBytes) must be provided");
        }
        configNotNull(keyStoreType, "The TLS key store type cannot be NULL (by default this is JKS)");
        configNotNull(keyStoreCertificateAlias, "The TLS key store certificate alias cannot be NULL");
    }
}
