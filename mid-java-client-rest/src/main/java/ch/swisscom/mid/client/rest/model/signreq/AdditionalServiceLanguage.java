
package ch.swisscom.mid.client.rest.model.signreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "Description",
    "UserLang"
})
public class AdditionalServiceLanguage extends AdditionalService {
    @JsonProperty("UserLang")
    private UserLang userLang;

    @JsonProperty("UserLang")
    public UserLang getUserLang() {
        return userLang;
    }

    @JsonProperty("UserLang")
    public void setUserLang(UserLang userLang) {
        this.userLang = userLang;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(AdditionalServiceLanguage.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("description");
        sb.append('=');
        sb.append(((this.getDescription() == null)?"<null>":this.getDescription()));
        sb.append(',');
        sb.append("userLang");
        sb.append('=');
        sb.append(((this.userLang == null)?"<null>":this.userLang));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
