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

package org.agorava.cdi;

import org.agorava.AgoravaConstants;
import org.agorava.api.atinject.Current;
import org.agorava.api.event.OAuthComplete;
import org.agorava.api.event.SocialEvent;
import org.agorava.api.exception.AgoravaException;
import org.agorava.api.exception.ProviderMismatchException;
import org.agorava.api.oauth.OAuthService;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.service.OAuthLifeCycleService;
import org.agorava.api.storage.GlobalRepository;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.spi.UserProfileService;
import static org.agorava.AgoravaContext.getServicesToQualifier;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author Antoine Sabot-Durand
 */
public class OAuthLifeCycleServiceImpl implements OAuthLifeCycleService {

    private static final long serialVersionUID = 7320799247595986232L;

    @Inject
    @Any
    private Event<OAuthComplete> completeEvt;

    @Inject
    private GlobalRepository globalRepository;

    private UserSessionRepository repository;

    @Inject
    @Current
    private Instance<OAuthSession> sessionInstance;

    @Inject
    @Any
    private Instance<UserProfileService> userProfileServices;

    @Inject
    @Any
    private Instance<OAuthService> services;

    @Inject
    OAuthLifeCycleServiceImpl(@Current Instance<UserSessionRepository> repositories) {
        if (repositories.isUnsatisfied())
            throw new AgoravaException("No User repo available, you should activate a producer bean");
        else
            repository = repositories.get();
    }

    @Override
    public OAuthService getCurrentService() {
        return services.select(getCurrentSession().getServiceQualifier()).get();
    }

    @Override
    public OAuthSession getCurrentSession() {
        return sessionInstance.get();
    }

    @Override
    public void killCurrentSession() {
        repository.removeCurrent();
    }

    @Override
    public void killSession(OAuthSession session) {
        repository.remove(session);
    }

    @Override
    public UserProfileService getCurrentUserProfileService() {
        return userProfileServices.select(getCurrentSession().getServiceQualifier()).get();
    }

    @Override
    public String startDanceFor(String providerName) {
        return startDanceFor(providerName, null);
    }


    @Override
    public synchronized void endDance() {
        if (getCurrentSession().getAccessToken() == null)
            getCurrentSession().setAccessToken(getCurrentService().getAccessToken(getCurrentSession().getRequestToken(),
                    getCurrentSession().getVerifier()));
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
    public void endDance(String verifier) {
        getCurrentSession().setVerifier(verifier);
        endDance();
    }

    @Override
    public OAuthSession buildSessionFor(String providerName) {
        OAuthSession res;
        Annotation qualifier = getServicesToQualifier().get(providerName);
        return buildSessionFor(qualifier);
    }

    @Override
    public OAuthSession buildSessionFor(Annotation qualifier) {
        OAuthSession res = new OAuthSession.Builder().qualifier(qualifier).repo(unProxifyRepo(repository)).build();
        repository.setCurrent(res);
        return res;
    }

    private UserSessionRepository unProxifyRepo(UserSessionRepository repo) {
        return globalRepository.get(repo.getId());
    }

    @Override
    public OAuthSession resolveSessionForQualifier(Annotation qualifier) {
        if (repository.getCurrent().equals(OAuthSession.NULL))
            buildSessionFor(qualifier);
        else if (!repository.getCurrent().getServiceQualifier().equals(qualifier))
            throw new ProviderMismatchException("Inconsistent state between repo and service. In repo Session provider is " +
                    repository.getCurrent().getServiceName() + " while service provider is " + qualifier);

        return repository.getCurrent();
    }


    @Override
    public String startDanceFor(String providerName, String internalCallBack) {
        OAuthSession session = buildSessionFor(providerName);
        if (internalCallBack != null && !"".equals(internalCallBack.trim()))
            session.getExtraData().put(AgoravaConstants.INTERN_CALLBACK_PARAM, internalCallBack);
        return getCurrentService().getAuthorizationUrl();
    }

    @Override
    public String startDanceFor(Annotation provider) {
        buildSessionFor(provider);
        return getCurrentService().getAuthorizationUrl();
    }

    @Override
    public String getVerifierParamName() {
        return getCurrentService().getVerifierParamName();
    }

    @Override
    public List<OAuthSession> getAllActiveSessions() {
        return new ArrayList(repository.getAll());
    }

    @Override
    public void setCurrentSession(OAuthSession session) {
        repository.setCurrent(session);
    }

}
