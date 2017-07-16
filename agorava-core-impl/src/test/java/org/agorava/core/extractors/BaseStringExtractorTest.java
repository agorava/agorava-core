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
import org.agorava.oauth.helpers.extractors.BaseStringExtractor;
import org.agorava.rest.OAuthRequestImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseStringExtractorTest {

    private BaseStringExtractor extractor;

    private OAuthRequest request;

    @Before
    public void setup() {
        request = ObjectMother.createSampleOAuthRequest();
        extractor = new BaseStringExtractor();
    }

    @Test
    public void shouldExtractBaseStringFromOAuthRequest() {
        String expected = "GET&http%3A%2F%2Fexample" +
                ".com&oauth_callback%3Dhttp%253A%252F%252Fexample%252Fcallback%26oauth_consumer_key%3DAS%2523%2524%255E%252A" +
                "%2540%2526%26oauth_signature%3DOAuth-Signature%26oauth_timestamp%3D123456";
        String baseString = extractor.extract(request);
        assertEquals(expected, baseString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfRquestIsNull() {
        OAuthRequest nullRequest = null;
        extractor.extract(nullRequest);
    }

    @Test(expected = OAuthParametersMissingException.class)
    public void shouldThrowExceptionIfRquestHasNoOAuthParameters() {
        OAuthRequest request = new OAuthRequestImpl(Verb.GET, "http://example.com");
        extractor.extract(request);
    }

    @Test
    public void shouldProperlyEncodeSpaces() {
        String expected = "GET&http%3A%2F%2Fexample" +
                ".com&body%3Dthis%2520param%2520has%2520whitespace%26oauth_callback%3Dhttp%253A%252F%252Fexample%252Fcallback" +
                "%26oauth_consumer_key%3DAS%2523%2524%255E%252A%2540%2526%26oauth_signature%3DOAuth-Signature" +
                "%26oauth_timestamp%3D123456";
        request.addBodyParameter("body", "this param has whitespace");
        assertEquals(expected, extractor.extract(request));
    }
}
