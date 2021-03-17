# The architecture of the Mobile ID client

This page provides an overview on the architecture of the Mobile ID client library. If you want to check the code and understand it faster, this
page is for you.

## Modules
The following modules are built in:

* _mobileid-client-core_: the main classes that the user works with. This module provides a simplified model to work with and provides the base 
  on which the REST and SOAP implementations are built
* _mobileid-client-rest_: the REST implementation of the client. Uses the REST API of Mobile ID
* _mobileid-client-soap_: the SOAP implementation of the client. Uses the SOAP API of Mobile ID
* _mobileid-client-usage_: samples of configuring and using the client library. Provides the CLI classes for calling the
  Mobile ID client from the command line

If you want to discover the code, start in _mobileid-client-core_. The main interface is _MIDClient_ that defines the contract that the final clients
are using. The implementations is, not surprinsingly, _MIDClientImpl_. This one is basically a delegate implementation, since it cannot do too much
without knowing that _actual_ implementation will the client choose (REST or SOAP or anything else). Therefore, it uses the concept of 
_ComProtocolHandler_. This second interface defines the methods that any implementation of a communication protocol (hence Com Protocol) with the 
Mobile ID service needs to have. 

There are two implementations of the _ComProtocolHandler_: a REST one (see _ComProtocolHandlerRestImpl_) and a SOAP one (see _ComProtocolHandlerSoapImpl_).
The discovery of available implementations is done via Java Service Provider Interface (SPI) standard protocol. There is a META-INF directory in each
REST and SOAP module that contains a file with the fully qualified name of the _ComProtocolHandler_ interface. Inside that file, on each module, there
is the fully qualified name of the _ComProtocolHandler_ implementation. At runtime, the _MIDClientImpl_ class uses SPI API to discover the available
implementations and instantiate the one that the final client has chosen.

So, if the final client does this:
```java
ClientConfiguration config = new ClientConfiguration();
config.setProtocolToRest();
```
then the REST implementation is chosen (which is also the default). If the final client does this:
```java
ClientConfiguration config = new ClientConfiguration();
config.setProtocolToSoap();
```
then the SOAP implementation is chosen.

## The REST implementation

The REST implementation is located in the _mobileid-client-rest_ module. It uses a JSON model that is generated from sample files 
(see the _gen-material_ directory from that module). There is a shell script file in that directory (_run_jsonschema2pojo.sh_) that can be used, 
together with the very nice [jsonschema2pojo](https://github.com/joelittlejohn/jsonschema2pojo/wiki/Getting-Started#the-command-line-interface) tool,
to generate JSON model classes that are already enriched with [Jackson](https://github.com/FasterXML/jackson) annotations.

This means that, if some changes are required (like a new field that needs to be added/processed on the request/response messages), then the flow is
more or less the following:
1. change the samples in the _gen-material_ directory
2. run the jsonschema2pojo tool to regenerate the classes
3. copy the classes in the _src/main/java_ directory of the module
4. start using the new version of the classes

The _ComProtocolHandlerRestImpl_ class is the main one in this module. It implements the entire communication protocol with the Mobile ID service, 
using the REST API. It uses an instance of an Apache HTTP Client version 5 to send and receive data. This HTTP client is configured to correctly
send and receive TLS certificates (client and server) and to correctly pool HTTP connections.

There are 4 interesting classes (_ProfileRequestModelUtils_, _ReceiptRequestModelUtils_, _SignatureRequestModelUtils_, _StatusQueryModelUtils_) that
are used for generating the JSON requests for the Mobile ID service and to parse the respective responses.

Finally, the _FaultProcessor_ is used for converting any faulty response into a common fault model to send back to the final client.

## The SOAP implementation

The SOAP implementation is located in the _mobileid-client-soap_ module. It uses the JAX-WS Maven plugin to generate Java classes out of the Mobile ID
service WSDL and XSD files located in the _src/main_resources/wsdl_ directory. The classes are generated in the _target/generated-sources/wsimport_ 
directory at build time.

The _ComProtocolHandlerSoapImpl_ is the main class in this module. It implements the entire communication protocol with the Mobile ID service,
using the SOAP API. It uses the _MssServiceFactory_ for generating SOAP WS port implementations (Java proxies over the SOAP port classes) and it pools
these port proxies using the Apache Commons Pool library. This ensures that multiple concurrent calls to this implementation will correctly be served
by separate port proxies while, at the same time, these proxies are pooled so that the cost of creating them is minimized.

There are two interesting classes (_MssRequestBuilder_ and _MssResponseProcessor_) that are used for generating the requests for the Mobile ID service 
and to parse the respective responses. 

Finally, the _MssFaultProcessor_ is used for converting any faulty response into a common fault model to send back to the final client.

## Loggers

Logging is an important part of any library and the Mobile ID client is no different. The _Loggers_ class defines the loggers that are used by this 
library (for either of the implementations, REST or SOAP). The names are the following:

- ch.swisscom.mid.client - logging related to common parts of the client's functionality
- ch.swisscom.mid.client.config - all activity related to configuring the client
- ch.swisscom.mid.client.protocol - all activity related to talking to the Mobile ID service (except the actual request and response messages; see next)
- ch.swisscom.mid.client.requestResponse - the (REST or SOAP) request and response messages, with any large data (e.g. Base64 signature, Base64 certificate content) stripped out
- ch.swisscom.mid.client.fullRequestResponse - the (REST or SOAP) request and response messages in full content (including any large data)

The following is also worth configuring:

- org.apache.hc - logging related to the Apache HTTP client, used for the REST implementation

There was a conscious decision to name these loggers as above, without actually using the name of the classes that are logging this data, in order 
to decouple the name of the logger from the class name. This ensures that any later refactoring of the classes will not impact the existing logging
configuration of final clients. 