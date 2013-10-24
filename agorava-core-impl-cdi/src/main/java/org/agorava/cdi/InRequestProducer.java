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

package org.agorava.cdi;

import org.agorava.api.atinject.Current;
import org.agorava.api.atinject.ProviderRelated;
import org.agorava.api.exception.AgoravaException;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.oauth.OAuthSessionBuilder;
import org.agorava.api.storage.UserSessionRepository;
import org.apache.deltaspike.core.api.exclude.Exclude;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Antoine Sabot-Durand
 */

@RequestScoped
@Exclude(onExpression = "producerScope!=request")
public class InRequestProducer implements Serializable {

    private static final long serialVersionUID = 6446160199657772110L;

    @Inject
    UserSessionRepository repository;


    @Produces
    @Current
    @RequestScoped
    public UserSessionRepository getCurrentRepo() {
        return repository;
    }


    @Produces
    public OAuthSession getCurrentSession(InjectionPoint ip) {
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
                    repository.setCurrent(new OAuthSessionBuilder().qualifier(service).repo(repository).build());
                }
            }
            return repository.getCurrent();
        }
        throw new UnsupportedOperationException("Cannot inject session whitout Current Qualifier in " + ip);
    }


}
