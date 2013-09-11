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

package org.agorava.core.oauth;

import org.agorava.core.api.oauth.OAuthProvider;
import org.agorava.core.api.oauth.OAuthRequest;
import org.agorava.core.api.oauth.Token;
import org.agorava.core.api.oauth.Verifier;
import org.agorava.core.api.rest.Verb;
import org.agorava.core.rest.OAuthRequestImpl;

/**
 * @author Antoine Sabot-Durand
 */
public abstract class OAuthProviderBase implements OAuthProvider {
    @Override
    public OAuthRequest requestFactory(Verb verb, String uri) {
        OAuthRequest res = new OAuthRequestImpl(verb, uri);
        return res;
    }

    public Token getAccessToken(Token requestToken, String verifier) {

        return getAccessToken(requestToken, new Verifier(verifier));
    }

}
