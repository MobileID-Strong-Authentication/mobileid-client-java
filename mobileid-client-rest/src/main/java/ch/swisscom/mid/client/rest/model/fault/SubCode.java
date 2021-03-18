
package ch.swisscom.mid.client.rest.model.fault;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Value",
    "ValueNs"
})
public class SubCode {

    @JsonProperty("Value")
    private String value;
    @JsonProperty("ValueNs")
    private String valueNs;

    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    @JsonProperty("Value")
    public void setValue(String value) {
        this.value = value;
    }

    public SubCode withValue(String value) {
        this.value = value;
        return this;
    }

    @JsonProperty("ValueNs")
    public String getValueNs() {
        return valueNs;
    }

    @JsonProperty("ValueNs")
    public void setValueNs(String valueNs) {
        this.valueNs = valueNs;
    }

    public SubCode withValueNs(String valueNs) {
        this.valueNs = valueNs;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SubCode.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("value");
        sb.append('=');
        sb.append(((this.value == null)?"<null>":this.value));
        sb.append(',');
        sb.append("valueNs");
        sb.append('=');
        sb.append(((this.valueNs == null)?"<null>":this.valueNs));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
