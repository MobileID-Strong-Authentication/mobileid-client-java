
package ch.swisscom.mid.client.rest.model.fault;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "SubCode",
    "Value",
    "ValueNs"
})
public class Code {

    @JsonProperty("SubCode")
    private SubCode subCode;
    @JsonProperty("Value")
    private String value;
    @JsonProperty("ValueNs")
    private String valueNs;

    @JsonProperty("SubCode")
    public SubCode getSubCode() {
        return subCode;
    }

    @JsonProperty("SubCode")
    public void setSubCode(SubCode subCode) {
        this.subCode = subCode;
    }

    public Code withSubCode(SubCode subCode) {
        this.subCode = subCode;
        return this;
    }

    @JsonProperty("Value")
    public String getValue() {
        return value;
    }

    @JsonProperty("Value")
    public void setValue(String value) {
        this.value = value;
    }

    public Code withValue(String value) {
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

    public Code withValueNs(String valueNs) {
        this.valueNs = valueNs;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Code.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("subCode");
        sb.append('=');
        sb.append(((this.subCode == null)?"<null>":this.subCode));
        sb.append(',');
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
