package ch.swisscom.mid.client.model;

/**
 * Utility interface for passing trace objects to components of the Mobile ID client. Such traces are objects that can provide contextual information
 * related the currently executed flow. This helps users of the Mobile ID client to have all the log messages that the Mobile ID client prints
 * containing the trace data printed inside each message.
 */
public interface Traceable {

    String getTrace();

}
