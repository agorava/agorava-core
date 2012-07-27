/*
 * Copyright 2012 Agorava
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

import org.agorava.core.api.JsonMapper;
import org.agorava.core.api.event.OAuthComplete;
import org.agorava.core.api.event.SocialEvent;
import org.agorava.core.api.oauth.*;
import org.agorava.core.api.rest.RestResponse;
import org.agorava.core.api.rest.RestVerb;
import org.agorava.core.cdi.AgoravaExtension;
import org.agorava.core.cdi.Current;
import org.apache.commons.lang3.StringUtils;
import org.jboss.solder.logging.Logger;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;

import static org.agorava.core.api.rest.RestVerb.*;
import static org.agorava.core.cdi.AgoravaExtension.getServicesToQualifier;

/**
 * This Abstract implementation of {@link OAuthService} uses an {@link OAuthProvider} to deal with remote OAuth Services
 *
 * @author Antoine Sabot-Durand
 */

public class OAuthServiceImpl implements OAuthService {

    private static final long serialVersionUID = -8423894021913341674L;
    private static Annotation currentLiteral = new AnnotationLiteral<Current>() {
        private static final long serialVersionUID = -2929657732814790025L;
    };

    @Inject
    @Any
    private Instance<OAuthProvider> providers;

    @Inject
    @Any
    private Event<OAuthComplete> completeEventProducer;

    private Annotation qualifier;

    private Map<String, String> requestHeader;

    void setQualifier(Annotation qualifier) {
        this.qualifier = qualifier;
    }

    @Inject
    protected JsonMapper jsonService;

    @Inject
    private Logger log;

    @Inject
    @Any
    protected Instance<OAuthSession> sessionInstances;

    @Inject
    protected AgoravaExtension socialConfig;

    private String type;

    @Override
    public String getType() {
        if (StringUtils.isEmpty(type))
            type = getServicesToQualifier().get(getQualifier());
        return type;
    }

    @Override
    public String getAuthorizationUrl() {
        return getProvider().getAuthorizationUrl(getRequestToken());
    }

    private OAuthProvider getProvider() {
        return providers.select(getQualifier()).get();
    }

    protected OAuthToken getRequestToken() {
        OAuthSession session = getSession();
        if (session.getRequestToken() == null)
            session.setRequestToken(getProvider().getRequestToken());
        return session.getRequestToken();
    }

    @Override
    public synchronized void initAccessToken() {
        OAuthSession session = getSession();
        if (session.getAccessToken() == null)
            session.setAccessToken(getProvider().getAccessToken(getRequestToken(), session.getVerifier()));
        if (session.getAccessToken() != null) {
            session.setRequestToken(null);
            log.debug("firing event for " + getQualifier() + " OAuth complete cycle");
            Event<OAuthComplete> event = completeEventProducer.select(getQualifier());
            event.fire(new OAuthComplete(SocialEvent.Status.SUCCESS, "", session));
            log.debug("After OAuth cycle completion");

        } else {
            // FIXME Launch an exception !!
        }

    }

    @Override
    public RestResponse sendSignedRequest(OAuthRequest request) {
        if (getRequestHeader() != null)
            request.getHeaders().putAll(getRequestHeader());
        getProvider().signRequest(getAccessToken(), request);
        return request.send();
    }

    @Override
    public RestResponse sendSignedRequest(RestVerb verb, String uri) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);
        return sendSignedRequest(request);

    }

    @Override
    public RestResponse sendSignedRequest(RestVerb verb, String uri, String key, Object value) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);

        request.addBodyParameter(key, value.toString());

        return sendSignedRequest(request);

    }

    @Override
    public RestResponse sendSignedXmlRequest(RestVerb verb, String uri, String payload) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);
        request.addPayload(payload);
        return sendSignedRequest(request);

    }

    @Override
    public RestResponse sendSignedRequest(RestVerb verb, String uri, Map<String, ? extends Object> params) {
        OAuthRequest request = getProvider().requestFactory(verb, uri);
        for (Entry<String, ? extends Object> ent : params.entrySet()) {
            request.addBodyParameter(ent.getKey(), ent.getValue().toString());
        }
        return sendSignedRequest(request);

    }

    @Override
    public void setVerifier(String verifierStr) {
        OAuthSession session = getSession();
        session.setVerifier(verifierStr);
    }

    @Override
    public String getVerifier() {
        OAuthSession session = getSession();
        return session.getVerifier();
    }

    @Override
    public OAuthToken getAccessToken() {
        OAuthSession session = getSession();
        return session.getAccessToken();
    }

    @Override
    public boolean isConnected() {
        return getSession().isConnected();
    }

    @Override
    public void setAccessToken(String token, String secret) {
        OAuthSession session = getSession();
        session.setAccessToken(getProvider().tokenFactory(token, secret));

    }

    @Override
    public void setAccessToken(OAuthToken token) {
        OAuthSession session = getSession();
        session.setAccessToken(token);

    }


    @Override
    public OAuthSession getSession() {
        OAuthSession session;
        if (socialConfig.isMultiSession())
            session = sessionInstances.select(currentLiteral).get();
        else
            session = sessionInstances.select(getQualifier()).get();
        return session;

    }

    @Override
    public <T> T get(String uri, Class<T> clazz) {
        return jsonService.mapToObject(sendSignedRequest(GET, uri), clazz);
    }

    @Override
    public <T> T get(String uri, Class<T> clazz, boolean signed) {
        RestResponse resp;
        if (signed)
            resp = sendSignedRequest(GET, uri);
        else
            resp = getProvider().requestFactory(GET, uri).send();
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
        RestResponse response = sendSignedRequest(request);
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
        sendSignedRequest(RestVerb.DELETE, uri);
    }


    public Annotation getQualifier() {
        return qualifier;
    }

    protected Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    @Override
    public void setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
    }

}
