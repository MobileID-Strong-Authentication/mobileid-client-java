# Troubleshooting the most common problems

The Mobile ID Java client is implemented with programmer-friendliness in mind. 
It tries to collect all available information and provide them back to the calling code (either via a return status or via a 
thrown exception) so that debugging is made easier. 

This page contains some of the most common problems that might appear when using the Mobile ID service and the Java client.

## Keystore not found

If the keystore, truststore or configuration file is not found, you get a very specific exception:
```text
ch.swisscom.mid.client.config.ConfigurationException: Failed to configure the TLS/SSL connection factory for the MID client
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.initialize(ComProtocolHandlerRestImpl.java:110)
        at ch.swisscom.mid.client.impl.MIDClientImpl.<init>(MIDClientImpl.java:50)
        at ch.swisscom.mid.client.cli.Cli.main(Cli.java:136)
Caused by: ch.swisscom.mid.client.config.ConfigurationException: Failed to initialize the TLS keystore
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.produceAKeyStore(ComProtocolHandlerRestImpl.java:264)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.initialize(ComProtocolHandlerRestImpl.java:97)
        ... 2 more
Caused by: java.io.FileNotFoundException: /home/user/unknown.jks (The system cannot find the file specified)
        at java.io.FileInputStream.open0(Native Method)
        at java.io.FileInputStream.open(Unknown Source)
        at java.io.FileInputStream.<init>(Unknown Source)
        at java.io.FileInputStream.<init>(Unknown Source)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.produceAKeyStore(ComProtocolHandlerRestImpl.java:247)
        ... 3 more
--------------------------------------------------------------------------------
Exception stack:
  ConfigurationException = Failed to configure the TLS/SSL connection factory for the MID client
  ConfigurationException = Failed to initialize the TLS keystore
  FileNotFoundException = /home/user/unknown.jks (The system cannot find the file specified)
```

## Invalid keystore password

The following exception is thrown when the keystore password is not correct:
```text
ch.swisscom.mid.client.config.ConfigurationException: Failed to configure the TLS/SSL connection factory for the MID client
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.initialize(ComProtocolHandlerRestImpl.java:110)
        at ch.swisscom.mid.client.impl.MIDClientImpl.<init>(MIDClientImpl.java:50)
        at ch.swisscom.mid.client.cli.Cli.main(Cli.java:136)
Caused by: ch.swisscom.mid.client.config.ConfigurationException: Failed to initialize the TLS keystore
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.produceAKeyStore(ComProtocolHandlerRestImpl.java:264)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.initialize(ComProtocolHandlerRestImpl.java:97)
        ... 2 more
Caused by: java.io.IOException: Keystore was tampered with, or password was incorrect
        at sun.security.provider.JavaKeyStore.engineLoad(Unknown Source)
        at sun.security.provider.JavaKeyStore$JKS.engineLoad(Unknown Source)
        at sun.security.provider.KeyStoreDelegator.engineLoad(Unknown Source)
        at sun.security.provider.JavaKeyStore$DualFormatJKS.engineLoad(Unknown Source)
        at java.security.KeyStore.load(Unknown Source)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.produceAKeyStore(ComProtocolHandlerRestImpl.java:248)
        ... 3 more
Caused by: java.security.UnrecoverableKeyException: Password verification failed
        ... 9 more
--------------------------------------------------------------------------------
Exception stack:
  ConfigurationException = Failed to configure the TLS/SSL connection factory for the MID client
  ConfigurationException = Failed to initialize the TLS keystore
  IOException = Keystore was tampered with, or password was incorrect
  UnrecoverableKeyException = Password verification failed
```

## Invalid private key password

The following exception is thrown when the password of the private key from within the keystore is not correct:
```text
ch.swisscom.mid.client.config.ConfigurationException: Failed to configure the TLS/SSL connection factory for the MID client
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.initialize(ComProtocolHandlerRestImpl.java:110)
        at ch.swisscom.mid.client.impl.MIDClientImpl.<init>(MIDClientImpl.java:50)
        at ch.swisscom.mid.client.cli.Cli.main(Cli.java:136)
Caused by: java.security.UnrecoverableKeyException: Cannot recover key
        at sun.security.provider.KeyProtector.recover(Unknown Source)
        at sun.security.provider.JavaKeyStore.engineGetKey(Unknown Source)
        at sun.security.provider.JavaKeyStore$JKS.engineGetKey(Unknown Source)
        at sun.security.provider.KeyStoreDelegator.engineGetKey(Unknown Source)
        at sun.security.provider.JavaKeyStore$DualFormatJKS.engineGetKey(Unknown Source)
        at java.security.KeyStore.getKey(Unknown Source)
        at sun.security.ssl.SunX509KeyManagerImpl.<init>(Unknown Source)
        at sun.security.ssl.KeyManagerFactoryImpl$SunX509.engineInit(Unknown Source)
        at javax.net.ssl.KeyManagerFactory.init(Unknown Source)
        at org.apache.hc.core5.ssl.SSLContextBuilder.loadKeyMaterial(SSLContextBuilder.java:274)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.initialize(ComProtocolHandlerRestImpl.java:97)
        ... 2 more
--------------------------------------------------------------------------------
Exception stack:
  ConfigurationException = Failed to configure the TLS/SSL connection factory for the MID client
  UnrecoverableKeyException = Cannot recover key
```

