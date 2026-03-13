/*
 *
 *  * Copyright 2021-2026 Swisscom (Schweiz) AG
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

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
package ch.swisscom.mid.client.model.service;

import ch.swisscom.mid.client.config.DefaultConfiguration;
import ch.swisscom.mid.client.model.App2App;

public class App2AppAdditionalService extends AdditionalService {

    private App2App app2app;

    public App2AppAdditionalService() {
        super(DefaultConfiguration.ADDITIONAL_SERVICE_APP2APP);

    }

    public App2AppAdditionalService(String redirectUri) {
        super(DefaultConfiguration.ADDITIONAL_SERVICE_APP2APP);
        this.app2app = new App2App();
        this.app2app.setRedirectUri(redirectUri);

    }

    public App2App getApp2app() {
        return app2app;
    }

    public void setApp2app(App2App app2app) {
        this.app2app = app2app;
    }
}
