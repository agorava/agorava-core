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

package org.agorava.core.cdi;

import org.agorava.core.api.ApplyQualifier;
import org.agorava.core.api.Current;
import org.agorava.core.api.GenericRoot;
import org.agorava.core.api.JsonMapper;
import org.agorava.core.api.event.OAuthComplete;
import org.agorava.core.api.event.SocialEvent;
import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.oauth.OAuthAppSettings;
import org.agorava.core.api.oauth.OAuthProvider;
import org.agorava.core.api.oauth.OAuthRequest;
import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.api.oauth.Token;
import org.agorava.core.api.rest.Response;
import org.agorava.core.api.rest.Verb;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;

import static org.agorava.core.api.rest.Verb.GET;
import static org.agorava.core.api.rest.Verb.POST;
import static org.agorava.core.api.rest.Verb.PUT;

//import org.agorava.utils.solder.logging.Logger;

/**
 * {@inheritDoc}
 *
 * @author Antoine Sabot-Durand
 */
@ApplyQualifier
@GenericRoot
public class OAuthServiceImpl implements OAuthService {

    private static final long serialVersionUID = -8423894021913341674L;
    private static Annotation currentLiteral = new AnnotationLiteral<Current>() {
        private static final long serialVersionUID = -2929657732814790025L;
    };
    @Inject
    protected JsonMapper jsonService;
    @Inject
    @Any
    protected Instance<OAuthSession> sessions;
    @Inject
    @ApplyQualifier
    protected OAuthProvider provider;
    @Inject
    @ApplyQualifier
    protected Event<OAuthComplete> completeEventProducer;
    @ApplyQualifier
    @Inject
    protected OAuthAppSettings settings;
    private String socialMediaName;
    private Annotation qualifier;
    private Map<String, String> requestHeader;

    @PostConstruct
    public void init() {

        socialMediaName = settings.getSocialMediaName();
        qualifier = AgoravaExtension.getServicesToQualifier().get(socialMediaName);
    }

    @Override
    public String getSocialMediaName() {
        return socialMediaName;
    }

    @Override
    public String getVerifierParamName() {
        return getProvider().getVerifierParamName();
    }

    @Override
    public String getAuthorizationUrl() {
        return getProvider().getAuthorizationUrl(getRequestToken());
    }

    private OAuthProvider getProvider() {
        return provider;
    }

    protected Token getRequestToken() {
        OAuthSession session = getSession();
        if (session.getRequestToken() == null) session.setRequestToken(getProvider().getRequestToken());
        return session.getRequestToken();
    }

    @Override
    public synchronized void initAccessToken() {
        OAuthSession session = getSession();
        if (session.getAccessToken() == null)
            session.setAccessToken(getProvider().getAccessToken(getRequestToken(), session.getVerifier()));
        if (session.getAccessToken() != null) {
            session.setRequestToken(null);
//            log.debug("firing event for " + getSocialMediaName() + " OAuth complete cycle");
            Event<OAuthComplete> event = completeEventProducer.select();
            event.fire(new OAuthComplete(SocialEvent.Status.SUCCESS, "", session));
//            log.debug("After OAuth cycle completion");

            //TODO: reactivate logger
        } else {
            // FIXME Launch an exception !!
        }

    }

    @Override
    public Response sendSignedRequest(OAuthRequest request) {
        if (getRequestHeader() != null) request.getHeaders().putAll(getRequestHeader());
        getProvider().signRequest(getAccessToken(), request);
        return request.send();
    }

    @Override
    public Response sendSignedRequest(Verb verb, String uri) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);
        return sendSignedRequest(request);

    }

    @Override
    public Response sendSignedRequest(Verb verb, String uri, String key, Object value) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);

        request.addBodyParameter(key, value.toString());

        return sendSignedRequest(request);

    }

    @Override
    public Response sendSignedXmlRequest(Verb verb, String uri, String payload) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);
        request.addPayload(payload);
        return sendSignedRequest(request);

    }

    @Override
    public Response sendSignedRequest(Verb verb, String uri, Map<String, ? extends Object> params) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);
        for (Entry<String, ? extends Object> ent : params.entrySet()) {
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
    public void setAccessToken(String token, String secret) {
        OAuthSession session = getSession();
        session.setAccessToken(new Token(token, secret));

    }

    @Override
    public OAuthSession getSession() {
        OAuthSession res = null;

        Instance<OAuthSession> currentSession = sessions.select(currentLiteral);
        if (currentSession.isAmbiguous()) {
            currentSession = currentSession.select(qualifier);

        }
        if (currentSession.isUnsatisfied()) {
            throw new AgoravaException("No OAuthSession defined with @Current qualifier");
        }
        res = currentSession.get();
        if (res.getServiceName() != getSocialMediaName())
            throw new AgoravaException("Bad remoteService for OAuthSession. Expected : " + getSocialMediaName() + ", " +
                    "but was : " + res.getServiceName());
        return res;

    }

    @Override
    public <T> T get(String uri, Class<T> clazz) {
        return jsonService.mapToObject(sendSignedRequest(GET, uri), clazz);
    }

    @Override
    public <T> T get(String uri, Class<T> clazz, boolean signed) {
        Response resp;
        if (signed) resp = sendSignedRequest(GET, uri);
        else resp = getProvider().requestFactory(GET, uri).send();
        return jsonService.mapToObject(resp, clazz);
    }

    @Override
    public <T> T get(String uri, Class<T> clazz, Object... urlParams) {
        String url = MessageFormat.format(uri, urlParams);
        return jsonService.mapToObject(sendSignedRequest(GET, url), clazz);
    }

    @Override
    public <T> T post(String uri, Map<String, ? extends Object> params, Class<T> clazz) {
        OAuthRequest request = getProvider().requestFactory(POST, uri);
        request.addBodyParameters(params);
        return jsonService.mapToObject(sendSignedRequest(request), clazz);
    }

    @Override
    public String post(String uri, Object toPost, Object... urlParams) {

        uri = MessageFormat.format(uri, urlParams);
        OAuthRequest request = getProvider().requestFactory(POST, uri);

        request.addPayload(jsonService.objectToJsonString(toPost));
        Response response = sendSignedRequest(request);
        return response.getHeader("Location");
    }

    @Override
    public void put(String uri, Object toPut, Object... urlParams) {
        uri = MessageFormat.format(uri, urlParams);
        OAuthRequest request = getProvider().requestFactory(PUT, uri);

        request.addPayload(jsonService.objectToJsonString(toPut));
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

}
