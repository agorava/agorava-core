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

package org.agorava.core.cdi;

import org.agorava.core.api.SocialMediaApiHub;
import org.agorava.core.api.oauth.OAuthAppSettingsBuilder;
import org.agorava.core.api.oauth.Param;
import org.agorava.core.oauth.PropertyOAuthAppSettingsBuilder;
import org.agorava.utils.solder.bean.generic.GenericType;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This Annotation is a JBoss Solder Generic annotation to work with {@link OAuthGenericManager}
 * It'll convey information to initialize an OAuth application connection
 *
 * @author Antoine Sabot-Durand
 */
@Retention(RUNTIME)
@GenericType(SocialMediaApiHub.class)
public @interface OAuthApplication {


    Class<? extends OAuthAppSettingsBuilder> builder() default PropertyOAuthAppSettingsBuilder.class;

    Param[] params() default {};

}
