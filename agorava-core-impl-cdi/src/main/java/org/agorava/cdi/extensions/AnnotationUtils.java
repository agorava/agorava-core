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

import org.agorava.api.atinject.ProviderRelated;
import org.agorava.api.exception.AgoravaException;

import javax.enterprise.inject.spi.Annotated;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

public class AnnotationUtils implements Serializable {
    /**
     * Inspects an annotated element for any annotations with the given meta
     * annotation. This should only be used for user defined meta annotations,
     * where the annotation must be physically present.
     *
     * @param element            The element to inspect
     * @param metaAnnotationType The meta annotation to search for
     * @return The annotation instances found on this element or an empty set if
     * no matching meta-annotation was found.
     */
    public static Set<Annotation> getAnnotationsWithMeta(Annotated element, final Class<? extends Annotation>
            metaAnnotationType) {
        return getAnnotationsWithMeta(element.getAnnotations(), metaAnnotationType);
    }

    public static Set<Annotation> getAnnotationsWithMeta(Set<Annotation> qualifiers, final Class<? extends Annotation>
            metaAnnotationType) {
        Set<Annotation> annotations = new HashSet<Annotation>();
        for (Annotation annotation : qualifiers) {
            if (annotation.annotationType().isAnnotationPresent(metaAnnotationType)) {
                annotations.add(annotation);
            }
        }
        return annotations;
    }

    public static Annotation getSingleProviderRelatedQualifier(Set<Annotation> qualifiers, boolean emptyAccepted) {
        Set<Annotation> annotations = getAnnotationsWithMeta(qualifiers, ProviderRelated.class);
        if (annotations.size() == 0)
            if (emptyAccepted)
                return null;
            else
                throw new AgoravaException("No ProviderRelated qualifier found");
        else if (annotations.size() > 1)
            throw new AgoravaException("Type should not have more than one ProviderRelated Qualifier");
        return annotations.iterator().next();
    }


}