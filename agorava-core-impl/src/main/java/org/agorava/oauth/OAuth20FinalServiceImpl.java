/*
 * Copyright 2014-2016 Agorava
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

import org.agorava.AgoravaConstants;
import org.agorava.api.atinject.Generic;
import org.agorava.api.atinject.InjectWithQualifier;
import org.agorava.api.oauth.OAuth;
import org.agorava.api.oauth.OAuthRequest;
import org.agorava.api.oauth.Token;
import org.agorava.api.oauth.Verifier;
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.rest.Response;
import org.agorava.rest.OAuthRequestImpl;
import org.agorava.spi.ProviderConfigOauth20;

import static org.agorava.api.oauth.OAuth.OAuthVersion.TWO_FINAL;

/**
 * @author Antoine Sabot-Durand
 * @author Werner Keil
 */
@SuppressWarnings("serial")
@Generic
@OAuth(TWO_FINAL)
public class OAuth20FinalServiceImpl extends OAuth20ServiceImpl {


    @InjectWithQualifier
    ProviderConfigOauth20 api;

    @InjectWithQualifier
    OAuthAppSettings config;

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequestImpl(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addBodyParameter(AgoravaConstants.CLIENT_ID, config.getApiKey());
        request.addBodyParameter(AgoravaConstants.CLIENT_SECRET, config.getApiSecret());
        request.addBodyParameter(AgoravaConstants.CODE, verifier.getValue());
        request.addBodyParameter(AgoravaConstants.REDIRECT_URI, config.getCallback());
        request.addBodyParameter(AgoravaConstants.GRANT_TYPE, "authorization_code");
        if (config.hasScope())
            request.addBodyParameter(AgoravaConstants.SCOPE, config.getScope());
        Response response = request.send(); //todo:should check return code and launch ResponseException if it's not 200
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    @Override
    public OAuth.OAuthVersion getVersion() {
        return TWO_FINAL;
    }
    
    @Override
    public OAuthAppSettings getConfig() {
    	return config;
    }
}
