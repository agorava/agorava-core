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

import org.agorava.AgoravaConstants;
import org.agorava.api.atinject.Current;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.cdi.deltaspike.DifferentOrNull;
import org.apache.deltaspike.core.api.common.DeltaSpike;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.exclude.Exclude;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Antoine Sabot-Durand
 */

@RequestScoped
@Exclude(onExpression = ApplicationResolver.RESOLVER + ",cookie", interpretedBy = DifferentOrNull.class)
public class CookieResolver extends RequestResolver {

    @Inject
    @ConfigProperty(name = AgoravaConstants.RESOLVER_COOKIE_LIFE_PARAM, defaultValue = "-1")
    Integer cookielife;


    @Inject
    @DeltaSpike
    private HttpServletResponse response;

    @Override
    protected String getRepoId() {
        String id;
        if (request.getCookies() != null)
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(AgoravaConstants.RESOLVER_REPO_COOKIE_NAME))
                    return cookie.getValue();
            }
        return null;
    }

    private void setCookie(String id) {
        Cookie cookie = new Cookie(AgoravaConstants.RESOLVER_REPO_COOKIE_NAME, id);
        cookie.setMaxAge(cookielife);
        String path = request.getContextPath().isEmpty() ? "/" : request.getContextPath();
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);
    }

    @Produces
    @Current
    @Named("currentRepo")
    @RequestScoped
    public UserSessionRepository getCurrentRepository() {
        String id = getRepoId();
        if (id == null || globalRepository.get(id) == null) {
            UserSessionRepository repo = globalRepository.createNew();
            setCookie(repo.getId());
            return globalRepository.createNew();
        } else
            return globalRepository.get(id);
    }


    @Produces
    public OAuthSession resolveOAuthSession(InjectionPoint ip, @Current UserSessionRepository repository) {
        return super.resolveOAuthSession(ip, repository);
    }

    @Produces
    @Named
    @Current
    @Override
    public OAuthSession getCurrentOAuthSession(@Current UserSessionRepository repository) {
        return super.getCurrentOAuthSession(repository);
    }
}
