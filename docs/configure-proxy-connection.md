# Configure HTTP proxy connection

For the cases when connecting to the Mobile ID backend the network call must go through an HTTP proxy, the Mobile ID client allows the
configuration of the HTTP proxy parameters. Both the REST and SOAP protocol implementations of the Mobile ID API can be used with an HTTP proxy. 

For the command line usage, configure these parameters in the _config.properties_ file:
```properties
# Enable HTTP proxy connection
client.proxy.enabled=false
# The host of the proxy. 
client.proxy.host=localhost
# The port of the proxy.
client.proxy.port=3128
# The optional username to use during proxy authentication. Can be left empty in which case the client will not authenticate to the proxy.
client.proxy.username=myuser
# The optional password to use during proxy authentication
client.proxy.password=mypass
```

For programmatic usage, configure the client with these snippet of code:
```java
ClientConfiguration config = new ClientConfiguration();
// ...
ProxyConfiguration proxy = config.getProxy();
proxy.setHost("localhost");
proxy.setPort(3128);
proxy.setUsername("myuser");
proxy.setPassword("mypass");
// ...
```

The username and password parameters are optional. Not setting them makes the client use the HTTP proxy without authentication.

## Notes on implementation

Note 1: as of version v1.4.0, only the HTTP proxy type is supported, with an optional basic authentication.

Note 2: if you want to check how the proxy is configured, for the REST protocol see the *ComProtocolHandlerRestImpl* class and for the SOAP
protocol see the *MssServiceFactory* and *ProxyAwareSSLSocketFactory* classes.

Note 3: both the REST and SOAP protocol implementations configure the proxy PER CLIENT, not per JVM. This means that using the Mobile ID client
as a library in a larger application allows you to configure just the Mobile ID client to go through a particular proxy, not the entire JVM
process.

Note 4: The classic approach with configuring the Java options "http(s).proxyHost" and "http(s).proxyPort" will not work for the Mobile ID 
client, as for the REST implementation it is using the Apache HTTP Client 5, and for SOAP it is using the JAX-WS implementation.

Note 5: Once a proxy configuration has been set for a Mobile ID client instance, it is not possible (or very difficult, anyway) to change it dynamically.
For example, you cannot change the host, port or enable/disable the proxy usage while the Mobile ID client instance is in use. The best approach is to 
recreate the Mobile ID client object with the new proxy configuration. 
