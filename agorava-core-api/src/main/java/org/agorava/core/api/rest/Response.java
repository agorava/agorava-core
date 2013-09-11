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

import java.io.InputStream;
import java.util.Map;

/**
 * @author Antoine Sabot-Durand
 */
public interface Response {
    boolean isSuccessful();

    /**
     * Obtains the HTTP Response body
     *
     * @return response body
     */
    String getBody();

    /**
     * Obtains the meaningful stream of the HttpUrlConnection, either inputStream
     * or errorInputStream, depending on the status code
     *
     * @return input stream / error stream
     */
    InputStream getStream();

    /**
     * Obtains the HTTP status code
     *
     * @return the status code
     */
    int getCode();

    /**
     * Obtains a {@link java.util.Map} containing the HTTP Response Headers
     *
     * @return headers
     */
    Map<String, String> getHeaders();

    /**
     * Obtains a single HTTP Header value, or null if undefined
     *
     * @param name the header name.
     * @return header value or null.
     */
    String getHeader(String name);


    String getUrl();
}
