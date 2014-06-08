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
import org.agorava.api.exception.AgoravaException;
import org.apache.deltaspike.core.api.common.DeltaSpike;
import org.picketlink.annotations.PicketLink;
import org.picketlink.authentication.Authenticator;

import java.lang.annotation.Annotation;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Antoine Sabot-Durand
 */
public class AuthenticatorSelector {

    @Inject
    @DeltaSpike
    HttpServletRequest request;

    @Inject
    @Any
    Instance<AgoravaAuthenticator> authenticators;

    @Produces
    @PicketLink
    public Authenticator authenticatorProducer() {

        String providerName = request.getParameter(AgoravaConstants.PROVIDER_PARAM);
        if (providerName == null || "".equals(providerName)) {
            throw new AgoravaException("Request doesn't contain " + AgoravaConstants.PROVIDER_PARAM + " parameter");
        }
        Annotation qualifier = AgoravaContext.getServicesToQualifier().get(providerName);

        if (qualifier == null) {
            throw new AgoravaException("No qualifier found for the following provider " + providerName);
        }
        return authenticators.select(qualifier).get();

    }
}
