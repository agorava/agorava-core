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

package org.agorava.jsf;

/**
 * @author Antoine Sabot-Durand
 */
public class FacesUrlTransformer {

    private static final String QUERY_STRING_DELIMITER = "?";

    private static final String PARAMETER_PAIR_DELIMITER = "&";

    private static final String PARAMETER_ASSIGNMENT_OPERATOR = "=";

    private String url;


    public FacesUrlTransformer(String url) {
        this.url = url;
    }

    public FacesUrlTransformer appendParamIfNecessary(String name, String value) {
        url = appendParameterIfNeeded(url, name, value);
        return this;
    }

    private static String appendParameterIfNeeded(String url, String parameterName, String parameterValue) {
        int queryStringIndex = url.indexOf(QUERY_STRING_DELIMITER);
        // if there is no query string or there is a query string but the param is
        // absent, then append it
        if (queryStringIndex < 0 || url.indexOf(parameterName + PARAMETER_ASSIGNMENT_OPERATOR, queryStringIndex) < 0) {
            StringBuilder builder = new StringBuilder(url);
            if (queryStringIndex < 0) {
                builder.append(QUERY_STRING_DELIMITER);
            } else {
                builder.append(PARAMETER_PAIR_DELIMITER);
            }
            builder.append(parameterName).append(PARAMETER_ASSIGNMENT_OPERATOR);
            if (parameterValue != null) {
                builder.append(parameterValue);
            }
            return builder.toString();
        } else {
            return url;
        }
    }

    public String getUrl() {
        return url;
    }

}
