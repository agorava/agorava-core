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

package org.agorava.core.api.oauth;

import org.agorava.core.api.UserProfile;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * Contains user data for a session connection to a given {@link OAuthService}.
 * It completes data in {@link OAuthAppSettings}.
 * More over it contains status and basic user data when OAuth connexion has been completed
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthSession extends Serializable {

    /**
     * @return the requestToken
     */
    public OAuthToken getRequestToken();

    /**
     * @param requestToken the requestToken to set
     */
    public void setRequestToken(OAuthToken requestToken);

    /**
     * @return the accessToken
     */
    public OAuthToken getAccessToken();

    /**
     * @param accessToken the accessToken to set
     */
    public void setAccessToken(OAuthToken accessToken);

    /**
     * @return the verifier
     */
    public String getVerifier();

    /**
     * @param verifier the verifier to set
     */
    public void setVerifier(String verifier);

    /**
     * @param userProfile to set
     */
    public void setUserProfile(UserProfile userProfile);

    /**
     * @return the connected userProfile
     */
    public UserProfile getUserProfile();

    /**
     * @return the annotation for the current Social Media
     */
    public Annotation getServiceQualifier();

    /**
     * @return true if the session is active
     */
    public boolean isConnected();

    /**
     * @return the name of the session to display for user
     */
    public String getName();
    
    /**
     * @return the name of the service
     */
    public String getServiceName();
}
