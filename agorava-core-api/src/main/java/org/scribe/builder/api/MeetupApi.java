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

/**
 * OAuth access to the Meetup.com API.
 * For more information visit http://www.meetup.com/api
 */
public class MeetupApi extends DefaultApi10a {
    private static final String AUTHORIZE_URL = "http://www.meetup.com/authenticate?oauth_token=%s";

    @Override
    public String getRequestTokenEndpoint() {
        return "http://api.meetup.com/oauth/request/";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "http://api.meetup.com/oauth/access/";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format(AUTHORIZE_URL, requestToken.getToken());
    }
}
