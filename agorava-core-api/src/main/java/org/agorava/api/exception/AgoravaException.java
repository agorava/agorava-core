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
 * Default Agorava exception.
 * Represents a generic problem in the Framework
 *
 * @author Antoine Sabot-Durand
 */
public class AgoravaException extends RuntimeException {

    private static final long serialVersionUID = -2374017612010236524L;

    /**
     * Default constructor
     *
     * @param message message explaining what went wrong
     * @param cause   original exception
     */
    public AgoravaException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * No-exception constructor. Used when there is no original exception
     *
     * @param message message explaining what went wrong
     */
    public AgoravaException(String message) {
        super(message, null);
    }

    /**
     * No-message constructor. Used when there is no obvious message to add
     *
     * @param cause original exception
     */
    public AgoravaException(Exception cause) {
        super(cause);
    }
}
