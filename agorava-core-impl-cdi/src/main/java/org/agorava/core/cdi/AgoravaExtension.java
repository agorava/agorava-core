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

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import org.agorava.core.api.ServiceRelated;
import org.agorava.core.api.SocialMediaApiHub;
import org.agorava.core.oauth.OAuthApplication;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.reflection.AnnotationInspector;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * @author Antoine Sabot-Durand
 */
@ApplicationScoped
public class AgoravaExtension implements Extension, Serializable {

    private final Set<String> servicesNames = newHashSet();
    private final Set<Annotation> servicesQualifiersConfigured = newHashSet();
    private static Set<Annotation> servicesQualifiersAvailable = newHashSet();
    private static BiMap<Annotation, String> servicesToQualifier = HashBiMap.create();
    private boolean multiSession = false;

    private static final Logger log = Logger.getLogger(AgoravaExtension.class);

    private void internalProcessApiHub(Annotated annotated) {
        log.infof("Found services hub %s", annotated.getBaseType());

        Set<Annotation> qualifiers = AnnotationInspector.getAnnotations(annotated, ServiceRelated.class);
        servicesQualifiersAvailable.addAll(qualifiers);
        if (annotated.isAnnotationPresent(OAuthApplication.class)) {
            log.debug("Bean is configured");
            servicesQualifiersConfigured.addAll(qualifiers);
        }
    }

    public void launchExtension(@Observes BeforeBeanDiscovery bbd) {
        log.info("Starting Agorava Framework initialization");
    }

    /**
     * This observer methods build the list of existing Qualifiers having the ServiceRelated meta Annotation on configured
     * service (having the {@link OAuthApplication} Annotation)
     *
     * @param pbean
     * @param beanManager
     */
    public void processApiHubProducer(@Observes ProcessProducer<?, SocialMediaApiHub> pbean, BeanManager beanManager) {
        Annotated annotated = pbean.getAnnotatedMember();
        internalProcessApiHub(annotated);
    }


    /**
     * This observer methods build the list of existing Qualifiers having the ServiceRelated meta Annotation on configured
     * service (having the {@link OAuthApplication} Annotation)
     *
     * @param pbean
     * @param beanManager
     */
    public void processApiHubBeans(@Observes ProcessBean<SocialMediaApiHub> pbean, BeanManager beanManager) {
        Annotated annotated = pbean.getAnnotated();
        internalProcessApiHub(annotated);
    }

    public Set<String> getSocialRelated() {
        return servicesNames;
    }

    public void processAfterDeploymentValidation(@Observes AfterDeploymentValidation adv, BeanManager beanManager) {

        CreationalContext ctx = beanManager.createCreationalContext(null);

        for (Annotation qual : servicesQualifiersAvailable) {
            Bean beanSoc = Iterables.getLast(beanManager.getBeans(SocialMediaApiHub.class, qual));
            SocialMediaApiHub smah = (SocialMediaApiHub) beanManager.getReference(beanSoc, beanSoc.getBeanClass(), ctx);
            String name = smah.getSocialMediaName();
            servicesToQualifier.put(qual, name);
        }
        ctx.release();
        for (Annotation annot : servicesQualifiersConfigured) {
            servicesNames.add(servicesToQualifier.get(annot));
        }

        log.info("Agorava initialization complete");

    }

    public static BiMap<Annotation, String> getServicesToQualifier() {
        return servicesToQualifier;
    }

    public static Set<Annotation> getServicesQualifiersAvailable() {
        return servicesQualifiersAvailable;
    }

    public boolean isMultiSession() {
        return multiSession;
    }

    public void setMultiSession(boolean multiSession) {
        this.multiSession = multiSession;
    }

}
