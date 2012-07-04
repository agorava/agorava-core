/*
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
 */
/**
 *
 */
package org.agorava.core.cdi.scribe;

import java.util.Map;

import org.agorava.core.api.rest.RestParameter;
import org.agorava.core.api.rest.RestParameterList;
import org.scribe.model.ParameterList;

/**
 * @author Antoine Sabot-Durand
 */
public class ParameterListScribe implements RestParameterList {

    private final ParameterList delegate;

    ParameterListScribe(ParameterList delegate) {
        super();
        this.delegate = delegate;
    }

    public ParameterListScribe() {
        delegate = new ParameterList();
    }

    public ParameterListScribe(Map<String, String> params) {
        delegate = new ParameterList(params);
    }

    @Override
    public void add(String key, String value) {
        delegate.add(key, value);
    }

    @Override
    public String appendTo(String url) {
        return delegate.appendTo(url);
    }

    @Override
    public String asOauthBaseString() {
        return delegate.asOauthBaseString();
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String asFormUrlEncodedString() {
        return delegate.asFormUrlEncodedString();
    }

    @Override
    public void addAll(RestParameterList other) {

        delegate.addAll(((ParameterListScribe) other).delegate);
    }

    @Override
    public void addQuerystring(String queryString) {
        delegate.addQuerystring(queryString);
    }

    @Override
    public boolean contains(RestParameter param) {
        return delegate.contains(((ParameterScribe) param).getDelegate());
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public RestParameterList sort() {
        return new ParameterListScribe(delegate.sort());
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
