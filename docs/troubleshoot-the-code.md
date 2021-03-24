# Troubleshooting the code (when using the Mobile ID Java client)

Here you can find some hints on solving some of the common issues with the Mobile ID Java client.

## Set proxy

If the Mobile ID client needs to access the backend service via a proxy, you can configure this at Java VM level (for more details 
see this [official documentation for Java 8](https://docs.oracle.com/javase/8/docs/technotes/guides/net/proxies.html)). For the 
current version of the Mobile ID client (v1.1.0), the only way to make the client go through a particular proxy host is to configure
the proxy at Java VM level, which means that the entire Java process will go through that proxy for network connections.

The proxy details can be configured either via command line options when starting the Java process, or in code.

For command line (choose only the parameters that are relevant for your case):
```shell
java -Dhttp.proxyHost=proxy.host.com \ 
     -Dhttp.proxyPort=8080 \
     -Dhttp.proxyUser=myUser \
     -Dhttp.proxyPassword=myPass \
     -Dhttp.nonProxyHosts="localhost|host.example.com" \
     -Dhttps.proxyHost=proxy.host.com \
     -Dhttps.proxyPort=8080 \
     -DsocksProxyHost=socks.host.com
```
For code wise, you can define the same properties as above as soon as possible when the Java application starts (before any network
connection is made) (again, define only the properties that are making sense for your case):
```java
System.setProperty("http.proxyHost", "proxy.host.com");
System.setProperty("http.proxyPort", "8080");
System.setProperty("http.proxyUser", "myUser");
System.setProperty("http.http.proxyUser", "myPass");
System.setProperty("http.nonProxyHosts", "localhost|host.example.com");
System.setProperty("https.proxyHost", "proxy.host.com");
System.setProperty("https.proxyPort", "8080");
System.setProperty("socksProxyHost", "socks.host.com");
```

## Extra debugging and logging

In many cases, when the code does not work as expected (or it doesn't work at all) you need extra debugging and logging. Just like
for the case of configuring the proxy, this can be done via command line or in code.

For command line:
```shell
# Enable remote debugging (Java 8+)
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000 ...
# and for Java 9+ (specific host on which to bind)
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=127.0.0.1:8000 ...

# Enable debug logging for SOAP communication (via com.sun.xml):
java -Dcom.sun.xml.ws.transport.http.client.HttpTransportPipe.dump=true -Djaxb.debug=true ...

# Enable debugging for TLS communication
java -Djavax.net.debug=all -Djava.security.debug=certpath
# or more filtering:
java -Djavax.net.debug=ssl,handshake -Djava.security.debug=certpath
# or even more filtering:
java -Djavax.net.debug=ssl:handshake:verbose:keymanager:trustmanager -Djava.security.debug=certpath
```
and code wise (use only what you need):
```java
// Remote debugging cannot be enabled as the Java process runs; it must be done before starting the process

// Enable debug logging for SOAP communication (via com.sun.xml):
System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
System.setProperty("jaxb.debug", "true");

// Enable debugging for TLS communication
System.setProperty("javax.net.debug", "all");
System.setProperty("java.security.debug", "certpath");
    
// or more filtering:
System.setProperty("javax.net.debug", "ssl,handshake");
System.setProperty("java.security.debug", "certpath");

// or even more filtering:
System.setProperty("javax.net.debug", "ssl:handshake:verbose:keymanager:trustmanager");
System.setProperty("java.security.debug", "certpath");
```

See this [Oracle link](https://docs.oracle.com/javase/7/docs/technotes/guides/security/jsse/JSSERefGuide.html#SSLOverview) 
for details on _javax.net.debug_ 
and this [Oracle link](https://docs.oracle.com/javase/8/docs/technotes/guides/security/troubleshooting-security.html) 
for more details on _java.security.debug_. 

