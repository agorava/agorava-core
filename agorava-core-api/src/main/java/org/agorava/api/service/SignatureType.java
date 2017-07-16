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

package org.agorava.api.service;

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
 * Qualifier to distinguish different {@link org.agorava.api.service.SignatureService} implementations
 *
 * @author Antoine Sabot-Durand
 */
@Qualifier
@Target({TYPE, METHOD, PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface SignatureType {

    /**
     * @return type of signature
     */
    Type value();


    /**
     * @author Antoine Sabot-Durand
     */
    enum Type {
        /**
         * For signature based on HMAC-SHA1
         *
         * @see <a href="http://tools.ietf.org/html/rfc2104">HMAC RFC</a>
         */
        HMACSHA1,

        /**
         * For signature in plain text
         */
        PLAINTEXT,

        /**
         * For signature based on RSA-SHA1
         *
         * @see <a href=http://www.w3.org/PICS/DSig/RSA-SHA1_1_0.html">w3c documentation on RSA SHA1</a>
         */
        RSASHA1,


        /**
         * available for third party developer
         */
        OTHER
    }
}
