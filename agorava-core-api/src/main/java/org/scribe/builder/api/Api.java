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

package org.scribe.builder.api;

import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;

/**
 * Contains all the configuration needed to instantiate a valid {@link org.scribe.oauth.OAuthService}
 *
 * @author Pablo Fernandez
 */
public interface Api {
    /**
     * Creates an {@link org.scribe.oauth.OAuthService}
     *
     * @param apiKey    your application api key
     * @param apiSecret your application api secret
     * @param callback  the callback url (or 'oob' for out of band OAuth)
     * @param scope     the OAuth scope
     * @return fully configured {@link org.scribe.oauth.OAuthService}
     */
    OAuthService createService(OAuthConfig config);
}
