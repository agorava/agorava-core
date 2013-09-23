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

import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.extractor.TokenExtractor;
import org.agorava.core.api.oauth.Token;
import org.agorava.core.helpers.extractors.TokenExtractor10;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TokenExtractorTest {

    private TokenExtractor extractor;

    @Before
    public void setup() {
        extractor = new TokenExtractor10();
    }

    @Test
    public void shouldExtractTokenFromOAuthStandardResponse() {
        String response = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=hdhd0244k9j7ao03";
        Token extracted = extractor.extract(response);
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("hdhd0244k9j7ao03", extracted.getSecret());
    }

    @Test
    public void shouldExtractTokenFromInvertedOAuthStandardResponse() {
        String response = "oauth_token_secret=hh5s93j4hdidpola&oauth_token=hdhd0244k9j7ao03";
        Token extracted = extractor.extract(response);
        assertEquals("hh5s93j4hdidpola", extracted.getSecret());
        assertEquals("hdhd0244k9j7ao03", extracted.getToken());
    }

    @Test
    public void shouldExtractTokenFromResponseWithCallbackConfirmed() {
        String response = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=hdhd0244k9j7ao03&callback_confirmed=true";
        Token extracted = extractor.extract(response);
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("hdhd0244k9j7ao03", extracted.getSecret());
    }

    @Test
    public void shouldExtractTokenWithEmptySecret() {
        String response = "oauth_token=hh5s93j4hdidpola&oauth_token_secret=";
        Token extracted = extractor.extract(response);
        assertEquals("hh5s93j4hdidpola", extracted.getToken());
        assertEquals("", extracted.getSecret());
    }

    @Test(expected = AgoravaException.class)
    public void shouldThrowExceptionIfTokenIsAbsent() {
        String response = "oauth_secret=hh5s93j4hdidpola&callback_confirmed=true";
        extractor.extract(response);
    }

    @Test(expected = AgoravaException.class)
    public void shouldThrowExceptionIfSecretIsAbsent() {
        String response = "oauth_token=hh5s93j4hdidpola&callback_confirmed=true";
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
