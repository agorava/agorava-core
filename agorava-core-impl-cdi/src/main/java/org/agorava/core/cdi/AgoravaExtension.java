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

package org.agorava.core.cdi;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.agorava.core.api.ApplyQualifier;
import org.agorava.core.api.GenericRoot;
import org.agorava.core.api.OAuth;
import org.agorava.core.api.OAuthVersion;
import org.agorava.core.api.ServiceRelated;
import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.oauth.OAuthAppSettings;
import org.agorava.core.api.oauth.OAuthProvider;
import org.agorava.core.oauth.OAuthSessionImpl;
import org.agorava.core.spi.TierConfigOauth;
import org.apache.deltaspike.core.api.literal.AnyLiteral;
import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMember;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.Producer;
import javax.inject.Inject;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static com.google.common.collect.Sets.newHashSet;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

/**
 * Agorava CDI extension to discover existing module and configured modules
 *
 * @author Antoine Sabot-Durand
 */
public class AgoravaExtension implements Extension, Serializable {

    private static final long serialVersionUID = 1L;
    private static final Set<Annotation> servicesQualifiersConfigured = newHashSet();
    private static Logger log = Logger.getLogger(AgoravaExtension.class.getName());
    private static BiMap<String, Annotation> servicesToQualifier = HashBiMap.create();
    private static boolean multiSession = false;
    private Map<Annotation, Set<Type>> overridedGenericServices = new HashMap<Annotation, Set<Type>>();
    private Map<OAuthVersion, Class<? extends OAuthProvider>> genericsOAuthProviders = Maps.newHashMap();
    private Map<Annotation, OAuthVersion> service2OauthVersion = new HashMap<Annotation, OAuthVersion>();

    /**
     * @return the set of all service's names present in the application
     */
    public static Set<String> getSocialRelated() {
        return servicesToQualifier.keySet();
    }

    /**
     * @return a {@link BiMap} associating service annotations (annotation bearing {@link ServiceRelated} meta-annotation) and their name present in the application
     */
    public static BiMap<String, Annotation> getServicesToQualifier() {
        return servicesToQualifier;
    }

    /**
     * @return the set of all service's qualifiers present in the application
     */
    public static Set<Annotation> getServicesQualifiersAvailable() {
        return servicesToQualifier.values();
    }

    /**
     * @return a boolean indicating if the application uses multi sessions or not
     */
    public static boolean isMultiSession() {
        return multiSession;
    }

    public static void setMultiSession(boolean ms) {
        multiSession = ms;
    }


    //-------------------- Utilities -------------------------------------

    /**
     * Inspects an annotated element for any annotations with the given meta
     * annotation. This should only be used for user defined meta annotations,
     * where the annotation must be physically present.
     *
     * @param element            The element to inspect
     * @param metaAnnotationType The meta annotation to search for
     * @return The annotation instances found on this element or an empty set if
     *         no matching meta-annotation was found.
     */
    public static Set<Annotation> getAnnotationsWithMeta(Annotated element, final Class<? extends Annotation> metaAnnotationType) {
        return getAnnotationsWithMeta(element.getAnnotations(), metaAnnotationType);
    }

    public static Set<Annotation> getAnnotationsWithMeta(Set<Annotation> qualifiers, final Class<? extends Annotation> metaAnnotationType) {
        Set<Annotation> annotations = new HashSet<Annotation>();
        for (Annotation annotation : qualifiers) {
            if (annotation.annotationType().isAnnotationPresent(metaAnnotationType)) {
                annotations.add(annotation);
            }
        }
        return annotations;
    }

    void applyQualifier(Annotation qual, AnnotatedType<?> at, AnnotatedTypeBuilder<?> atb) {
        //do a loop on all field to replace annotation mark by CDI annotations
        for (AnnotatedField af : at.getFields())
            if (af.isAnnotationPresent(ApplyQualifier.class))
                atb.addToField(af, qual);


        //loop on constructors to do the same
        for (AnnotatedConstructor ac : at.getConstructors()) {
            if (ac.isAnnotationPresent(Inject.class)) {
                Annotation[][] pa = ac.getJavaMember().getParameterAnnotations();
                //loop on args to detect marked param
                for (int i = 0; i < pa.length; i++)
                    for (int j = 0; j < pa[i].length; j++)
                        if (pa[i][j].equals(ApplyQualifierLiteral.instance))
                            atb.addToConstructorParameter(ac.getJavaMember(), i, qual);
            }
        }

        //loop on other methods (setters)
        for (AnnotatedMethod am : at.getMethods())
            if (am.isAnnotationPresent(ApplyQualifierLiteral.class))
                atb.addToMethod(am, qual);


    }


    //----------------- Before Bean discovery Phase ----------------------------------

