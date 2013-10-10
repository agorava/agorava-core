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

package org.agorava.api.event;

import java.io.Serializable;

/**
 * Generic event for all Agorava Event. Carry information and generic event data
 *
 * @param <T> generic type for the data of the event
 * @author Antoine Sabot-Durand
 */
public class SocialEvent<T> implements Serializable {

    private static final long serialVersionUID = -2626322336079857836L;

    private final Status status;

    private final String message;

    private final T eventData;

    /**
     * @param status    Status of the event
     * @param message   message related tot he event
     * @param eventData data for the event
     */
    public SocialEvent(Status status, String message, T eventData) {
        this.status = status;
        this.message = message;
        this.eventData = eventData;
    }

    /**
     * @return the status of the event
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return a message related to the event
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return data related to the event
     */
    public T getEventData() {
        return eventData;
    }

    /**
     * Status of the Event
     *
     * @author Antoine Sabot-Durand
     */
    public enum Status {
        /**
         * Event reports a success
         */
        SUCCESS,
        /**
         * Event reports a failure
         */
        FAILURE,
        /**
         * Event status not significant
         */
        UNKNOWN
    }

}
