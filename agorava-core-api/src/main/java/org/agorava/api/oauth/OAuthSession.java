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
import org.agorava.api.storage.Identifiable;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.spi.UserProfile;

import java.lang.annotation.Annotation;

/**
 * Contains user data for a session connection to a given {@link OAuthService}.
 * It completes data in {@link org.agorava.api.oauth.application.OAuthAppSettings}.
 * More over it contains status and basic user data when OAuth connexion has been completed
 *
 * @author Antoine Sabot-Durand
 */
public class OAuthSession implements Identifiable {

    private static final long serialVersionUID = -2526192334215289830L;

    /**
     * A null OAuthSession used to avoid NPE
     */
    public static OAuthSession NULL = new OAuthSession("NULL");

    private final Annotation qualifier;

    private final String id;

    private Token requestToken;

    private Token accessToken;

    private String verifier;

    private UserProfile userProfile;

    private UserSessionRepository repo;


    OAuthSession(Annotation qualifier, Token requestToken, Token accessToken, String verifier,
                 UserProfile userProfile, UserSessionRepository repo, String id) {

        this.qualifier = qualifier;
        this.requestToken = requestToken;
        this.accessToken = accessToken;
        this.verifier = verifier;
        this.userProfile = userProfile;
        this.repo = repo;
        this.id = id;
    }

    OAuthSession(String id) {
        qualifier = null;
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * @return the repository which contains this session
     */
    public UserSessionRepository getRepo() {
        return repo;
    }

    /**
     * Attach this Session the given Repository
     *
     * @param repo the repository that will contain this session
     */
    public void setRepo(UserSessionRepository repo) {
        this.repo = repo;
    }

    /**
     * @return the requestToken
     */
    public Token getRequestToken() {
        return requestToken;
    }

    /**
     * @param requestToken the requestToken to set
     */
    public void setRequestToken(Token requestToken) {
        this.requestToken = requestToken;
    }

    /**
     * @return the accessToken
     */
    public Token getAccessToken() {
        return accessToken;
    }

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(Token accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * @return the verifier
     */
    public String getVerifier() {
        return verifier;
    }

    /**
     * @param verifier the verifier to set
     */
    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    /**
     * @return the connected userProfile
     */
    public UserProfile getUserProfile() {
        return userProfile;
    }

    /**
     * @param userProfile to set
     */
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    /**
     * @return the annotation for the current Social Media
     */
    public Annotation getServiceQualifier() {
        return qualifier;
    }

    /**
     * @return true if the session is active
     */
    public boolean isConnected() {
        return userProfile != null;
    }

    /**
     * @return the name of the session to display for user
     */
    public String getName() {
        return toString();
    }

    @Override
    public String toString() {
        return getServiceName() + " - " + (isConnected() ? getUserProfile().getFullName() : "not connected");
    }

    /**
     * @return the name of the service
     */
    public String getServiceName() {
        return AgoravaContext.getQualifierToService().get(getServiceQualifier());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userProfile == null) ? id.hashCode() : userProfile.hashCode());
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
        OAuthSession other = (OAuthSession) obj;
        if (userProfile != null)
            return userProfile.equals(other.userProfile);
        else
            return id.equals(other.getId());
    }


}
