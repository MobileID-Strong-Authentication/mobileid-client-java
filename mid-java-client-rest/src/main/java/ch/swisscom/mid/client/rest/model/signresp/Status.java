
package ch.swisscom.mid.client.rest.model.signresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "StatusCode",
    "StatusMessage"
})
public class Status {

    @JsonProperty("StatusCode")
    private StatusCode statusCode;
    @JsonProperty("StatusMessage")
    private String statusMessage;

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

    @JsonProperty("StatusMessage")
    public String getStatusMessage() {
        return statusMessage;
    }

    @JsonProperty("StatusMessage")
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Status withStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
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
        sb.append("statusMessage");
        sb.append('=');
        sb.append(((this.statusMessage == null)?"<null>":this.statusMessage));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
