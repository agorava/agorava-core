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

import org.agorava.core.api.rest.Verb;

/**
 * Provides low level services and OAuth initialisation (methods for OAuth Dance).
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthProvider {
    /**
     * Returns an OAuth request token to initiate an OAuth connection. It's the the first step of OAuth negotiation connexion
     *
     * @return an OAuth request token
     */
    Token getRequestToken();

    /**
     * Returns the Oauth access token from request token and verifier
     *
     * @param requestToken Request token sent to Social Media
     * @param verifier     Verifier returned by Social Media after sending the Request Token
     * @return an OAuth access token
     */
    Token getAccessToken(Token requestToken, String verifier);

    /**
     * Retrieve the access token
     *
     * @param requestToken request token (obtained previously)
     * @param verifier     verifier code
     * @return access token
     */
    Token getAccessToken(Token requestToken, Verifier verifier);

    /**
     * Sign an OAuthRequest in order to make it valid for targeted service
     *
     * @param accessToken the OAuth access token for the current OAuth session
     * @param request     the OAuth request to sign
     */
    void signRequest(Token accessToken, OAuthRequest request);

    /**
     * Gives the OAuth version of the provider
     *
     * @return the OAuth version used by the provider (i.e. 1.0a or 2.0)
     */
    String getVersion();

    /**
     * Generates the OAuth authorization URL from the given request Token. It's the step 2 of OAuth negotiation
     *
     * @param requestToken request token to generate Authorization URL
     * @return the authorization URL to call to aks user for delegation on her behalf
     */
    String getAuthorizationUrl(Token requestToken);

    /**
     * Creates an OAuthRequest with the given Rest Verb and uri
     *
     * @param verb Rest verb to build the request
     * @param uri  URI of the request
     * @return the created OAuthRequest
     */
    OAuthRequest requestFactory(Verb verb, String uri);

    /**
     * Gives the OAuth verifier parameter name
     *
     * @return the verifier name
     */
    String getVerifierParamName();


}
