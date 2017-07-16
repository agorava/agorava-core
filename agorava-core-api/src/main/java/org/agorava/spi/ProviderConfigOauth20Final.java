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

package org.agorava.spi;

import org.agorava.api.extractor.TokenExtractor;
import org.agorava.api.oauth.OAuth;
import org.agorava.api.rest.Verb;

import javax.inject.Inject;

import static org.agorava.api.oauth.OAuth.OAuthVersion;
import static org.agorava.api.oauth.OAuth.OAuthVersion.TWO_FINAL;

/**
 * Default Provider configuration implementing the OAuth protocol, version 2.0 final
 * <p/>
 * This class is meant to be extended by concrete implementations of the API,
 * providing the endpoints and endpoint-http-verbs.
 * <p/>
 * If your Api adheres to the 2.0 final protocol correctly, you just need to extend
 * this class and define the getters for your endpoints.
 * <p/>
 * If your Api does something a bit different, you can override the different
 * extractors or services, in order to fine-tune the process.
 *
 * @author Antoine Sabot-Durand
 */
public abstract class ProviderConfigOauth20Final extends ProviderConfigOauth20 {

    @Inject
    @OAuth(TWO_FINAL)
    TokenExtractor AccessTokenExtractor;

    /**
     * {@inheritDoc}
     */
    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenExtractor getAccessTokenExtractor() {
        return AccessTokenExtractor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthVersion getOAuthVersion() {
        return TWO_FINAL;
    }
}
