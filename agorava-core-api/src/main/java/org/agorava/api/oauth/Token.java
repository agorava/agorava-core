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

package org.agorava.api.oauth;

import org.agorava.api.service.Preconditions;

import java.io.Serializable;

/**
 * Represents an OAuth token (either request or access token) and its secret
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 */
public class Token implements Serializable {
    private static final long serialVersionUID = 715000866082812683L;

    private final String token;

    private final String secret;

    /**
     * Default constructor
     *
     * @param token  token value. Can't be null.
     * @param secret token secret. Can't be null.
     */
    public Token(String token, String secret) {
        Preconditions.checkNotNull(token, "Token can't be null");
        Preconditions.checkNotNull(secret, "Secret can't be null");
        this.token = token;
        this.secret = secret;
    }

    /**
     * @return public part of the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @return secret part of the token
     */
    public String getSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return String.format("Token[%s , %s]", token, secret);
    }

    /**
     * @return true if the token is empty (token = "", secret = "")
     */
    public boolean isEmpty() {
        return equals(OAuthConstants.EMPTY_TOKEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Token that = (Token) o;
        return token.equals(that.token) && secret.equals(that.secret);
    }

    @Override
    public int hashCode() {
        return 31 * token.hashCode() + secret.hashCode();
    }
}
