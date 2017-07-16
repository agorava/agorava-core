/*
 * Copyright 2013 Agorava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.agorava.cdi.test;

import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.spi.ProviderConfigOauth20;

/**
 * @author Antoine Sabot-Durand
 */
@FakeService2
public class FakeProvider2 extends ProviderConfigOauth20 {
    @Override
    public String getProviderName() {
        return "Facebook";
    }


    @Override
    public String getAccessTokenEndpoint() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAuthorizationUrl(OAuthAppSettings config) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

