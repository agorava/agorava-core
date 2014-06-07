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

package org.agorava.cdi.test.oauthsession;

import org.agorava.api.atinject.Current;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.cdi.resolver.ApplicationResolver;
import org.agorava.cdi.test.FakeService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * @author Antoine Sabot-Durand
 */
public class FaultyResolver extends ApplicationResolver {


    @ApplicationScoped
    @Produces
    @FakeService
    OAuthAppSettings settings;

    @Current
    @FakeService
    @Produces
    public OAuthSession getCurrentSession(@Current UserSessionRepository repository) {
        return super.getCurrentSession(repository);
    }

    @Override
    @Produces
    @Current
    public UserSessionRepository getCurrentRepository() {
        return super.getCurrentRepository();
    }


}
