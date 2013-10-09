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

package org.agorava.core.cdi.extensions;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.agorava.core.api.atinject.GenericBean;
import org.agorava.core.api.atinject.InjectWithQualifier;
import org.agorava.core.api.atinject.OAuth;
import org.agorava.core.api.atinject.ProviderRelated;
import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.application.OAuthAppSettings;
import org.agorava.core.api.oauth.application.OAuthAppSettingsBuilder;
import org.agorava.core.api.oauth.application.OAuthApplication;
import org.agorava.core.oauth.OAuthSessionImpl;
import org.agorava.core.spi.ProviderConfigOauth;
import org.agorava.core.utils.AgoravaContext;
import org.apache.deltaspike.core.api.literal.AnyLiteral;
import org.apache.deltaspike.core.util.bean.BeanBuilder;
import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;

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
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
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
public class AgoravaExtension extends AgoravaContext implements Extension, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Set<Annotation> servicesQualifiersConfigured = newHashSet();

    private static Logger log = Logger.getLogger(AgoravaExtension.class.getName());

    private Map<Annotation, Set<Type>> overridedGenericServices = new HashMap<Annotation, Set<Type>>();

    private Map<OAuth.OAuthVersion, Class<? extends OAuthService>> genericsOAuthProviders = Maps.newHashMap();

    private Map<Annotation, OAuth.OAuthVersion> service2OauthVersion = new HashMap<Annotation, OAuth.OAuthVersion>();


    /**
     * @return the set of all service's qualifiers present in the application
     */
    public static Collection<Annotation> getServicesQualifiersAvailable() {
        return getServicesToQualifier().values();
    }


    //-------------------- Utilities -------------------------------------

    void applyQualifier(Annotation qual, AnnotatedType<?> at, AnnotatedTypeBuilder<?> atb) {
        //do a loop on all field to replace annotation mark by CDI annotations
        for (AnnotatedField af : at.getFields())
            if (af.isAnnotationPresent(InjectWithQualifier.class)) {
                atb.addToField(af, InjectLiteral.instance);
                atb.addToField(af, qual);

            }

        //loop on constructors to do the same
        for (AnnotatedConstructor ac : at.getConstructors()) {
            Annotation[][] pa = ac.getJavaMember().getParameterAnnotations();
            //loop on args to detect marked param
            for (int i = 0; i < pa.length; i++)
                for (int j = 0; j < pa[i].length; j++)
                    if (pa[i][j].equals(InjectWithQualifierLiteral.instance)) {
                        atb.addToConstructor(ac, InjectLiteral.instance);
                        atb.addToConstructorParameter(ac.getJavaMember(), i, qual);
                    }

        }

        //loop on other methods (setters)
        for (AnnotatedMethod am : at.getMethods())
            if (am.isAnnotationPresent(InjectWithQualifierLiteral.class)) {
                atb.addToMethod(am, InjectLiteral.instance);
                atb.addToMethod(am, qual);
            }

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
        if (!at.isAnnotationPresent(GenericBean.class)) {
            log.log(INFO, "Found a Bean of class {0} overriding generic bean", at.getBaseType());
            Annotation qual = AnnotationUtils.getSingleProviderRelatedQualifier(at.getAnnotations(), true);
            if (qual != null) {
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

    public void processGenericOauthProvider(@Observes ProcessAnnotatedType<? extends OAuthService> pat) {
        if (processGenericAnnotatedType(pat)) {
            AnnotatedType<? extends OAuthService> at = pat.getAnnotatedType();
            Class<? extends OAuthService> clazz = (Class<? extends OAuthService>) at.getBaseType();
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
     * This observer decorates the produced {@link org.agorava.core.api.oauth.application.OAuthAppSettingsImpl} by injecting
     * its own qualifier and service name
     * and build the list of Qualifiers bearing the ProviderRelated meta annotation (configured services)
     *
     * @param pp the Process producer event
     */
    public void processOAuthSettingsProducer(@Observes final ProcessProducer<?, OAuthAppSettings> pp) {
        final AnnotatedMember<OAuthAppSettings> annotatedMember = (AnnotatedMember<OAuthAppSettings>) pp.getAnnotatedMember();

        Annotation qual = null;
        try {
            qual = Iterables.getLast(AnnotationUtils.getAnnotationsWithMeta(annotatedMember,
                    ProviderRelated.class));
        } catch (NoSuchElementException e) {
            pp.addDefinitionError(new AgoravaException("OAuthAppSettings producers should be annotated with a Service " +
                    "Provider on " + annotatedMember.getJavaMember().getName() + " in " + annotatedMember.getJavaMember()
                    .getDeclaringClass()));
        }


        if (annotatedMember.isAnnotationPresent(OAuthApplication.class)) {
            if (annotatedMember instanceof AnnotatedField) {

                final OAuthApplication app = annotatedMember.getAnnotation(OAuthApplication.class);

                OAuthAppSettingsBuilder builderOAuthApp = null;
                try {
                    builderOAuthApp = app.builder().newInstance();
                } catch (Exception e) {
                    pp.addDefinitionError(new AgoravaException("Unable to create Settings Builder with class " +
                            app.builder(), e));
                }

                builderOAuthApp.qualifier(qual)
                        .params(app.params());

                pp.setProducer(new OAuthAppSettingsProducerWithBuilder(builderOAuthApp, qual));
            } else
                pp.addDefinitionError(new AgoravaException("@OAuthApplication are only supported on Field. Agorava cannot " +
                        "process producer " + annotatedMember.getJavaMember().getName() + " in class " + annotatedMember
                        .getJavaMember().getDeclaringClass()));
        } else {
            final Producer<OAuthAppSettings> oldProducer = pp.getProducer();
            pp.setProducer(new OAuthAppSettingsProducerDecorator(oldProducer, qual));
        }


        log.log(INFO, "Found settings for {0}", qual);
        servicesQualifiersConfigured.add(qual);
    }


    //----------------- Process Bean Phase ----------------------------------

    /*
     * This does practically not do much anymore after the discovery was moved
     * to AfterDeploymentValidation. see https://issues.jboss.org/browse/CDI-274
     * Kept around to do simple deployment validation of ProviderRelated qualifier.
     */
    private void CommonsProcessOAuthTier(ProcessBean<? extends ProviderConfigOauth> pb) {

        Annotated annotated = pb.getAnnotated();
        Set<Annotation> qualifiers = AnnotationUtils.getAnnotationsWithMeta(annotated, ProviderRelated.class);
        if (qualifiers.size() != 1)
            throw new AgoravaException("A RemoteService bean should have one and only one service related Qualifier : " + pb
                    .getAnnotated().toString());

        Class<? extends ProviderConfigOauth> clazz = (Class<? extends ProviderConfigOauth>) pb.getBean().getBeanClass();
        try {
            service2OauthVersion.put(Iterables.getOnlyElement(qualifiers), clazz.newInstance().getOAuthVersion());
        } catch (Exception e) {
            throw new AgoravaException("Error while retrieving version of OAuth in tier config", e);
        }
    }

    public void processRemoteServiceRoot(@Observes ProcessBean<? extends ProviderConfigOauth> pb) {
        CommonsProcessOAuthTier(pb);
    }

    private void captureGenericOAuthService(@Observes ProcessBean<? extends OAuthService> pb) {
        Bean<? extends OAuthService> bean = pb.getBean();
        if (bean.getQualifiers().contains(GenericBeanLiteral.INSTANCE)) {
            log.info("here");
        }
    }

    /*private void captureGenericOauthSession(@Observes ProcessBean<? extends OAuthSession> pb) {
        Bean<? extends OAuthSession> bean = pb.getBean();
        if (bean.getQualifiers().contains(GenericBeanLiteral.INSTANCE)) {
            genericOAuthSession = bean;
        }
    }

    private void captureGenericOAuthProvider(@Observes ProcessBean<? extends OAuthProvider> pb) {
        Bean<? extends OAuthProvider> bean = pb.getBean();

        if (bean.getQualifiers().contains(GenericBeanLiteral.INSTANCE)) {
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
     * After all {@link org.agorava.core.api.oauth.application.OAuthAppSettingsImpl} were discovered we get their bean to
     * retrieve the actual name of Social Media
     * and associates it with the corresponding Qualifier
     *
     * @param abd
     * @param beanManager
     */
    public void registerGenericBeans(@Observes AfterBeanDiscovery abd, BeanManager beanManager) {


        for (Annotation qual : servicesQualifiersConfigured) {
            OAuth.OAuthVersion version = service2OauthVersion.get(qual);

            Class clazz = genericsOAuthProviders.get(version);

            beanRegisterer(clazz, qual, Dependent.class, abd, beanManager, OAuthService.class);
            beanRegisterer(OAuthSessionImpl.class, qual, Dependent.class, abd, beanManager);
        }

    }


    //--------------------- After Deployment validation phase

    public void endOfExtension(@Observes AfterDeploymentValidation adv, BeanManager beanManager) {

        registerServiceNames(beanManager);

        log.info("Agorava initialization complete");
    }

    private void registerServiceNames(BeanManager beanManager) {
        Set<Bean<?>> beans = beanManager.getBeans(ProviderConfigOauth.class, new AnyLiteral());

        for (Bean<?> bean : beans) {
            Set<Annotation> qualifiers = AnnotationUtils.getAnnotationsWithMeta(bean.getQualifiers(), ProviderRelated.class);
            Annotation qual = Iterables.getOnlyElement(qualifiers);
            CreationalContext<?> ctx = beanManager.createCreationalContext(null);
            final ProviderConfigOauth tierConfig = (ProviderConfigOauth) beanManager.getReference(bean,
                    ProviderConfigOauth.class, ctx);
            String name = tierConfig.getProviderName();
            getServicesToQualifier().put(name, qual);
            ctx.release();
        }
        if (servicesQualifiersConfigured.size() != getServicesToQualifier().size())
            log.log(WARNING, "Some Service modules present in the application are not configured so won't be available");
        //TODO:list the service without config

    }


}
