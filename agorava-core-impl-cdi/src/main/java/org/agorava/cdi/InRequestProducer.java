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
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.oauth.application.SimpleOAuthAppSettingsBuilder;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.cdi.deltaspike.DifferentOrNull;
import org.agorava.jsf.FacesUrlTransformer;
import org.agorava.spi.AppSettingsTuner;
import org.apache.deltaspike.core.api.exclude.Exclude;
import org.apache.deltaspike.servlet.api.Web;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Antoine Sabot-Durand
 */

@RequestScoped
@Exclude(onExpression = "producerScope,request", interpretedBy = DifferentOrNull.class)
public class InRequestProducer extends InApplicationProducer {

    private static final long serialVersionUID = 6446160199657772110L;



    @Inject
    @Web
    protected HttpServletRequest request;

    protected String getRepoId() {
        return request.getParameter("repoid");
    }


    @Produces
    @Current
    @Named
    @RequestScoped
    public UserSessionRepository getCurrentRepo() {
        String id = getRepoId();
        if (id == null || globalRepository.get(id) == null)
            return globalRepository.createNew();
        else
            return globalRepository.get(id);
    }


    @Produces
    public OAuthSession resolveSession(InjectionPoint ip, @Current UserSessionRepository repository) {
        return super.resolveSession(ip, repository);

    }

    @Produces
    @RequestScoped
    public AppSettingsTuner produceCallBackTuner(@Current UserSessionRepository repo) {
        return new addRepoToCallbackTuner(repo);
    }

    static public class addRepoToCallbackTuner implements AppSettingsTuner {


        UserSessionRepository repo;

        public addRepoToCallbackTuner(UserSessionRepository repo) {
            this.repo = repo;
        }

        @Override
        public OAuthAppSettings tune(OAuthAppSettings toTune) {
            return new SimpleOAuthAppSettingsBuilder()
                    .readFromSettings(toTune)
                    .callback(new FacesUrlTransformer(toTune.getCallback())
                            .appendParamIfNecessary("repoid", repo.getId()).getUrl())
                    .build();
        }
    }

    @Produces
    @Named
    @Override
    public OAuthSession getCurrentSession(@Current UserSessionRepository repository) {
        return super.getCurrentSession(repository);
    }

}
