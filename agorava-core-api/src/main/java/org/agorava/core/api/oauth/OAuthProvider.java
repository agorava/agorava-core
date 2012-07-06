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

package org.agorava.core.api.oauth;

import org.agorava.core.api.rest.RestVerb;

/**
 * Implementation of this Interface will provide low level services (mainly Token generation and signature) for OAuth
 * management.
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthProvider {
    /**
     * Returns an OAuth request token to initiate an OAuth connection. It's the the first step of OAuth negotiation connection
     *
     * @return an OAuth request token
     */
    public OAuthToken getRequestToken();

    /**
     * Returns the Oauth access token from request token and verifier
     *
     * @param requestToken
     * @param verifier
     * @return an OAuth access token
     */
    public OAuthToken getAccessToken(OAuthToken requestToken, String verifier);

    /**
     * Sign an OAuthRequest in order to make it valid for targeted service
     *
     * @param accessToken the OAuth access token for the current OAuth session
     * @param request     the OAuth request to sign
     */
    public void signRequest(OAuthToken accessToken, OAuthRequest request);

    /**
     * Gives the OAuth version of the provider
     *
     * @return the OAuth version used by the provider
     */
    public String getVersion();

    /**
     * Generates the OAuth authorization URL from the given request Token. It's the step 2 of OAuth negotiation
     *
     * @param tok
     * @return
     */
    public String getAuthorizationUrl(OAuthToken requestToken);

    /**
     * Creates an OAuthRequest with the given Rest Verb and uri
     *
     * @param verb
     * @param uri
     * @return the created OAuthRequest
     */
    public OAuthRequest requestFactory(RestVerb verb, String uri);

    /**
     * Creates an OAuthToken with the given token and given secret
     *
     * @param token
     * @param secret
     * @return then created OAuthToken
     */
    public OAuthToken tokenFactory(String token, String secret);

}
