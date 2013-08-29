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
 * OAuth API for ImgUr
 *
 * @author David Wursteisen
 * @see <a href="http://api.imgur.com/#authapi">ImgUr API</a>
 */
public class ImgUrApi extends DefaultApi10a {

    @Override
    public String getRequestTokenEndpoint() {
        return "https://api.imgur.com/oauth/request_token";
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.imgur.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(Token requestToken) {
        return String.format("https://api.imgur.com/oauth/authorize?oauth_token=%s", requestToken.getToken());
    }
}

