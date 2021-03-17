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
        System.out.println(">>> " + Cli.SEPARATOR.substring(4));
        if (protocol == ComProtocol.REST) {
            System.out.println(toPrettyJson(trace.getBody()));
        } else {
            System.out.println(trace.getBody());
        }
        System.out.println(Cli.SEPARATOR);
    }

    @Override
    public void notifyOfIncomingResponse(ResponseTrace trace, ComProtocol protocol) {
        System.out.println("<<< " + Cli.SEPARATOR.substring(4));
        if (protocol == ComProtocol.REST) {
            System.out.println(toPrettyJson(trace.getBody()));
        } else {
            System.out.println(trace.getBody());
        }
        System.out.println(Cli.SEPARATOR);
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
