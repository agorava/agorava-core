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

package org.agorava.oauth;


import org.agorava.api.atinject.GenericBean;
import org.agorava.api.atinject.InjectWithQualifier;
import org.agorava.api.oauth.OAuth;
import org.agorava.api.oauth.OAuthConstants;
import org.agorava.api.oauth.OAuthRequest;
import org.agorava.api.oauth.Token;
import org.agorava.api.oauth.Verifier;
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.rest.Response;
import org.agorava.rest.OAuthRequestImpl;
import org.agorava.spi.ProviderConfigOauth20;

import static org.agorava.api.oauth.OAuth.OAuthVersion.TWO_DRAFT_11;

@GenericBean
@OAuth(TWO_DRAFT_11)
public class OAuth20ServiceImpl extends OAuthServiceBase {

    @InjectWithQualifier
    ProviderConfigOauth20 api;

    @InjectWithQualifier
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
        Response response = request.send(); //todo:should check return code and launch ResponseException if it's not 200
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    public Token getRequestToken() {
        throw new UnsupportedOperationException("Unsupported operation, please use 'startDance' and redirect your " +
                "users there");
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


    @Override
    public String getVerifierParamName() {
        return OAuthConstants.CODE;
    }

    @Override
    public String getAuthorizationUrl() {
        return api.getAuthorizationUrl(config);
    }

}
