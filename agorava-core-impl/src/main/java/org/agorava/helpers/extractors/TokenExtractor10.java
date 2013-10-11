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

package org.agorava.helpers.extractors;

import org.agorava.api.exception.AgoravaException;
import org.agorava.api.extractor.ExtractorType;
import org.agorava.api.extractor.TokenExtractor;
import org.agorava.api.oauth.Token;
import org.agorava.api.service.OAuthEncoder;
import org.agorava.api.service.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.agorava.api.extractor.ExtractorType.Type.TOKEN_STD;

/**
 * Default implementation of {@RequestTokenExtractor} and {@AccessTokenExtractor}. Conforms to OAuth 1.0a
 * <p/>
 * The process for extracting access and request tokens is similar so this class can do both things.
 *
 * @author Pablo Fernandez
 */
@ExtractorType(TOKEN_STD)
public class TokenExtractor10 implements TokenExtractor {
    private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");

    private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]*)");

    /**
     * {@inheritDoc}
     */
    public Token extract(String response) {
        Preconditions.checkEmptyString(response, "Response body is incorrect. Can't extract a token from an empty string");
        String token = extract(response, TOKEN_REGEX);
        String secret = extract(response, SECRET_REGEX);
        return new Token(token, secret);
    }

    private String extract(String response, Pattern p) {
        Matcher matcher = p.matcher(response);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return OAuthEncoder.decode(matcher.group(1));
        } else {
            throw new AgoravaException("Response body is incorrect. Can't extract token and secret from this: '" + response +
                    "'", null);
        }
    }
}
