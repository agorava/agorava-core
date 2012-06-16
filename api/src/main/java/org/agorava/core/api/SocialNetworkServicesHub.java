/*******************************************************************************
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
 ******************************************************************************/
/**
 * 
 */
package org.agorava.core.api;

import org.agorava.core.api.oauth.OAuthServiceAware;
import org.agorava.core.api.oauth.OAuthSession;

/**
 * 
 * Beans implementing this interface are the entry point of Social Media interaction. They have 2 roles
 * <ul>
 * <li>gather all Api families for a given Social Media</li>
 * <li>provide generic services for the underlying Social Media (for instance get user profile) to allow using it without
 * knowing it specificities</li>
 * </ul>
 * 
 * 
 * @author Antoine Sabot-Durand
 * 
 */
public interface SocialNetworkServicesHub extends OAuthServiceAware, SocialMediaAware {

    /**
     * @return the profile of the connected user
     */
    public UserProfile getMyProfile();

    /**
     * @return the oauth session settings of the connected user
     */
    public OAuthSession getSession();

    /**
     * Close connexion if needed
     */
    public void resetConnection();

    /**
     * @return true if the Social Media is connected
     */
    public boolean isConnected();

    /**
     * 
     * @return the parameter name with which Social Media return verifier to achieve OAuth cycle
     */
    public String getVerifierParamName();

}
