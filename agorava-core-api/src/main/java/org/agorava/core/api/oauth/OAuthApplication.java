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

package org.agorava.core.api.oauth;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation is used to produce {@link OAuthAppSettings} in code
 *
 * @author Antoine Sabot-Durand
 */
@Target({METHOD, FIELD})
@Retention(RUNTIME)
@Documented
public @interface OAuthApplication {

    /**
     * @return the builder class name used to build the final {@link OAuthAppSettings}
     */
    Class<? extends OAuthAppSettingsBuilder> builder() default OAuthAppSettingsBuilder.class;

    /**
     * @return a list of optional {@link Param} to configure the builder
     */
    Param[] params() default {};

}
