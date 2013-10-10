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

package org.agorava.api.util;

import org.junit.Test;

public class PreconditionsTest {

    private static final String ERROR_MSG = "";

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullObjects() {
        Preconditions.checkNotNull(null, ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullStrings() {
        Preconditions.checkEmptyString(null, ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForEmptyStrings() {
        Preconditions.checkEmptyString("", ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForSpacesOnlyStrings() {
        Preconditions.checkEmptyString("               ", ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForInvalidUrls() {
        Preconditions.checkValidUrl("this/is/not/a/valid/url", ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullUrls() {
        Preconditions.checkValidUrl(null, ERROR_MSG);
    }

    @Test
    public void shouldAllowValidUrls() {
        Preconditions.checkValidUrl("http://www.example.com", ERROR_MSG);
    }

    @Test
    public void shouldAllowSSLUrls() {
        Preconditions.checkValidUrl("https://www.example.com", ERROR_MSG);
    }

    @Test
    public void shouldAllowSpecialCharsInScheme() {
        Preconditions.checkValidUrl("custom+9.3-1://www.example.com", ERROR_MSG);
    }

    @Test
    public void shouldAllowNonStandarProtocolsForAndroid() {
        Preconditions.checkValidUrl("x-url-custom://www.example.com", ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowStrangeProtocolNames() {
        Preconditions.checkValidUrl("$weird*://www.example.com", ERROR_MSG);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAllowUnderscoreInScheme() {
        Preconditions.checkValidUrl("http_custom://www.example.com", ERROR_MSG);
    }

    @Test
    public void shouldAllowOutOfBandAsValidCallbackValue() {
        Preconditions.checkValidOAuthCallback("oob", ERROR_MSG);
    }
}
