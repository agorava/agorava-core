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

import org.agorava.core.api.ExtractorKind;
import org.agorava.core.api.OAuth;
import org.agorava.core.api.exceptions.OAuthException;
import org.agorava.core.api.extractors.TokenExtractor;
import org.agorava.core.api.oauth.Token;
import org.agorava.core.api.utils.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.agorava.core.api.ExtractorKind.Kind.JSON;
import static org.agorava.core.api.OAuthVersion.ONE;

@OAuth(ONE)
@ExtractorKind(JSON)
public class JsonTokenExtractor implements TokenExtractor {
    private Pattern accessTokenPattern = Pattern.compile("\"access_token\":\\s*\"(\\S*?)\"");

    public Token extract(String response) {
        Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
        Matcher matcher = accessTokenPattern.matcher(response);
        if (matcher.find()) {
            return new Token(matcher.group(1), "", response);
        } else {
            throw new OAuthException("Cannot extract an acces token. Response was: " + response);
        }
    }

}