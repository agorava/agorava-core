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

package org.agorava.core.api.oauth;

import org.agorava.core.api.UserSessionRepository;
import org.agorava.core.spi.UserProfile;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * Contains user data for a session connection to a given {@link OAuthService}.
 * It completes data in {@link org.agorava.core.api.oauth.application.OAuthAppSettingsImpl}.
 * More over it contains status and basic user data when OAuth connexion has been completed
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthSession extends Serializable {

    String getId();

    UserSessionRepository getRepo();

    void setRepo(UserSessionRepository repo);


    /**
     * @return the requestToken
     */
    Token getRequestToken();

    /**
     * @param requestToken the requestToken to set
     */
    void setRequestToken(Token requestToken);

    /**
     * @return the accessToken
     */
    Token getAccessToken();

    /**
     * @param accessToken the accessToken to set
     */
    void setAccessToken(Token accessToken);

    /**
     * @return the verifier
     */
    String getVerifier();

    /**
     * @param verifier the verifier to set
     */
    void setVerifier(String verifier);

    /**
     * @param userProfile to set
     */
    void setUserProfile(UserProfile userProfile);

    /**
     * @return the connected userProfile
     */
    UserProfile getUserProfile();

    /**
     * @return the annotation for the current Social Media
     */
    Annotation getServiceQualifier();

    /**
     * @return true if the session is active
     */
    boolean isConnected();

    /**
     * @return the name of the session to display for user
     */
    String getName();

    /**
     * @return the name of the service
     */
    String getServiceName();
}
