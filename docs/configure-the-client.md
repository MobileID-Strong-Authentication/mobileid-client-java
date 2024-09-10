# Configure the Mobile ID client

To use the Mobile ID client, you have to [obtain it (or build it)](build-download-or-link.md), then you have to configure it. The way
you configure the client depends on how you plan to use the client and integrate it in your project/setup.

## Properties file for CLI usage

For the CLI usage of the client, you need to create a properties file with a content similar to this one:

```properties
# The MSSP AP ID that identifies your client to the server. Obtain this from Swisscom as part of the authentication details
client.msspApId=<My AP ID>
# The MSSP AP password that authenticates your client to the server. Obtain this from Swisscom as part of the authentication details
client.msspApPassword=<My AP password>

# --
# The Java keystore that contains the client certificate and private key to be used during TLS client authentication. 
# Enroll this certificate to Swisscom during the initial setup phase
client.keyStore.file=keystore.jks
# The Java keystore's password 
client.keyStore.password=secret
# The password of the private key 
client.keyStore.keyPassword=secret
# The name of the certificate alias to use 
client.keyStore.certAlias=my-cert-alias

# --
# The HTTP connection timeout in SECONDS (the maximum time allowed for the HTTP client to wait for the TCP socket connection
# to be established until the request is dropped and the client gives up).
client.http.connectionTimeoutInSeconds=20
# The HTTP response timeout in SECONDS (the maximum time allowed for the HTTP client to wait for the response to be received
# for any one request until the request is dropped and the client gives up).
client.http.responseTimeoutInSeconds=100

# --
# Enable HTTP proxy connection. Set this to true to make the Mobile ID client connect to the Mobile ID backend via an HTTP proxy.
# If this is set to false, the rest of proxy configuration parameter can be left empty. 
client.proxy.enabled=false
# The host of the proxy. 
client.proxy.host=
# The port of the proxy.
client.proxy.port=
# The optional username to use during proxy authentication. Can be left empty in which case the client will not authenticate to the proxy.
client.proxy.username=
# The optional password to use during proxy authentication
client.proxy.password=

# ---
# The Java truststore file with the CA certificates used for validating the signer's certificate path
client.signatureValidation.trustStore.file=signature-validation-truststore.jks
# The type of the truststore
client.signatureValidation.trustStore.type=jks
# The password that protects the truststore (optional)
client.signatureValidation.trustStore.password=secret

# --
# The Java truststore file with the server certificates (e.g. CA certificates) to trust
server.trustStore.file=truststore.jks
# The Java truststore's password
server.trustStore.password=secret
# Whether to perform server hostname validation during TLS handshake or not (set to false for testing purposes, e.g. server certificates
# with a different Subject Alternative Name) 
server.hostnameVerification=true

# --
# The REST URL for the Mobile ID server
server.rest.url=https://mobileid.swisscom.com/rest/service
# The SOAP Signature port URL for the Mobile ID server 
server.soap.signatureUrl=https://mobileid.swisscom.com/soap/services/MSS_SignaturePort
# The SOAP Status Query port URL for the Mobile ID server
server.soap.statusQueryUrl=https://mobileid.swisscom.com/soap/services/MSS_StatusQueryPort
# The SOAP Receipt port URL for the Mobile ID server
server.soap.receiptUrl=https://mobileid.swisscom.com/soap/services/MSS_ReceiptPort
# The SOAP Profile Query port URL for the Mobile ID server
server.soap.profileQueryUrl=https://mobileid.swisscom.com/soap/services/MSS_ProfileQueryPort
```

Once you create this file and configure its properties accordingly, it can either be picked up by the Mobile ID client automatically
when used via the CLI interface (if it is named _config.properties_) or you can specify the name of the file using the _-config_ 
argument.

## Programmatic configuration

The Mobile ID client can easily be configured in code:
```java
// configuration for the Mobile ID client; this is done once per application lifetime
ClientConfiguration config = new ClientConfiguration();
config.setProtocolToRest(); // or SOAP
config.setApId("mid://id-received-from-swisscom");
config.setApPassword("pass-received-from-swisscom");

UrlsConfiguration urls = config.getUrls();
urls.setAllServiceUrlsTo(DefaultConfiguration.DEFAULT_INTERNET_BASE_URL + DefaultConfiguration.REST_ENDPOINT_SUB_URL);

TlsConfiguration tls = config.getTls();
tls.setKeyStoreFile("keystore.jks");
tls.setKeyStorePassword("secret");
tls.setKeyStoreKeyPassword("secret");
tls.setKeyStoreCertificateAlias("mid-cert");
tls.setTrustStoreFile("truststore.jks");
tls.setTrustStorePassword("secret");
tls.setSslContext("SSLv3");

HttpConfiguration http = config.getHttp();
http.setConnectionTimeoutInMs(20 * 1000);
http.setResponseTimeoutInMs(100 * 1000);

# Optional proxy configuration
ProxyConfiguration proxy = config.getProxy();
proxy.setHost("localhost");
proxy.setPort(3128);
proxy.setUsername("myuser");
proxy.setPassword("mypass");

// this client is thread safe and can be reused anywhere in your application
MIDClient client = new MIDClientImpl(config);
```

## Logging configuration

The Mobile ID client uses SLF4j and Logback for logging. It uses the following loggers:

- ch.swisscom.mid.client - logging related to common parts of the client's functionality
- ch.swisscom.mid.client.config - all activity related to configuring the client
- ch.swisscom.mid.client.protocol - all activity related to talking to the Mobile ID service (except the actual request and response messages; see next)
- ch.swisscom.mid.client.requestResponse - the (REST or SOAP) request and response messages, with any large data (e.g. Base64 signature, Base64 certificate content) stripped out
- ch.swisscom.mid.client.fullRequestResponse - the (REST or SOAP) request and response messages in full content (including any large data)

To configure the logging for the library, you can use the following config:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="LOGS" value="./logs"/>

    <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{ISO8601} %highlight(%-5level) [%blue(%t)] %yellow(%C{1.}): %msg%n%throwable</pattern>
        </encoder>
    </appender>

    <!-- LOG everything at INFO level -->
    <root level="info">
        <appender-ref ref="Console"/>
    </root>

    <logger name="org.apache.hc" level="warn"/>
    <logger name="ch.swisscom.mid.client" level="debug"/>
    <logger name="ch.swisscom.mid.client.config" level="debug"/>
    <logger name="ch.swisscom.mid.client.protocol" level="debug"/>
    <logger name="ch.swisscom.mid.client.requestResponse" level="debug"/>
    <logger name="ch.swisscom.mid.client.fullRequestResponse" level="warn"/>

</configuration>
```
