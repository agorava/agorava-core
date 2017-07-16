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

package org.agorava.rest;

import org.agorava.api.rest.HttpParameters;
import org.agorava.api.service.OAuthEncoder;
import org.agorava.api.service.Preconditions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Antoine Sabot-Durand
 * @author Pablo Fernandez
 */
public class HttpParametersImpl implements HttpParameters {
    private static final char QUERY_STRING_SEPARATOR = '?';

    private static final String PARAM_SEPARATOR = "&";

    private static final String PAIR_SEPARATOR = "=";

    private static final String EMPTY_STRING = "";

    List<Parameter> getParams() {
        return params;
    }

    private final List<Parameter> params;


    public HttpParametersImpl() {
        params = new ArrayList<Parameter>();
    }

    HttpParametersImpl(List<Parameter> params) {
        this.params = new ArrayList<Parameter>(params);
    }

    public HttpParametersImpl(Map<String, String> map) {
        this();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.add(new Parameter(entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public HttpParameters add(String key, String value) {
        params.add(new Parameter(key, value));
        return this;
    }

    @Override
    public String asUrl(String url) {
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
        Collections.sort(params);
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
    public HttpParameters addAll(HttpParameters other) {
        params.addAll(((HttpParametersImpl) other).params);
        return this;
    }

    @Override
    public HttpParameters addQuerystring(String queryString) {
        if (queryString != null && queryString.length() > 0) {
            for (String param : queryString.split(PARAM_SEPARATOR)) {
                String pair[] = param.split(PAIR_SEPARATOR);
                String key = OAuthEncoder.decode(pair[0]);
                String value = pair.length > 1 ? OAuthEncoder.decode(pair[1]) : EMPTY_STRING;
                params.add(new Parameter(key, value));
            }
        }
        return this;
    }

    @Override
    public HttpParameters addMap(Map<String, ? extends Object> map) {
        for (String s : map.keySet()) {
            add(s, map.get(s).toString());
        }

        return this;
    }

    @Override
    public int size() {

        return getParams().size();
    }


    /**
     * Represents a parameter in a REST request
     *
     * @author Pablo Fernandez
     * @author Antoine Sabot-Durand
     */
    static class Parameter implements Comparable<Parameter> {

        private final String key;

        private final String value;

        /**
         * Default constructor
         *
         * @param key   name of the param
         * @param value value of the param
         */
        public Parameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        /**
         * @return key and value encoded for URL
         */
        public String asUrlEncodedPair() {
            return OAuthEncoder.encode(key).concat("=").concat(OAuthEncoder.encode(value));
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;
            if (other == this) return true;
            if (!(other instanceof Parameter)) return false;

            Parameter otherParam = (Parameter) other;
            return otherParam.key.equals(key) && otherParam.value.equals(value);
        }

        @Override
        public int hashCode() {
            return key.hashCode() + value.hashCode();
        }

        @Override
        public int compareTo(Parameter parameter) {
            int keyDiff = key.compareTo(parameter.key);

            return keyDiff != 0 ? keyDiff : value.compareTo(parameter.value);
        }
    }
}
