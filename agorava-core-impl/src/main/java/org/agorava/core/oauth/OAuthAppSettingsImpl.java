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

package org.agorava.core.oauth;

import org.agorava.core.api.ApplyQualifier;
import org.agorava.core.api.oauth.OAuthAppSettings;

import java.lang.annotation.Annotation;

/**
 * {@inheritDoc}
 *
 * @author Antoine Sabot-Durand
 */
@ApplyQualifier
public class OAuthAppSettingsImpl implements OAuthAppSettings {

    private static final long serialVersionUID = -8018722725677732853L;

    private final String apiKey;

    private final String apiSecret;

    private final String callback;

    private final String scope;

    private final String name;

    private final Annotation qualifier;

    public String getApiKey() {
        return apiKey;
    }

    @Override
    public String getApiSecret() {
        return apiSecret;
    }

    @Override
    public String getCallback() {
        return callback;
    }


    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public String getSocialMediaName() {
        return name;
    }

    @Override
    public Annotation getQualifier() {
        return qualifier;
    }

    OAuthAppSettingsImpl(String name, String apiKey, String apiSecret, String callback, String scope, Annotation qualifier) {
        super();
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callback = callback;
        this.scope = scope;
        this.name = name;
        this.qualifier = qualifier;
    }

    @Override
    public String toString() {
        return "OAuthAppSettingsImpl [apiKey=" + apiKey + ", apiSecret=" + apiSecret + ", callback=" + callback
                + ", scope=" + scope + ", serviceName=" + getSocialMediaName() + "]";
    }


}
