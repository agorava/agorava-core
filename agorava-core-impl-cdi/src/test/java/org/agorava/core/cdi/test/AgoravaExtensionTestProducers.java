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

package org.agorava.core.cdi.test;

import org.agorava.core.api.oauth.DefaultOAuth20Api;
import org.agorava.core.api.oauth.OAuthApi;
import org.agorava.core.api.oauth.OAuthAppSettings;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.cdi.Current;
import org.agorava.core.oauth.PropertyOAuthAppSettingsBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

/**
 * @author Antoine Sabot-Durand
 */
public class AgoravaExtensionTestProducers {


    @Produces
    @FakeService2
    public OAuthApi produceFake2Root() {
        return new DefaultOAuth20Api() {
            @Override
            public String getServiceName() {
                return "Facebook";
            }
        };
    }

    @ApplicationScoped
    @Produces
    @FakeService
    public OAuthAppSettings produceFirstSetting() {
        PropertyOAuthAppSettingsBuilder builder = new PropertyOAuthAppSettingsBuilder();
        return builder.build();
    }

    @ApplicationScoped
    @Produces
    @FakeService2
    public OAuthAppSettings produceSecondSetting() {
        PropertyOAuthAppSettingsBuilder builder = new PropertyOAuthAppSettingsBuilder();
        return builder.prefix("test").build();
    }

    @ApplicationScoped
    @Produces
    @Current
    @FakeService
    public OAuthSession produceAppSession(@FakeService @Default OAuthSession session) {

        return session;
    }
}
