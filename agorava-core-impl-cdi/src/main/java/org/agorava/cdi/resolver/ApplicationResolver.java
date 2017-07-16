/*
 * Copyright 2014 Agorava
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

package org.agorava.cdi.resolver;

import org.agorava.api.atinject.Current;
import org.agorava.api.exception.AgoravaException;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.storage.GlobalRepository;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.cdi.config.DifferentOrNull;
import org.agorava.cdi.extensions.AnnotationUtils;
import org.agorava.spi.OAuthSessionResolver;
import org.agorava.spi.UserSessionRepositoryResolver;
import org.apache.deltaspike.core.api.exclude.Exclude;

import java.lang.annotation.Annotation;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Antoine Sabot-Durand
 */

@ApplicationScoped
@Exclude(onExpression = ApplicationResolver.RESOLVER + ",application", interpretedBy = DifferentOrNull.class)
public class ApplicationResolver implements OAuthSessionResolver, UserSessionRepositoryResolver {

    public static final String RESOLVER = "resolverType";

    @Inject
    GlobalRepository globalRepository;


    @Override
    @Produces
    @Current
    @Named("currentRepo")
    @ApplicationScoped
    public UserSessionRepository getCurrentRepository() {
        return globalRepository.createNew();
    }


    /**
     * This producer is enhanced by extension to bear all RelatedProvider
     * It should find an existing session depending on the requested provider (annotation on InjectionPoint)
     *
     * @param ip
     * @param repository
     * @return
     */
    @Produces
    protected OAuthSession resolveOAuthSession(InjectionPoint ip, @Current UserSessionRepository repository) {
        if (ip == null) {
            throw new UnsupportedOperationException("Cannot resolve session for a null InjectionPoint");
        }
        Annotation qual = AnnotationUtils.getSingleProviderRelatedQualifier(ip.getQualifiers(), false);
        OAuthSession res = repository.getForProvider(qual);
        if (res.isNull()) {
            throw new AgoravaException("There's no active session for requested provider");
        }
        return res;
    }

    @Override
    @Produces
    @Named
    @Current
    public OAuthSession getCurrentSession(@Current UserSessionRepository repository) {
        return repository.getCurrent();

    }
}
