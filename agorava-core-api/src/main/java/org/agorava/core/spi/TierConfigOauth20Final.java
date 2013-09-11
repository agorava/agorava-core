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

package org.agorava.core.spi;

import org.agorava.core.api.OAuth;
import org.agorava.core.api.OAuthVersion;
import org.agorava.core.api.extractors.TokenExtractor;
import org.agorava.core.api.rest.Verb;

import javax.inject.Inject;

import static org.agorava.core.api.OAuthVersion.TWO_FINAL;

/**
 * @author Antoine Sabot-Durand
 */
public abstract class TierConfigOauth20Final extends TierConfigOauth20 {

    @Inject
    @OAuth(TWO_FINAL)
    TokenExtractor AccessTokenExtractor;

    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    @Override
    public TokenExtractor getAccessTokenExtractor() {
        return AccessTokenExtractor;
    }

    @Override
    public OAuthVersion getOAuthVersion() {
        return TWO_FINAL;
    }
}