## Invalid TLS client alias

If the alias for the TLS client certificate to use is incorrect, the following fault response is returned, with the subsequent
client exception:
```text
{
  "Fault" : {
    "Code" : {
      "SubCode" : {
        "Value" : "_104",
        "ValueNs" : "http://uri.etsi.org/TS102204/v1.1.2#"
      },
      "Value" : "Sender",
      "ValueNs" : "http://www.w3.org/2003/05/soap-envelope"
    },
    "Detail" : "Wrong SSL credentials",
    "Reason" : "UNAUTHORIZED_ACCESS"
  }
}
--------------------------------------------------------------------------------
ch.swisscom.mid.client.MIDFlowException: Fault response received from Mobile ID server. See embedded MIDFault
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.sendAndReceive(ComProtocolHandlerRestImpl.java:368)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.requestAsyncSignature(ComProtocolHandlerRestImpl.java:153)
        at ch.swisscom.mid.client.impl.MIDClientImpl.requestAsyncSignature(MIDClientImpl.java:66)
        at ch.swisscom.mid.client.cli.Cli.main(Cli.java:159)
--------------------------------------------------------------------------------
Fault{failureReason=MID_SERVICE_FAILURE, failureDetail='null', statusCodeString='_104', statusCode=UNAUTHORIZED_ACCESS, statusDetail='Wrong SSL credentials', statusFaultReason='UNAUTHORIZED_ACCESS'}
---
Exception stack:
  MIDFlowException = Fault response received from Mobile ID server. See embedded MIDFault
```

## Mobile ID endpoint not accessible (cannot connect to it)

In some cases, because of a network connectivity issue, the Mobile ID service endpoint might not accessible. You get the following 
exception:
```text
ch.swisscom.mid.client.MIDFlowException: Communication failure for MSS Signature (async)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.sendAndReceive(ComProtocolHandlerRestImpl.java:361)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.requestAsyncSignature(ComProtocolHandlerRestImpl.java:153)
        at ch.swisscom.mid.client.impl.MIDClientImpl.requestAsyncSignature(MIDClientImpl.java:66)
        at ch.swisscom.mid.client.cli.Cli.main(Cli.java:159)
Caused by: org.apache.hc.client5.http.ConnectTimeoutException: Connect to https://some-host.com:8075 [...] failed: connect timed out
        at java.net.DualStackPlainSocketImpl.waitForConnect(Native Method)
        at java.net.DualStackPlainSocketImpl.socketConnect(Unknown Source)
        at java.net.AbstractPlainSocketImpl.doConnect(Unknown Source)
        at java.net.AbstractPlainSocketImpl.connectToAddress(Unknown Source)
        at java.net.AbstractPlainSocketImpl.connect(Unknown Source)
        at java.net.PlainSocketImpl.connect(Unknown Source)
        at java.net.SocksSocketImpl.connect(Unknown Source)
        at java.net.Socket.connect(Unknown Source)
        at org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory$1.run(SSLConnectionSocketFactory.java:222)
        at java.security.AccessController.doPrivileged(Native Method)
        at org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory.connectSocket(SSLConnectionSocketFactory.java:219)
        at org.apache.hc.client5.http.impl.io.DefaultHttpClientConnectionOperator.connect(DefaultHttpClientConnectionOperator.java:148)
        at org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager.connect(PoolingHttpClientConnectionManager.java:409)
        at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:164)
        at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:174)
        at org.apache.hc.client5.http.impl.classic.ConnectExec.execute(ConnectExec.java:135)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.ProtocolExec.execute(ProtocolExec.java:165)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.HttpRequestRetryExec.execute(HttpRequestRetryExec.java:93)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.RedirectExec.execute(RedirectExec.java:116)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.ContentCompressionExec.execute(ContentCompressionExec.java:128)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.InternalHttpClient.doExecute(InternalHttpClient.java:178)
        at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:75)
        at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:89)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.sendAndReceive(ComProtocolHandlerRestImpl.java:321)
        ... 3 more
--------------------------------------------------------------------------------
Fault{failureReason=RESPONSE_TIMEOUT_FAILURE, failureDetail='Connect to https://some-host.com:8075 [...] failed: connect timed out', statusCodeString='INTERNAL_ERROR', statusCode=INTERNAL_ERROR, statusDetail='null', statusFaultReason='null'}
---
Exception stack:
  MIDFlowException = Communication failure for MSS Signature (async)
  ConnectTimeoutException = Connect to https://some-host.com:8075 [...] failed: connect timed out
```

