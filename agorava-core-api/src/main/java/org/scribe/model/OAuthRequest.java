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

package org.scribe.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The representation of an OAuth HttpRequest.
 * <p/>
 * Adds OAuth-related functionality to the {@link org.scribe.model.Request}
 *
 * @author Pablo Fernandez
 */
public class OAuthRequest extends Request {
    private static final String OAUTH_PREFIX = "oauth_";
    private Map<String, String> oauthParameters;

    /**
     * Default constructor.
     *
     * @param verb Http verb/method
     * @param url  resource URL
     */
    public OAuthRequest(Verb verb, String url) {
        super(verb, url);
        this.oauthParameters = new HashMap<String, String>();
    }

    /**
     * Adds an OAuth parameter.
     *
     * @param key   name of the parameter
     * @param value value of the parameter
     * @throws IllegalArgumentException if the parameter is not an OAuth parameter
     */
    public void addOAuthParameter(String key, String value) {
        oauthParameters.put(checkKey(key), value);
    }

    private String checkKey(String key) {
        if (key.startsWith(OAUTH_PREFIX) || key.equals(OAuthConstants.SCOPE)) {
            return key;
        } else {
            throw new IllegalArgumentException(String.format("OAuth parameters must either be '%s' or start with '%s'", OAuthConstants.SCOPE, OAUTH_PREFIX));
        }
    }

    /**
     * Returns the {@link java.util.Map} containing the key-value pair of parameters.
     *
     * @return parameters as map
     */
    public Map<String, String> getOauthParameters() {
        return oauthParameters;
    }

    @Override
    public String toString() {
        return String.format("@OAuthRequest(%s, %s)", getVerb(), getUrl());
    }
}
