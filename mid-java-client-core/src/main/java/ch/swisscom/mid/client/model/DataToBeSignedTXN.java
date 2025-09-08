package ch.swisscom.mid.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DataToBeSignedTXN {
    @JsonProperty("format_version")
    private String formatVersion;

    @JsonProperty("content_string")
    private List<Map<String, String>> dtbd = new ArrayList<>();

    public DataToBeSignedTXN() {
    }

    public DataToBeSignedTXN(String formatVersion, List<Map<String, String>> dtbd) {
        this.formatVersion = formatVersion;
        this.dtbd = dtbd;
    }

    public String getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public List<Map<String, String>> getDtbd() {
        return dtbd;
    }

    public void setDtbd(List<Map<String, String>> dtbd) {
        this.dtbd = dtbd;
    }

    @Override
    public String toString() {
        return "DataToBeSignedTXN{" +
                "formatVersion='" + formatVersion + '\'' +
                ", content_string=" + dtbd +
                '}';
    }
}
