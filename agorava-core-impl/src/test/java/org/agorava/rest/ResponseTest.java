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

import org.agorava.api.rest.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class ResponseTest {

    private Response response;

    private ConnectionStub connection;

    @Before
    public void setup() throws Exception {
        connection = new ConnectionStub();
        connection.addResponseHeader("one", "one");
        connection.addResponseHeader("two", "two");
        response = new ResponseImpl(connection);
    }

    @Test
    public void shouldPopulateResponseHeaders() {
        assertEquals(2, response.getHeaders().size());
        assertEquals("one", response.getHeader("one"));
    }

    @Test
    public void shouldParseBodyContents() {
        assertEquals("contents", response.getBody());
        assertEquals(1, connection.getTimesCalledInpuStream());
    }

    @Test
    public void shouldParseBodyContentsOnlyOnce() {
        assertEquals("contents", response.getBody());
        assertEquals("contents", response.getBody());
        assertEquals("contents", response.getBody());
        assertEquals(1, connection.getTimesCalledInpuStream());
    }

    @Test
    public void shouldHandleAConnectionWithErrors() throws Exception {
        Response errResponse = new ResponseImpl(new FaultyConnection());
        assertEquals(400, errResponse.getCode());
        assertEquals("errors", errResponse.getBody());
    }

    private static class FaultyConnection extends ConnectionStub {

        public FaultyConnection() throws Exception {
            super();
        }

        @Override
        public InputStream getErrorStream() {
            return new ByteArrayInputStream("errors".getBytes());
        }

        @Override
        public int getResponseCode() throws IOException {
            return 400;
        }
    }
}
