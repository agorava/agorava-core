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

package org.agorava.core.oauth;

import com.google.common.collect.Iterables;
import org.agorava.core.api.ServiceRelated;
import org.agorava.core.api.SocialMediaApiHub;
import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.oauth.*;
import org.agorava.core.oauth.scribe.OAuthProviderScribe;
import org.jboss.solder.bean.generic.ApplyScope;
import org.jboss.solder.bean.generic.Generic;
import org.jboss.solder.bean.generic.GenericConfiguration;
import org.jboss.solder.logging.Logger;
import org.jboss.solder.reflection.AnnotationInspector;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedMember;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.lang.annotation.Annotation;

/**
 * This Bean is used by JBoss Solder Generic Bean extension to generate multiple bean from one Bean declaration
 * should this one have the {@link OAuthApplication} annotation.
 * All produced beans will have the same Qualifier than the declared bean
 *
 * @author Antoine Sabot-Durand
 */
@GenericConfiguration(OAuthApplication.class)
public class OAuthGenericManager {

    Annotation qual;

    @Inject
    @Generic
    SocialMediaApiHub servicesHub;

    @Inject
    @Generic
    OAuthApplication app;

    @Inject
    @Generic
    private AnnotatedMember<?> annotatedMember;

    @Inject
    Logger log;

    @Inject
    private Event<OAuthService> serviceEventProducer;

    @Produces
    @ApplyScope
    protected OAuthService produceService(OAuthServiceImpl service) {
        service.setQualifier(qual);
        serviceEventProducer.select(qual).fire(service);
        return service;
    }

    OAuthAppSettings settings;

    @Produces
    @ApplyScope
    public OAuthProvider produceProvider() {
        return new OAuthProviderScribe(settings);
    }

    @Produces
    @SessionScoped
    public OAuthSession produceSession() {
        return new OAuthSessionImpl(qual);
    }

    @Inject
    BeanManager beanManager;

    @PostConstruct
    void init() {
        qual = Iterables.getLast(AnnotationInspector.getAnnotations(annotatedMember, ServiceRelated.class));
        log.debugf("in OAuthGenericManager creating Hub for %s", qual.toString());
        Class<? extends OAuthAppSettingsBuilder> builderClass = app.builder();
        OAuthAppSettingsBuilder builderOAuthApp = null;
        try {
            builderOAuthApp = builderClass.newInstance();
        } catch (Exception e) {
            throw new AgoravaException("Unable to create Settings Builder with class " + builderClass, e);
        }

        settings = builderOAuthApp.name(servicesHub.getSocialMediaName()).params(app.params()).build();
    }

}
