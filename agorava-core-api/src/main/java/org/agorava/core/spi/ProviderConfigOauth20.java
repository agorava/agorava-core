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

import org.agorava.core.api.atinject.OAuth;
import org.agorava.core.api.extractor.TokenExtractor;
import org.agorava.core.api.oauth.SignaturePlace;
import org.agorava.core.api.oauth.application.OAuthAppSettings;
import org.agorava.core.api.rest.Verb;

import javax.inject.Inject;

import static org.agorava.core.api.atinject.OAuth.OAuthVersion;
import static org.agorava.core.api.atinject.OAuth.OAuthVersion.TWO_DRAFT_11;

/**
 * Default Provider configuration implementing the OAuth protocol, version 2.0 (draft 11)
 * <p/>
 * This class is meant to be extended by concrete implementations of the API,
 * providing the endpoints and endpoint-http-verbs.
 * <p/>
 * If your Api adheres to the 2.0 (draft 11) protocol correctly, you just need to extend
 * this class and define the getters for your endpoints.
 * <p/>
 * If your Api does something a bit different, you can override the different
 * extractors or services, in order to fine-tune the process.
 *
 * @author Diego Silveira
 * @author Antoine Sabot-Durand
 */
public abstract class ProviderConfigOauth20 extends ProviderConfigOauth {


    @Inject
    @OAuth(TWO_DRAFT_11)
    TokenExtractor AccessTokenExtractor;

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenExtractor getAccessTokenExtractor() {
        return AccessTokenExtractor;
    }

    /**
     * Returns the verb for the access token endpoint (defaults to GET)
     *
     * @return access token endpoint verb
     */
    public Verb getAccessTokenVerb() {
        return Verb.GET;
    }

    /**
     * Returns the URL that receives the access token requests.
     *
     * @return access token URL
     */
    public abstract String getAccessTokenEndpoint();

    /**
     * Returns the URL where you should redirect your users to authenticate
     * your application.
     *
     * @param config OAuth 2.0 configuration param object
     * @return the URL where you should redirect your users
     */
    public abstract String getAuthorizationUrl(OAuthAppSettings config);

    /**
     * {@inheritDoc}
     */
    @Override
    public SignaturePlace getSignatureType() {
        return SignaturePlace.HEADER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OAuthVersion getOAuthVersion() {
        return TWO_DRAFT_11;
    }

}
