package ch.swisscom.mid.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TXNApprovalReqType {

    @JsonProperty("type")
    private String type;

    @JsonProperty("dtbd")
    private List<Map<String, String>> dtbd = new ArrayList<>();

    public TXNApprovalReqType() {
    }

    public TXNApprovalReqType(String type, List<Map<String, String>> dtbd) {
        this.type = type;
        this.dtbd = dtbd;
    }

    @Override
    public String toString() {
        return "TXNApprovalReqType{" +
                "type='" + type + '\'' +
                ", dtbd=" + dtbd +
                '}';
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDtbd(List<Map<String, String>> dtbd) {
        this.dtbd = dtbd;
    }

    public String getType() {
        return type;
    }

    public List<Map<String, String>> getDtbd() {
        return dtbd;
    }
}
