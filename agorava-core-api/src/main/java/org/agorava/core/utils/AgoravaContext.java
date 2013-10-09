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

package org.agorava.core.utils;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class containing configuration for Agorava.
 * Static field here are stored here from third parties helper classes.
 *
 * @author Antoine Sabot-Durand
 */
public class AgoravaContext {

    /**
     * The complete Web Context path (protocol, server, application context) of this Agorava Instance
     */
    public static String webAbsolutePath = "undefined";

    protected static Map<String, Annotation> servicesToQualifier = new HashMap<String, Annotation>();

    protected static Map<Annotation, String> qualifierToService = new HashMap<Annotation, String>();


    protected AgoravaContext() {
    }

    /**
     * @return a {@link BiMap} associating service annotations (annotation bearing {@link org.agorava.core.api.atinject
     * .ProviderRelated} meta-annotation) and their name present in the application
     */
    public static Map<String, Annotation> getServicesToQualifier() {
        return servicesToQualifier;
    }

    public static Map<Annotation, String> getQualifierToService() {
        if (qualifierToService.isEmpty()) {
            for (String s : servicesToQualifier.keySet()) {
                if (qualifierToService.put(servicesToQualifier.get(s), s) != null)
                    throw new IllegalStateException("There's more than one qualifier for name " + s);
            }
        }
        return qualifierToService;
    }

    /**
     * @return the set of all service's names present in the application
     */
    public static Set<String> getSocialRelated() {
        return getServicesToQualifier().keySet();
    }
}
