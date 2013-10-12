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

package org.agorava.api.storage;

import org.agorava.api.oauth.OAuthSession;

import java.util.Collection;

/**
 * Represents a Repository containing data for all OAuth connexion in the application
 *
 * @author Antoine Sabot-Durand
 */
public interface GlobalRepository extends Repository<UserSessionRepository> {


    /**
     * @return a collection containing all existing OAuthSession contained in the application
     */
    Collection<OAuthSession> getAllSessions();


    /**
     * Create a new element and set it as current
     *
     * @return the created element
     */
    UserSessionRepository createNew();


}
