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

package org.agorava.cdi.extensions;

import org.agorava.api.atinject.BeanResolver;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 * @author Antoine Sabot-Durand
 */
public class BeanResolverCdi extends BeanResolver {

    public BeanResolverCdi() {
        BeanResolver.instance = this;
    }

    @Override
    public Object resolve(String name) {

        return BeanProvider.getContextualReference(name, false);
    }

    @Override
    public <T> T resolve(Class<T> clazz) {
        return BeanProvider.getContextualReference(clazz, false);
    }

    @Override
    public Object resolve(String name, boolean optional) {

        return BeanProvider.getContextualReference(name, optional);
    }

    @Override
    public <T> T resolve(Class<T> clazz, boolean optional) {
        return BeanProvider.getContextualReference(clazz, optional);
    }
}