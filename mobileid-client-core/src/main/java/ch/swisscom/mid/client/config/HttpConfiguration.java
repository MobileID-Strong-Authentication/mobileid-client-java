/*
 * Copyright 2021 Swisscom (Schweiz) AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.swisscom.mid.client.config;

import static ch.swisscom.mid.client.utils.Utils.configTrue;

public class HttpConfiguration {

    private int maxTotalConnections = DefaultConfiguration.HTTP_CLIENT_MAX_TOTAL_CONNECTIONS;
    private int maxConnectionsPerRoute = DefaultConfiguration.HTTP_CLIENT_DEFAULT_CONNECTIONS_PER_ROUTE;
    private int connectionTimeoutInMs = DefaultConfiguration.HTTP_CLIENT_DEFAULT_CONNECTION_TIMEOUT_IN_MS;
    private int responseTimeoutInMs = DefaultConfiguration.HTTP_CLIENT_DEFAULT_SOCKET_READ_TIMEOUT_IN_MS;

    // ----------------------------------------------------------------------------------------------------

    public int getConnectionTimeoutInMs() {
        return connectionTimeoutInMs;
    }

    public void setConnectionTimeoutInMs(int connectionTimeoutInMs) {
        this.connectionTimeoutInMs = connectionTimeoutInMs;
    }

    public int getResponseTimeoutInMs() {
        return responseTimeoutInMs;
    }

    public void setResponseTimeoutInMs(int responseTimeoutInMs) {
        this.responseTimeoutInMs = responseTimeoutInMs;
    }

    public int getMaxTotalConnections() {
        return maxTotalConnections;
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    public int getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    // ----------------------------------------------------------------------------------------------------

    public void validateYourself() {
        configTrue(maxTotalConnections > 0,
                   "The maxTotalConnections must be higher than zero");
        configTrue(maxConnectionsPerRoute > 0,
                   "The maxConnectionsPerRoute must be higher than zero");
        configTrue(connectionTimeoutInMs > 0,
                   "The connectionTimeoutInMs must be higher than zero");
        configTrue(responseTimeoutInMs > 0,
                   "The responseTimeoutInMs must be higher than zero");
    }

}
