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

package org.agorava.api.service;

import org.agorava.api.oauth.OAuthService;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.spi.UserProfileService;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Antoine Sabot-Durand
 */
public interface OAuthLifeCycleService extends Serializable {


    OAuthService getCurrentService();

    OAuthSession getCurrentSession();

    void disconnect();

    void disconnect(OAuthSession session);

    UserProfileService getCurrentUserProfileService();


    void endDance();

    void endDance(String verifier);

    /**
     * Instantiate a new Service which become the current service
     *
     * @return the oauth session related tot his new service
     */
    OAuthSession createSessionForName(String providerName);


    OAuthSession getSessionForQualifier(Annotation qualifier);

    OAuthSession initSessionForQualifier(Annotation qualifier);

    String startDance(String providerName);

    String getVerifierParamName();

    List<OAuthSession> getAllActiveSessions();

    void setCurrentSession(OAuthSession session);
}
