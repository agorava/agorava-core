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

package org.agorava.oauth.helpers;

import org.agorava.api.exception.AgoravaException;
import org.agorava.oauth.helpers.signatures.HMACSha1SignatureService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HMACSha1SignatureServiceTest {

    private HMACSha1SignatureService service;

    @Before
    public void setup() {
        service = new HMACSha1SignatureService();
    }

    @Test
    public void shouldReturnSignatureMethodString() {
        String expected = "HMAC-SHA1";
        assertEquals(expected, service.getSignatureMethod());
    }

    @Test
    public void shouldReturnSignature() {
        String apiSecret = "api secret";
        String tokenSecret = "token secret";
        String baseString = "base string";
        String signature = "uGymw2KHOTWI699YEaoi5xyLT50=";
        assertEquals(signature, service.getSignature(baseString, apiSecret, tokenSecret));
    }

    @Test(expected = AgoravaException.class)
    public void shouldThrowExceptionIfBaseStringIsNull() {
        service.getSignature(null, "apiSecret", "tokenSecret");
    }

    @Test(expected = AgoravaException.class)
    public void shouldThrowExceptionIfBaseStringIsEmpty() {
        service.getSignature("  ", "apiSecret", "tokenSecret");
    }

    @Test(expected = AgoravaException.class)
    public void shouldThrowExceptionIfApiSecretIsNull() {
        service.getSignature("base string", null, "tokenSecret");
    }

    @Test(expected = AgoravaException.class)
    public void shouldThrowExceptionIfApiSecretIsEmpty() {
        service.getSignature("base string", "  ", "tokenSecret");
    }
}
