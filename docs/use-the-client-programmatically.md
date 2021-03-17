# Use the Mobile ID client programmatically

The Mobile ID client library is designed to be used inside your project, as one of its dependencies. It is implemented in a thread-safe
way so it can be created once per application/process and reused anywhere in your application where you need it.

## Dependency configuration

For Maven projects, add the following in your _POM_ file:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

    <!-- ... -->
    
    <repositories>
        <repository>
            <id>swisscom-mobile-id-client</id>
            <name>Swisscom Mobile ID client repository</name>
            <url>TODO add link</url>
        </repository>
    </repositories>
    
    <!-- ... -->

    <dependencies>
        <dependency>
            <groupId>ch.swisscom.mid.client</groupId>
            <artifactId>mid-client-rest</artifactId>
            <version>1.0.0</version> <!-- or any later version - see README.md in the repository's root -->
        </dependency>
    </dependencies>
    <!-- Alternatively, you can also reference the SOAP implementation of the client; adding both of them does not make much sense -->
    <dependencies>
        <dependency>
            <groupId>ch.swisscom.mid.client</groupId>
            <artifactId>mid-client-soap</artifactId>
            <version>1.0.0</version> <!-- or any later version - see README.md in the repository's root -->
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

repositories {
    mavenCentral()
    maven {
        url 'TODO add link'
    }
}

dependencies {
    compile 'ch.swisscom.mid.client:mid-client-rest:1.0.0' // or any later version - see README.md in the repository's root
    // Alternatively, you can also reference the SOAP implementation of the client; adding both of them does not make much sense
    compile 'ch.swisscom.mid.client:mid-client-soap:1.0.0' // or any later version - see README.md in the repository's root
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
request.getDataToBeSigned().setData("ApplicationProvider.com: Please sign this document");
request.getMobileUser().setMsisdn("41790000000");
request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
request.addAdditionalService(new SubscriberInfoAdditionalService());

SignatureResponse response = client.requestSyncSignature(request);
System.out.println(response.toString());
```

To create a signature in asynchronous mode AND send a receipt to the mobile user after the signature is finished, you can do the following:
```java
SignatureRequest request = new SignatureRequest();
request.setUserLanguage(UserLanguage.ENGLISH);
request.getDataToBeSigned().setData("ApplicationProvider.com: Please sign this document");
request.getMobileUser().setMsisdn("41790000000");
request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
request.addAdditionalService(new SubscriberInfoAdditionalService());

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
receiptRequest.getMessageToBeDisplayed().setData("Document signed successfully");
receiptRequest.setStatusCode(StatusCode.REQUEST_OK);
receiptRequest.addReceiptRequestExtension();

ReceiptResponse receiptResponse = client.requestSyncReceipt(signatureResponse.getTracking(), receiptRequest);
System.out.println(receiptResponse.toString());
```
