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
/**
 *
 */
package org.agorava.api.event;

import org.agorava.api.oauth.OAuthSession;

/**
 * Event sent at the end of OAuth authentication cycle containing the new {@link OAuthSession}
 *
 * @author Antoine Sabot-Durand
 */
public class OAuthComplete extends SocialEvent<OAuthSession> {

    private static final long serialVersionUID = -2428276251131445054L;

    /**
     * @param status  the status of the event
     * @param message specific message about the event
     * @param session the OAuth user session that has been completed
     */
    public OAuthComplete(Status status, String message, OAuthSession session) {
        super(status, message, session);
    }

}
