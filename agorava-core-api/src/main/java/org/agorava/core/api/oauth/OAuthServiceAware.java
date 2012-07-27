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
/**
 *
 */
package org.agorava.core.api.oauth;


/**
 * Class implementing this interface have access to their underlying OAuth Service
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthServiceAware {

    /**
     * @return the underlying OAuthService
     */
    OAuthService getService();

    /**
     * @return the current Session with which the service is working
     */
    OAuthSession getSession();
}
