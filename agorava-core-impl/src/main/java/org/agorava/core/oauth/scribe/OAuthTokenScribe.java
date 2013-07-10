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

package org.agorava.core.oauth.scribe;

import org.agorava.core.api.oauth.OAuthToken;
import org.scribe.model.Token;

/**
 * {@inheritDoc}
 *
 * @author Antoine Sabot-Durand
 */
public class OAuthTokenScribe implements OAuthToken {

    private static final long serialVersionUID = 6598671815429418539L;

    final Token delegate;


    public OAuthTokenScribe(String token, String secret) {
        delegate = new Token(token, secret);
    }

    OAuthTokenScribe(Token delegate) {
        this.delegate = delegate;
    }

    public String getToken() {
        return delegate.getToken();
    }

    public String getSecret() {
        return delegate.getSecret();
    }

    public String toString() {
        return delegate.toString();
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

}
