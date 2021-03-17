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
package ch.swisscom.mid.client.rest.model.profqresp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "App",
        "Sim"
})
public class Sscds {

    @JsonProperty("App")
    private List<App> app = new ArrayList<App>();
    @JsonProperty("Sim")
    private Sim sim;

    @JsonProperty("App")
    public List<App> getApp() {
        return app;
    }

    @JsonProperty("App")
    public void setApp(List<App> app) {
        this.app = app;
    }

    public Sscds withApp(List<App> app) {
        this.app = app;
        return this;
    }

    @JsonProperty("Sim")
    public Sim getSim() {
        return sim;
    }

    @JsonProperty("Sim")
    public void setSim(Sim sim) {
        this.sim = sim;
    }

    public Sscds withSim(Sim sim) {
        this.sim = sim;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(50);
        sb.append(Sscds.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("app");
        sb.append('=');
        sb.append(((this.app == null) ? "<null>" : this.app));
        sb.append(',');
        sb.append("sim");
        sb.append('=');
        sb.append(((this.sim == null) ? "<null>" : this.sim));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
