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

import org.apache.deltaspike.core.api.exclude.annotation.Exclude;

//import org.agorava.utils.solder.logging.Logger;

/**
 * This Bean is used by JBoss Solder Generic Bean extension to generate multiple bean from one Bean declaration
 * should this one have the {@link OAuthApplication} annotation.
 * All produced beans will have the same Qualifier than the declared bean
 *
 * @author Antoine Sabot-Durand
 */
//@GenericConfiguration(OAuthApplication.class)
@Exclude
public class OAuthGenericManager {
/*
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
        service.qualifier = qual;
        serviceEventProducer.select(qual).fire(service); //Allow an observer to customize service (specific HTTP header for instance)
        return service;
    }

    OAuthAppSettings settings;

    @Produces
    @ApplyScope
    public OAuthProvider produceProvider() {
        return new OAuthProviderScribe(settings);
    }

    @Produces
    @Web
    @SessionScoped
    public OAuthSession produceWebSession() {
        return new OAuthSessionImpl(qual);
    }

    @Produces
    @Default
    @ApplyScope
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
*/
}
