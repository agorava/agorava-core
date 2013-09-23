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
package org.agorava.core.api.oauth;

/**
 * This interface contains OAuth constants, used project-wide
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 */
public interface OAuthConstants {
    /**
     * Time stamp field name in OAuth request
     */
    String TIMESTAMP = "oauth_timestamp";

    /**
     * Signature method field name in OAuth request
     */
    String SIGN_METHOD = "oauth_signature_method";

    /**
     * Signature field name in OAuth request
     */
    String SIGNATURE = "oauth_signature";

    /**
     * Consumer secret key field name in OAuth request
     */
    String CONSUMER_SECRET = "oauth_consumer_secret";

    /**
     * Consumer key field name in OAuth request
     */
    String CONSUMER_KEY = "oauth_consumer_key";

    /**
     * Call Back Url field name in OAuth request
     */
    String CALLBACK = "oauth_callback";

    /**
     * OAuth Version field name in OAuth request
     */
    String VERSION = "oauth_version";

    /**
     * Unique stamp to field name in OAuth request
     */
    String NONCE = "oauth_nonce";

    /**
     * Prefix field name in OAuth request
     */
    String PARAM_PREFIX = "oauth_";

    /**
     * Token field name in OAuth request
     */
    String TOKEN = "oauth_token";

    /**
     * Secret token field name in OAuth request
     */
    String TOKEN_SECRET = "oauth_token_secret";

    /**
     * Special "Out Of Band" Call back for non Web app
     */
    String OUT_OF_BAND = "oob";

    /**
     * Returned Verifier field name in OAuth request
     */
    String VERIFIER = "oauth_verifier";

    /**
     * HEADER field name in OAuth request
     */
    String HEADER = "Authorization";

    /**
     * An empty Token
     */
    Token EMPTY_TOKEN = new Token("", "");

    /**
     * Scope field name in OAuth request
     */
    String SCOPE = "scope";

    //OAuth 2.0 specific

    /**
     * Access Token field name in OAuth request
     */
    String ACCESS_TOKEN = "access_token";

    /**
     * Client Id field name in OAuth request
     */
    String CLIENT_ID = "client_id";

    /**
     * Client Secret field name in OAuth request
     */
    String CLIENT_SECRET = "client_secret";

    /**
     * Redirect uri field name in OAuth request
     */
    String REDIRECT_URI = "redirect_uri";

    /**
     * Returned code field name in OAuth request
     */
    String CODE = "code";


}
