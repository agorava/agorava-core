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

package org.agorava.core.api.exception;

import org.agorava.core.api.rest.Response;

import java.text.MessageFormat;

/**
 * Exception thrown when a problem occurred in high level Rest exchanges. Request was well send and tier sent a {@link
 * Response} with an HTTP return code not in 200 family
 *
 * @author Antoine Sabot-Durand
 */
public class ResponseException extends AgoravaException {

    private static final long serialVersionUID = -1780448283631156367L;

    private static final String MSG = "Remote service returned the error code {0} for the following Request : {1}\n" +
            "The following data was returned :\n{2}\n";

    private Response response;

    /**
     * Default constructor
     *
     * @param response the response containing error
     */
    public ResponseException(Response response) {
        super(MessageFormat.format(MSG, response.getCode(), response.getRequest(), response.getBody()));
        this.response = response;
    }

    /**
     * @return the response
     */
    public Response getResponse() {
        return response;
    }
}
