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

package org.agorava.core.rest;

import org.agorava.core.api.rest.Parameter;
import org.agorava.core.api.rest.ParameterList;
import org.agorava.core.api.utils.OAuthEncoder;
import org.agorava.core.api.utils.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: Pablo Fernandez
 */
public class ParameterListImpl implements ParameterList {
    private static final char QUERY_STRING_SEPARATOR = '?';
    private static final String PARAM_SEPARATOR = "&";
    private static final String PAIR_SEPARATOR = "=";
    private static final String EMPTY_STRING = "";

    private final List<Parameter> params;

    public ParameterListImpl() {
        params = new ArrayList<Parameter>();
    }

    ParameterListImpl(List<Parameter> params) {
        this.params = new ArrayList<Parameter>(params);
    }

    public ParameterListImpl(Map<String, String> map) {
        this();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.add(new Parameter(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public void add(String key, String value) {
        params.add(new Parameter(key, value));
    }

    @Override
    public String appendTo(String url) {
        Preconditions.checkNotNull(url, "Cannot append to null URL");
        String queryString = asFormUrlEncodedString();
        if (queryString.equals(EMPTY_STRING)) {
            return url;
        } else {
            url += url.indexOf(QUERY_STRING_SEPARATOR) != -1 ? PARAM_SEPARATOR : QUERY_STRING_SEPARATOR;
            url += queryString;
            return url;
        }
    }

    @Override
    public String asOauthBaseString() {
        return OAuthEncoder.encode(asFormUrlEncodedString());
    }

    @Override
    public String asFormUrlEncodedString() {
        if (params.size() == 0) return EMPTY_STRING;

        StringBuilder builder = new StringBuilder();
        for (Parameter p : params) {
            builder.append('&').append(p.asUrlEncodedPair());
        }
        return builder.toString().substring(1);
    }

    @Override
    public void addAll(ParameterList other) {
        params.addAll(((ParameterListImpl) other).params);
    }

    @Override
    public void addQuerystring(String queryString) {
        if (queryString != null && queryString.length() > 0) {
            for (String param : queryString.split(PARAM_SEPARATOR)) {
                String pair[] = param.split(PAIR_SEPARATOR);
                String key = OAuthEncoder.decode(pair[0]);
                String value = pair.length > 1 ? OAuthEncoder.decode(pair[1]) : EMPTY_STRING;
                params.add(new Parameter(key, value));
            }
        }
    }

    @Override
    public boolean contains(Parameter param) {
        return params.contains(param);
    }

    @Override
    public int size() {
        return params.size();
    }

    @Override
    public ParameterListImpl sort() {
        ParameterListImpl sorted = new ParameterListImpl(params);
        Collections.sort(sorted.params);
        return sorted;
    }
}
