/*
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
 */

package org.agorava.core.api.oauth;

import java.io.Serializable;

/**
 * Interface for model containing settings needed to access to an OAuth 1.0a and 2.0 service.
 * It's used by {@link OAuthService} to setup
 * connection to OAuth Service
 *
 * @author Antoine Sabot-Durand
 * @see OAuthService
 */
public interface OAuthAppSettings extends Serializable {

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
     * @return the scope requested
     */
    public String getScope();

    /**
     * @return the name of the service
     */
    public String getSocialMediaName();


}
