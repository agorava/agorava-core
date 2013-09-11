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

package org.agorava.core.oauth;


import org.agorava.core.api.ApplyQualifier;
import org.agorava.core.api.GenericRoot;
import org.agorava.core.api.OAuth;
import org.agorava.core.api.oauth.OAuthAppSettings;
import org.agorava.core.api.oauth.OAuthConstants;
import org.agorava.core.api.oauth.OAuthRequest;
import org.agorava.core.api.oauth.Token;
import org.agorava.core.api.oauth.Verifier;
import org.agorava.core.api.rest.Response;
import org.agorava.core.rest.OAuthRequestImpl;
import org.agorava.core.spi.TierConfigOauth20;

import javax.inject.Inject;

import static org.agorava.core.api.OAuthVersion.TWO_DRAFT_11;

@GenericRoot
@OAuth(TWO_DRAFT_11)
public class OAuth20ProviderImpl extends OAuthProviderBase {

    @Inject
    @ApplyQualifier
    TierConfigOauth20 api;

    @Inject
    @ApplyQualifier
    OAuthAppSettings config;


    /**
     * {@inheritDoc}
     */
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequestImpl(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
        request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        if (config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
        Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    public Token getRequestToken() {
        throw new UnsupportedOperationException("Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
    }

    /**
     * {@inheritDoc}
     */
    public String getVersion() {
        return TWO_DRAFT_11.getLabel();
    }

    /**
     * {@inheritDoc}
     */
    public void signRequest(Token accessToken, OAuthRequest request) {
        request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
    }

    /**
     * {@inheritDoc}
     */
    public String getAuthorizationUrl(Token requestToken) {
        return api.getAuthorizationUrl(config);
    }

    @Override
    public String getVerifierParamName() {
        return OAuthConstants.CODE;
    }

}
