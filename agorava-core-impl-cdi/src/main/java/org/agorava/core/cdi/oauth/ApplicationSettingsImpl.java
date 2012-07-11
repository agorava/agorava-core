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

package org.agorava.core.cdi.oauth;

import org.agorava.core.api.oauth.ApplicationSettings;

/**
 * @author Antoine Sabot-Durand
 */

public class ApplicationSettingsImpl implements ApplicationSettings {

    private static final long serialVersionUID = -8018722725677732853L;

    private String apiKey;

    private String apiSecret;

    private String callback;

    private String scope;

    private String name;

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    @Override
    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    @Override
    public String getCallback() {
        return callback;
    }

    @Override
    public void setCallback(String callback) {
        this.callback = callback;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public String getSocialMediaName() {
        return name;
    }

    ApplicationSettingsImpl(String name, String apiKey, String apiSecret, String callback, String scope) {
        super();
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callback = callback;
        this.scope = scope;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ApplicationSettingsImpl [apiKey=" + apiKey + ", apiSecret=" + apiSecret + ", callback=" + callback
                + ", scope=" + scope + ", serviceName=" + getSocialMediaName() + "]";
    }


}
