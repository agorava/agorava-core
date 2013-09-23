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

package org.agorava.core.api.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Pablo Fernandez
 */
public class OAuthEncoderTest {
    @Test
    public void shouldPercentEncodeString() {
        String plain = "this is a test &^";
        String encoded = "this%20is%20a%20test%20%26%5E";
        Assert.assertEquals(encoded, OAuthEncoder.encode(plain));
    }

    @Test
    public void shouldFormURLDecodeString() {
        String encoded = "this+is+a+test+%26%5E";
        String plain = "this is a test &^";
        Assert.assertEquals(plain, OAuthEncoder.decode(encoded));
    }

    @Test
    public void shouldPercentEncodeAllSpecialCharacters() {
        String plain = "!*'();:@&=+$,/?#[]";
        String encoded = "%21%2A%27%28%29%3B%3A%40%26%3D%2B%24%2C%2F%3F%23%5B%5D";
        Assert.assertEquals(encoded, OAuthEncoder.encode(plain));
        Assert.assertEquals(plain, OAuthEncoder.decode(encoded));
    }

    @Test
    public void shouldNotPercentEncodeReservedCharacters() {
        String plain = "abcde123456-._~";
        String encoded = plain;
        Assert.assertEquals(encoded, OAuthEncoder.encode(plain));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfStringToEncodeIsNull() {
        String toEncode = null;
        OAuthEncoder.encode(toEncode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfStringToDecodeIsNull() {
        String toDecode = null;
        OAuthEncoder.decode(toDecode);
    }

    @Test
    public void shouldPercentEncodeCorrectlyTwitterCodingExamples() {
        // These tests are part of the Twitter dev examples here -> https://dev.twitter
        // .com/docs/auth/percent-encoding-parameters
        String sources[] = {"Ladies + Gentlemen", "An encoded string!", "Dogs, Cats & Mice"};
        String encoded[] = {"Ladies%20%2B%20Gentlemen", "An%20encoded%20string%21", "Dogs%2C%20Cats%20%26%20Mice"};

        for (int i = 0; i < sources.length; i++) {
            Assert.assertEquals(encoded[i], OAuthEncoder.encode(sources[i]));
        }
    }
}
