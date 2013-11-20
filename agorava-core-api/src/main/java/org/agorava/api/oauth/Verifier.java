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

package org.agorava.api.oauth;

import org.agorava.api.service.Preconditions;

/**
 * Represents an OAuth verifier code.
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 */
public class Verifier {

    private final String value;

    /**
     * Default constructor.
     *
     * @param value verifier value
     */
    public Verifier(String value) {
        Preconditions.checkNotNull(value, "Must provide a valid string as verifier");
        this.value = value;
    }

    /**
     * @return verifier value
     */
    public String getValue() {
        return value;
    }
}
