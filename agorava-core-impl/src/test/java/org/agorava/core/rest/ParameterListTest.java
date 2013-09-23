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

import org.agorava.core.api.rest.HttpParameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author: Pablo Fernandez
 */
public class ParameterListTest {
    private HttpParameters params;

    @Before
    public void setup() {
        this.params = new HttpParametersImpl();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenAppendingNullMapToQuerystring() {
        String url = null;
        params.asUrl(url);
    }

    @Test
    public void shouldAppendNothingToQuerystringIfGivenEmptyMap() {
        String url = "http://www.example.com";
        Assert.assertEquals(url, params.asUrl(url));
    }

    @Test
    public void shouldAppendParametersToSimpleUrl() {
        String url = "http://www.example.com";
        String expectedUrl = "http://www.example.com?param1=value1&param2=value%20with%20spaces";

        params.add("param1", "value1");
        params.add("param2", "value with spaces");

        url = params.asUrl(url);
        Assert.assertEquals(url, expectedUrl);
    }

    @Test
    public void shouldAppendParametersToUrlWithQuerystring() {
        String url = "http://www.example.com?already=present";
        String expectedUrl = "http://www.example.com?already=present&param1=value1&param2=value%20with%20spaces";

        params.add("param1", "value1");
        params.add("param2", "value with spaces");

        url = params.asUrl(url);
        Assert.assertEquals(url, expectedUrl);
    }

}
