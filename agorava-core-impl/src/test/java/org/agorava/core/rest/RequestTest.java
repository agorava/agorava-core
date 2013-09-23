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

package org.agorava.core.rest;

import org.agorava.core.api.rest.Request;
import org.agorava.core.api.rest.Verb;
import org.agorava.core.rest.HttpParametersImpl.Parameter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RequestTest {
    private static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";


    private RequestImpl getRequest;

    private RequestImpl postRequest;

    private ConnectionStub connection;


    @Before
    public void setup() throws Exception {
        connection = new ConnectionStub();
        postRequest = new RequestImpl(Verb.POST, "http://example.com");
        postRequest.addBodyParameter("param", "value");
        postRequest.addBodyParameter("param with spaces", "value with spaces");
        postRequest.setConnection(connection);
        getRequest = new RequestImpl(Verb.GET, "http://example.com?qsparam=value&other+param=value+with+spaces");
        getRequest.setConnection(connection);
    }

    @Test
    public void shouldSetRequestVerb() {
        getRequest.send();
        assertEquals("GET", connection.getRequestMethod());
    }

    @Test
    public void shouldGetQueryStringParameters() {
        Assert.assertEquals(2, getRequest.getQueryStringParams().size());
        assertEquals(0, postRequest.getQueryStringParams().size());
        assertTrue(getRequest.getQueryStringParams().getParams().contains(new Parameter("qsparam", "value")));
    }

    @Test
    public void shouldAddRequestHeaders() {
        getRequest.addHeader("Header", "1");
        getRequest.addHeader("Header2", "2");
        getRequest.send();
        assertEquals(2, getRequest.getHeaders().size());
        assertEquals(2, connection.getHeaders().size());
    }

    @Test
    public void shouldSetBodyParamsAndAddContentLength() {
        assertEquals("param=value&param%20with%20spaces=value%20with%20spaces", postRequest.getBodyContents());
        postRequest.send();
        assertTrue(connection.getHeaders().containsKey("Content-Length"));
    }

    @Test
    public void shouldSetPayloadAndHeaders() {
        postRequest.addPayload("PAYLOAD");
        postRequest.send();
        assertEquals("PAYLOAD", postRequest.getBodyContents());
        assertTrue(connection.getHeaders().containsKey("Content-Length"));
    }

    @Test
    public void shouldAllowAddingQuerystringParametersAfterCreation() {
        Request request = new RequestImpl(Verb.GET, "http://example.com?one=val");
        request.addQuerystringParameter("two", "other val");
        request.addQuerystringParameter("more", "params");
        assertEquals(3, request.getQueryStringParams().size());
    }

    @Test
    public void shouldReturnTheCompleteUrl() {
        Request request = new RequestImpl(Verb.GET, "http://example.com?one=val");
        request.addQuerystringParameter("two", "other val");
        request.addQuerystringParameter("more", "params");
        assertEquals("http://example.com?one=val&two=other%20val&more=params", request.getCompleteUrl());
    }

    @Test
    public void shouldHandleQueryStringSpaceEncodingProperly() {
        assertTrue(getRequest.getQueryStringParams().getParams().contains(new Parameter("other param", "value with spaces")));
    }

    @Test
    public void shouldAutomaticallyAddContentTypeForPostRequestsWithBytePayload() {
        postRequest.addPayload("PAYLOAD".getBytes());
        postRequest.send();
        Assert.assertEquals(DEFAULT_CONTENT_TYPE, connection.getHeaders().get("Content-Type"));
    }

    @Test
    public void shouldAutomaticallyAddContentTypeForPostRequestsWithStringPayload() {
        postRequest.addPayload("PAYLOAD");
        postRequest.send();
        assertEquals(DEFAULT_CONTENT_TYPE, connection.getHeaders().get("Content-Type"));
    }

    @Test
    public void shouldAutomaticallyAddContentTypeForPostRequestsWithBodyParameters() {
        postRequest.send();
        assertEquals(DEFAULT_CONTENT_TYPE, connection.getHeaders().get("Content-Type"));
    }

    @Test
    public void shouldBeAbleToOverrideItsContentType() {
        postRequest.addHeader("Content-Type", "my-content-type");
        postRequest.send();
        assertEquals("my-content-type", connection.getHeaders().get("Content-Type"));
    }

    @Test
    public void shouldNotAddContentTypeForGetRequests() {
        getRequest.send();
        assertFalse(connection.getHeaders().containsKey("Content-Type"));
    }
}