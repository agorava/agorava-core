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

/**
 * Low level exception in Rest attempt to connect to tier service
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 */
public class ConnectionException extends AgoravaException {
    private static final String MSG = "There was a problem while creating a connection to the remote service.";

    private static final long serialVersionUID = 7840947809613813090L;

    /**
     * Default constructor
     *
     * @param cause original exception
     */
    public ConnectionException(Exception cause) {
        super(MSG, cause);
    }
}
