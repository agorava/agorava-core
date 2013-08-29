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

public class TwitterApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize?oauth_token=%s";
    private static final String REQUEST_TOKEN_RESOURCE = "api.twitter.com/oauth/request_token";
    private static final String ACCESS_TOKEN_RESOURCE = "api.twitter.com/oauth/access_token";

    @Override
    public String getAccessTokenEndpoint() {
        return "http://" + ACCESS_TOKEN_RESOURCE;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return "http://" + REQUEST_TOKEN_RESOURCE;
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }

    public static class SSL extends TwitterApi {
        @Override
        public String getAccessTokenEndpoint() {
            return "https://" + ACCESS_TOKEN_RESOURCE;
        }

        @Override
        public String getRequestTokenEndpoint() {
            return "https://" + REQUEST_TOKEN_RESOURCE;
        }
    }

    /**
     * Twitter 'friendlier' authorization endpoint for OAuth.
     * <p/>
     * Uses SSL.
     */
    public static class Authenticate extends SSL {
        private static final String AUTHENTICATE_URL = "https://api.twitter.com/oauth/authenticate?oauth_token=%s";

        @Override
        public String getAuthorizationUrl(Token requestToken) {
            return String.format(AUTHENTICATE_URL, requestToken.getToken());
        }
    }

    /**
     * Just an alias to the default (SSL) authorization endpoint.
     * <p/>
     * Need to include this for symmetry with 'Authenticate' only.
     */
    public static class Authorize extends SSL {
    }
}
