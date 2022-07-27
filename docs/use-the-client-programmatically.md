# Use the Mobile ID client programmatically

The Mobile ID client library is designed to be used inside your project, as one of its dependencies. It is implemented in a thread-safe
way so it can be created once per application/process and reused anywhere in your application where you need it.

## Dependency configuration

For Maven projects, add the following in your _POM_ file:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!-- ... -->
    <dependencies>
        <dependency>
            <groupId>ch.mobileid.mid-java-client</groupId>
            <artifactId>mid-java-client-rest</artifactId>
            <version>1.5.0</version> <!-- or any later version - see README.md in the repository's root -->
        </dependency>
    </dependencies>
    <!-- Alternatively, you can also reference the SOAP implementation of the client; adding both of them does not make much sense -->
    <dependencies>
        <dependency>
            <groupId>ch.mobileid.mid-java-client</groupId>
            <artifactId>mid-java-client-soap</artifactId>
            <version>1.5.0</version> <!-- or any later version - see README.md in the repository's root -->
        </dependency>
    </dependencies>
</project>
```

For Gradle projects, add the following to your _build.gradle_ file:
```groovy
plugins {
    id 'java'
}

// ...

dependencies {
    compile 'ch.mobileid.mid-java-client:mid-java-client-rest:1.5.0' // or any later version - see README.md in the repository's root
    // Alternatively, you can also reference the SOAP implementation of the client; adding both of them does not make much sense
    compile 'ch.mobileid.mid-java-client:mid-java-client-soap:1.5.0' // or any later version - see README.md in the repository's root
    // ...
}
```

## Using the library

This section describes the usage of the library in code. See the sample files in the _mobileid-client-usage_ module for complete
examples of how to configure and use the library.

First create a _ClientConfiguration_ object to hold all the authentication and connection parameters. This needs to be done
once per application lifetime and can be reused by any service or component that your application might have. The client is thread
safe and makes good use of HTTP connection and SOAP port pooling, in order to efficiently reuse resources.
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

HttpConfiguration http = config.getHttp();
http.setConnectionTimeoutInMs(20 * 1000);
http.setResponseTimeoutInMs(100 * 1000);
http.setMaxTotalConnections(20);
http.setMaxConnectionsPerRoute(10);
```
Then use the configuration object to create a _MIDClient_ object:
```java
MIDClient client = new MIDClientImpl(config);
```

Once you are done with using the client (e.g. when your application/service shuts down), make sure you call the _close()_ method of
the client, to ensure its resources are correctly deallocated. Don't call this method after each request!

Once the client is up and running, you can start using it. 

For example, requesting the profile of a particular MSISDN can be done as it follows:
```java
ProfileRequest request = new ProfileRequest();
request.getMobileUser().setMsisdn("41790000000");
request.setExtensionParamsToAllValues();

ProfileResponse response = client.requestProfile(request);
System.out.println(response.toString());
```
To create a signature in synchronous mode (call and wait for the signature to finish), you would implement the following:
```java
SignatureRequest request = new SignatureRequest();
request.setUserLanguage(UserLanguage.ENGLISH);
request.getDataToBeSigned().setData("Test: Do you want to login?");
request.getMobileUser().setMsisdn("41790000000");
request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
request.addAdditionalService(new GeofencingAdditionalService());

SignatureResponse response = client.requestSyncSignature(request);
System.out.println(response.toString());
```

To create a signature in asynchronous mode AND send a receipt to the mobile user after the signature is finished, you can do the following:
```java
SignatureRequest request = new SignatureRequest();
request.setUserLanguage(UserLanguage.ENGLISH);
request.getDataToBeSigned().setData("Test: Do you want to login?");
request.getMobileUser().setMsisdn("41790000000");
request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
request.addAdditionalService(new GeofencingAdditionalService());

// start the signature process
SignatureResponse response = client.requestAsyncSignature(request);

// poll for signature status
while (response.getStatus().getStatusCode() == StatusCode.REQUEST_OK ||
       response.getStatus().getStatusCode() == StatusCode.OUTSTANDING_TRANSACTION) {
    Thread.sleep(5000);
    System.out.println("Pending: " + response);
    response = client.pollForSignatureStatus(response.getTracking());
}
System.out.println(response.toString());

// send a receipt; do this only if the signature is finished successfully AND on your side the signature was correctly used 
// (e.g. the document was signed, access was granted, etc)
ReceiptRequest receiptRequest = new ReceiptRequest();
receiptRequest.getMessageToBeDisplayed().setData("Login completed successfully");
receiptRequest.setStatusCode(StatusCode.REQUEST_OK);
receiptRequest.addReceiptRequestExtension();

ReceiptResponse receiptResponse = client.requestSyncReceipt(signatureResponse.getTracking(), receiptRequest);
System.out.println(receiptResponse.toString());
```

## Custom AP ID and AP password per request

The _ClientConfiguration_ class provides a central place where the Application Provider (AP) ID and AP password credentials can be configured. 
This information is used whenever a request (signature, status, profile, receipt) is sent to Mobile ID. 
This information is used by Mobile ID to know which AP is the one that is sending the request.

While this central configuration of AP credentials is very convenient, there are scenarios where one single Mobile ID client instance
should serve multiple APs, each with its own AP ID and AP password. For this case, since v1.3.0, the Mobile ID client allows you to 
override the AP ID and AP password per request:

```java
SignatureRequest request = new SignatureRequest();
request.setOverrideApId("custom-ap-id");
request.setOverrideApPassword("custom-ap-password")
...
```

These methods are available for all operations and are also transferred in the tracking object that is used when an async signature
operation is called and the client needs to poll for that signature's status.
