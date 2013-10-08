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

import org.agorava.core.api.UserSessionRepository;
import org.agorava.core.api.event.OAuthComplete;
import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.cdi.extensions.AgoravaExtension;
import org.agorava.core.oauth.OAuthSessionImpl;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static org.agorava.core.cdi.extensions.AgoravaExtension.getServicesToQualifier;

/**
 * {@inheritDoc}
 *
 * @author Antoine Sabot-Durand
 */

public class UserSessionRepositoryImpl implements UserSessionRepository {

    private static final long serialVersionUID = 2681869484541158766L;

    private final Set<OAuthSession> activeSessions = new HashSet<OAuthSession>();

    @Inject
    @Any
    private Instance<OAuthService> serviceInstances;

    @Inject
    @Any
    private Instance<OAuthSession> sessionInstances;

    private List<String> listOfServices;

    private OAuthSession currentSession = new OAuthSessionImpl();

    private String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }

    @Override
    public OAuthSession getCurrent() {
        return currentSession;
    }

    @Override
    public void setCurrent(OAuthSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public OAuthSession setCurrent(String id) throws IllegalArgumentException {
        OAuthSession res = get(id);
        if (res == null)
            throw new IllegalArgumentException("There is no session with id " + id + " in the repository");
        else
            return res;
    }

    @Override
    public Collection<OAuthSession> getAll() {
        return Collections.unmodifiableCollection(activeSessions);
    }

    @Override
    public OAuthSession get(String id) {
        for (OAuthSession session : activeSessions) {
            if (id.equals(session.getId()))
                return session;
        }
        return null;
    }

    @Override
    public boolean removeCurrent() {
        if (getCurrent() != null) {
            activeSessions.remove(getCurrent());
            setCurrent(activeSessions.size() > 0 ? getLast(activeSessions) : null);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(String id) {
        OAuthSession elt = get(id);
        if (elt != null) {
            return remove(elt);
        }
        return false;
    }

    @Override
    public boolean remove(OAuthSession element) {
        if (element.equals(getCurrent())) {
            return removeCurrent();
        } else {
            return activeSessions.remove(element);
        }
    }

    @Override
    public void add(OAuthSession elt) {
        activeSessions.add(elt); //TODO : elt could be a proxy : we should test and copy it.
    }

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
        return serviceInstances.select(getCurrent().getServiceQualifier()).get();
    }

    @Override
    public boolean isCurrentServiceConnected() {
        return getCurrentService() != null && getCurrentService().isConnected();
    }

    @Override
    public synchronized void connectCurrentService() {
        getCurrentService().initAccessToken();
    }

    private void processOAuthComplete(@Observes OAuthComplete event) {
        OAuthSession session = event.getEventData();
        UserSessionRepository repository = session.getRepo();
        repository.add(session);
    }

    @Override
    public String initNewSession(String servType) {
        OAuthSession res;
        Annotation qualifier = getServicesToQualifier().get(servType);
        res = sessionInstances.select(qualifier).get();
        res.setRepo(this);
        setCurrent(res);

        return getCurrentService().getAuthorizationUrl();

    }

    @Override
    public Iterator<OAuthSession> iterator() {
        return new Iterator<OAuthSession>() {

            Iterator<OAuthSession> wrapped = getAll().iterator();

            @Override
            public boolean hasNext() {
                return wrapped.hasNext();
            }

            @Override
            public OAuthSession next() {
                currentSession = wrapped.next();
                return currentSession;
            }

            @Override
            public void remove() {
                wrapped.remove();

            }
        };
    }
}
