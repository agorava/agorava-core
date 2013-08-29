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

import org.scribe.model.Token;

public class DiggApi extends DefaultApi10a {

    private static final String AUTHORIZATION_URL = "http://digg.com/oauth/authorize?oauth_token=%s";
    private static final String BASE_URL = "http://services.digg.com/oauth/";

    @Override
    public String getRequestTokenEndpoint() {
        return BASE_URL + "request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return BASE_URL + "access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZATION_URL, requestToken.getToken());
    }

}
