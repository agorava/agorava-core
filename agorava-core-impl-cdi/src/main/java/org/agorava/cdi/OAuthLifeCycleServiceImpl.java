/*
 * Copyright 2014-2016 Agorava
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
import static org.agorava.AgoravaContext.getClassToQualifierQualifier;
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

    @Inject
    @Current
    private Instance<UserSessionRepository> repositories;

    @Inject
    @Current
    private Instance<OAuthSession> sessionInstance;

    @Inject
    @Any
    private Instance<UserProfileService> userProfileServices;

    @Inject
    @Any
    private Instance<OAuthService> services;

    @Override
    public UserSessionRepository getCurrentRepository() {
        return repositories.get();
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
    public void setCurrentSession(OAuthSession session) {
        getCurrentRepository().setCurrent(session);
    }

    @Override
    public void killCurrentSession() {
        getCurrentRepository().removeCurrent();
    }

    @Override
    public void killSession(OAuthSession session) {
        getCurrentRepository().remove(session);
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
        OAuthSession currentSession = getCurrentSession();
        System.out.println("Verifier ("+ getVerifierParamName() + ") : " + currentSession.getVerifier()); // FIXME replace with logger or disable
        if (currentSession.getAccessToken() == null) {
            currentSession.setAccessToken(getCurrentService().getAccessToken(currentSession.getRequestToken(),
                    currentSession.getVerifier()));
        }
        if (currentSession.getAccessToken() != null) {
            currentSession.setRequestToken(null);
            currentSession.setUserProfile(getCurrentUserProfileService().getUserProfile());
            currentSession.getRepo().add(currentSession);
            completeEvt.select(currentSession.getServiceQualifier()).fire(new OAuthComplete(SocialEvent.Status.SUCCESS, "",
                    currentSession));

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
        if (qualifier == null) {
            throw new AgoravaException("Cannot find configured service provider with name : " + providerName);
        }
        return buildSessionFor(qualifier);
    }

    @Override
    public OAuthSession buildSessionFor(Annotation qualifier) {
        OAuthSession res = new OAuthSession.Builder().qualifier(qualifier).repo(unProxifyRepo(getCurrentRepository())).build();
        getCurrentRepository().setCurrent(res);
        return res;
    }

    private UserSessionRepository unProxifyRepo(UserSessionRepository repo) {
        return globalRepository.get(repo.getId());
    }

    @Override
    public OAuthSession resolveSessionForQualifier(Annotation qualifier) {
        OAuthSession current = getCurrentSession();
        if (current.getServiceQualifier().equals(qualifier)) {
            return current;
        }
        if (getCurrentRepository().getCurrent().equals(OAuthSession.NULL)) {
            buildSessionFor(qualifier);
        } else if (!getCurrentRepository().getCurrent().getServiceQualifier().equals(qualifier)) {
            throw new ProviderMismatchException("Inconsistent state between repo and service. In repo Session provider is " +
                    getCurrentRepository().getCurrent().getServiceName() + " while service provider is " + qualifier);
        }

        return getCurrentRepository().getCurrent();
    }

    @Override
    public String startDanceFor(String providerName, String internalCallBack) {
        OAuthSession session = buildSessionFor(providerName);
        if (internalCallBack != null && !"".equals(internalCallBack.trim())) {
            session.getExtraData().put(AgoravaConstants.INTERN_CALLBACK_PARAM, internalCallBack);
        }
        return getCurrentService().getAuthorizationUrl();
    }

    @Override
    public String startDanceFor(Annotation provider) {
        buildSessionFor(provider);
        return getCurrentService().getAuthorizationUrl();
    }

    @Override
    public String startDanceFor(Class<? extends Annotation> providerClass) {
        Annotation qualifier = getClassToQualifierQualifier().get(providerClass);
        if (qualifier == null) {
            throw new AgoravaException("Cannot find configured service provider with class : " + providerClass);
        }
        return startDanceFor(qualifier);
    }

    @Override
    public String getVerifierParamName() {
        return getCurrentService().getVerifierParamName();
    }

    @Override
    public List<OAuthSession> getAllActiveSessions() {
        return new ArrayList(getCurrentRepository().getAll());
    }

}
