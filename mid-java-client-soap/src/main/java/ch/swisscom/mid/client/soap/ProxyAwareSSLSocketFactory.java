package ch.swisscom.mid.client.soap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Base64;

import javax.net.ssl.SSLSocketFactory;

import ch.swisscom.mid.client.config.ProxyConfiguration;

public class ProxyAwareSSLSocketFactory extends SSLSocketFactory {

    private final ProxyConfiguration proxyConfig;
    private final SSLSocketFactory delegateSocketFactory;
    private Socket proxySocket;

    public ProxyAwareSSLSocketFactory(ProxyConfiguration proxyConfig, SSLSocketFactory delegateSocketFactory) {
        this.proxyConfig = proxyConfig;
        this.delegateSocketFactory = delegateSocketFactory;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        if (proxySocket == null) {
            proxySocket = connectAndReturnProxySocket(host, port);
        }
        return delegateSocketFactory.createSocket(proxySocket, host, port, autoClose);
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        if (proxySocket == null) {
            proxySocket = connectAndReturnProxySocket(host, port);
        }
        return delegateSocketFactory.createSocket(proxySocket, host, port, true);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) {
        throw new UnsupportedOperationException("This SSL socket factory method is not supported");
    }

    @Override
    public Socket createSocket(InetAddress host, int port) {
        throw new UnsupportedOperationException("This SSL socket factory method is not supported");
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) {
        throw new UnsupportedOperationException("This SSL socket factory method is not supported");
    }

    // ----------------------------------------------------------------------------------------------------

    private Socket connectAndReturnProxySocket(String targetHost, int targetPort) throws IOException {
        try {
            /*
             * Set up a socket to do tunneling through the proxy.
             * Start it off as a regular socket, then delegate to the configured SSLSocketFactory for the actual data transfer.
             */
            String proxyHost = proxyConfig.getHost();
            int proxyPort = proxyConfig.getPort();
            Socket proxySocket = new Socket(proxyHost, proxyPort);
            OutputStream proxySocketOutput = proxySocket.getOutputStream();
            String httpContent = "CONNECT " + targetHost + ":" + targetPort + " HTTP/1.1\r\n"
                                 + buildUserAgentHeader()
                                 + buildProxyAuthorizationHeader()
                                 + "\r\n";
            byte[] httpContentBytes;
            try {
                /*
                 * We really do want ASCII7 -- the http protocol doesn't change with locale.
                 */
                httpContentBytes = httpContent.getBytes("ASCII7");
            } catch (UnsupportedEncodingException ignored) {
                /*
                 * If ASCII7 isn't there, something serious is wrong, but Paranoia Is Good (tm)
                 */
                httpContentBytes = httpContent.getBytes();
            }
            proxySocketOutput.write(httpContentBytes);
            proxySocketOutput.flush();

            /*
             * We need to store the reply so we can create a detailed error message to the user.
             */
            byte[] reply = new byte[200];
            int replyLen = 0;
            int newlinesSeen = 0;
            boolean headerDone = false;     /* Done on first newline */

            InputStream proxySocketInput = proxySocket.getInputStream();
            while (newlinesSeen < 2) {
                int i = proxySocketInput.read();
                if (i < 0) {
                    throw new IOException("Unexpected EOF from proxy");
                }
                if (i == '\n') {
                    headerDone = true;
                    ++newlinesSeen;
                } else if (i != '\r') {
                    newlinesSeen = 0;
                    if (!headerDone && replyLen < reply.length) {
                        reply[replyLen++] = (byte) i;
                    }
                }
            }

            /*
             * Converting the byte array to a string is slightly wasteful in the case where the connection was successful,
             * but it is insignificant compared to the network overhead.
             */
            String replyStr;
            try {
                replyStr = new String(reply, 0, replyLen, "ASCII7");
            } catch (UnsupportedEncodingException ignored) {
                replyStr = new String(reply, 0, replyLen);
            }

            /* We asked for HTTP/1.1, so we should get that back */
            if (!replyStr.startsWith("HTTP/1.1 200")) {
                throw new IOException("Unable to proxy through " + proxyHost + ":" + proxyPort + ". Proxy returned \"" + replyStr + "\"");
            }

            return proxySocket;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private String buildUserAgentHeader() {
        String javaVersion = "Java/" + System.getProperty("java.version");
        String userAgentValue = System.getProperty("http.agent") == null ? javaVersion : System.getProperty("http.agent") + " " + javaVersion;
        return "User-Agent: " + userAgentValue + "\r\n";
    }

    private String buildProxyAuthorizationHeader() throws UnsupportedEncodingException {
        if (proxyConfig.getUsername() != null) {
            return "Proxy-Authorization: Basic " + Base64.getEncoder()
                .encodeToString((proxyConfig.getUsername() + ":" + proxyConfig.getPassword()).getBytes("ASCII7")) + "\r\n";
        } else {
            return "";
        }
    }

}
