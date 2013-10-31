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
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.storage.UserSessionRepository;
import org.apache.deltaspike.core.api.exclude.Exclude;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Named;

/**
 * @author Antoine Sabot-Durand
 */

@ApplicationScoped
@Exclude(onExpression = "producerScope!=application")
public class InApplicationProducer extends InRequestProducer {

    @Override
    @Produces
    @Current
    @Named
    @ApplicationScoped
    public UserSessionRepository getCurrentRepo() {
        return super.getCurrentRepo();
    }


    @Override
    @Produces
    public OAuthSession getCurrentSession(InjectionPoint ip, @Current UserSessionRepository repository) {
        return super.getCurrentSession(ip, repository);
    }
}
