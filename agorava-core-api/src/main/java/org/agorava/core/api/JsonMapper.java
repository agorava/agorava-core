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

package org.agorava.core.api;

import org.agorava.core.api.rest.RestResponse;

import java.io.Serializable;

/**
 * Implementation of this Interface will manage JSON Serialization / De-serialization to and from Object
 *
 * @author Antoine Sabot-Durand
 */
public interface JsonMapper extends Serializable {

    /**
     * Parse the content of the provided {@link RestResponse} to de-serialize to an object of the given Class
     *
     * @param resp  the response to de-serialize
     * @param clazz the target class of the object
     * @return an object of the given Class with fields coming from the response
     */
    public <T> T mapToObject(RestResponse resp, Class<T> clazz);

    /**
     * Transforms an object to a JSON string
     *
     * @param object : the object to transform
     * @return a String representing the object in JSON format
     */
    public String objectToJsonString(Object object);

}
