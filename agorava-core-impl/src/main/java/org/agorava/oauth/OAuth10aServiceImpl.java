/*
 * Copyright 2014 Agorava
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
import org.agorava.api.rest.Request;
import org.agorava.api.rest.RequestTuner;
import org.agorava.api.rest.Response;
import org.agorava.spi.ProviderConfigOauth10a;
import org.agorava.utils.MapUtils;
import static org.agorava.api.oauth.OAuth.OAuthVersion.ONE;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * OAuth 1.0a implementation of {@link org.agorava.api.oauth.OAuthProvider}
 *
 * @author Antoine Sabot-Durand
 * @author Pablo Fernandez
 */

@Generic
@OAuth(ONE)
public class OAuth10aServiceImpl extends OAuthServiceBase {
    private static Logger LOGGER = Logger.getLogger(OAuth10aServiceImpl.class.getName());


    @InjectWithQualifier
    ProviderConfigOauth10a api;

    /**
     * {@inheritDoc}
     */
    public Token getRequestToken(int timeout, TimeUnit unit) {
        return getRequestToken(new TimeoutTuner(timeout, unit));
    }

    @Override
    public Token getRequestToken() {
        return getRequestToken(2, TimeUnit.SECONDS);
    }

    @Override
    public String getVerifierParamName() {
        return AgoravaConstants.VERIFIER;
    }

    @Override
    public String getAuthorizationUrl() {
        Token token = getRequestToken();
        getSession().setRequestToken(token);
        return getAuthorizationUrl(token);
    }

    public Token getRequestToken(RequestTuner tuner) {

        OAuthAppSettings config = getTunedOAuthAppSettings();
        LOGGER.fine("obtaining request token from " + api.getRequestTokenEndpoint());
        OAuthRequest request = requestFactory(api.getRequestTokenVerb(), api.getRequestTokenEndpoint());

        LOGGER.fine("setting oauth_callback to " + config.getCallback());
        request.addOAuthParameter(AgoravaConstants.CALLBACK, config.getCallback());
        addOAuthParams(request, AgoravaConstants.EMPTY_TOKEN);
        appendSignature(request);

        LOGGER.fine("sending request...");
        Response response = request.send(tuner);
        //todo:should check return code and launch ResponseException if it's not 200
        String body = response.getBody();

        LOGGER.fine("response status code: " + response.getCode());
        LOGGER.fine("response body: " + body);
        return api.getRequestTokenExtractor().extract(body);
    }

    private void addOAuthParams(OAuthRequest request, Token token) {
        request.addOAuthParameter(AgoravaConstants.TIMESTAMP, api.getTimestampService().getTimestampInSeconds());
        request.addOAuthParameter(AgoravaConstants.NONCE, api.getTimestampService().getNonce());
        request.addOAuthParameter(AgoravaConstants.CONSUMER_KEY, config.getApiKey());
        request.addOAuthParameter(AgoravaConstants.SIGN_METHOD, api.getSignatureService().getSignatureMethod());
        request.addOAuthParameter(AgoravaConstants.VERSION, getVersion().getLabel());
        if (config.hasScope())
            request.addOAuthParameter(AgoravaConstants.SCOPE, config.getScope());
        request.addOAuthParameter(AgoravaConstants.SIGNATURE, getSignature(request, token));

        LOGGER.fine("appended additional OAuth parameters: " + MapUtils.toString(request.getOauthParameters()));
    }

    /**
     * {@inheritDoc}
     */
    public Token getAccessToken(Token requestToken, Verifier verifier, int timeout, TimeUnit unit) {
        return getAccessToken(requestToken, verifier, new TimeoutTuner(timeout, unit));
    }

    public Token getAccessToken(Token requestToken, Verifier verifier) {
        return getAccessToken(requestToken, verifier, 2, TimeUnit.SECONDS);
    }

    public Token getAccessToken(Token requestToken, Verifier verifier, RequestTuner tuner) {
        LOGGER.fine("obtaining access token from " + api.getAccessTokenEndpoint());
        OAuthRequest request = requestFactory(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        request.addOAuthParameter(AgoravaConstants.TOKEN, requestToken.getToken());
        request.addOAuthParameter(AgoravaConstants.VERIFIER, verifier.getValue());

        LOGGER.fine("setting token to: " + requestToken + " and verifier to: " + verifier);
        addOAuthParams(request, requestToken);
        appendSignature(request);
        Response response = request.send(tuner);
        //todo:should check return code and launch ResponseException if it's not 200
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    public void signRequest(Token token, OAuthRequest request) {
        LOGGER.fine("signing request: " + request.getCompleteUrl());

        // Do not append the token if empty. This is for two legged OAuth calls.
        if (!token.isEmpty()) {
            request.addOAuthParameter(AgoravaConstants.TOKEN, token.getToken());
        }
        LOGGER.fine("setting token to: " + token);
        addOAuthParams(request, token);
        appendSignature(request);
    }

    /**
     * {@inheritDoc}
     */
    public OAuth.OAuthVersion getVersion() {
        return ONE;
    }

    /**
     * {@inheritDoc}
     */
    public String getAuthorizationUrl(Token requestToken) {
        return api.getAuthorizationUrl(requestToken);
    }

    private String getSignature(OAuthRequest request, Token token) {
        LOGGER.fine("generating signature...");
        String baseString = api.getBaseStringExtractor().extract(request);
        String signature = api.getSignatureService().getSignature(baseString, config.getApiSecret(), token.getSecret());

        LOGGER.fine("base string is: " + baseString);
        LOGGER.fine("signature is: " + signature);
        return signature;
    }

    private void appendSignature(OAuthRequest request) {
        switch (api.getSignaturePlace()) {
            case HEADER:
                LOGGER.fine("using Http HEADER signature");

                String oauthHeader = api.getHeaderExtractor().extract(request);
                request.addHeader(AgoravaConstants.HEADER, oauthHeader);
                break;
            case QUERY_STRING:
                LOGGER.fine("using Querystring signature");

                for (Map.Entry<String, String> entry : request.getOauthParameters().entrySet()) {
                    request.addQuerystringParameter(entry.getKey(), entry.getValue());
                }
                break;
        }
    }

    private static class TimeoutTuner implements RequestTuner {
        private final int duration;

        private final TimeUnit unit;

        public TimeoutTuner(int duration, TimeUnit unit) {
            this.duration = duration;
            this.unit = unit;
        }

        @Override
        public void tune(Request request) {
            request.setReadTimeout(duration, unit);
        }
    }
}
