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

package org.agorava.picketlink;


import org.agorava.api.atinject.Current;
import org.agorava.api.atinject.Generic;
import org.agorava.api.atinject.InjectWithQualifier;
import org.agorava.api.exception.AgoravaException;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.service.OAuthLifeCycleService;
import org.agorava.spi.UserProfile;
import org.apache.deltaspike.core.api.common.DeltaSpike;
import org.picketlink.authentication.BaseAuthenticator;
import org.picketlink.credential.DefaultLoginCredentials;
import org.picketlink.idm.credential.Credentials.Status;

import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Generic
public class AgoravaAuthenticator extends BaseAuthenticator implements Serializable {

    @InjectWithQualifier
    OAuthAppSettings settings;

    @Inject
    DefaultLoginCredentials credentials;


    @Inject
    @DeltaSpike
    Instance<HttpServletResponse> response;


    @Inject
    OAuthLifeCycleService lifeCycleService;

    @Inject
    @Current
    private OAuthSession session;


    @Override
    public void authenticate() {

        if (session.isConnected()) {
            UserProfile userProfile = session.getUserProfile();
            credentials.setCredential(session.getAccessToken());
            setStatus(AuthenticationStatus.SUCCESS);
            setAccount(new AgoravaUser(userProfile));
        } else {
            String authorizationUrl = lifeCycleService.startDanceFor(settings.getQualifier());
            try {
                response.get().sendRedirect(authorizationUrl);
            } catch (IOException e) {
                throw new AgoravaException("Unable to redirect user to: " + authorizationUrl);
            }
            credentials.setStatus(Status.IN_PROGRESS);
            setStatus(AuthenticationStatus.DEFERRED);
        }
    }
}
