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

import org.agorava.AgoravaConstants;
import org.agorava.api.oauth.OAuthRequest;
import org.agorava.api.rest.Verb;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OAuthRequestTest {

    private OAuthRequest request;

    @Before
    public void setup() {
        request = new OAuthRequestImpl(Verb.GET, "http://example.com");
    }

    @Test
    public void shouldAddOAuthParamters() {
        request.addOAuthParameter(AgoravaConstants.TOKEN, "token");
        request.addOAuthParameter(AgoravaConstants.NONCE, "nonce");
        request.addOAuthParameter(AgoravaConstants.TIMESTAMP, "ts");
        request.addOAuthParameter(AgoravaConstants.SCOPE, "feeds");

        assertEquals(4, request.getOauthParameters().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfParameterIsNotOAuth() {
        request.addOAuthParameter("otherParam", "value");
    }
}
