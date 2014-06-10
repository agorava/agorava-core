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

import org.agorava.AgoravaConstants;
import org.agorava.AgoravaContext;
import org.agorava.api.atinject.Current;
import org.agorava.api.event.OAuthComplete;
import org.agorava.api.exception.AgoravaException;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.service.OAuthLifeCycleService;
import org.picketlink.Identity;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.Authenticator;
import org.picketlink.authentication.event.PostLoggedOutEvent;
import org.picketlink.idm.model.Account;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Antoine Sabot-Durand
 */
@RequestScoped
@Named("plaService")
public class PicketLinkAgoravaService implements Serializable {


    @Inject
    @Any
    Instance<AgoravaAuthenticator> authenticators;
    @Inject
    OAuthLifeCycleService lfs;
    @Inject
    @Current
    private OAuthSession session;
    private String provider;

    public List<String> getListOfServices() {
        return AgoravaContext.getListOfServices();
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    protected void listenEndDance(@Observes OAuthComplete complete, Identity identity) {
        Identity.AuthenticationResult result = identity.login();
    }

    protected void listenLogout(@Observes PostLoggedOutEvent plo) {
        Account account = plo.getAccount();
        if (account instanceof AgoravaUser) {
            lfs.getCurrentRepository().clear();
        }
    }

    @Produces
    @PicketLink
    @RequestScoped
    public Authenticator authenticatorProducer() {

        Annotation qualifier;

        if (session != OAuthSession.NULL) {
            qualifier = session.getServiceQualifier();
        } else if (AgoravaContext.getListOfServices().contains(provider)) {
            qualifier = AgoravaContext.getServicesToQualifier().get(provider);
        } else {
            throw new AgoravaException("Current OAuthSession is NULL and Request doesn't contain " + AgoravaConstants
                    .PROVIDER_PARAM + " parameter");
        }
        if (qualifier == null) {
            throw new AgoravaException("No qualifier found for the following provider " + provider);
        }
        return authenticators.select(qualifier).get();

    }
}
