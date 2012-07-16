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

import org.agorava.core.api.SocialMediaApiHub;
import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.oauth.*;
import org.agorava.core.oauth.scribe.OAuthProviderScribe;
import org.jboss.solder.bean.generic.ApplyScope;
import org.jboss.solder.bean.generic.Generic;
import org.jboss.solder.bean.generic.GenericConfiguration;
import org.jboss.solder.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AnnotatedMember;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.lang.annotation.Annotation;

/**
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

    ApplicationSettings settings;

    @Produces
    @ApplyScope
    public OAuthProvider produceProvider() {
        return new OAuthProviderScribe(settings);
    }

    @Produces
    @SessionScoped
    // qualifier is applied thanks to Generic Beans Extension
    public OAuthSession produceSession() {
        return new OAuthSessionImpl(qual);
    }

    @Inject
    BeanManager beanManager;

    @PostConstruct
    void init() {
        qual = servicesHub.getQualifier();
        log.debugf("in OAuthGenericManager creating Hub for %s", qual.toString());
        Class<? extends SettingsBuilder> builderClass = app.builder();
        SettingsBuilder builder = null;
        try {
            builder = builderClass.newInstance();
        } catch (Exception e) {
            throw new AgoravaException("Unable to create Settings Builder with class " + builderClass, e);
        }

        settings = builder.name(servicesHub.getSocialMediaName()).params(app.params()).build();
    }

}
