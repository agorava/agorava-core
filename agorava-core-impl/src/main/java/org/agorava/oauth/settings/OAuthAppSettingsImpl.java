/*
 * Copyright 2013-2016 Agorava
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
package org.agorava.oauth.settings;

import org.agorava.api.oauth.application.OAuthAppSettings;

import java.lang.annotation.Annotation;

/**
 * Configuration settings needed to access to an OAuth 1.0a and 2.0 service tier.
 * Used by {@link org.agorava.api.oauth.OAuthService} to setup
 * connection to Social Media
 *
 * @author Antoine Sabot-Durand
 * @author Werner Keil
 */
public class OAuthAppSettingsImpl implements OAuthAppSettings {

    private static final long serialVersionUID = -8018722725677732853L;

    private String apiKey;

    private String apiSecret;

    private String callback;

    private String scope;

    private String name;

    private Annotation qualifier;
    
    private boolean enabled;

    OAuthAppSettingsImpl() {
    }

    OAuthAppSettingsImpl(String name, String apiKey, String apiSecret, String callback, String scope, Annotation qualifier,
    		String enable) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callback = callback;
        this.scope = scope;
        this.name = name;
        this.qualifier = qualifier;
        if (enable != null && enable.length() > 0) {
        	this.enabled = Boolean.parseBoolean(enable);
        } else {
        	this.enabled = true;
        }
    }

    @Override
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

    @Override
    public String toString() {
        return "OAuthAppSettings [apiKey=" + apiKey + ", apiSecret=" + apiSecret + ", callback=" + callback
                + ", scope=" + scope + ", serviceName=" + getSocialMediaName() + "]";
    }

    @Override
    public boolean hasScope() {
        return (scope != null && !"".equals(scope));
    }

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