    /**
     * This observer is started at the beginning of the extension
     *
     * @param bbd
     */
    public void launchExtension(@Observes BeforeBeanDiscovery bbd) {
        log.info("Starting Agorava Framework initialization");
    }


    //----------------- Process AnnotatedType Phase ----------------------------------

    private <T> boolean processGenericAnnotatedType(ProcessAnnotatedType<T> pat) {
        AnnotatedType<T> at = pat.getAnnotatedType();
        if (!at.isAnnotationPresent(GenericRoot.class)) {
            log.log(INFO, "Found a Bean of class {0} overriding generic bean", at.getBaseType());
            Set<Annotation> qualifiers = AgoravaExtension.getAnnotationsWithMeta(at, ServiceRelated.class);
            if (qualifiers.size() != 0) {
                if (qualifiers.size() > 1)
                    throw new AgoravaException("Beans with multiple Service Related Qualifier are not supported");
                Annotation qual = Iterables.getOnlyElement(qualifiers);
                if (overridedGenericServices.containsKey(qual))
                    overridedGenericServices.get(qual).addAll(at.getTypeClosure());
                else
                    overridedGenericServices.put(qual, new HashSet<Type>(at.getTypeClosure()));

                Class<T> clazz = (Class) at.getBaseType();

                AnnotatedTypeBuilder<T> atb = new AnnotatedTypeBuilder<T>()
                        .readFromType(clazz)
                        .setJavaClass(clazz);

                applyQualifier(qual, atb.create(), atb);
                pat.setAnnotatedType(atb.create());
            }
            return false;

        } else {
            pat.veto();
            return true;
        }
    }

    public void processGenericOauthService(@Observes ProcessAnnotatedType<? extends OAuthServiceImpl> pat) {
        processGenericAnnotatedType(pat);
    }

    public void processGenericOauthProvider(@Observes ProcessAnnotatedType<? extends OAuthProvider> pat) {
        if (processGenericAnnotatedType(pat)) {
            AnnotatedType<? extends OAuthProvider> at = pat.getAnnotatedType();
            Class<? extends OAuthProvider> clazz = (Class<? extends OAuthProvider>) at.getBaseType();
            OAuth qualOauth = at.getAnnotation(OAuth.class);
            genericsOAuthProviders.put(qualOauth.value(), clazz);

        }
        ;
    }

    public void processGenericSession(@Observes ProcessAnnotatedType<? extends OAuthSessionImpl> pat) {
        processGenericAnnotatedType(pat);
    }


    //----------------- Process Producer Phase ----------------------------------

    /**
     * This observer decorates the produced {@link OAuthAppSettings} by injecting its own qualifier and service name
     * and build the list of Qualifiers bearing the ServiceRelated meta annotation (configured services)
     *
     * @param pp the Process producer event
     */
    public void processOAuthSettingsProducer(@Observes final ProcessProducer<?, OAuthAppSettings> pp) {
        final AnnotatedMember<OAuthAppSettings> annotatedMember = (AnnotatedMember<OAuthAppSettings>) pp.getAnnotatedMember();
        final Annotation qual = Iterables.getLast(AgoravaExtension.getAnnotationsWithMeta(annotatedMember, ServiceRelated.class));
        final Producer<OAuthAppSettings> oldProducer = pp.getProducer();

        if (annotatedMember.isAnnotationPresent(OAuthApplication.class)) {
     /* TODO:CODE below for future support of OAuthAppSettings creation via annotation

     final OAuthApplication app = annotatedMember.getAnnotation(OAuthApplication.class);

        Class<? extends OAuthAppSettingsBuilder> builderClass = app.builder();
        OAuthAppSettingsBuilder builderOAuthApp = null;
        try {
            builderOAuthApp = builderClass.newInstance();
        } catch (Exception e) {
            throw new AgoravaException("Unable to create Settings Builder with class " + builderClass, e);
        }

        final OAuthAppSettingsBuilder finalBuilderOAuthApp = builderOAuthApp; */
        }

        pp.setProducer(new OAuthAppSettingsProducerDecorator(oldProducer, qual));

        log.log(INFO, "Found settings for {0}", qual);
        servicesQualifiersConfigured.add(qual);

        //settings = builderOAuthApp.name(servicesHub.getSocialMediaName()).params(app.params()).build();
    }


    //----------------- Process Bean Phase ----------------------------------

