
package ch.swisscom.mid.client.rest.model.fault;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Fault"
})
public class MSSFault {

    @JsonProperty("Fault")
    private Fault fault;

    @JsonProperty("Fault")
    public Fault getFault() {
        return fault;
    }

    @JsonProperty("Fault")
    public void setFault(Fault fault) {
        this.fault = fault;
    }

    public MSSFault withFault(Fault fault) {
        this.fault = fault;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MSSFault.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("fault");
        sb.append('=');
        sb.append(((this.fault == null)?"<null>":this.fault));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
