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

package org.agorava.core.utils;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.agorava.core.api.exception.AgoravaException;
import org.apache.commons.beanutils.BeanMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Utils to deal with URL and url-encodings
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 */
public class URLUtils {

    private static class formUrlEncodeFunc implements Function<Object, String> {

        @Override
        public String apply(Object input) {
            // TODO Auto-generated method stub
            return formURLEncode(input.toString());
        }
    }

    private static final String EMPTY_STRING = "";
    private static final String UTF_8 = "UTF-8";
    private static final String PAIR_SEPARATOR = "=";
    private static final String PARAM_SEPARATOR = "&";
    private static final char MULTI_VALUE_SEPARATOR = ',';
    private static final char QUERY_STRING_SEPARATOR = '?';

    private static final String ERROR_MSG = String.format("Cannot find specified encoding: %s", UTF_8);


    public static Joiner commaJoiner = Joiner.on(MULTI_VALUE_SEPARATOR).skipNulls();
    private static final MapJoiner queryMapJoiner = Joiner.on(PARAM_SEPARATOR).withKeyValueSeparator(PAIR_SEPARATOR);


    /**
     * Turns a map into a form-urlencoded string
     *
     * @param parameters any map
     * @return form-url-encoded string
     */
    private static String formURLEncodeMap(Map<String, ?> parameters) {
        return (parameters.size() <= 0) ? EMPTY_STRING : doFormUrlEncode(parameters);
    }

    private static String doFormUrlEncode(Map<String, ?> params) {
        Map<String, String> urlEncodeMap = Maps.transformValues(params, new formUrlEncodeFunc());
        return queryMapJoiner.join(urlEncodeMap);
    }


    /**
     * Translates a string into application/x-www-form-urlencoded format
     *
     * @param string to encode
     * @return form-urlencoded string
     */
    private static String formURLEncode(String string) {
        try {
            return URLEncoder.encode(string, UTF_8);
        } catch (UnsupportedEncodingException uee) {
            throw new IllegalStateException(ERROR_MSG, uee);
        }
    }


    /**
     * Append given parameters to the query string of the url
     *
     * @param url        the url to append parameters to
     * @param parameters any map
     * @return new url with parameters on query string
     */
    public static String buildUri(String url, Map<String, ?> parameters) {
        String queryString = URLUtils.formURLEncodeMap(parameters);
        if (queryString.equals(EMPTY_STRING)) {
            return url;
        } else {
            url += url.indexOf(QUERY_STRING_SEPARATOR) != -1 ? PARAM_SEPARATOR : QUERY_STRING_SEPARATOR;
            url += queryString;
            return url;
        }
    }

    public static String buildUri(String url, Object pojo) {
        Multimap<String, Object> pojoMap = copyIntoMultiMap(pojo);
        String queryString = URLUtils.mapToQueryString(pojoMap);
        if (queryString.equals(EMPTY_STRING)) {
            return url;
        } else {
            url += url.indexOf(QUERY_STRING_SEPARATOR) != -1 ? PARAM_SEPARATOR : QUERY_STRING_SEPARATOR;
            url += queryString;
            return url;
        }
    }


    /**
     * @param parameters map to build the query string from
     * @return a query string corresponding to the map
     */
    private static String mapToQueryString(Multimap<String, Object> parameters) {
        if (parameters.size() == 0)
            return EMPTY_STRING;
        Multimap<String, String> urlEncodeMap = Multimaps.transformValues(parameters, new formUrlEncodeFunc());
        return queryMapJoiner.join(urlEncodeMap.entries());
    }

    /**
     * Create a {@link Multimap} from a simple bean.
     * <p/>
     * All standard properties will become keys in the target map
     * <p/>
     * Collection properties will become multi-entries
     * <p/>
     * Map properties will trigger an exception
     *
     * @param pojo the bean to convert
     * @return the multimap
     * @throws AgoravaException if one property is a Map
     */
    @SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection"})
    private static Multimap<String, Object> copyIntoMultiMap(Object pojo) {
        Map<String, ?> pojoMap = new BeanMap(pojo);
        Multimap<String, Object> res = HashMultimap.create();
        for (String key : pojoMap.keySet()) {
            if (!"class".equals(key)) {
                Object value = pojoMap.get(key);
                if (value instanceof Map)
                    throw new AgoravaException("Cannot convert Pojo containing a Map to a Multimap");
                if (value instanceof Collection) {
                    for (Object elt : (Collection<Object>) value) {
                        res.put(key, elt);
                    }
                } else
                    res.put(key, value);
            }
        }
        return res;
    }

    /**
     * Append given parameter to the query string of the url
     *
     * @param url   the url to append parameters to
     * @param key   name of the parameter ro add
     * @param value value of the parameter to add
     * @return new url with parameters on query string
     */
    public static String buildUri(String url, String key, String value) {
        if ("".equals(key) || key == null) {
            return url;
        } else {
            url += url.indexOf(QUERY_STRING_SEPARATOR) != -1 ? PARAM_SEPARATOR : QUERY_STRING_SEPARATOR;
            url += key + PAIR_SEPARATOR + formURLEncode(value);
            return url;
        }
    }


    public static Map<String, String> buildPagingParametersWithCount(int page, int pageSize, long sinceId, long maxId) {
        Map<String, String> parameters = newHashMap();
        parameters.put("page", String.valueOf(page));
        parameters.put("count", String.valueOf(pageSize));
        if (sinceId > 0) {
            parameters.put("since_id", String.valueOf(sinceId));
        }
        if (maxId > 0) {
            parameters.put("max_id", String.valueOf(maxId));
        }
        return parameters;
    }

    public static Map<String, String> buildPagingParametersWithPerPage(int page, int pageSize, long sinceId, long maxId) {
        Map<String, String> parameters = newHashMap();
        parameters.put("page", String.valueOf(page));
        parameters.put("per_page", String.valueOf(pageSize));
        if (sinceId > 0) {
            parameters.put("since_id", String.valueOf(sinceId));
        }
        if (maxId > 0) {
            parameters.put("max_id", String.valueOf(maxId));
        }
        return parameters;
    }

}
