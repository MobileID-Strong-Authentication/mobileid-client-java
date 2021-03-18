
package ch.swisscom.mid.client.rest.model.profqresp;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Algorithm",
    "State",
    "X509Certificate",
    "X509SubjectName"
})
public class MobileUserCertificate {

    @JsonProperty("Algorithm")
    private String algorithm;
    @JsonProperty("State")
    private String state;
    @JsonProperty("X509Certificate")
    private List<String> x509Certificate = new ArrayList<String>();
    @JsonProperty("X509SubjectName")
    private List<String> x509SubjectName = new ArrayList<String>();

    @JsonProperty("Algorithm")
    public String getAlgorithm() {
        return algorithm;
    }

    @JsonProperty("Algorithm")
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public MobileUserCertificate withAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    @JsonProperty("State")
    public String getState() {
        return state;
    }

    @JsonProperty("State")
    public void setState(String state) {
        this.state = state;
    }

    public MobileUserCertificate withState(String state) {
        this.state = state;
        return this;
    }

    @JsonProperty("X509Certificate")
    public List<String> getX509Certificate() {
        return x509Certificate;
    }

    @JsonProperty("X509Certificate")
    public void setX509Certificate(List<String> x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    public MobileUserCertificate withX509Certificate(List<String> x509Certificate) {
        this.x509Certificate = x509Certificate;
        return this;
    }

    @JsonProperty("X509SubjectName")
    public List<String> getX509SubjectName() {
        return x509SubjectName;
    }

    @JsonProperty("X509SubjectName")
    public void setX509SubjectName(List<String> x509SubjectName) {
        this.x509SubjectName = x509SubjectName;
    }

    public MobileUserCertificate withX509SubjectName(List<String> x509SubjectName) {
        this.x509SubjectName = x509SubjectName;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MobileUserCertificate.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("algorithm");
        sb.append('=');
        sb.append(((this.algorithm == null)?"<null>":this.algorithm));
        sb.append(',');
        sb.append("state");
        sb.append('=');
        sb.append(((this.state == null)?"<null>":this.state));
        sb.append(',');
        sb.append("x509Certificate");
        sb.append('=');
        sb.append(((this.x509Certificate == null)?"<null>":this.x509Certificate));
        sb.append(',');
        sb.append("x509SubjectName");
        sb.append('=');
        sb.append(((this.x509SubjectName == null)?"<null>":this.x509SubjectName));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
