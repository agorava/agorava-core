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

package org.agorava.arquillian;

import org.agorava.cdi.test.oauthsession.OAuthSessionWithCurrentAndProviderRelatedQualifiersException;
import org.jboss.arquillian.container.spi.client.container.DeploymentExceptionTransformer;

/**
 * @author Antoine Sabot-Durand
 */
public class AgoravaDeploymentExceptionTranformer implements DeploymentExceptionTransformer {
    @Override
    public Throwable transform(Throwable throwable) {
        if (throwable != null && throwable.getMessage().contains("OAuthSession cannot have @Current annotation and a " +
                "provider")) {
            return new OAuthSessionWithCurrentAndProviderRelatedQualifiersException("");
        } else {
            return null;
        }

    }
}
