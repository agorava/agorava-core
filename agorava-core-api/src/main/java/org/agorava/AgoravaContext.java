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

package org.agorava;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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


    /**
     * Scope for the producer
     *
     * @see AgoravaContext#getProducerScope()
     */
    protected static String producerScope = "";

    private static Map<String, Annotation> servicesToQualifier = new HashMap<String, Annotation>();

    private static Map<Annotation, String> qualifierToService = new HashMap<Annotation, String>();

    private static List<String> listOfServices = null;

    protected static String internalCallBack;


    /**
     * @return the way that {@link org.agorava.api.oauth.OAuthSession} and {@link org.agorava.api.storage
     * .UserSessionRepository} are produce and stored
     */
    public static String getProducerScope() {
        return producerScope;
    }

    /**
     * Default protected constructor to prevent instantiation of this class
     */
    protected AgoravaContext() {
    }

    /**
     * @return a map associating provider names to provider qualifiers (qualifiers bearing {@link org.agorava.api.atinject
     * .ProviderRelated} meta-annotation)
     */
    public static Map<String, Annotation> getServicesToQualifier() {
        return servicesToQualifier;
    }

    /**
     * @return a map associating provider qualifiers (qualifiers bearing {@link org.agorava.api.atinject
     * .ProviderRelated} meta-annotation) to provider names
     */
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

    /**
     * @return the list of all service's names present in the application
     */
    public static List<String> getListOfServices() {
        if (listOfServices == null)
            listOfServices = new ArrayList<String>(getSocialRelated());
        return listOfServices;
    }

    /**
     * @return the relative url of the default page to return after an OAuth callback
     */
    public static String getInternalCallBack() {
        return internalCallBack;
    }
}
