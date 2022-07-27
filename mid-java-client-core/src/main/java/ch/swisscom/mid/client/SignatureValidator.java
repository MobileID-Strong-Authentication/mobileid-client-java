package ch.swisscom.mid.client;

import ch.swisscom.mid.client.model.SignatureValidationResult;
import ch.swisscom.mid.client.model.Traceable;

/**
 * Interface for the components that can validate the CMS content of a digital signature that is received
 * after a successful signature transaction.
 *
 * @since v1.5.0
 */
public interface SignatureValidator {

    /**
     * Validates a digital signature content given as a Base64-encoded CMS data. The validation involves:
     * <ol>
     *     <li>Validates if the received certificate (with the public key) inside the signature response data is trustworthy, which means
     *         the certificate chain must have an anchor certificate that matches one of the root certificates in the configured truststore file.
     *         This step ensures that the public key was not replaced by a MITM-attack.</li>
     *     <li>Verifies the digital signature (which requires the public key of the end user certificate). This step ensures that the signature
     *         was done by the correct user/private-key and not by anyone else.</li>
     *     <li>Verifies if the signed data (which is the DTBD/DTBS message) inside the digital signature actually matches the DTBD message that
     *         was sent in the request.</li>
     * </ol>
     *
     * @param base64SignatureContent the Base64-encoded CMS signature content received in the signature response
     * @param requestedDtbs          the requested DTBD/DTBS from the signature request
     * @param trace                  an optional trace object to print details about the current flow
     * @return a validation result containing boolean flags for the types of validations that are performed (and whether the validations passed
     *     or not) and various information bits extracted from the signature content
     */
    SignatureValidationResult validateSignature(String base64SignatureContent, String requestedDtbs, Traceable trace);

}
