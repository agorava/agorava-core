/*
 * Copyright 2013-2020 Agorava
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

package org.agorava.oauth.helpers.extractors;

import org.agorava.api.exception.AgoravaException;
import org.agorava.api.extractor.TokenExtractor;
import org.agorava.api.oauth.OAuth;
import org.agorava.api.oauth.Token;
import org.agorava.api.service.OAuthEncoder;
import org.agorava.api.service.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.agorava.api.oauth.OAuth.OAuthVersion.TWO_DRAFT_11;

/**
 * Default implementation of {@code TokenExtractor}. Conforms to OAuth 2.0
 * @see TokenExtractor
 * @author Werner Keil
 */
@OAuth(TWO_DRAFT_11)
public class TokenExtractor20 implements TokenExtractor {
    private static final String TOKEN_REGEX = "access_token=([^&]+)";

    private static final String EMPTY_SECRET = "";

    /**
     * {@inheritDoc}
     */
    @Override
    public Token extract(String response) {
        Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");

        Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
        if (matcher.find()) {
            String token = OAuthEncoder.decode(matcher.group(1));
            return new Token(token, EMPTY_SECRET);
        } else {
            throw new AgoravaException("Response body is incorrect. Can't extract a token from this: '" + response + "'", null);
        }
    }
}
