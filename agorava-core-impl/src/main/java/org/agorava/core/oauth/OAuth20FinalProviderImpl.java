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

import static org.agorava.core.api.OAuthVersion.TWO_FINAL;


/**
 * @author Antoine Sabot-Durand
 */
@GenericRoot
@OAuth(TWO_FINAL)
public class OAuth20FinalProviderImpl extends OAuth20ProviderImpl {

    @Inject
    @ApplyQualifier
    TierConfigOauth20 api;

    @Inject
    @ApplyQualifier
    OAuthAppSettings config;

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequestImpl(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
        request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
        request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
        request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
        request.addBodyParameter("grant_type", "authorization_code");
        if (config.hasScope()) request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
        Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    @Override
    public String getVersion() {
        return TWO_FINAL.getLabel();
    }
}