    /*
     * This does practically not do much anymore after the discovery was moved
     * to AfterDeploymentValidation. see https://issues.jboss.org/browse/CDI-274
     * Kept around to do simple deployment validation of ServiceRelated qualifier.
     */
    private void CommonsProcessOAuthTier(ProcessBean<? extends TierConfigOauth> pb) {

        Annotated annotated = pb.getAnnotated();
        Set<Annotation> qualifiers = AgoravaExtension.getAnnotationsWithMeta(annotated, ServiceRelated.class);
        if (qualifiers.size() != 1)
            throw new AgoravaException("A RemoteService bean should have one and only one service related Qualifier : " + pb.getAnnotated().toString());

        Class<? extends TierConfigOauth> clazz = (Class<? extends TierConfigOauth>) pb.getBean().getBeanClass();
        try {
            service2OauthVersion.put(Iterables.getOnlyElement(qualifiers), clazz.newInstance().getOAuthVersion());
        } catch (ReflectiveOperationException e) {
            throw new AgoravaException("Error while retrieving version of OAuth in tier config", e);
        }
    }

    public void processRemoteServiceRoot(@Observes ProcessBean<? extends TierConfigOauth> pb) {
        CommonsProcessOAuthTier(pb);
    }


  /*  private void captureGenericOAuthService(@Observes ProcessBean<? extends OAuthService> pb) {
        Bean<? extends OAuthService> bean = pb.getBean();
        if (bean.getQualifiers().contains(GenericRootLiteral.INSTANCE)) {
            genericOAuthService = bean;
        }
    }

    private void captureGenericOauthSession(@Observes ProcessBean<? extends OAuthSession> pb) {
        Bean<? extends OAuthSession> bean = pb.getBean();
        if (bean.getQualifiers().contains(GenericRootLiteral.INSTANCE)) {
            genericOAuthSession = bean;
        }
    }

    private void captureGenericOAuthProvider(@Observes ProcessBean<? extends OAuthProvider> pb) {
        Bean<? extends OAuthProvider> bean = pb.getBean();

        if (bean.getQualifiers().contains(GenericRootLiteral.INSTANCE)) {
            for (Annotation annotation : bean.getQualifiers()) {
                if (annotation instanceof OAuth) {
                    genericsOAuthProviders.put(((OAuth) annotation).value(), bean);

                }
            }
        }
    }*/


    //----------------- Process After Bean Discovery Phase ----------------------------------

    private <T> void beanRegisterer(Class<T> clazz, Annotation qual, Class<? extends Annotation> scope, AfterBeanDiscovery abd,
                                    BeanManager beanManager, Type... types) {

        if (!(overridedGenericServices.containsKey(qual) && overridedGenericServices.get(qual).contains(clazz))) {

            AnnotatedType<T> at = beanManager.createAnnotatedType(clazz);
            AnnotatedTypeBuilder<T> atb = new AnnotatedTypeBuilder<T>()
                    .readFromType(clazz)
                    .addToClass(qual)
                    .setJavaClass(clazz);

            applyQualifier(qual, at, atb);

            BeanBuilder<T> providerBuilder = new BeanBuilder<T>(beanManager)
                    .readFromType(atb.create())
                    .scope(scope)
                    .passivationCapable(true)
                    .addTypes(types);

            Bean<T> newBean = providerBuilder.create();
            abd.addBean(newBean);
        }
    }

    /**
     * After all {@link OAuthAppSettings} were discovered we get their bean to retrieve the actual name of Social Media
     * and associates it with the corresponding Qualifier
     *
     * @param abd
     * @param beanManager
     */
    public void registerGenericBeans(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {


        for (Annotation qual : servicesQualifiersConfigured) {
            OAuthVersion version = service2OauthVersion.get(qual);

            Class clazz = genericsOAuthProviders.get(version);

            beanRegisterer(clazz, qual, ApplicationScoped.class, abd, beanManager, OAuthProvider.class);
            beanRegisterer(OAuthSessionImpl.class, qual, Dependent.class, abd, beanManager);
            beanRegisterer(OAuthServiceImpl.class, qual, ApplicationScoped.class, abd, beanManager);
        }

    }


    //--------------------- After Deployment validation phase

    public void endOfExtension(@Observes AfterDeploymentValidation adv, BeanManager beanManager) {

        registerServiceNames(beanManager);

        log.info("Agorava initialization complete");
    }

    private void registerServiceNames(BeanManager beanManager) {
        Set<Bean<?>> beans = beanManager.getBeans(TierConfigOauth.class, new AnyLiteral());

        for (Bean<?> bean : beans) {
            Set<Annotation> qualifiers = getAnnotationsWithMeta(bean.getQualifiers(), ServiceRelated.class);
            Annotation qual = Iterables.getOnlyElement(qualifiers);
            CreationalContext<?> ctx = beanManager.createCreationalContext(null);
            final TierConfigOauth tierConfig = (TierConfigOauth) beanManager.getReference(bean, TierConfigOauth.class, ctx);
            String name = tierConfig.getServiceName();
            servicesToQualifier.put(name, qual);
            ctx.release();
        }
        if (servicesQualifiersConfigured.size() != servicesToQualifier.size())
            log.log(WARNING, "Some Service modules present in the application are not configured so won't be available"); //TODO:list the service without config

    }


}
