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
import org.agorava.cdi.deltaspike.DifferentOrNull;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.servlet.api.Web;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Antoine Sabot-Durand
 */

@SessionScoped
@Exclude(onExpression = "producerScope,cookie", interpretedBy = DifferentOrNull.class)
public class InCookieProducer extends InRequestProducer {

    public final static String REPO_COOKIE_NAME = "agorava_repo_id";

    @Inject
    @ConfigProperty(name = "cookie.life", defaultValue = "-1")
    Integer cookielife;


    @Inject
    @Web
    private HttpServletResponse response;

    @Override
    protected String getRepoId() {
        String id;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(REPO_COOKIE_NAME))
                return cookie.getValue();
        }
        return null;
    }

    private void setCookie(String id) {
        Cookie cookie = new Cookie(REPO_COOKIE_NAME, id);
        cookie.setMaxAge(cookielife);
        response.addCookie(cookie);
    }

    @Produces
    @Current
    @Named
    @RequestScoped
    public UserSessionRepository getCurrentRepo() {
        String id = getRepoId();
        if (id == null || globalRepository.get(id) == null) {
            UserSessionRepository repo = globalRepository.createNew();
            setCookie(repo.getId());
            return globalRepository.createNew();
        } else
            return globalRepository.get(id);
    }


    @Produces
    public OAuthSession getCurrentSession(InjectionPoint ip, @Current UserSessionRepository repository) {
        return super.getCurrentSession(ip, repository);
    }
}
