package ch.swisscom.mid.client.cli;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.swisscom.mid.client.config.ComProtocol;
import ch.swisscom.mid.client.config.RequestTrace;
import ch.swisscom.mid.client.config.ResponseTrace;
import ch.swisscom.mid.client.config.TrafficObserver;
import ch.swisscom.mid.client.impl.Loggers;

public class PrettyPrintingTrafficObserver implements TrafficObserver {

    private static final Logger logClient = LoggerFactory.getLogger(Loggers.LOGGER_CLIENT);
    private final ObjectMapper jacksonMapper = new ObjectMapper();

    @Override
    public void notifyOfGeneratedApTransId(String apTransId, ComProtocol protocol) {
        // do nothing
    }

    @Override
    public void notifyOfOutgoingRequest(RequestTrace trace, ComProtocol protocol) {
        String requestMessage;
        if (protocol == ComProtocol.REST) {
            requestMessage = toPrettyJson(trace.getBody());
        } else {
            requestMessage = trace.getBody();
        }
        logClient.info("Outgoing request: [{}]", requestMessage);
    }

    @Override
    public void notifyOfIncomingResponse(ResponseTrace trace, ComProtocol protocol) {
        String responseMessage;
        if (protocol == ComProtocol.REST) {
            responseMessage = toPrettyJson(trace.getBody());
        } else {
            responseMessage = trace.getBody();
        }
        logClient.info("Incoming response: [{}]", responseMessage);
    }

    // ----------------------------------------------------------------------------------------------------

    private String toPrettyJson(String json) {
        try {
            Object jsonObject = jacksonMapper.readValue(json, Object.class);
            return jacksonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception exception) {
            logClient.warn("Failed to convert non-pretty JSON to pretty JSON", exception);
            return json;
        }
    }
}
