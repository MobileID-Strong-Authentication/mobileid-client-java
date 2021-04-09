/*
 * Copyright 2021 Swisscom (Schweiz) AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.swisscom.mid.client.soap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import ch.swisscom.mid.client.config.ComProtocol;
import ch.swisscom.mid.client.config.RequestTrace;
import ch.swisscom.mid.client.config.ResponseTrace;
import ch.swisscom.mid.client.config.TrafficObserver;
import ch.swisscom.mid.client.impl.Loggers;
import ch.swisscom.mid.client.utils.Utils;

/**
 * SOAPHandler used to log the contents of incoming and outgoing messages.
 */
public class SoapTrafficHandler implements SOAPHandler<SOAPMessageContext> {

    private static final Logger logClient = LoggerFactory.getLogger(Loggers.LOGGER_CLIENT);
    private static final Logger logRequestResponse = LoggerFactory.getLogger(Loggers.LOGGER_REQUEST_RESPONSE);
    private static final Logger logFullRequestResponse = LoggerFactory.getLogger(Loggers.LOGGER_FULL_REQUEST_RESPONSE);

    private TrafficObserver trafficObserver;

    // ----------------------------------------------------------------------------------------------------

    public void setTrafficObserver(TrafficObserver trafficObserver) {
        this.trafficObserver = trafficObserver;
    }

    // ----------------------------------------------------------------------------------------------------

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        boolean isRequestMessage = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        String soapMessageString = serializeSoapMessageToString(smc);
        if (isRequestMessage) {
            if (logRequestResponse.isInfoEnabled()) {
                logRequestResponse.info("Sending SOAP request: [{}]", soapMessageString);
            }
            if (logFullRequestResponse.isInfoEnabled()) {
                logFullRequestResponse.info("Sending SOAP request: [{}]", soapMessageString);
            }
        } else {
            if (logRequestResponse.isInfoEnabled()) {
                String strippedResponse = Utils.stripInnerLargeBase64Content(soapMessageString, '>', '<');
                logRequestResponse.info("Received SOAP response: [{}]", strippedResponse);
            }
            if (logFullRequestResponse.isInfoEnabled()) {
                logFullRequestResponse.info("Received SOAP response: [{}]", soapMessageString);
            }
        }

        if (trafficObserver != null) {
            if (isRequestMessage) {
                trafficObserver.notifyOfOutgoingRequest(new RequestTrace(soapMessageString), ComProtocol.SOAP);
            } else {
                int httpCode = (Integer) smc.get(MessageContext.HTTP_RESPONSE_CODE);
                trafficObserver.notifyOfIncomingResponse(new ResponseTrace(httpCode, "-", soapMessageString), ComProtocol.SOAP);
            }
        }
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        String soapMessageString = serializeSoapMessageToString(smc);
        logRequestResponse.info("Received SOAP fault:\n{}", soapMessageString);

        if (trafficObserver != null) {
            int httpCode = (Integer) smc.get(MessageContext.HTTP_RESPONSE_CODE);
            trafficObserver.notifyOfIncomingResponse(new ResponseTrace(httpCode, "-", soapMessageString), ComProtocol.SOAP);
        }
        return true;
    }

    public void close(MessageContext messageContext) {
        // no code here, nothing to cleanup
    }

    // ----------------------------------------------------------------------------------------------------

    private String serializeSoapMessageToString(SOAPMessageContext smc) {
        String result = convertToPrettyPrintedMessage(smc.getMessage());
        if (result == null) {
            result = convertToUnformattedString(smc.getMessage());
        }
        if (result == null) {
            result = "Content not available";
        }
        return result;
    }

    private String convertToUnformattedString(SOAPMessage soapMessage) {
        try (ByteArrayOutputStream streamOut = new ByteArrayOutputStream()) {
            soapMessage.writeTo(streamOut);
            return streamOut.toString();
        } catch (Exception e) {
            logClient.error("Failed to log unformatted SOAP message.", e);
            return null;
        }
    }

    private String convertToPrettyPrintedMessage(SOAPMessage soapMessage) {
        try {
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            // Set formatting
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            Source sc = soapMessage.getSOAPPart().getContent();
            ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(streamOut);
            tf.transform(sc, result);
            return streamOut.toString();
        } catch (Exception e) {
            logClient.error("Failed to pretty print SOAP message for logging purpose", e);
            return null;
        }
    }

}