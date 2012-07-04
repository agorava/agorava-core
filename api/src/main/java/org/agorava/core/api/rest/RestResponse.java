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

package org.agorava.core.api.rest;

import java.io.InputStream;
import java.util.Map;

/**
 * A Response returned from a {@link RestRequest}
 *
 * @author Antoine Sabot-Durand
 */
public interface RestResponse {

    /**
     * @return the body of the response in a {@link String}
     */
    public String getBody();

    /**
     * @return the body of the response in a {@link InputStream}
     */
    public InputStream getStream();

    /**
     * @return the HTTP return code of the response
     */
    public int getCode();

    /**
     * @return the HTTP Response headers in {@link Map}
     */
    public Map<String, String> getHeaders();

    /**
     * @param name of the HTTP header
     * @return the value of the HTTP header
     */
    public String getHeader(String name);

    /**
     * @return The url which this object is a response
     */
    public String getUrl();

}
