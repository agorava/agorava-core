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

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation used in Generic Bean (qualified with {@link GenericBean})to mark injection point that should be modified by
 * framework bootstrap to bear the same {@link ProviderRelated} qualifier than the containing bean.
 * <p>For example:
 * <pre>
 *   &#064;org.agorava.api.atinject.GenericBean
 *   public OAuthServiceImpl extends OAuthService {
 *
 *       &#064;InjectWithQualifier
 *       protected OAuthProvider provider; // Will inject provider with the same ProviderRelated Qualifier than the bean
 *
 *   }</pre>
 *
 * @author Antoine Sabot-Durand
 * @see ProviderRelated
 * @see GenericBean
 */
@Target({METHOD, FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
public @interface InjectWithQualifier {
}
