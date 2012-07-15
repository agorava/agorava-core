/*
 * Copyright 2012 Agorava
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

package org.agorava.core.oauth;

import org.agorava.core.api.oauth.ApplicationSettings;
import org.agorava.core.api.oauth.SettingsBuilder;
import org.agorava.core.utils.AgoravaContext;

/**
 * This builder create an {@link org.agorava.core.api.oauth.ApplicationSettings} with OAuth Application params
 *
 * @author Antoine Sabot-Durand
 */
public class SimpleSettingsBuilder extends SettingsBuilder {

    protected String name;
    protected String apiKey;
    protected String apiSecret;
    protected String callback = "oob";
    protected String scope = "";

    @Override
    public SimpleSettingsBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SimpleSettingsBuilder setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public SimpleSettingsBuilder setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    public SimpleSettingsBuilder setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public SimpleSettingsBuilder setScope(String scope) {
        this.scope = scope;
        return this;
    }

    @Override
    public ApplicationSettings build() {
        checkCallback();
        return new ApplicationSettingsImpl(name, apiKey, apiSecret, callback, scope);
    }

    protected void checkCallback() {
        if (!"oob".equals(callback) && (!callback.startsWith("http://") || callback.startsWith("https://")))
            callback = AgoravaContext.webAbsolutePath + callback;
    }
}
