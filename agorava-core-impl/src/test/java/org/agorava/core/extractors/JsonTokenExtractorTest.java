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

import org.agorava.core.api.oauth.Token;
import org.agorava.core.helpers.extractors.JsonTokenExtractor;
import org.junit.Assert;
import org.junit.Test;

public class JsonTokenExtractorTest {
    private String response = "'{ \"access_token\":\"I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X\"}'";

    private JsonTokenExtractor extractor = new JsonTokenExtractor();

    @Test
    public void shouldParseResponse() {
        Token token = extractor.extract(response);
        Assert.assertEquals(token.getToken(), "I0122HHJKLEM21F3WLPYHDKGKZULAUO4SGMV3ABKFTDT3T3X");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForNullParameters() {
        extractor.extract(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfForEmptyStrings() {
        extractor.extract("");
    }
}
