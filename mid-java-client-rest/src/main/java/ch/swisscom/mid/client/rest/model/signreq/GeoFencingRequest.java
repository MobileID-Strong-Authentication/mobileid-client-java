package ch.swisscom.mid.client.rest.model.signreq;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
        "countrywhitelist",
        "countryblacklist",
        "mindeviceconfidence",
        "minlocationconfidence",
        "maxtimestampminutes",
        "maxaccuracymeters"
})
@Data
@ToString(includeFieldNames = true)
@Builder
public class GeoFencingRequest {
    @JsonProperty("countrywhitelist")
    private List<String> countryWhiteList;
    @JsonProperty("countryblacklist")
    private List<String> countryBlackList;
    @JsonProperty("mindeviceconfidence")
    private String minDeviceConfidence;
    @JsonProperty("minlocationconfidence")
    private String minLocationConfidence;
    @JsonProperty("maxtimestampminutes")
    private String maxTimestampMinutes;
    @JsonProperty("maxaccuracymeters")
    private String maxAccuracyMeters;
}
