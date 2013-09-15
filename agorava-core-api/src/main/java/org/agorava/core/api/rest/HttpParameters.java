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

package org.agorava.core.api.rest;

import java.util.Map;

/**
 * Http Parameters manipulation with various String transformation
 *
 * @author Antoine Sabot-Durand
 */
public interface HttpParameters {
    /**
     * Create a param from key value and add it to the list
     *
     * @param key   name of the param
     * @param value value of the param
     * @return this
     */
    HttpParameters add(String key, String value);

    /**
     * Merge another HttpParameters in this list
     *
     * @param other the HttpParameters to merge
     * @return this
     */
    HttpParameters addAll(HttpParameters other);

    /**
     * Add a Query String in this param list. Query string will be parsed and it's parameter added here
     *
     * @param queryString the string to parse feed this list
     * @return this
     */
    HttpParameters addQuerystring(String queryString);

    /**
     * Add a map to this param list
     *
     * @param map the map to add
     * @return this
     */
    HttpParameters addMap(Map<String, ? extends Object> map);

    /**
     * Append the list to an URL
     *
     * @param url to append
     * @return the appended url
     */
    String asUrl(String url);

    /**
     * @return the parameter list converted in OAuth 1.0a base string (the string to sign)
     * @see <a href="http://oauth.net/core/1.0/#anchor14">the oauth spec</a> for more info on this.
     */
    String asOauthBaseString();

    /**
     * @return the parameter list converted in HTTP form encoded String
     */
    String asFormUrlEncodedString();

}