## Client's IP is not allowed on Mobile ID side

Once of the steps needed to obtain access to the Mobile ID is to have the client's IP address marked as allowed on the Mobile ID
service side. Failing to do this might result in the following exception 
(notice the difference between "Connect timed out" from above and the "Connection refused" from below):
```text
ch.swisscom.mid.client.MIDFlowException: Communication failure for MSS Signature (async)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.sendAndReceive(ComProtocolHandlerRestImpl.java:361)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.requestAsyncSignature(ComProtocolHandlerRestImpl.java:153)
        at ch.swisscom.mid.client.impl.MIDClientImpl.requestAsyncSignature(MIDClientImpl.java:66)
        at ch.swisscom.mid.client.cli.Cli.main(Cli.java:159)
Caused by: org.apache.hc.client5.http.HttpHostConnectException: Connect to https://mobileid.swisscom.com:8075 [mobileid.swisscom.com/127.0.0.1] failed: Connection refused: connect
        at java.net.DualStackPlainSocketImpl.waitForConnect(Native Method)
        at java.net.DualStackPlainSocketImpl.socketConnect(Unknown Source)
        at java.net.AbstractPlainSocketImpl.doConnect(Unknown Source)
        at java.net.AbstractPlainSocketImpl.connectToAddress(Unknown Source)
        at java.net.AbstractPlainSocketImpl.connect(Unknown Source)
        at java.net.PlainSocketImpl.connect(Unknown Source)
        at java.net.SocksSocketImpl.connect(Unknown Source)
        at java.net.Socket.connect(Unknown Source)
        at org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory$1.run(SSLConnectionSocketFactory.java:222)
        at java.security.AccessController.doPrivileged(Native Method)
        at org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory.connectSocket(SSLConnectionSocketFactory.java:219)
        at org.apache.hc.client5.http.impl.io.DefaultHttpClientConnectionOperator.connect(DefaultHttpClientConnectionOperator.java:148)
        at org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager.connect(PoolingHttpClientConnectionManager.java:409)
        at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:164)
        at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:174)
        at org.apache.hc.client5.http.impl.classic.ConnectExec.execute(ConnectExec.java:135)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.ProtocolExec.execute(ProtocolExec.java:165)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.HttpRequestRetryExec.execute(HttpRequestRetryExec.java:93)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.RedirectExec.execute(RedirectExec.java:116)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.ContentCompressionExec.execute(ContentCompressionExec.java:128)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.InternalHttpClient.doExecute(InternalHttpClient.java:178)
        at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:75)
        at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:89)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.sendAndReceive(ComProtocolHandlerRestImpl.java:321)
        ... 3 more
--------------------------------------------------------------------------------
Fault{failureReason=HOST_CONNECTION_FAILURE, failureDetail='Connect to https://mobileid.swisscom.com:8075 [mobileid.swisscom.com/127.0.0.1] failed: Connection refused: connect', statusCodeString='INTERNAL_ERROR', statusCode=INTERNAL_ERROR, statusDetail='null', statusFaultReason='null'}
---
Exception stack:
  MIDFlowException = Communication failure for MSS Signature (async)
  HttpHostConnectException = Connect to https://mobileid.swisscom.com:8075 [mobileid.swisscom.com/127.0.0.1] failed: Connection refused: connect
```

## Invalid TLS certificate or AP ID

Failing to correctly configure the TLS certificate or having the wrong AP ID results in the following fault response and the 
subsequent client exception:
```text
{
  "Fault" : {
    "Code" : {
      "SubCode" : {
        "Value" : "_104",
        "ValueNs" : "http://uri.etsi.org/TS102204/v1.1.2#"
      },
      "Value" : "Sender",
      "ValueNs" : "http://www.w3.org/2003/05/soap-envelope"
    },
    "Detail" : "Wrong SSL credentials",
    "Reason" : "UNAUTHORIZED_ACCESS"
  }
}
--------------------------------------------------------------------------------
ch.swisscom.mid.client.MIDFlowException: Fault response received from Mobile ID server. See embedded MIDFault
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.sendAndReceive(ComProtocolHandlerRestImpl.java:368)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.requestAsyncSignature(ComProtocolHandlerRestImpl.java:153)
        at ch.swisscom.mid.client.impl.MIDClientImpl.requestAsyncSignature(MIDClientImpl.java:66)
        at ch.swisscom.mid.client.cli.Cli.main(Cli.java:159)
--------------------------------------------------------------------------------
Fault{failureReason=MID_SERVICE_FAILURE, failureDetail='null', statusCodeString='_104', statusCode=UNAUTHORIZED_ACCESS, statusDetail='Wrong SSL credentials', statusFaultReason='UNAUTHORIZED_ACCESS'}
---
Exception stack:
  MIDFlowException = Fault response received from Mobile ID server. See embedded MIDFault
```

