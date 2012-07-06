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
/**
 *
 */
package org.agorava.core.cdi;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.agorava.core.api.SocialMediaAware;
import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthServiceAware;
import org.agorava.core.api.oauth.OAuthSession;

/**
 * @author Antoine Sabot-Durand
 */
public abstract class AbstractOAuthServiceAwareImpl implements OAuthServiceAware, SocialMediaAware {

    @Inject
    @Any
    protected Instance<OAuthService> serviceInstances;

    @Override
    public OAuthService getService() {
        return serviceInstances.select(getQualifier()).get();
    }

    public OAuthSession getSession() {
        return getService().getSession();
    }

}
