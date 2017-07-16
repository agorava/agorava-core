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

package org.agorava.core.extractors;

import org.agorava.api.exception.AgoravaException;
import org.agorava.api.oauth.Token;
import org.agorava.oauth.helpers.extractors.TokenExtractor20;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenExtractor20Test {

    private TokenExtractor20 extractor;

    @Before
    public void setup() {
        extractor = new TokenExtractor20();
    }

    @Test
    public void shouldExtractTokenFromOAuthStandardResponse() {
        String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600" +
                ".1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE";
        Token extracted = extractor.extract(response);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getToken());
        assertEquals("", extracted.getSecret());
    }

    @Test
    public void shouldExtractTokenFromResponseWithExpiresParam() {
        String response = "access_token=166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600" +
                ".1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE&expires=5108";
        Token extracted = extractor.extract(response);
        assertEquals("166942940015970|2.2ltzWXYNDjCtg5ZDVVJJeg__.3600.1295816400-548517159|RsXNdKrpxg8L6QNLWcs2TVTmcaE",
                extracted.getToken());
        assertEquals("", extracted.getSecret());
    }

    @Test
    public void shouldExtractTokenFromResponseWithManyParameters() {
        String response = "access_token=foo1234&other_stuff=yeah_we_have_this_too&number=42";
        Token extracted = extractor.extract(response);
        assertEquals("foo1234", extracted.getToken());
        assertEquals("", extracted.getSecret());
    }

    @Test(expected = AgoravaException.class)
    public void shouldThrowExceptionIfTokenIsAbsent() {
        String response = "&expires=5108";
        extractor.extract(response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfResponseIsNull() {
        String response = null;
        extractor.extract(response);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfResponseIsEmptyString() {
        String response = "";
        extractor.extract(response);
    }
}
