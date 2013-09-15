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


package org.agorava.core.cdi;

import org.agorava.core.api.atinject.Current;
import org.agorava.core.api.event.OAuthComplete;
import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.api.service.MultiSessionService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.agorava.core.cdi.AgoravaExtension.getServicesToQualifier;

/**
 * {@inheritDoc}
 *
 * @author Antoine Sabot-Durand
 */
@SessionScoped
public class MultiSessionServiceImpl implements MultiSessionService, Serializable {

    private static final long serialVersionUID = 2681869484541158766L;

    @Inject
    @Any
    private Instance<OAuthService> serviceInstances;


    @Inject
    @Any
    private Instance<OAuthSession> sessionInstances;


    private List<String> listOfServices;

    private final Set<OAuthSession> activeSessions = newHashSet();

    @Produces
    @Named
    @Current
    private OAuthSession currentSession;

    @PostConstruct
    void init() {
        listOfServices = newArrayList(AgoravaExtension.getSocialRelated());
        AgoravaExtension.setMultiSession(true);
    }

    @Override
    public List<String> getListOfServices() {
        return listOfServices;
    }

    @Override
    public OAuthService getCurrentService() {
        return serviceInstances.select(getCurrentSession().getServiceQualifier()).get();
    }


    @Override
    public boolean isCurrentServiceConnected() {
        return getCurrentService() != null && getCurrentService().isConnected();
    }

    @Override
    public synchronized void connectCurrentService() {
        getCurrentService().initAccessToken();
    }

    private void processOAuthComplete(@Observes(notifyObserver = Reception.IF_EXISTS) OAuthComplete event) {
        activeSessions.add(event.getEventData());
    }

    @Override
    public String initNewSession(String servType) {
        Annotation qualifier = getServicesToQualifier().get(servType);
        setCurrentSession(sessionInstances.select(qualifier).get());
        return getCurrentService().getAuthorizationUrl();

    }

    @Override
    public void destroyCurrentSession() {
        if (getCurrentSession() != null) {
            activeSessions.remove(getCurrentSession());
            setCurrentSession(activeSessions.size() > 0 ? getLast(activeSessions) : null);
        }
    }

    @Override
    public void setCurrentSession(OAuthSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public OAuthSession getCurrentSession() {
        return currentSession;
    }

    @Override
    public Set<OAuthSession> getActiveSessions() {
        return activeSessions;
    }

}
