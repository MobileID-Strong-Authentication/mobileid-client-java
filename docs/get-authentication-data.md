# Get authentication details for the Mobile ID client

The authentication of the Mobile ID client happens over two separate channels:

- at connection time, during TLS handshake, the Mobile ID server authenticates the client via TLS client authentication. You therefore
  need a client certificate to be enrolled on Swisscom's side.
- after the connection is established, there is another pair of Application Provider (AP) ID and password that need to be provided with
  each request sent to the Mobile ID service. This is provided by Swisscom during test/production account setup.
- optionally, for the TLS handshake phase, you might also need the certificates of the CA that issued the server TLS certificate. If you
  are using the production version of Mobile ID, then those CAs should be already trusted on your host. If otherwise, the CA certificates
  need to be added in a Java truststore that is configured on the Mobile ID client before first run.

To start using the Mobile ID client, you need the client (and server) TLS certificates and the AP ID and password. 

## Using these details
As an example, once you have the authentication data ready for the Mobile ID client, you can use it for configuring the 
client. Please continue to [configure the client](configure-the-client.md).

## Side-note - generating a test certificate

Should you need it, here is a quick guide on generating a test certificate to be enrolled on the Mobile ID service. For these steps,
a local installation of [OpenSSL](https://www.openssl.org/) is needed (for Windows, the best option is to use the one that comes
with GIT for Windows (see _<git>/usr/bin/openssl.exe_)).

Generate first a private key:
```shell
openssl genrsa -des3 -out my-mid-client.key 2048
```
Then generate a Certificate Signing Request (CSR):
```shell
openssl req -new -key my-mid-client.key -out my-mid-client.csr
```
You will be asked for the following:
```text
Country Name (2 letter code) [AU]: US
State or Province Name (full name) [Some-State]: YourCity
Locality Name (eg, city) []: YourCity
Organization Name (eg, company) [Internet Widgits Pty Ltd]: TEST Your Company
Organizational Unit Name (eg, section) []: For test purposes only
Common Name (e.g. server FQDN or YOUR name) []: TEST Your Name
Email Address []: your.name@yourmail.com
```

Then generate a self-signed certificate:
```shell
openssl x509 -req -days 365 -in my-mid-client.csr -signkey my-mid-client.key -out my-mid-client.crt
```
This is the last step. Now you have a self-signed certificate that can be enrolled on Mobile ID service's side in order to get a test
account.

