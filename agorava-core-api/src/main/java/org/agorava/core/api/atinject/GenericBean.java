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

package org.agorava.core.api.atinject;

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
 * This qualifier is used to mark Class to indicate that they hare the default implementation for a Generic bean
 * For a given {@link TierServiceRelated} Qualifier.
 * <p/>
 * Tier services (i.e. Social Media) are dynamically discovered when Agorava is bootstrapped thanks to Tier services qualifiers
 * defined in each module and bearing the {@link TierServiceRelated} annotation.
 * <p/>
 * Bootstrap process retrieve Generic Beans (thanks to this qualifier) and produces qualified versions with {@link
 * TierServiceRelated} qualifiers discovered previously except if specific implementation bearing concrete service related
 * qualifier is found.
 * <p/>
 * In any case (whether the bean is automatically produced from generic or specifically by third party developer) the
 * bootstrapping process will analyse generic beans to replace {@link InjectWithQualifier} annotation by injection with Bean
 * qualifier
 *
 * @author Antoine Sabot-Durand
 * @see TierServiceRelated
 * @see InjectWithQualifier
 */
@Qualifier
@Target({TYPE, METHOD, PARAMETER, FIELD})
@Retention(RUNTIME)
@Documented
public @interface GenericBean {
}
