
package ch.swisscom.mid.client.rest.model.receiptreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "StatusCode",
    "StatusDetail"
})
public class Status {

    @JsonProperty("StatusCode")
    private StatusCode statusCode;
    @JsonProperty("StatusDetail")
    private StatusDetail statusDetail;

    @JsonProperty("StatusCode")
    public StatusCode getStatusCode() {
        return statusCode;
    }

    @JsonProperty("StatusCode")
    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public Status withStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    @JsonProperty("StatusDetail")
    public StatusDetail getStatusDetail() {
        return statusDetail;
    }

    @JsonProperty("StatusDetail")
    public void setStatusDetail(StatusDetail statusDetail) {
        this.statusDetail = statusDetail;
    }

    public Status withStatusDetail(StatusDetail statusDetail) {
        this.statusDetail = statusDetail;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Status.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("statusCode");
        sb.append('=');
        sb.append(((this.statusCode == null)?"<null>":this.statusCode));
        sb.append(',');
        sb.append("statusDetail");
        sb.append('=');
        sb.append(((this.statusDetail == null)?"<null>":this.statusDetail));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
