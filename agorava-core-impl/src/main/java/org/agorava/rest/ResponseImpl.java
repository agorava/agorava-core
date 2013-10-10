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

package org.agorava.rest;

import org.agorava.api.exception.AgoravaException;
import org.agorava.api.rest.Request;
import org.agorava.api.rest.Response;
import org.agorava.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Represents an HTTP Response.
 *
 * @author Pablo Fernandez
 */
public class ResponseImpl implements Response {
    public static final String GZIP_CONTENT_ENCODING = "gzip";

    public static final String CONTENT_ENCODING = "Content-Encoding";

    private static final String EMPTY = "";

    private int code;

    private String body;

    private InputStream stream;

    private Map<String, String> headers;

    private URL url;

    private Request request;

    ResponseImpl(HttpURLConnection connection) {

        try {
            connection.connect();
            url = connection.getURL();
            code = connection.getResponseCode();
            headers = parseHeaders(connection);
            InputStream res = isSuccessful() ? connection.getInputStream() : connection.getErrorStream();
            if (GZIP_CONTENT_ENCODING.equals(headers.get(CONTENT_ENCODING)))
                try {
                    stream = new GZIPInputStream(res);
                } catch (IOException e) {
                    throw new AgoravaException("Unable to create GZIPInputStream", e);
                }
            else
                stream = res;
        } catch (IOException e) {
            throw new AgoravaException("The IP address of a host could not be determined.", e);
        }
    }

    ResponseImpl(RequestImpl request) throws IOException {
        this(request.getConnection());
        this.request = request;
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

    @Override
    public boolean isSuccessful() {
        return getCode() >= 200 && getCode() < 400;
    }

    @Override
    public String getBody() {
        return body != null ? body : parseBodyContents();
    }

    @Override
    public InputStream getStream() {
        return stream;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Request getRequest() {
        return request;
    }

}