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

package org.agorava.core.api;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthSession;

/**
 * Implementation of this interface allow to manage multiple OAuth connection. The connection to service are backed by a Set to
 * avoid null or duplicate connection. Uniqueness of a connection is based on service type and User name on the service
 * 
 * 
 * @author Antoine Sabot-Durand
 * 
 */
public interface MultiServicesManager extends Serializable {

    /**
     * @return Set of available service to connect to
     */
    public List<String> getListOfServices();

    /**
     * Instantiate a new service from a Service String name
     * 
     * @param serviceName the name of the service
     * @return the Bean of the new service
     * 
     *         public OAuthService getService(String serviceName);
     */

    /**
     * @return the current service
     */
    public OAuthService getCurrentService();

    /**
     * @return the status of the current service.
     */
    public boolean isCurrentServiceConnected();

    /**
     * Connect the current service at the end of the OAuth process
     */
    public void connectCurrentService();

    /**
     * Instantiate a new Service which become the new current service
     * 
     * @param servType the type of the service to Instantiate
     * @return the authorization url to call to start the OAuth process
     */
    public String initNewSession(String type);

    /**
     * Disconnect the current service and remove it from Set of managed service. Reset the currentService to null
     */
    public void destroyCurrentSession();

    /**
     * @return
     */
    public OAuthSession getCurrentSession();

    /**
     * @param currentSession
     */
    public void setCurrentSession(OAuthSession currentSession);

    /**
     * @return
     */
    Set<OAuthSession> getActiveSessions();

    /**
     * @return
     */
    SocialNetworkServicesHub getCurrentServiceHub();

}
