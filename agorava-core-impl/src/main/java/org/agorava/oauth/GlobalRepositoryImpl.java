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

import org.agorava.api.exception.AgoravaException;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.storage.GlobalRepository;
import org.agorava.api.storage.UserSessionRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.inject.Singleton;

/**
 * @author Antoine Sabot-Durand
 */
@Singleton
public class GlobalRepositoryImpl extends GlobalRepository {


    private static final long serialVersionUID = -4639395999488886050L;
    private final Set<UserSessionRepository> userRepos = new HashSet<UserSessionRepository>();

    private UserSessionRepository current;

    public GlobalRepositoryImpl() {
        instance = this;
    }

    @Override
    public Collection<OAuthSession> getAllOauthSessions() {
        Set<OAuthSession> res = new HashSet<OAuthSession>();
        for (UserSessionRepository userRepo : userRepos) {
            res.addAll(userRepo.getAll());
        }
        return res;
    }

    @Override
    public OAuthSession getOauthSession(String sid) {
        if (sid != null && !sid.isEmpty()) {
            for (OAuthSession session : getAllOauthSessions()) {
                if (session.getId().equals(sid)) {
                    return session;
                }
            }
        }
        return null;
    }

    @Override
    public UserSessionRepository createNew() {
        UserSessionRepository res = new UserSessionRepositoryImpl();
        add(res);
        setCurrent(res);
        return res;
    }

    @Override
    public UserSessionRepository createNew(String id) {
        if (get(id) != null) {
            throw new AgoravaException("Unable to create new user repository : repository with id " + id + " already exists");
        } else {
            UserSessionRepository res = new UserSessionRepositoryImpl(id);
            add(res);
            setCurrent(res);
            return res;
        }
    }

    @Override
    public UserSessionRepository getCurrent() {
        return current;
    }

    @Override
    public void setCurrent(UserSessionRepository element) {
        current = element;
    }

    @Override
    public Collection<UserSessionRepository> getAll() {
        return Collections.unmodifiableCollection(userRepos);
    }

    @Override
    public UserSessionRepository setCurrent(String id) throws IllegalArgumentException {
        UserSessionRepository res = get(id);

        if (res == null) {
            throw new IllegalArgumentException("There's no OauthSessionRepository with id " + id);
        }
        return res;
    }

    @Override
    public UserSessionRepository get(String id) {
        if (id != null && !id.isEmpty())
            for (UserSessionRepository userRepo : userRepos) {
                if (userRepo.getId().equals(id))
                    return userRepo;
            }
        return null;
    }

    @Override
    public boolean removeCurrent() {
        if (getCurrent() != null) {
            return remove(current);
        }
        return false;
    }

    @Override
    public boolean remove(String id) {
        UserSessionRepository res = get(id);
        if (res != null)
            return remove(res);
        return false;
    }

    @Override
    public boolean remove(UserSessionRepository element) {
        return userRepos.remove(element);
    }

    @Override
    public void add(UserSessionRepository element) {
        userRepos.add(element);

    }

    @Override
    public void clear() {
        userRepos.clear();
    }

    @Override
    public Iterator<UserSessionRepository> iterator() {
        return new Iterator<UserSessionRepository>() {

            Iterator<UserSessionRepository> wrapped = getAll().iterator();

            @Override
            public boolean hasNext() {
                return wrapped.hasNext();
            }

            @Override
            public UserSessionRepository next() {
                current = wrapped.next();
                return current;
            }

            @Override
            public void remove() {
                wrapped.remove();

            }
        };
    }
}
