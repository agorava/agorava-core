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

package org.agorava.core.api.rest;

import java.io.Serializable;
import java.util.Map;

import org.agorava.core.api.UserProfile;

/**
 * @author antoine
 * 
 */
public interface RestService extends Serializable {

    /**
     * @return
     */
    public UserProfile getMyProfile();

    /**
     * Returns the status of this ServiceHndler
     * 
     * @return true if the connection process is over and successful
     */
    public boolean isConnected();

    /**
     * Returns the name/type of the Social Network we're connected to
     * 
     * @return name of the service
     */
    public String getType();

    /**
     * Close connexion if needed
     */
    public void resetConnection();

    /**
     * @param <T>
     * @param uri
     * @param clazz
     * @return
     */
    public <T> T getForObject(String uri, Class<T> clazz);

    /**
     * @param uri
     * @param params
     * @param clazz
     * @return
     */
    public <T> T postForObject(String uri, Map<String, ? extends Object> params, Class<T> clazz);

    /**
     * @param uri
     * @param clazz
     * @param params
     * @return
     */
    public <T> T getForObject(String uri, Class<T> clazz, Map<String, ? extends Object> params);

    /**
     * @param uri
     * @param clazz
     * @param urlParams
     * @return
     */
    public <T> T getForObject(String uri, Class<T> clazz, Object... urlParams);

    /**
     * @param uri
     * @param toPost
     * @param queryStringData
     * @param urlVariables
     * @return
     */
    String postForLocation(String uri, Object toPost, Map<String, String> queryStringData, Object... urlParams);

    /**
     * @param uri
     * @param toPost
     * @param urlVariables
     * @return
     */
    String postForLocation(String uri, Object toPost, Object... urlParams);

    /**
     * @param uri
     * @param toPut
     * @param urlParams
     */
    void put(String uri, Object toPut, Object... urlParams);

    /**
     * @param uri
     */
    void delete(String uri);

    /**
     * @return
     */
    Map<String, String> getRequestHeader();

    /**
     * @param requestHeader
     */
    void setRequestHeader(Map<String, String> requestHeader);

}
