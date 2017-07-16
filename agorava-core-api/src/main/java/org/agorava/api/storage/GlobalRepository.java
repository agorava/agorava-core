/*
 * Copyright 2014 Agorava
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

package org.agorava.api.storage;

import org.agorava.api.oauth.OAuthSession;

import java.util.Collection;

/**
 * Represents a Repository containing data for all OAuth connexion in the application
 *
 * @author Antoine Sabot-Durand
 */
public abstract class GlobalRepository implements Repository<UserSessionRepository> {

    protected static GlobalRepository instance;


    public static GlobalRepository getInstance() {
        return instance;
    }
    
    /**
     * @return a collection containing all existing {@link OAuthSession} contained in the application
     */
    public abstract Collection<OAuthSession> getAllOauthSessions();


    /**
     *
     * Return the {@link OAuthSession} with the given id or null if there's no OAuthSession with this id  
     *
     * @param id the unqiue id of the OauthSession
     * @return the corresponding {@link OAuthSession} or null if doesn't exist
     */
    public abstract OAuthSession getOauthSession(String id);

    /**
     * Create a new element and set it as current
     *
     * @return the created element
     */
    public abstract UserSessionRepository createNew();


    public abstract UserSessionRepository createNew(String id);
}
