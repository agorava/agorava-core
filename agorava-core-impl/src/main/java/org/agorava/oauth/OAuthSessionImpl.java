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
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.oauth.Token;
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.spi.UserProfile;

import java.lang.annotation.Annotation;
import java.util.UUID;

/**
 * {@inheritDoc}
 *
 * @author Antoine Sabot-Durand
 */
@GenericBean
public class OAuthSessionImpl implements OAuthSession {

    private static final long serialVersionUID = -2526192334215289830L;

    private Token requestToken;

    private Token accessToken;

    private String verifier;

    private UserProfile userProfile;

    public UserSessionRepository getRepo() {
        return repo;
    }

    public void setRepo(UserSessionRepository repo) {
        this.repo = repo;
    }

    private UserSessionRepository repo;

    private String id = UUID.randomUUID().toString();

    @InjectWithQualifier
    private OAuthAppSettings settings;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Token getRequestToken() {
        return requestToken;
    }

    @Override
    public void setRequestToken(Token requestToken) {
        this.requestToken = requestToken;
    }

    @Override
    public Token getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String getVerifier() {
        return verifier;
    }

    @Override
    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    @Override
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userProfile == null) ? 0 : userProfile.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OAuthSessionImpl other = (OAuthSessionImpl) obj;
        if (userProfile == null) {
            if (other.userProfile != null)
                return false;
        } else if (!userProfile.equals(other.userProfile))
            return false;
        return true;
    }

    @Override
    public Annotation getServiceQualifier() {
        return settings.getQualifier();
    }

    @Override
    public String toString() {
        return getServiceName() + " - " + (isConnected() ? getUserProfile().getFullName() : "not connected");
    }

    @Override
    public boolean isConnected() {
        return accessToken != null;
    }

    @Override
    public String getName() {
        return toString();
    }

    @Override
    public String getServiceName() {
        return settings.getSocialMediaName();
    }

}
