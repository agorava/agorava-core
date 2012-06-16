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

package org.agorava.core.api.event;

/**
 * 
 * Event sent after a status update
 * 
 * @author Antoine Sabot-Durand
 * 
 */
public class StatusUpdated extends SocialEvent<Object> {

    private static final long serialVersionUID = -2757535195468162752L;

    public StatusUpdated(Status status, String message, Object payload) {
        super(status, message, payload);
    }

}
