package ch.swisscom.mid.client.model;

public class CertificateData {

    private String subjectName;

    private String certificateAsBase64;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCertificateAsBase64() {
        return certificateAsBase64;
    }

    public void setCertificateAsBase64(String certificateAsBase64) {
        this.certificateAsBase64 = certificateAsBase64;
    }

    @Override
    public String toString() {
        return "CertificateData{" +
               "subjectName='" + subjectName + '\'' +
               ", certificateAsBase64='" + (certificateAsBase64 == null ? "null" : "(not-null)") + '\'' +
               '}';
    }

}
