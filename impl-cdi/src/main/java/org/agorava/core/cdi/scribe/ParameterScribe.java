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
/**
 * 
 */
package org.agorava.core.cdi.scribe;

import org.agorava.core.api.rest.RestParameter;
import org.scribe.model.Parameter;

/**
 * @author Antoine Sabot-Durand
 * 
 */
public class ParameterScribe implements Comparable<ParameterScribe>, RestParameter {

    private final Parameter delegate;

    public ParameterScribe(String key, String value) {
        super();
        delegate = new Parameter(key, value);
    }

    ParameterScribe(Parameter parameter) {
        delegate = parameter;
    }

    @Override
    public int compareTo(ParameterScribe o) {
        return delegate.compareTo(o.delegate);
    }

    @Override
    public String asUrlEncodedPair() {
        return delegate.asUrlEncodedPair();
    }

    @Override
    public boolean equals(Object other) {
        return delegate.equals(other);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    Parameter getDelegate() {
        return delegate;
    }

}
