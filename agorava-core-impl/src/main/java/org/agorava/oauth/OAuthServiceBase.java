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

import org.agorava.api.atinject.Current;
import org.agorava.api.atinject.InjectWithQualifier;
import org.agorava.api.oauth.OAuthRequest;
import org.agorava.api.oauth.OAuthService;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.oauth.Token;
import org.agorava.api.oauth.Verifier;
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.rest.Response;
import org.agorava.api.rest.Verb;
import org.agorava.api.service.JsonMapperService;
import org.agorava.api.service.OAuthLifeCycleService;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.rest.OAuthRequestImpl;

import javax.inject.Inject;
import java.text.MessageFormat;
import java.util.Map;

import static org.agorava.api.rest.Verb.GET;
import static org.agorava.api.rest.Verb.POST;
import static org.agorava.api.rest.Verb.PUT;

/**
 * @author Antoine Sabot-Durand
 */
public abstract class OAuthServiceBase implements OAuthService {


    @Inject
    OAuthLifeCycleService OAuthLifeCycleService;

    @Inject
    @Current
    UserSessionRepository repo;

    @Inject
    JsonMapperService mapperService;

    @InjectWithQualifier
    OAuthAppSettings config;

    private Map<String, String> requestHeader;

    @Override
    public OAuthRequest requestFactory(Verb verb, String uri) {
        OAuthRequest res = new OAuthRequestImpl(verb, uri);
        return res;
    }

    @Override
    public JsonMapperService getJsonMapper() {
        return mapperService;
    }

    @Override
    public Response sendSignedRequest(OAuthRequest request) {
        if (getRequestHeader() != null) request.getHeaders().putAll(getRequestHeader());
        signRequest(getAccessToken(), request);
        return request.send(); //todo:should check return code and launch ResponseException if it's not 200
    }

    @Override
    public Response sendSignedRequest(Verb verb, String uri) {
        OAuthRequest request = requestFactory(verb, uri);
        return sendSignedRequest(request);

    }

    @Override
    public Response sendSignedRequest(Verb verb, String uri, String key, Object value) {
        OAuthRequest request = requestFactory(verb, uri);

        request.addBodyParameter(key, value.toString());

        return sendSignedRequest(request);

    }

    @Override
    public Response sendSignedXmlRequest(Verb verb, String uri, String payload) {
        OAuthRequest request = requestFactory(verb, uri);
        request.addPayload(payload);
        return sendSignedRequest(request);

    }

    @Override
    public OAuthSession getSession() {
        return OAuthLifeCycleService.getSessionForQualifier(config.getQualifier());

    }

    @Override
    public Response sendSignedRequest(Verb verb, String uri, Map<String, ? extends Object> params) {
        OAuthRequest request = requestFactory(verb, uri);
        for (Map.Entry<String, ? extends Object> ent : params.entrySet()) {
            request.addBodyParameter(ent.getKey(), ent.getValue().toString());
        }
        return sendSignedRequest(request);

    }

    @Override
    public String getVerifier() {
        OAuthSession session = getSession();
        return session.getVerifier();
    }

    @Override
    public void setVerifier(String verifierStr) {
        OAuthSession session = getSession();
        session.setVerifier(verifierStr);
    }

    @Override
    public Token getAccessToken() {
        OAuthSession session = getSession();
        return session.getAccessToken();
    }

    @Override
    public void setAccessToken(Token token) {
        OAuthSession session = getSession();
        session.setAccessToken(token);

    }

    @Override
    public boolean isConnected() {
        return getSession().isConnected();
    }

    @Override
    public String getSocialMediaName() {
        return config.getSocialMediaName();
    }

    @Override
    public void setAccessToken(String token, String secret) {
        OAuthSession session = getSession();
        session.setAccessToken(new Token(token, secret));

    }

    @Override
    public <T> T get(String uri, Class<T> clazz) {
        return getJsonMapper().mapToObject(sendSignedRequest(GET, uri), clazz);
    }

    @Override
    public <T> T get(String uri, Class<T> clazz, boolean signed) {
        Response resp;
        if (signed) resp = sendSignedRequest(GET, uri);
        else
            resp = requestFactory(GET, uri).send(); //todo:should check return code and launch
        // ResponseException if it's not 200
        return getJsonMapper().mapToObject(resp, clazz);
    }

    @Override
    public <T> T get(String uri, Class<T> clazz, Object... urlParams) {
        String url = MessageFormat.format(uri, urlParams);
        return getJsonMapper().mapToObject(sendSignedRequest(GET, url), clazz);
    }

    @Override
    public <T> T post(String uri, Map<String, ? extends Object> params, Class<T> clazz) {
        OAuthRequest request = requestFactory(POST, uri);
        request.addBodyParameters(params);
        return getJsonMapper().mapToObject(sendSignedRequest(request), clazz);
    }

    @Override
    public String post(String uri, Object toPost, Object... urlParams) {

        uri = MessageFormat.format(uri, urlParams);
        OAuthRequest request = requestFactory(POST, uri);

        request.addPayload(getJsonMapper().objectToJsonString(toPost));
        Response response = sendSignedRequest(request);
        return response.getHeader("Location");
    }

    @Override
    public void put(String uri, Object toPut, Object... urlParams) {
        uri = MessageFormat.format(uri, urlParams);
        OAuthRequest request = requestFactory(PUT, uri);

        request.addPayload(getJsonMapper().objectToJsonString(toPut));
        sendSignedRequest(request);

    }

    @Override
    public void delete(String uri) {
        sendSignedRequest(Verb.DELETE, uri);
    }

    protected Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    @Override
    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

    @Override
    public void resetSession() {

        OAuthSession session = getSession();
        session.setAccessToken(null);
        session.setVerifier(null);
        session.setUserProfile(null);

    }

    @Override
    public Token getAccessToken(Token requestToken, String verifier) {
        return getAccessToken(requestToken, new Verifier(verifier));
    }


}
