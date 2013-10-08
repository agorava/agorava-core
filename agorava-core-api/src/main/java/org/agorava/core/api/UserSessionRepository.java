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

package org.agorava.core.api;

import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthSession;

import java.util.List;

/**
 * Allows to manage multiple OAuth Session. The connection to service are backed by a Set to
 * avoid null or duplicate connection. Uniqueness of a connection is based on service type and User name on the service
 *
 * @author Antoine Sabot-Durand
 */
public interface UserSessionRepository extends Repository<OAuthSession>, Identifiable {

    /**
     * @return List of available service to connect to
     */
    List<String> getListOfServices();

    /**
     * @return the current service
     */
    OAuthService getCurrentService();

    /**
     * @return the status of the current service.
     */
    boolean isCurrentServiceConnected();

    /**
     * Connect the current service. To be used at the end of the OAuth process
     */
    void connectCurrentService();

    /**
     * Instantiate a new Service which become the new current service
     *
     * @param type the type of the service to Instantiate
     * @return the authorization url to call to start the OAuth process
     */
    String initNewSession(String type);


}
