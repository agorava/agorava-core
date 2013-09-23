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

import org.agorava.core.api.oauth.Token;
import org.junit.Test;

import static junit.framework.Assert.assertNotSame;
import static org.junit.Assert.assertEquals;

public class TokenTest {
    @Test
    public void shouldTestEqualityBasedOnTokenAndSecret() throws Exception {
        Token expected = new Token("access", "secret");
        Token actual = new Token("access", "secret");

        assertEquals(expected, actual);
        assertEquals(actual, actual);
    }

    @Test
    public void shouldNotDependOnRawString() throws Exception {
        Token expected = new Token("access", "secret");
        Token actual = new Token("access", "secret");

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnSameHashCodeForEqualObjects() throws Exception {
        Token expected = new Token("access", "secret");
        Token actual = new Token("access", "secret");

        assertEquals(expected.hashCode(), actual.hashCode());
    }

    @Test
    public void shouldNotBeEqualToNullOrOtherObjects() throws Exception {
        Token expected = new Token("access", "secret");

        assertNotSame(expected, null);
        assertNotSame(expected, new Object());
    }
}
