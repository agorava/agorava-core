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

package org.agorava.core.cdi.scribe;

import org.agorava.core.api.exception.AgoravaRestException;
import org.agorava.core.api.oauth.OAuthRequest;
import org.agorava.core.api.rest.RestParameterList;
import org.agorava.core.api.rest.RestResponse;
import org.agorava.core.api.rest.RestVerb;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Verb;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Antoine Sabot-Durand
 */
public class OAuthRequestScribe implements OAuthRequest {

    /**
     *
     */
    private static final long serialVersionUID = 6560621737726192278L;
    private final org.scribe.model.OAuthRequest request;

    org.scribe.model.OAuthRequest getDelegate() {
        return request;
    }

    OAuthRequestScribe(org.scribe.model.OAuthRequest request) {
        super();
        this.request = request;
    }

    /**
     * @param verb
     * @param uri
     */
    public OAuthRequestScribe(RestVerb verb, String url) {
        request = new org.scribe.model.OAuthRequest(Verb.valueOf(verb.toString()), url);
    }

    @Override
    public void addOAuthParameter(String key, String value) {
        request.addOAuthParameter(key, value);
    }

    @Override
    public RestResponse send() {

        RestResponse resp = null;
        try {
            resp = new RestResponseScribe(request.send(), request.getUrl());
        } catch (OAuthException e) {
            throw new AgoravaRestException(request.getUrl(), e);
        }
        if (resp.getCode() >= 400)
            throw new AgoravaRestException(resp.getCode(), request.getUrl(), resp.getHeaders().toString());
        return resp;
    }

    @Override
    public Map<String, String> getOauthParameters() {
        return request.getOauthParameters();
    }

    @Override
    public String toString() {
        return request.toString();
    }

    @Override
    public int hashCode() {
        return request.hashCode();
    }

    @Override
    public void addHeader(String key, String value) {
        request.addHeader(key, value);
    }

    @Override
    public void addBodyParameter(String key, String value) {
        request.addBodyParameter(key, value);
    }

    @Override
    public void addQuerystringParameter(String key, String value) {
        request.addQuerystringParameter(key, value);
    }

    @Override
    public void addPayload(String payload) {
        request.addPayload(payload);
    }

    @Override
    public RestParameterList getQueryStringParams() {
        return new ParameterListScribe(request.getQueryStringParams());
    }

    @Override
    public boolean equals(Object obj) {
        return request.equals(obj);
    }

    @Override
    public RestParameterList getBodyParams() {
        return new ParameterListScribe(request.getBodyParams());
    }

    @Override
    public String getUrl() {
        return request.getUrl();
    }

    @Override
    public String getSanitizedUrl() {
        return request.getSanitizedUrl();
    }

    @Override
    public String getBodyContents() {
        return request.getBodyContents();
    }

    @Override
    public RestVerb getVerb() {
        return RestVerb.valueOf(request.getVerb().toString());
    }

    @Override
    public Map<String, String> getHeaders() {
        return request.getHeaders();
    }

    @Override
    public void setConnectTimeout(int duration, TimeUnit unit) {
        request.setConnectTimeout(duration, unit);
    }

    @Override
    public void setReadTimeout(int duration, TimeUnit unit) {
        request.setReadTimeout(duration, unit);
    }

    @Override
    public String getCompleteUrl() {
        return request.getCompleteUrl();
    }

    @Override
    public void addPayload(byte[] payload) {
        request.addPayload(payload);
    }

    @Override
    public String getCharset() {
        return request.getCharset();
    }

    @Override
    public void setCharset(String charsetName) {
        request.setCharset(charsetName);
    }

    @Override
    public void setConnectionKeepAlive(boolean connectionKeepAlive) {
        request.setConnectionKeepAlive(connectionKeepAlive);
    }

    @Override
    public void addBodyParameters(Map<String, ? extends Object> toAdd) {
        for (String key : toAdd.keySet()) {
            addBodyParameter(key, toAdd.get(key).toString());

        }
    }

}
