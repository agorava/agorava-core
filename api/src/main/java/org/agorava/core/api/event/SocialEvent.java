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

import java.io.Serializable;

/**
 * 
 * Generic event Payload for all Agorava Event
 * 
 * 
 * @author Antoine Sabot-Durand
 * 
 */
public class SocialEvent<T> implements Serializable {

    private static final long serialVersionUID = -2626322336079857836L;

    /**
     * 
     * Status of the Event
     * 
     * @author Antoine Sabot-Durand
     * 
     */
    public enum Status {
        SUCCESS, FAILURE
    }

    private final Status status;

    private final String message;

    private final T eventData;

    public SocialEvent(Status status, String message) {
        this(status, message, null);
    }

    public SocialEvent(Status status, String message, T payload) {
        super();
        this.status = status;
        this.message = message;
        this.eventData = payload;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getEventData() {
        return eventData;
    }

}
