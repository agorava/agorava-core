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

package org.agorava.api.rest;

import java.io.Serializable;
import java.util.Map;

/**
 * Manage basic REST actions
 *
 * @author Antoine Sabot-Durand
 */
public interface RestService extends Serializable {

    /**
     * Returns the status of this Service
     *
     * @return true if the connection process is over and successful
     */
    boolean isConnected();

    /**
     * Returns the name/type of the Social Network we're connected to
     *
     * @return name of the service
     */
    String getSocialMediaName();

    /**
     * Perform a REST Get command and return an object of the provided class
     *
     * @param uri   the uri to perform the rest get call
     * @param clazz class of the returned object
     * @return an object of the asked class
     */
    <T> T get(String uri, Class<T> clazz);


    /**
     * Perform a REST get command with given parameters to put in the given URI and return an object of the provided class
     *
     * @param uri       a string with {@link java.text.MessageFormat} placeholders (i.e. {0},
     *                  {1}) style for params. It's the uri to perform the REST get call
     * @param clazz     class of the returned object
     * @param urlParams list of params to feed the uri with
     * @param <T>       generic type for returned object
     * @return an object of the asked class
     */
    <T> T get(String uri, Class<T> clazz, Object... urlParams);


    /**
     * Perform a REST post command with given parameters to put in Body and return an object of the provided class
     *
     * @param uri    the URI to post to
     * @param params parameters to put in body
     * @param clazz  class of the returned object
     * @param <T>    generic type for returned object
     * @return an object of the asked class
     */
    <T> T post(String uri, Map<String, ?> params, Class<T> clazz);


    /**
     * Perform a REST post command with given object and given URI params. Expecting a String (a new URL) as returned data
     *
     * @param uri       a string with {@link java.text.MessageFormat} placeholders (i.e. {0},
     *                  {1}) style for params. It's the uri to perform the REST post call
     * @param toPost    the object to post
     * @param urlParams list of params to feed the uri with
     * @return an uri pointing to a resource corresponding to posted object
     */
    String post(String uri, Object toPost, Object... urlParams);

    /**
     * Perform a REST put command with given object and given URI params
     *
     * @param uri       a string with {@link java.text.MessageFormat} placeholders (i.e. {0},
     *                  {1}) style for params. It's the uri to perform the REST put call
     * @param toPut     the object to put
     * @param urlParams list of params to feed the uri with
     */
    void put(String uri, Object toPut, Object... urlParams);

    /**
     * Perform a REST delete command with given uri
     *
     * @param uri to perform te REST delete call
     */
    void delete(String uri);


}
