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

import org.scribe.exceptions.OAuthException;
import org.scribe.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents an HTTP Response.
 *
 * @author Pablo Fernandez
 */
public class Response {
    private static final String EMPTY = "";

    private int code;
    private String body;
    private InputStream stream;
    private Map<String, String> headers;

    Response(HttpURLConnection connection) throws IOException {
        try {
            connection.connect();
            code = connection.getResponseCode();
            headers = parseHeaders(connection);
            stream = isSuccessful() ? connection.getInputStream() : connection.getErrorStream();
        } catch (UnknownHostException e) {
            throw new OAuthException("The IP address of a host could not be determined.", e);
        }
    }

    private String parseBodyContents() {
        body = StreamUtils.getStreamContents(getStream());
        return body;
    }

    private Map<String, String> parseHeaders(HttpURLConnection conn) {
        Map<String, String> headers = new HashMap<String, String>();
        for (String key : conn.getHeaderFields().keySet()) {
            headers.put(key, conn.getHeaderFields().get(key).get(0));
        }
        return headers;
    }

    public boolean isSuccessful() {
        return getCode() >= 200 && getCode() < 400;
    }

    /**
     * Obtains the HTTP Response body
     *
     * @return response body
     */
    public String getBody() {
        return body != null ? body : parseBodyContents();
    }

    /**
     * Obtains the meaningful stream of the HttpUrlConnection, either inputStream
     * or errorInputStream, depending on the status code
     *
     * @return input stream / error stream
     */
    public InputStream getStream() {
        return stream;
    }

    /**
     * Obtains the HTTP status code
     *
     * @return the status code
     */
    public int getCode() {
        return code;
    }

    /**
     * Obtains a {@link java.util.Map} containing the HTTP Response Headers
     *
     * @return headers
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Obtains a single HTTP Header value, or null if undefined
     *
     * @param name the header name.
     * @return header value or null.
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

}