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

package org.agorava.api.oauth;

import org.agorava.AgoravaContext;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.spi.UserProfile;

import java.lang.annotation.Annotation;
import java.util.UUID;

public class OAuthSessionBuilder {

    private Annotation qualifier;

    private Token requestToken;

    private Token accessToken;

    private String verifier;

    private UserProfile userProfile = null;

    private UserSessionRepository repo;

    private String id;

    public OAuthSessionBuilder providerName(String providerName) {
        this.qualifier = AgoravaContext.getServicesToQualifier().get(providerName);
        return this;
    }

    public OAuthSessionBuilder qualifier(Annotation qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    public OAuthSessionBuilder requestToken(Token requestToken) {
        this.requestToken = requestToken;
        return this;
    }

    public OAuthSessionBuilder accessToken(Token accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public OAuthSessionBuilder verifier(String verifier) {
        this.verifier = verifier;
        return this;
    }

    public OAuthSessionBuilder userProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        return this;
    }

    public OAuthSessionBuilder repo(UserSessionRepository repo) {
        this.repo = repo;
        return this;
    }

    public OAuthSessionBuilder id(String id) {
        this.id = id;
        return this;
    }

    public OAuthSessionBuilder readFromOAuthSession(OAuthSession session) {
        qualifier(session.getServiceQualifier())
                .requestToken(session.getRequestToken())
                .accessToken(session.getAccessToken())
                .verifier(session.getVerifier())
                .userProfile(session.getUserProfile())
                .repo(session.getRepo())
                .id(session.getId());
        return this;
    }

    public OAuthSession build() {
        if (qualifier == null)
            throw new IllegalArgumentException("OAuthSession should be associated to Provider");
        if (id == null)
            id = UUID.randomUUID().toString();

        return new OAuthSession(qualifier, requestToken, accessToken, verifier, userProfile, repo, id);
    }
}