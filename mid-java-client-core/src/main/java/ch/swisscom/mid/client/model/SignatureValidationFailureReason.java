package ch.swisscom.mid.client.model;

public enum SignatureValidationFailureReason implements DocumentedEnum {

    FAILED_TO_EXTRACT_SIGNING_CERTIFICATE("Cannot extract or identify the signing certificate from the signature CMS content"),
    SIGNING_CERTIFICATE_NOT_VALID("The certificate used for digital signing is not valid"),
    SIGNING_CERTIFICATE_PATH_NOT_VALID("The certification path of the signing certificate does not end with a trusted CA certificate"),
    SIGNATURE_VALIDATION_FAILED("Cannot validate the signature from the provided CMS content"),
    DATA_TO_BE_SIGNED_NOT_MATCHING("The data to be signed does not match between the data requested and the data that was actually signed");

    private final String description;

    SignatureValidationFailureReason(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

}
