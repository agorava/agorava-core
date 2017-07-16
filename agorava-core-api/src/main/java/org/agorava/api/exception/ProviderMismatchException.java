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

/**
 * Exception launch when an action has to used two beans belonging to different providers
 * (i.e an {@link org.agorava.api.oauth.OAuthSession} for Provider A and an {@link org.agorava.api.oauth.OAuthService} for
 * provider B
 *
 * @author Antoine Sabot-Durand
 */
public class ProviderMismatchException extends AgoravaException {

    private static final long serialVersionUID = 802948737665876233L;

    /**
     * Default constructor
     *
     * @param message of the exception
     */
    public ProviderMismatchException(String message) {
        super(message);
    }
}
