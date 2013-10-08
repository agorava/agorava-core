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

package org.agorava.core.api.oauth.application;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Support annotation to transport additional parameter for {@link OAuthApplication} annotation helping configuring the final
 * {@link OAuthAppSettingsBuilder}
 *
 * @author Antoine Sabot-Durand
 */
@Target({ElementType.ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface Param {

    /**
     * @return name of the param
     */
    String name();

    /**
     * @return value for the param
     */
    String value();


}
