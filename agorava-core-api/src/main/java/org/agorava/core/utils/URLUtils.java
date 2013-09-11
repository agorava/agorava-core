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

package org.agorava.core.utils;

import java.util.Map;

/**
 * @author Antoine Sabot-Durand
 */
public interface URLUtils {
    /**
     * Append given parameters to the query string of the url
     *
     * @param url        the url to append parameters to
     * @param parameters any map
     * @return new url with parameters on query string
     */
    String buildUri(String url, Map<String, ?> parameters);

    String buildUri(String url, Object pojo);

    /**
     * Append given parameter to the query string of the url
     *
     * @param url   the url to append parameters to
     * @param key   name of the parameter ro add
     * @param value value of the parameter to add
     * @return new url with parameters on query string
     */
    String buildUri(String url, String key, String value);
}
