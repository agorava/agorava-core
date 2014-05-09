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


package org.agorava.oauth;

import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.storage.UserSessionRepository;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * {@inheritDoc}
 *
 * @author Antoine Sabot-Durand
 */
public class UserSessionRepositoryImpl implements UserSessionRepository {

    private static final long serialVersionUID = 2681869484541158766L;

    private final Set<OAuthSession> activeSessions = new HashSet<OAuthSession>();
    private final String id;
    private OAuthSession currentSession = OAuthSession.NULL;

    UserSessionRepositoryImpl(String id) {
        this.id = id;
    }

    public UserSessionRepositoryImpl() {
        this(UUID.randomUUID().toString());
    }


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
    public OAuthSession getForProvider(Annotation qual) {
        if (getCurrent().getServiceQualifier().equals(qual)) {
            return getCurrent();
        } else {
            for (OAuthSession session : activeSessions) {
                if (session.getServiceQualifier().equals(qual)) {
                    return session;
                }
            }
        }
        return OAuthSession.NULL;
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
            setCurrent(activeSessions.size() > 0 ? activeSessions.iterator().next() : OAuthSession.NULL);
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
        if (elt.getClass() != OAuthSession.class)
            elt = new OAuthSession.Builder().readFromOAuthSession(elt).build();
        activeSessions.add(elt);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSessionRepositoryImpl)) {
            return false;
        }

        UserSessionRepositoryImpl that = (UserSessionRepositoryImpl) o;

        if (!id.equals(that.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
