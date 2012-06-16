/*******************************************************************************
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
 ******************************************************************************/

package org.agorava.core.api.oauth;

import java.util.Map;

import org.agorava.core.api.rest.RestRequest;

/**
 * Implementation of this interface represents an OAuth Request
 * 
 * @author Antoine Sabot-Durand
 * 
 */
public interface OAuthRequest extends RestRequest {

    /**
     * Adds an OAuth parameter.
     * 
     * @param key name of the parameter
     * @param value value of the parameter
     * 
     */
    public void addOAuthParameter(String name, String value);

    /**
     * Returns the {@link Map} containing the key-value pair of parameters.
     * 
     * @return parameters as map
     */
    public Map<String, String> getOauthParameters();

}
