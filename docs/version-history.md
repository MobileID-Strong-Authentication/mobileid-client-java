# Version history

# v1.5.0
Add support for signature validation, after a mobile signature is successfully created. Add a new component that can be configured separately 
for validating the signing certificate, the certificate path, the actual signature and the DTBD/DTBS that was signed.

# v1.4.0
Add support for HTTP proxy configuration. With this version, the Mobile ID Java client can be configured to use an HTTP proxy, with or 
without Basic authentication. This works both for command line and for programmatic mode. 

# v1.3.0
Add optional override of AP ID and AP Password for each request and for the tracking object of a signature operation. 
This helps with sending requests on behalf of various APs, instead of relying only on the common AP ID + AP password 
configured in ClientConfiguration.

The AP ID and password values are taken by default from ClientConfiguration if there is none configured in the 
signature/profile/receipt request. Otherwise, the value(s) from the request are used. These values, if configured per
request, are also transferred into the signature tracking object, to help with monitoring the status of an async
signature.

# v1.2.0
No new features. Just updating the Maven artifacts and bumping the version to 1.2.0. 
The artifacts are now available via Maven Central Repository.

## v1.1.0
First official release of the Mobile ID Java client.
Contains the usage of the Geofencing additional service (instead of the deprecated Subscriber Info).
Provides a new CLI parameter called "req-timeout" to allow for setting the signature request timeout.
