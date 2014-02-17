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
import org.agorava.api.atinject.ProviderRelated;
import org.agorava.api.exception.AgoravaException;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.storage.GlobalRepository;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.cdi.deltaspike.DifferentOrNull;
import org.agorava.spi.SessionResolver;
import org.agorava.spi.UserSessionRepositoryResolver;
import org.apache.deltaspike.core.api.exclude.Exclude;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Antoine Sabot-Durand
 */

@ApplicationScoped
@Exclude(onExpression = ApplicationResolver.RESOLVER + ",application", interpretedBy = DifferentOrNull.class)
public class ApplicationResolver implements SessionResolver, UserSessionRepositoryResolver {

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


    @Produces
    protected OAuthSession resolveSession(InjectionPoint ip, @Current UserSessionRepository repository) {
        if (ip == null)
            return repository.getCurrent();
        Set<Annotation> quals = ip.getQualifiers();
        OAuthSession res;

        Annotation service = null;
        boolean iscurrent = false;
        for (Annotation qual : quals) {
            if (qual.annotationType().isAnnotationPresent(ProviderRelated.class)) {
                if (service != null)
                    throw new AgoravaException("There's more thant one provider related qualifier aon Injection Point" +
                            ip);
                service = qual;
            }
            if (qual.annotationType().equals(Current.class))
                iscurrent = true;
        }
        if (iscurrent) {
            if (service != null) {
                if (!service.equals(repository.getCurrent().getServiceQualifier())) {
                    repository.setCurrent(new OAuthSession.Builder().qualifier(service).repo(repository).build());
                }
            }
            return repository.getCurrent();
        }
        throw new UnsupportedOperationException("Cannot inject session whitout Current Qualifier in " + ip);
    }

    @Override
    @Produces
    @Named
    public OAuthSession getCurrentSession(@Current UserSessionRepository repository) {
        return resolveSession(null, repository);

    }
}
