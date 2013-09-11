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

package org.agorava.core.api.oauth;

import org.agorava.core.api.rest.Request;

import java.util.Map;

/**
 * @author Antoine Sabot-Durand
 */
public interface OAuthRequest extends Request {
    /**
     * Adds an OAuth parameter.
     *
     * @param key   name of the parameter
     * @param value value of the parameter
     * @throws IllegalArgumentException if the parameter is not an OAuth parameter
     */
    void addOAuthParameter(String key, String value);

    /**
     * Returns the {@link java.util.Map} containing the key-value pair of parameters.
     *
     * @return parameters as map
     */
    Map<String, String> getOauthParameters();
}
