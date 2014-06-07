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

package org.agorava.api.service;

import org.agorava.api.exception.ProviderMismatchException;
import org.agorava.api.oauth.OAuthService;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.spi.UserProfileService;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Main entry point for Agorava. This class manages the OAuth initialization phase ("OAuth dance") and the lifecycle
 * of the {@link org.agorava.api.oauth.OAuthSession} collections for a given {@link org.agorava.api.storage
 * .UserSessionRepository}
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthLifeCycleService extends Serializable {


    UserSessionRepository getCurrentRepository();

    /**
     * @return the current service (associated to the current session)
     */
    OAuthService getCurrentService();

    /**
     * @return the current session
     */
    OAuthSession getCurrentSession();

    /**
     * Makes the given OAuth session the current session
     *
     * @param session to put as current session
     */
    void setCurrentSession(OAuthSession session);

    /**
     * kill (disconnect and garbage) the current session
     */
    void killCurrentSession();

    /**
     * kill (disconnect and garbage) the given session
     *
     * @param session to kill
     */
    void killSession(OAuthSession session);

    /**
     * @return the service needed to retrieve user profile for the given provider
     */
    UserProfileService getCurrentUserProfileService();

    /**
     * OAuth Dance entry point. For a given Provider name initialize OAuth workflow by returning authorization url end user
     * should connect to in order to grant permission to the OAuth application to use her account or her behalf
     *
     * @param providerName name of the service provider to connect to
     * @return the Authorization url needed to continue the OAuth Dance workflow
     */
    String startDanceFor(String providerName);

    /**
     * OAuth Dance entry point. For a given Provider name initialize OAuth workflow by returning authorization url end user
     * should connect to in order to grant permission to the OAuth application to use her account or her behalf
     *
     * @param providerName     name of the service provider to connect to
     * @param internalCallBack the internal URL to go back to after ending the dance
     * @return the Authorization url needed to continue the OAuth Dance workflow
     */
    String startDanceFor(String providerName, String internalCallBack);

    /**
     * OAuth dance entry point. For a given Provider {@link org.agorava.api.atinject.ProviderRelated} qualifier,
     * initializes OAuth workflow by returning authorization url end user should connect to in order to grant permission to
     * the OAuth application to use her account or her behalf
     *
     * @param provider qualifier with {@link org.agorava.api.atinject.ProviderRelated} meta annotation
     * @return the Authorization url needed to continue the OAuth Dance workflow
     */
    String startDanceFor(Annotation provider);

    /**
     * OAuth dance entry point. For a given Provider {@link org.agorava.api.atinject.ProviderRelated} qualifier class,
     * initializes OAuth workflow by returning authorization url end user should connect to in order to grant permission to
     * the OAuth application to use her account or her behalf
     *
     * @param provider qualifier class with {@link org.agorava.api.atinject.ProviderRelated} meta annotation
     * @return the Authorization url needed to continue the OAuth Dance workflow
     */
    String startDanceFor(Class<? extends Annotation> provider);

    /**
     * OAuth dance exit point. After visiting the URL returned by startDanceFor
     *
     * @param verifier the verifier or code value returned in callback by provider service to validate final
     */
    void endDance(String verifier);

    /**
     * OAuth dance exit point. This version is used when activating session with existing Access Token
     */
    void endDance();

    /**
     * Build a new OAuth Session for the given provider name and set it as current
     *
     * @param providerName name of the provider to build the session for
     * @return the new session
     */
    OAuthSession buildSessionFor(String providerName);

    /**
     * Build a new OAuth Session for the given provider qualifier and set it as current
     *
     * @param qualifier qualifier of the provider to build the session for
     * @return the new session
     */
    OAuthSession buildSessionFor(Annotation qualifier);

    /**
     * Resolve session for the given qualifier.
     * <ul>
     * <li>If the current session is NULL, build a new session, set it as current an return it</li>
     * <li>If the current session is not NULL and has the same qualifier, return it</li>
     * <li>Otherwise throw an exception</li>
     * </ul>
     *
     * @param qualifier provider {@link org.agorava.api.atinject.ProviderRelated} qualifier
     * @return a session having the qualifier provided
     * @throws ProviderMismatchException if current session is not NULL and its qualifier is different than the one provided
     */
    OAuthSession resolveSessionForQualifier(Annotation qualifier) throws ProviderMismatchException;

    /**
     * return the name of the parameter for the verifier (OAuth 1.0a) or code (OAuth 2.0) returned by the provider in the
     * callback
     *
     * @return parameter name
     */
    String getVerifierParamName();

    /**
     * @return all active session (connected session) in the current {@link org.agorava.api.storage.UserSessionRepository}
     */
    List<OAuthSession> getAllActiveSessions();
}
