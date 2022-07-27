# Validate signature after a successful authentication/authorization process

## In code

After successfully performing a mobile signature process, it is a good idea to validate the obtained signature, to ensure it is correct from the 
following point of views:
* the signing certificate (user certificate) is still valid
* the signing certificate has a valid certificate path (it roots to a Certification Authority that is trusted in the context of the client)
* the performed signature is correct
* the DTBD/DTBS that was requested is identical to the one that was signed

We start first with the code for performing a mobile signature:

```java
ClientConfiguration config = new ClientConfiguration();
// set all the configuration parameters for the client
    
// use the config to create a new client; do this only once per application
MIDClient client = new MIDClientImpl(config);

// configure the request and send it to the Mobile ID backend
SignatureRequest request = new SignatureRequest();
request.getDataToBeSigned().setData("Test: Do you want to login?");
request.setUserLanguage(UserLanguage.ENGLISH);
request.getMobileUser().setMsisdn("41790000000");
request.setSignatureProfile(SignatureProfiles.DEFAULT_PROFILE);

SignatureResponse response = client.requestSyncSignature(request);

if (response.getStatus().getStatusCode() == StatusCode.SIGNATURE) {
    // at this point, we have successfully acquired a mobile signature
    // you can find it in response.getBase64Signature(), as a Base64-encoded CMS content
    
    // let's validate it
    
    // just like the client, the signature validator needs a bit of configuration
    SignatureValidationConfiguration svConfig = new SignatureValidationConfiguration();
    // you can also set the truststore as a classpath file or as a byte array
    svConfig.setTrustStoreFile("signature-validation-truststore.jks");
    svConfig.setTrustStoreType("jks")
    svConfig.setTrustStorePassword("secret"); // optional
    
    // now, with the config, let's create the signature validator and use it
    SignatureValidator validator = new SignatureValidatorImpl(svConfig);
    SignatureValidationResult result = validator.validateSignature(response.getBase64Signature(), request.getDataToBeSigned().getData(), null);
    
    if (result.isValidationSuccessful()) {
        // all is good, the signature is perfectly valid
        // moreover, we can also get some extra data from the signature
        System.out.println("Mobile ID serial number = " + result.getMobileIdSerialNumber());
        System.out.println("Signed DTBS = " + result.getSignedDtbs());
        
    } else {
        // something failed
        System.out.println("Validation failure reason = " + result.getValidationFailureReason());
        System.out.println("Signing certificate path validation = " + result.isSignerCertificatePathValid());
        System.out.println("Signing certificate validation = " + result.isSignerCertificateValid());
        System.out.println("Signature validation = " + result.isSignatureValid());
        System.out.println("DTBS matching = " + result.isDtbsMatching());
        if (result.getValidationException() != null) {
            result.getValidationException().printStackTrace();
        } 
    }
    
}
```

## In CLI mode

If you are using the Mobile ID Client in CLI mode, you can use the "-validate" flag to ask the client to validate the signature once it is 
successfully acquired. 

First you need to configure the signature validation truststore in your "config.properties" file:

```properties
client.signatureValidation.trustStore.file=signature-validation-truststore.jks
client.signatureValidation.trustStore.type=jks
client.signatureValidation.trustStore.password=secret
```

Then request a digital signature and ask for the validation of it at the end:

```shell
./bin/mid-client.sh -sign -msisdn=41790000000 -lang=en -dtbs "Do you want to login?" -validate  
```
