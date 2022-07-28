# Mobile ID Java client (v1.5.1)

Swisscom Mobile ID is a cost-efficient, managed authentication service from Swisscom. The customer-facing API is
based on open standard ETSI 102 2041. The library from this repository is a reference implementation for 
a Java client that provides access to the main services provided by Mobile ID:

* Mobile digital signature (sync and async)
* Signature receipt
* Mobile user profile query

The repository provides the main Java-based reference implementation for the Mobile ID REST and SOAP API client. The user can choose 
the implementation during configuration phase. There is also a command-line interface that allows you to play with this client 
as a tool from your own terminal.

You can see the list of changes between versions on the [version history page](docs/version-history.md).

## Who is this for?

The library provided by this repository is for all clients that are developing Java-based projects that need secure authentication
and authorization services using the mobile phone. The library works with Java 8+ projects and can be added as a project dependency
and used in any scenario that needs to access the Swisscom Mobile ID service. 

## Getting started

To start using the Swisscom Mobile ID service and this client library, follow these steps:
1. [Get authentication data to use with the Mobile ID client](docs/get-authentication-data.md)
2. [Build, download or link the Mobile ID client binary package](docs/build-download-or-link.md)
3. [Configure the Mobile ID client for your specific use case](docs/configure-the-client.md)
4. Use the Mobile ID client, either [programmatically](docs/use-the-client-programmatically.md) or 
   from the [command line](docs/use-the-client-via-cli.md)
5. Learn about the code and the overall architecture of the client. See the [ARCHITECTURE.md](ARCHITECTURE.md) file
   
For extra help:
- [How to validate signatures](/docs/validate-signatures.md)
- [How to use an HTTP proxy with or without authentication](/docs/configure-proxy-connection.md) 
- [How to solve the most common problems](/docs/troubleshoot-common-problems.md)

## Quick examples

The rest of this page provides some quick examples for using the Mobile ID client. Please see the links above for detailed 
instructions on how to get authentication data, download and configure the client. The next parts of the file assume that you are
already set up.

### Command line usage

Get a help message by calling the client without any arguments:
```shell
./bin/mid-client.sh 
```
or 
```shell
./bin/mid-client.sh -help 
```

Get a default configuration file set in the current folder using the _-init_ argument:
```shell
./bin/mid-client.sh -init 
```

Get the profile information for a particular phone number (MSISDN) that your application provider is controlling:
```shell
./bin/mid-client.sh -profile-query -msisdn 41790000000 
```

Get the same profile information using a particular configuration file and the SOAP interface of Mobile ID:
```shell
./bin/mid-client.sh -profile-query -msisdn 41790000000 -config local-config.properties -soap 
```

Request a digital signature to a particular phone number (MSISDN), in sync mode:
```shell
./bin/mid-client.sh -sign -msisdn=41790000000 -lang=en -dtbs "Do you want to login?" -sync  
```

Request a digital signature to a particular phone number (MSISDN), in async mode (this is the default mode) and with signature receipt:
```shell
./bin/mid-client.sh -sign -msisdn=41790000000 -lang=en -dtbs "Do you want to login?" -async -receipt  
```

You can also add the following parameters for extra help:

- _-v_: verbose log output (sets most of the client loggers to info)
- _-vv_: extra verbose log output (sets all the client loggers to debug, without HTTP and TLS traffic)
- _-vvv_: extra-extra verbose log output (sets all the client loggers to debug, also logging the HTTP and TLS traffic)
- _-config_: select a custom properties file for configuration (by default it looks for the one named _config.properties_)

### Programmatic usage

Once you add the Mobile ID client as a dependency to your project, you should configure it:
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

// this client is thread safe and can be reused anywhere in your application
MIDClient client = new MIDClientImpl(config);
```

Finally, test the client with a sync signature:
```java
// setup the data for the signature; you would do this each time you create a signature; alternatively, this can be cached 
// on a PER USER level 
SignatureRequest request = new SignatureRequest();
request.setUserLanguage(UserLanguage.ENGLISH);
request.getDataToBeSigned().setData("Test: Do you want to login?");
request.getMobileUser().setMsisdn("41790000000");
request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
request.addAdditionalService(new GeofencingAdditionalService());

SignatureResponse response = client.requestSyncSignature(request);
System.out.println(response.toString());
```

or with an async one (you have to do the polling for the final status):
```java
SignatureRequest request = new SignatureRequest();
request.setUserLanguage(UserLanguage.ENGLISH);
request.getDataToBeSigned().setData("Test: Do you want to login?");
request.getMobileUser().setMsisdn("41790000000");
request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);
request.addAdditionalService(new GeofencingAdditionalService());

try {
    SignatureResponse response = client.requestAsyncSignature(request);
    while (response.getStatus().getStatusCode() == StatusCode.REQUEST_OK ||
           response.getStatus().getStatusCode() == StatusCode.OUTSTANDING_TRANSACTION) {
        Thread.sleep(5000);
        System.out.println("Pending: " + response);
        response = client.pollForSignatureStatus(response.getTracking());
    }
    System.out.println(response.toString());
} catch (Exception e) {
    System.out.println(prettyPrintTheException(e));
} finally {
    client.close();
}
```

## License

The content of this repository is licensed under the Apache License, Version 2.0. You may not use this content except 
in compliance with this license. You may obtain a copy of the license at http://www.apache.org/licenses/LICENSE-2.0
