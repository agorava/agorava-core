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
import org.agorava.core.api.exception.AgoravaException;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.reflection.AnnotationInspector;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

/**
 * Agorava CDI extension to discover existing module and configured modules
 *
 * @author Antoine Sabot-Durand
 */
public class AgoravaExtension implements Extension, Serializable {

    private static final Set<String> servicesNames = newHashSet();
    private static final Set<Annotation> servicesQualifiersConfigured = newHashSet();
    private static Set<Annotation> servicesQualifiersAvailable = newHashSet();
    private static BiMap<String, Annotation> servicesToQualifier = HashBiMap.create();
    private static boolean multiSession = false;
    private static Set<Annotation> qualifiersOnAuthService = newHashSet();

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
     * This observer methods build the list of existing Qualifiers having the ServiceRelated meta Annotation on producers
     * having the {@link OAuthApplication} Annotation
     *
     * @param pbean
     */
    public void processApiHubProducer(@Observes ProcessProducer<?, SocialMediaApiHub> pbean) {
        Annotated annotated = pbean.getAnnotatedMember();
        internalProcessApiHub(annotated);
    }


    /**
     * This observer methods build the list of existing Qualifiers having the ServiceRelated meta Annotation on configured
     * service (having the {@link OAuthApplication} Annotation)
     *
     * @param pbean
     */
    public void processApiHubBeans(@Observes ProcessBean<SocialMediaApiHub> pbean) {
        Annotated annotated = pbean.getAnnotated();
        internalProcessApiHub(annotated);
    }

/*
    public void processOAuthServiceBeans(@Observes ProcessBean<OAuthService> pBean) {
        Set<Annotation> qualifiers = pBean.getBean().getQualifiers();
        Set<Annotation> serviceQualifiers = Sets.filter(qualifiers, new ServiceRelatedPredicate());
        if (serviceQualifiers.size() > 0) {
            try {
                qualifiersOnAuthService.add(Iterables.getOnlyElement(serviceQualifiers));
            } catch (Exception e) {
                throw new AgoravaException("OAuthService should have zero or one Service Related Qualifier", e);
            }
        }
    }


    public void registerOAuthServices(@Observes AfterBeanDiscovery abd, BeanManager bm)
    {
        BeanBuilder<OAuthServiceImpl> bb;
        AnnotatedTypeBuilder<OAuthServiceImpl> ab=new AnnotatedTypeBuilder();
        ab.readFromType(OAuthServiceImpl.class);
        AnnotatedType<OAuthServiceImpl> at=ab.create();
        for (Annotation annot : servicesQualifiersAvailable)
                    {
            if (! qualifiersOnAuthService.contains(annot))
            {
                bb=new BeanBuilder(bm);
                bb.readFromType(at);
                bb.addQualifier(annot);
                abd.addBean(bb.create());
            }
        }
    }
*/

    public static Set<String> getSocialRelated() {
        return servicesNames;
    }

    /**
     * After all {@link SocialMediaApiHub} were discovered we get there bean to retrieve the actual name of Social Media
     * and associates it with the corresponding Qualifier
     *
     * @param adv
     * @param beanManager
     */
    public void processAfterDeploymentValidation(@Observes AfterDeploymentValidation adv, BeanManager beanManager) {

        CreationalContext ctx = beanManager.createCreationalContext(null);

        for (Annotation qual : servicesQualifiersAvailable) {
            Bean beanSoc = Iterables.getLast(beanManager.getBeans(SocialMediaApiHub.class, qual));
            SocialMediaApiHub smah = (SocialMediaApiHub) beanManager.getReference(beanSoc, beanSoc.getBeanClass(), ctx);
            String name = smah.getSocialMediaName();
            if (servicesToQualifier.containsKey(name) && ! servicesToQualifier.get(name).equals(qual)) {
                throw new AgoravaException("This social media name : " + name + " is already registered with another qualifier. Check your modules or you SocialMediaApiHub producers.");
            }
            servicesToQualifier.put(name, qual);
        }
        ctx.release();
        for (Annotation annot : servicesQualifiersConfigured) {
            servicesNames.add(servicesToQualifier.inverse().get(annot));
        }

        log.info("Agorava initialization complete");

    }

    public static BiMap<String, Annotation> getServicesToQualifier() {
        return servicesToQualifier;
    }

    public static Set<Annotation> getServicesQualifiersAvailable() {
        return servicesQualifiersAvailable;
    }

    public static boolean isMultiSession() {
        return multiSession;
    }

    public static void setMultiSession(boolean ms) {
        multiSession = ms;
    }

}