## Hostname verification fails when connection to Mobile ID

In some cases, especially when testing against a test version of the Mobile ID service (or the pre-production environment), the hostname
of the backend service does not match the TLS server certificate presented to the client. In this case you get the exception below: 
```text
ch.swisscom.mid.client.MIDFlowException: TLS/SSL connection failure for MSS Signature (async)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.sendAndReceive(ComProtocolHandlerRestImpl.java:359)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.requestAsyncSignature(ComProtocolHandlerRestImpl.java:153)
        at ch.swisscom.mid.client.impl.MIDClientImpl.requestAsyncSignature(MIDClientImpl.java:66)
        at ch.swisscom.mid.client.cli.Cli.main(Cli.java:159)
Caused by: javax.net.ssl.SSLPeerUnverifiedException: Certificate for <mobileid2.swisscom.com> doesn't match any of the subject alternative names: [mobileid.swisscom.com, www.mobileid.swisscom.com]
        at org.apache.hc.client5.http.ssl.DefaultHostnameVerifier.matchDNSName(DefaultHostnameVerifier.java:172)
        at org.apache.hc.client5.http.ssl.DefaultHostnameVerifier.verify(DefaultHostnameVerifier.java:117)
        at org.apache.hc.client5.http.ssl.TlsSessionValidator.verifySession(TlsSessionValidator.java:112)
        at org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory.verifySession(SSLConnectionSocketFactory.java:314)
        at org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory.verifyHostname(SSLConnectionSocketFactory.java:303)
        at org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory.createLayeredSocket(SSLConnectionSocketFactory.java:277)
        at org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory.connectSocket(SSLConnectionSocketFactory.java:244)
        at org.apache.hc.client5.http.impl.io.DefaultHttpClientConnectionOperator.connect(DefaultHttpClientConnectionOperator.java:148)
        at org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager.connect(PoolingHttpClientConnectionManager.java:409)
        at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:164)
        at org.apache.hc.client5.http.impl.classic.InternalExecRuntime.connectEndpoint(InternalExecRuntime.java:174)
        at org.apache.hc.client5.http.impl.classic.ConnectExec.execute(ConnectExec.java:135)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.ProtocolExec.execute(ProtocolExec.java:165)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.HttpRequestRetryExec.execute(HttpRequestRetryExec.java:93)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.RedirectExec.execute(RedirectExec.java:116)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement$1.proceed(ExecChainElement.java:57)
        at org.apache.hc.client5.http.impl.classic.ContentCompressionExec.execute(ContentCompressionExec.java:128)
        at org.apache.hc.client5.http.impl.classic.ExecChainElement.execute(ExecChainElement.java:51)
        at org.apache.hc.client5.http.impl.classic.InternalHttpClient.doExecute(InternalHttpClient.java:178)
        at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:75)
        at org.apache.hc.client5.http.impl.classic.CloseableHttpClient.execute(CloseableHttpClient.java:89)
        at ch.swisscom.mid.client.rest.ComProtocolHandlerRestImpl.sendAndReceive(ComProtocolHandlerRestImpl.java:321)
        ... 3 more
--------------------------------------------------------------------------------
Fault{failureReason=TLS_CONNECTION_FAILURE, failureDetail='Certificate for <mobileid2.swisscom.com> doesn't match any of the subject alternative names: [mobileid.swisscom.com, www.mobileid.swisscom.com]', statusCodeString='INTERNAL_ERROR', statusCode=INTERNAL_ERROR, statusDetail='null', statusFaultReason='null'}
---
Exception stack:
  MIDFlowException = TLS/SSL connection failure for MSS Signature (async)
  SSLPeerUnverifiedException = Certificate for <mobileid2.swisscom.com> doesn't match any of the subject alternative names: [mobileid.swisscom.com, www.mobileid.swisscom.com]
```

To *temporarily* get around this issue, the Java client can be instructed to ignore the hostname-vs-certificate mismatch when checking 
the server during the TLS handshake:
```java
ClientConfiguration config = new ClientConfiguration();
// ...

TlsConfiguration tls = config.getTls();
tls.setHostnameVerification(false); // <<<< set this to false to disable the hostname verification; default is true
// ...
```
