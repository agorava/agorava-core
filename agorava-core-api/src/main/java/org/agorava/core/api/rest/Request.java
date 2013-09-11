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
import java.util.concurrent.TimeUnit;

/**
 * @author Antoine Sabot-Durand
 */
public interface Request {
    String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

    /**
     * Execute the request and return a {@link org.agorava.core.api.model.ResponseImpl}
     *
     * @return Http Response
     * @throws RuntimeException if the connection cannot be created.
     */
    Response send(RequestTuner tuner);

    Response send();

    /**
     * Returns the complete url (host + resource + encoded querystring parameters).
     *
     * @return the complete url.
     */
    String getCompleteUrl();

    /**
     * Add an HTTP Header to the Request
     *
     * @param key   the header name
     * @param value the header value
     */
    void addHeader(String key, String value);

    /**
     * Add a body Parameter (for POST/ PUT Requests)
     *
     * @param key   the parameter name
     * @param value the parameter value
     */
    void addBodyParameter(String key, String value);

    /**
     * Add a QueryString parameter
     *
     * @param key   the parameter name
     * @param value the parameter value
     */
    void addQuerystringParameter(String key, String value);

    /**
     * Add body payload.
     * <p/>
     * This method is used when the HTTP body is not a form-url-encoded string,
     * but another thing. Like for example XML.
     * <p/>
     * Note: The contents are not part of the OAuth signature
     *
     * @param payload the body of the request
     */
    void addPayload(String payload);

    /**
     * Overloaded version for byte arrays
     *
     * @param payload
     */
    void addPayload(byte[] payload);

    /**
     * Get a {@link org.agorava.core.api.model.ParameterListImpl} with the query string parameters.
     *
     * @return a {@link org.agorava.core.api.model.ParameterListImpl} containing the query string parameters.
     * @throws org.agorava.core.api.exceptions.OAuthException
     *          if the request URL is not valid.
     */
    ParameterList getQueryStringParams();

    /**
     * Obtains a {@link org.agorava.core.api.model.ParameterListImpl} of the body parameters.
     *
     * @return a {@link org.agorava.core.api.model.ParameterListImpl}containing the body parameters.
     */
    ParameterList getBodyParams();

    /**
     * Obtains the URL of the HTTP Request.
     *
     * @return the original URL of the HTTP Request
     */
    String getUrl();

    /**
     * Returns the URL without the port and the query string part.
     *
     * @return the OAuth-sanitized URL
     */
    String getSanitizedUrl();

    /**
     * Returns the body of the request
     *
     * @return form encoded string
     * @throws org.agorava.core.api.exceptions.OAuthException
     *          if the charset chosen is not supported
     */
    String getBodyContents();

    /**
     * Returns the HTTP Verb
     *
     * @return the verb
     */
    Verb getVerb();

    /**
     * Returns the connection headers as a {@link java.util.Map}
     *
     * @return map of headers
     */
    Map<String, String> getHeaders();

    /**
     * Returns the connection charset. Defaults to {@link java.nio.charset.Charset} defaultCharset if not set
     *
     * @return charset
     */
    String getCharset();

    /**
     * Set the charset of the body of the request
     *
     * @param charsetName name of the charset of the request
     */
    void setCharset(String charsetName);

    /**
     * Sets the connect timeout for the underlying {@link java.net.HttpURLConnection}
     *
     * @param duration duration of the timeout
     * @param unit     unit of time (milliseconds, seconds, etc)
     */
    void setConnectTimeout(int duration, TimeUnit unit);

    /**
     * Sets the read timeout for the underlying {@link java.net.HttpURLConnection}
     *
     * @param duration duration of the timeout
     * @param unit     unit of time (milliseconds, seconds, etc)
     */
    void setReadTimeout(int duration, TimeUnit unit);

    /**
     * Sets whether the underlying Http Connection is persistent or not.
     *
     * @param connectionKeepAlive
     * @see http://download.oracle.com/javase/1.5.0/docs/guide/net/http-keepalive.html
     */
    void setConnectionKeepAlive(boolean connectionKeepAlive);

    void addBodyParameters(Map<String, ? extends Object> params);
}
