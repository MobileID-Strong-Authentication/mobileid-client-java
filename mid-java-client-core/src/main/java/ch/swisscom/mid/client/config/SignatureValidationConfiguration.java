package ch.swisscom.mid.client.config;

import static ch.swisscom.mid.client.utils.Utils.configNotNull;
import static ch.swisscom.mid.client.utils.Utils.configTrue;

/**
 * The configuration for the {@link ch.swisscom.mid.client.impl.SignatureValidatorImpl} component.
 *
 * @since v1.5.0
 */
public class SignatureValidationConfiguration {

    /**
     * The file in the local file system from where the trust store content should be loaded. Set either this field or
     * {@link #trustStoreClasspathFile} or {@link #trustStoreBytes}.
     */
    private String trustStoreFile;

    /**
     * The file in the current process classpath from where the trust store content should be loaded. Set either this field or
     * {@link #trustStoreFile} or {@link #trustStoreBytes}.
     */
    private String trustStoreClasspathFile;

    /**
     * The bytes of the trust store content. Set either this field or {@link #trustStoreFile} or {@link #trustStoreClasspathFile}.
     */
    private byte[] trustStoreBytes;

    /**
     * The binary format of the trust store. Defaults to "JKS".
     */
    private String trustStoreType = "JKS";

    /**
     * The optional password that protects the trust store content.
     */
    private String trustStorePassword;

    // ----------------------------------------------------------------------------------------------------

    public String getTrustStoreFile() {
        return trustStoreFile;
    }

    public void setTrustStoreFile(String trustStoreFile) {
        this.trustStoreFile = trustStoreFile;
    }

    public String getTrustStoreClasspathFile() {
        return trustStoreClasspathFile;
    }

    public void setTrustStoreClasspathFile(String trustStoreClasspathFile) {
        this.trustStoreClasspathFile = trustStoreClasspathFile;
    }

    public byte[] getTrustStoreBytes() {
        return trustStoreBytes;
    }

    public void setTrustStoreBytes(byte[] trustStoreBytes) {
        this.trustStoreBytes = trustStoreBytes;
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

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        if (trustStoreFile == null && trustStoreClasspathFile == null && trustStoreBytes == null) {
            //noinspection ConstantConditions
            configTrue(false, "At least a trust store material source (one of trustStoreFile, "
                              + "trustStoreClasspathFile or trustStoreBytes) must be provided");
        }
        configNotNull(trustStoreType, "The TLS trust store type cannot be NULL (by default this is JKS)");
    }

}
