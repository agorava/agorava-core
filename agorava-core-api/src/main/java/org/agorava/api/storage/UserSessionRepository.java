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

import org.agorava.api.function.Identifiable;
import org.agorava.api.oauth.OAuthSession;

import java.lang.annotation.Annotation;

/**
 * Allows to manage multiple {@link OAuthSession}. The connection to service are backed by a Set to
 * avoid null or duplicate connection. Uniqueness of a connection is based on service type and User name on the service
 *
 * @author Antoine Sabot-Durand
 */
public interface UserSessionRepository extends Repository<OAuthSession>, Identifiable {


    /**
     * Get a an existing {@link OAuthSession} for a given service provider
     *
     * @param qual the provider qualifier
     * @return a session associated to the provider or {@link OAuthSession#NULL} if no OAuthSession exists for the provider
     */
    OAuthSession getForProvider(Annotation qual);
}
