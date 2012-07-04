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

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * A Rest Request to be sent to a Social Media
 *
 * @author Antoine Sabot-Durand
 */
public interface RestRequest extends Serializable {

    /**
     * Execute the request and return a {@link RestResponse}
     *
     * @return Rest Response
     */
    public RestResponse send();

    /**
     * Add an HTTP Header to the Request
     *
     * @param key   the header name
     * @param value the header value
     */
    public void addHeader(String key, String value);

    /**
     * Add a body Parameter (for POST/ PUT Requests)
     *
     * @param key   the parameter name
     * @param value the parameter value
     */
    public void addBodyParameter(String key, String value);

    public void addBodyParameters(Map<String, ? extends Object> toAdd);

    /**
     * Add a QueryString parameter
     *
     * @param key   the parameter name
     * @param value the parameter value
     */
    public void addQuerystringParameter(String key, String value);

    /**
     * Add body payload.
     * <p/>
     * This method is used when the HTTP body is not a form-url-encoded string, but another thing. Like for example XML.
     *
     * @param payload the body of the request
     */
    public void addPayload(String payload);

    /**
     * Get a {@link RestParameterList} of the query string parameters.
     *
     * @return a RestParameterList containing the query string parameters
     */
    public RestParameterList getQueryStringParams();

    /**
     * Obtains a {@link RestParameterList} of the body parameters.
     *
     * @return a RestParameterList containing the body parameters.
     */
    public RestParameterList getBodyParams();

    /**
     * Obtains the URL of the HTTP Request.
     *
     * @return the original URL of the HTTP Request
     */
    public String getUrl();

    /**
     * Returns the URL without the port and the query string part.
     *
     * @return the sanitized URL
     */
    public String getSanitizedUrl();

    /**
     * @return the Body of the request
     */
    public String getBodyContents();

    /**
     * @return the REST verb
     */
    public RestVerb getVerb();

    /**
     * Returns the connection headers as a {@link Map}
     *
     * @return map of headers
     */
    public Map<String, String> getHeaders();

    /**
     * Sets the connect timeout for the underlying {@link HttpURLConnection}
     *
     * @param duration duration of the timeout
     * @param unit     unit of time (milliseconds, seconds, etc)
     */
    public void setConnectTimeout(int duration, TimeUnit unit);

    /**
     * Sets the read timeout for the underlying {@link HttpURLConnection}
     *
     * @param duration duration of the timeout
     * @param unit     unit of time (milliseconds, seconds, etc)
     */
    public void setReadTimeout(int duration, TimeUnit unit);

    /**
     * @return
     */
    String getCompleteUrl();

    /**
     * @param payload
     */
    void addPayload(byte[] payload);

    /**
     * @return
     */
    String getCharset();

    /**
     * @param charsetName
     */
    void setCharset(String charsetName);

    /**
     * @param connectionKeepAlive
     */
    void setConnectionKeepAlive(boolean connectionKeepAlive);

}
