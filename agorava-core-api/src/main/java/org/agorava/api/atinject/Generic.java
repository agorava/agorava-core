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

package org.agorava.api.atinject;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * This qualifier is used to mark Class to indicate that it should be used to generate different beans for each {@link
 * ProviderRelated} Qualifier.
 * <p/>
 * Provider services (i.e. Social Media) are dynamically discovered when Agorava is bootstrapped thanks to Provider services
 * qualifiers
 * defined in each module and bearing the {@link ProviderRelated} annotation.
 * <p/>
 * Bootstrap process retrieve Generic Beans (thanks to this qualifier) and produces qualified versions with {@link
 * ProviderRelated} qualifiers discovered previously.
 * <p/>
 * The bootstrapping process will analyse generic beans to replace {@link InjectWithQualifier} annotation by injection with Bean
 * qualifier
 *
 * @author Antoine Sabot-Durand
 * @see ProviderRelated
 * @see InjectWithQualifier
 */
@Qualifier
@Target({ TYPE, METHOD, PARAMETER, FIELD })
@Retention(RUNTIME)
@Documented
public @interface Generic {
}
