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

package org.agorava.api.exception;

import org.agorava.api.oauth.OAuthRequest;

/**
 * Specialized exception that represents a missing OAuth parameter.
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 */
public class OAuthParametersMissingException extends AgoravaException {

    private static final long serialVersionUID = 1745308760111976671L;

    private static final String MSG = "Could not find oauth parameters in request: %s. "
            + "OAuth parameters must be specified with the addOAuthParameter() method";

    /**
     * Default constructor.
     *
     * @param request OAuthRequest that caused the error
     */
    public OAuthParametersMissingException(OAuthRequest request) {
        super(String.format(MSG, request));
    }
}
