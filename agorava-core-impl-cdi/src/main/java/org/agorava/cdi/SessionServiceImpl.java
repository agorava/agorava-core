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
import org.agorava.api.event.OAuthComplete;
import org.agorava.api.event.SocialEvent;
import org.agorava.api.oauth.OAuthService;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.oauth.OAuthSessionBuilder;
import org.agorava.api.service.SessionService;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.oauth.UserSessionRepositoryImpl;
import org.agorava.spi.UserProfileService;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.annotation.Annotation;

import static org.agorava.AgoravaContext.getServicesToQualifier;

/**
 * @author Antoine Sabot-Durand
 */
public class SessionServiceImpl implements SessionService {

    private static final long serialVersionUID = 7320799247595986232L;

    private static final UserSessionRepository fakerepo = new UserSessionRepositoryImpl();

    @Inject
    @Any
    private Event<OAuthComplete> completeEvt;

    /*@Inject
    @Current
    private OAuthSession session;*/

    private UserSessionRepository repository;

    @Inject
    @Any
    private Instance<UserProfileService> userProfileServices;

    @Inject
    @Any
    private Instance<OAuthService> services;

    @Inject
    SessionServiceImpl(@Current Instance<UserSessionRepository> repositories) {
        if (repositories.isUnsatisfied())
            repository = fakerepo;
        else
            repository = repositories.get();
    }

    @Override
    public OAuthService getCurrentService() {
        return services.select(getCurrentSession().getServiceQualifier()).get();
    }

    public OAuthSession getCurrentSession() {
        return repository.getCurrent();
    }


    @Override
    public UserProfileService getCurrentUserProfileService() {
        return userProfileServices.select(getCurrentSession().getServiceQualifier()).get();
    }

    @Override
    public synchronized void completeSession() {
        if (getCurrentSession().getAccessToken() == null)
            getCurrentSession().setAccessToken(getCurrentService().getAccessToken(getCurrentSession().getRequestToken(), getCurrentSession().getVerifier()));
        if (getCurrentSession().getAccessToken() != null) {
            getCurrentSession().setRequestToken(null);
            getCurrentSession().setUserProfile(getCurrentUserProfileService().getUserProfile());
            getCurrentSession().getRepo().add(getCurrentSession());
            completeEvt.select(getCurrentSession().getServiceQualifier()).fire(new OAuthComplete(SocialEvent.Status.SUCCESS, "",
                    getCurrentSession()));

            //TODO: reactivate logger
        } else {
            // FIXME Launch an exception !!
        }

    }

    @Override
    public void completeSession(String verifier) {
        getCurrentSession().setVerifier(verifier);
        completeSession();
        ;
    }

    @Override
    public String initNewSession(String servType) {
        OAuthSession res;
        Annotation qualifier = getServicesToQualifier().get(servType);
        res = new OAuthSessionBuilder().providerName(servType).repo(repository).build();
        repository.setCurrent(res);

        return getCurrentService().getAuthorizationUrl();

    }
}
