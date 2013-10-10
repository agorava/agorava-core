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

package org.agorava.api.atinject;

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
 * Qualifier to distinguish different implementations of services or helpers regarding OAuth version or OAuth 2.0 flavor
 *
 * @author Antoine Sabot-Durand
 */
@Qualifier
@Target({TYPE, METHOD, PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface OAuth {

    /**
     * @return flavor of OAuth to distinguish
     */
    OAuthVersion value();

    /**
     * The different values of OAuth
     *
     * @author Antoine Sabot-Durand
     */
    enum OAuthVersion {

        /**
         * Value for OAuth 1.0a
         *
         * @see <a href="https://datatracker.ietf.org/doc/rfc5849/">OAuth 1.0a RFC</a>
         */
        ONE("1.0"),

        /**
         * Value for OAuth 2.0 draft 11
         * Implementation used in code coming from Scribe
         *
         * @see <a href="http://tools.ietf.org/html/draft-ietf-oauth-v2-11">OAuth 2.0 RFC draft 11</a>
         */
        TWO_DRAFT_11("2.0"),

        /**
         * Value OAuth 2.0 final, RFC 6749
         *
         * @see <a href="https://datatracker.ietf.org/doc/rfc6749/">OAuth 2.0 RFC</a>
         */
        TWO_FINAL("2.0"),

        /**
         * available for third party developer
         */
        OTHER("other");

        private final String label;

        OAuthVersion(String label) {
            this.label = label;
        }

        /**
         * @return label associated with current value
         */
        public String getLabel() {
            return label;
        }
    }
}
