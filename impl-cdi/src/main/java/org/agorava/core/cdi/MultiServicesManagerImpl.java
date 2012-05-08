/*******************************************************************************
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
 ******************************************************************************/


package org.agorava.core.cdi;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.agorava.core.api.MultiServicesManager;
import org.agorava.core.api.SocialNetworkServicesHub;
import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.cdi.oauth.OAuthSessionImpl;

/**
 * 
 * Default implementation of {@link MultiServicesManager}
 * 
 * @author Antoine Sabot-Durand
 * 
 */
@SessionScoped
public class MultiServicesManagerImpl implements MultiServicesManager, Serializable {

    private static final long serialVersionUID = 2681869484541158766L;

    @Inject
    @Any
    private Instance<OAuthService> serviceInstances;

    @Inject
    @Any
    private Instance<SocialNetworkServicesHub> hubInstances;

    @Inject
    private AgoravaExtension socialConfig;

    private List<String> listOfServices;

    private final Set<OAuthSession> activeSessions;

    @Produces
    @Named
    @Current
    private OAuthSession currentSession;

    @PostConstruct
    void init() {
        listOfServices = newArrayList(socialConfig.getSocialRelated());
        socialConfig.setMultiSession(true);
    }

    @Override
    public List<String> getListOfServices() {
        return listOfServices;
    }

    public MultiServicesManagerImpl() {
        super();
        activeSessions = newHashSet();
    }

    @Override
    public OAuthService getCurrentService() {
        return serviceInstances.select(getCurrentSession().getServiceQualifier()).get();
    }

    @Override
    public SocialNetworkServicesHub getCurrentServiceHub() {
        return hubInstances.select(getCurrentSession().getServiceQualifier()).get();
    }

    @Override
    public boolean isCurrentServiceConnected() {
        return getCurrentService() != null && getCurrentService().isConnected();
    }

    @Override
    public synchronized void connectCurrentService() {
        getCurrentService().initAccessToken();
        activeSessions.add(currentSession);
    }

    @Override
    public String initNewSession(String servType) {
        Annotation qualifier = AgoravaExtension.getServicesToQualifier().inverse().get(servType);
        setCurrentSession(new OAuthSessionImpl(qualifier));
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
