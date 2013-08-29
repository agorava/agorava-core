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

package org.scribe.model;

import org.scribe.utils.OAuthEncoder;

/**
 * @author: Pablo Fernandez
 */
public class Parameter implements Comparable<Parameter> {
    private static final String UTF = "UTF8";

    private final String key;
    private final String value;

    public Parameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String asUrlEncodedPair() {
        return OAuthEncoder.encode(key).concat("=").concat(OAuthEncoder.encode(value));
    }

    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Parameter)) return false;

        Parameter otherParam = (Parameter) other;
        return otherParam.key.equals(key) && otherParam.value.equals(value);
    }

    public int hashCode() {
        return key.hashCode() + value.hashCode();
    }

    public int compareTo(Parameter parameter) {
        int keyDiff = key.compareTo(parameter.key);

        return keyDiff != 0 ? keyDiff : value.compareTo(parameter.value);
    }
}
