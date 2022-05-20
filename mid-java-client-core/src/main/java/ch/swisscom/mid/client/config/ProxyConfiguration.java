package ch.swisscom.mid.client.config;

import static ch.swisscom.mid.client.utils.Utils.dataGreaterThanZero;
import static ch.swisscom.mid.client.utils.Utils.dataNotEmpty;

public class ProxyConfiguration {

    private boolean enabled;

    private String host;

    private int port;

    private String username;

    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
        this.enabled = true;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
        this.enabled = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        this.enabled = true;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        if (enabled) {
            dataNotEmpty(host, "The proxy host type cannot be NULL or empty");
            dataGreaterThanZero(port, "The proxy port must be configured");
            if (username != null && username.trim().length() > 0) {
                dataNotEmpty(password, "If a proxy username is configured, then also the password needs to be configured");
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "ProxyConfiguration{" +
               "enabled='" + enabled + '\'' +
               ", host='" + host + '\'' +
               ", port=" + port +
               ", username='" + username + '\'' +
               ", password='" + (password != null ? "(not-null)" : "null") + '\'' +
               '}';
    }
}
