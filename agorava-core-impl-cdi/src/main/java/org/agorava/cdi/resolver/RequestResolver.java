/*
 * Copyright 2016 Agorava
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
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.cdi.config.DifferentOrNull;
import org.agorava.jsf.FacesUrlTransformer;
import org.agorava.oauth.settings.SimpleOAuthAppSettingsBuilder;
import org.agorava.spi.AppSettingsTuner;
import org.apache.deltaspike.core.api.common.DeltaSpike;
import org.apache.deltaspike.core.api.exclude.Exclude;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Antoine Sabot-Durand
 * @author Werner Keil
 */
@RequestScoped
@Exclude(onExpression = ApplicationResolver.RESOLVER + ",request", interpretedBy = DifferentOrNull.class)
public class RequestResolver extends ApplicationResolver {

    private static final long serialVersionUID = 6446160199657772110L;

    private static Logger log = Logger.getLogger(RequestResolver.class.getName());

    @Inject
    @DeltaSpike
    protected HttpServletRequest request;

    private OAuthSession currentSession;

    private UserSessionRepository currentRepo;

    protected String getRepoId() {
        return request.getParameter(AgoravaConstants.REPOID_PARAM);
    }

    protected String getSessionId() {
        return request.getParameter(AgoravaConstants.SESSIONID_PARAM);
    }

    @PostConstruct
    protected void init() {
        UserSessionRepository r;
        OAuthSession s;

        s = globalRepository.getOauthSession(getSessionId());
        if (s != null) {
            currentSession = s;
            currentRepo = s.getRepo();

        } else {
            r = globalRepository.get(getRepoId());
            if (r != null) {
                currentRepo = r;
            } else {
                currentRepo = globalRepository.createNew();
            }
            currentSession = currentRepo.getCurrent();
        }
    }


    @Produces
    @Current
    @Named("currentRepo")
    @RequestScoped
    public UserSessionRepository getCurrentRepository() {
        return currentRepo;

    }

    @Produces
    public OAuthSession resolveOAuthSession(InjectionPoint ip, @Current UserSessionRepository repository) {
        return super.resolveOAuthSession(ip, repository);
    }

    @Produces
    @RequestScoped
    public AppSettingsTuner produceCallBackTuner(@Current OAuthSession session) {
        return new addSessionToCallbackTuner(session);
    }

    @Produces
    @Named
    @Current
    @RequestScoped
    public OAuthSession getCurrentSession() {
        return currentSession;
    }

    static public class addSessionToCallbackTuner implements AppSettingsTuner {
        OAuthSession session;

        public addSessionToCallbackTuner(OAuthSession session) {
            this.session = session;
        }

        @Override
        public OAuthAppSettings tune(OAuthAppSettings toTune) {
            return new SimpleOAuthAppSettingsBuilder()
                    .readFromSettings(toTune)
                    .callback(new FacesUrlTransformer(toTune.getCallback())
                            .appendParamIfNecessary(AgoravaConstants.SESSIONID_PARAM, session.getId()).getUrl())
                    .build();
        }
    }
}
