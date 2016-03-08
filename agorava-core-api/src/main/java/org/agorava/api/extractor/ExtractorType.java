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

package org.agorava.api.extractor;

import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Qualifier to distinguish different {@link org.agorava.api.extractor.Extractor} implementations
 *
 * @author Antoine Sabot-Durand
 */
@Qualifier
@Target({TYPE, METHOD, PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface ExtractorType {

    /**
     * @return type of extractor
     */
    Type value();


    /**
     * Different types of  Extractor
     *
     * @author Antoine Sabot-Durand
     */
    enum Type {
        /**
         * standard token extractor
         */
        TOKEN_STD,

        /**
         * JSON token extractor
         */
        TOKEN_JSON,

        /**
         * HEADER extractor
         */
        HEADER,

        /**
         * OAuth 1.0a base string extractor (string being signed)
         *
         * @see <a href="http://oauth.net/core/1.0/#anchor14">Base string info in OAuth 1.0a spec</a>
         */
        OAUTH1_BASE_STRING,
        
        /**
         * OAuth 1.0a ext (user id) string extractor (string being signed)
         *
         * @see <a href="http://oauth.net/core/1.0/#anchor14">Base string info in OAuth 1.0a spec</a>
         */
        OAUTH1_USER_ID,

        /**
         * available for third party developer
         */
        OTHER
    }
}
