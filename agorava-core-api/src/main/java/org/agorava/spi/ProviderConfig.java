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

package org.agorava.spi;

import org.agorava.api.atinject.ProviderRelated;
import org.agorava.api.exception.AgoravaException;

import java.lang.annotation.Annotation;

/**
 * Root of all providers services configuration
 *
 * @author Antoine Sabot-Durand
 */
public abstract class ProviderConfig {

    private final String providerName;

    private final Annotation providerAnnotation;

    /**
     * Default constructor used to find Provider's name in {@link org.agorava.api.atinject.ProviderRelated} meta
     * annotation that must be present on mandatory annotation on all concrete ProviderConfig descendants
     */
    protected ProviderConfig() {

        Annotation res = null;
        Class<? extends ProviderConfig> clazz = getClass();
        for (Annotation a : clazz.getAnnotations()) {
            if (a.annotationType().isAnnotationPresent(ProviderRelated.class)) {
                if (res == null)
                    res = a;
                else
                    throw new AgoravaException("Class " + getClass() + " have more than one ProviderRelated " +
                            "annotation");
            }
        }
        if (res == null)
            throw new AgoravaException("Class " + getClass() + " doesn't have a ProviderRelated annotation");
        providerAnnotation = res;
        providerName = res.annotationType().getAnnotation(ProviderRelated.class).value();

    }

    /**
     * @return the name of the provider that was resolved at construction time
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * @return the Annotation of the provider
     */
    public Annotation getProviderAnnotation() {
        return providerAnnotation;
    }
}
