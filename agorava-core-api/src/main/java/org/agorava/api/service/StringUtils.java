/*
 * Copyright 2014 Agorava
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

package org.agorava.api.service;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Antoine Sabot-Durand
 */
public class StringUtils {


    /* preventing instantiation */
    private StringUtils() {

    }

    /**
     * Join a collection of Object with a given character as a separator
     *
     * @param collection collection to join
     * @param separator  char to separate values
     * @return resulting string of the concatained values with separator
     */
    public static String join(Collection collection, Character separator) {
        String res = "";

        if (collection != null && separator != null) {
            for (Object s : collection) {
                res += s.toString() + separator;
            }
            res = res.substring(0, res.length() - 1);
        }
        return res;
    }

    /**
     * Join an array of Object with a given character as a separator
     *
     * @param array array to join
     * @param separator  char to separate values
     * @return resulting string of the concatained values with separator
     */
    public static String join(Object[] array, char separator) {
        if (array != null)
            return join(Arrays.asList(array), ',');
        else
            return "";
    }
}
