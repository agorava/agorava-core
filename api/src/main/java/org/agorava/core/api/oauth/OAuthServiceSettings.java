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

package org.agorava.core.api.oauth;

import java.io.Serializable;
import java.lang.annotation.Annotation;

/**
 * Interface for model containing settings needed to access to an OAuth 1.0a service It's used by {@link OAuthService} to setup
 * connection to OAuth Service
 * 
 * @author Antoine Sabot-Durand
 * @see OAuthService
 */
public interface OAuthServiceSettings extends Serializable {

    /**
     * @return the key consumer key for the OAuth service
     */
    public String getApiKey();

    /**
     * @return the consumer secret key for the OAuth service
     */
    public String getApiSecret();

    /**
     * @return the call back URL for the OAuth service
     */
    public String getCallback();

    /**
     * @param callback call back URL registered for this OAuth service
     */
    public void setCallback(String callback);

    /**
     * @param apiSecret consumer secret for this OAuth service
     */
    public void setApiSecret(String apiSecret);

    /**
     * @param apiKey consumer key for this OAuth service
     */
    public void setApiKey(String apiKey);

    /**
     * @param scope scope requested for this OAuth 2.0 service
     */
    public void setScope(String scope);

    /**
     * @return the scope requested
     */
    public String getScope();

    /**
     * @return
     */
    public String getServiceName();

    /**
     * @return
     */
    public Annotation getServiceQualifier();

    /**
     * @param serviceQualifier
     */
    public void setServiceQualifier(Annotation serviceQualifier);

}
