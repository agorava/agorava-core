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

package org.agorava.api.oauth.application;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * Configuration settings needed to access to an OAuth 1.0a and 2.0 service tier.
 * Used by {@link org.agorava.api.oauth.OAuthService} to setup
 * connection to Social Media
 *
 * @author Antoine Sabot-Durand
 */
public class OAuthAppSettings implements Serializable {

    private static final long serialVersionUID = -8018722725677732853L;

    private String apiKey;

    private String apiSecret;

    private String callback;

    private String scope;

    private String name;

    private Annotation qualifier;

    OAuthAppSettings() {
    }

    OAuthAppSettings(String name, String apiKey, String apiSecret, String callback, String scope, Annotation qualifier) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callback = callback;
        this.scope = scope;
        this.name = name;
        this.qualifier = qualifier;
    }

    /**
     * @return the key consumer key for the OAuth service
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @return the consumer secret key for the OAuth service
     */
    public String getApiSecret() {
        return apiSecret;
    }

    /**
     * @return the call back URL for the OAuth service
     */
    public String getCallback() {
        return callback;
    }

    /**
     * @return the scope requested
     */
    public String getScope() {
        return scope;
    }

    /**
     * @return the name of the service
     */
    public String getSocialMediaName() {
        return name;
    }

    /**
     * @return the qualifier associated to the service
     */
    public Annotation getQualifier() {
        return qualifier;
    }

    @Override
    public String toString() {
        return "OAuthAppSettings [apiKey=" + apiKey + ", apiSecret=" + apiSecret + ", callback=" + callback
                + ", scope=" + scope + ", serviceName=" + getSocialMediaName() + "]";
    }

    /**
     * @return true if this OAuthAppSettings contains a scope
     */
    public boolean hasScope() {
        return (scope != null && !"".equals(scope));
    }


}
