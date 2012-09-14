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

import com.google.common.base.Predicate;
import org.agorava.core.api.ServiceRelated;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: antoine
 * Date: 17/08/12
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class ServiceRelatedPredicate implements Predicate<Annotation> {

    @Override
    public boolean apply(@Nullable Annotation input) {
        return input.annotationType().isAnnotationPresent(ServiceRelated.class);
    }
}
