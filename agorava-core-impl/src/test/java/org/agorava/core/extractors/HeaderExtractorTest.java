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

import org.agorava.api.exception.OAuthParametersMissingException;
import org.agorava.api.oauth.OAuthRequest;
import org.agorava.api.rest.Verb;
import org.agorava.core.mock.ObjectMother;
import org.agorava.oauth.helpers.extractors.HeaderExtractor;
import org.agorava.rest.OAuthRequestImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HeaderExtractorTest {

    private HeaderExtractor extractor;

    private OAuthRequest request;

    @Before
    public void setup() {
        request = ObjectMother.createSampleOAuthRequest();
        extractor = new HeaderExtractor();
    }

    @Test
    public void shouldExtractStandardHeader() {
        String[] expected = {"OAuth", "oauth_callback=\"http%3A%2F%2Fexample%2Fcallback\"",
                "oauth_signature=\"OAuth-Signature\"",
                "oauth_consumer_key=\"AS%23%24%5E%2A%40%26\"", "oauth_timestamp=\"123456\""};
        String header = extractor.extract(request);
        for (String s : expected) {
            Assert.assertTrue(header.contains(s));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldExceptionIfRequestIsNull() {
        OAuthRequest nullRequest = null;
        extractor.extract(nullRequest);
    }

    @Test(expected = OAuthParametersMissingException.class)
    public void shouldExceptionIfRequestHasNoOAuthParams() {
        OAuthRequest emptyRequest = new OAuthRequestImpl(Verb.GET, "http://example.com");
        extractor.extract(emptyRequest);
    }
}
