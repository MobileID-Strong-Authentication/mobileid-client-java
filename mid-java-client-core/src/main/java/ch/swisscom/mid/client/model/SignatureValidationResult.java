package ch.swisscom.mid.client.model;

public class SignatureValidationResult {

    private boolean signatureValid;
    private boolean signerCertificateValid;
    private boolean signerCertificatePathValid;
    private boolean dtbsMatching;

    private String mobileIdSerialNumber;
    private String signedDtbs;

    private SignatureValidationFailureReason validationFailureReason;
    private Exception validationException;

    public String getMobileIdSerialNumber() {
        return mobileIdSerialNumber;
    }

    public void setMobileIdSerialNumber(String mobileIdSerialNumber) {
        this.mobileIdSerialNumber = mobileIdSerialNumber;
    }

    public String getSignedDtbs() {
        return signedDtbs;
    }

    public void setSignedDtbs(String signedDtbs) {
        this.signedDtbs = signedDtbs;
    }

    public boolean isSignatureValid() {
        return signatureValid;
    }

    public void setSignatureValid(boolean signatureValid) {
        this.signatureValid = signatureValid;
    }

    public boolean isSignerCertificateValid() {
        return signerCertificateValid;
    }

    public void setSignerCertificateValid(boolean signerCertificateValid) {
        this.signerCertificateValid = signerCertificateValid;
    }

    public boolean isSignerCertificatePathValid() {
        return signerCertificatePathValid;
    }

    public void setSignerCertificatePathValid(boolean signerCertificatePathValid) {
        this.signerCertificatePathValid = signerCertificatePathValid;
    }

    public boolean isDtbsMatching() {
        return dtbsMatching;
    }

    public void setDtbsMatching(boolean dtbsMatching) {
        this.dtbsMatching = dtbsMatching;
    }

    public SignatureValidationFailureReason getValidationFailureReason() {
        return validationFailureReason;
    }

    public void setValidationFailureReason(SignatureValidationFailureReason validationFailureReason) {
        this.validationFailureReason = validationFailureReason;
    }

    public Exception getValidationException() {
        return validationException;
    }

    public void setValidationException(Exception validationException) {
        this.validationException = validationException;
    }

    public boolean isValidationSuccessful() {
        return signerCertificateValid &&
               signerCertificatePathValid &&
               signatureValid &&
               dtbsMatching;
    }
}
