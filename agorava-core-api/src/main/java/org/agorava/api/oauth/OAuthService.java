/*
 * Copyright 2014 Agorava
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

import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.rest.Response;
import org.agorava.api.rest.RestService;
import org.agorava.api.rest.Verb;
import org.agorava.api.service.JsonMapperService;

import java.util.Map;

/**
 * Provides all Oauth Services
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthService extends RestService {

    /**
     * @return the access token for the OAuth service
     */
    Token getAccessToken();

    /**
     * Set the Access Token with for an OAuth access
     *
     * @param token the token to set
     */
    void setAccessToken(Token token);

    /**
     * Returns the url to the OAuth service to ask an authorization to access the service.
     *
     * @return the REST URL to use request access
     */
    String getAuthorizationUrl();

    /**
     * Access to OAuth verifier
     *
     * @return the OAUth verifier
     */
    String getVerifier();

    /**
     * Used to initialize verifier code returned by OAuth service
     *
     * @param verifierStr verifier code
     */
    void setVerifier(String verifierStr);

  /*  *//**
     * Initialize the OAuth access token after the service gave an authorization with the Verifier
     *//*
    void initAccessToken();
*/

    /**
     * Send an OAuth request signed without any parameter
     *
     * @param verb a REST verb
     * @param uri  the REST address of the request
     * @return an HttpResponse containing the response. It could be in various format (json, xml, string)
     */
    Response sendSignedRequest(Verb verb, String uri);

    /**
     * Send an OAuth request signed with a list a parameter
     *
     * @param verb   a REST verb
     * @param uri    the REST address of the request
     * @param params a Map of key value parameters to send in the header of the request
     * @return an HttpResponse containing the response. It could be in various format (json, xml, string)
     */
    Response sendSignedRequest(Verb verb, String uri, Map<String, ?> params);

    /**
     * Send an OAuth request signed with a single parameter
     *
     * @param verb  a REST verb
     * @param uri   the REST address of the request
     * @param key   name of the parameter
     * @param value value of the parameter
     * @return an HttpResponse containing the response. It could be in various format (json, xml, string)
     */
    Response sendSignedRequest(Verb verb, String uri, String key, Object value);

    /**
     * Initialize and set an OAuth access token from its  and private keys
     *
     * @param token  key
     * @param secret secret keys
     */
    void setAccessToken(String token, String secret);

    /**
     * Send an OAuth request signed with an XML Payload as content
     *
     * @param verb    the REST verb of the request
     * @param uri     the url of the remote request
     * @param payload the content of the XML payload to send to the service
     * @return an HttpResponse containing the response. It could be in various format (json, xml, string)
     */
    Response sendSignedXmlRequest(Verb verb, String uri, String payload);

    /**
     * @return the session settings of the given service
     */
    OAuthSession getSession();

    /**
     * Signs and sends a simple request
     *
     * @param request the request to sign and send
     * @return the response
     */
    Response sendSignedRequest(OAuthRequest request);

    /**
     * Perform a conditionally signed REST get command and return an object of the provided class
     *
     * @param uri    a string with {@link java.text.MessageFormat} placeholders (i.e. {0},
     *               {1}) style for params. It's the uri to perform the REST get call
     * @param clazz  class of the returned object
     * @param signed indicate if the request has to be signed or not
     * @param <T>    generic type for returned object
     * @return the answer in the asked class T
     */
    <T> T get(String uri, Class<T> clazz, boolean signed);

    /**
     * Gives the OAuth verifier parameter name
     *
     * @return the verifier name
     */
    String getVerifierParamName();


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
    OAuth.OAuthVersion getVersion();

    /**
     * Creates an OAuthRequest with the given Rest Verb and uri
     *
     * @param verb Rest verb to build the request
     * @param uri  URI of the request
     * @return the created OAuthRequest
     */
    OAuthRequest requestFactory(Verb verb, String uri);

    /**
     * @return a service to map Json to POJO
     */
    JsonMapperService getJsonMapper();

    /**
     * @return configuration settings needed to access an OAuth 1.0a and 2.0 service tier
     */
    OAuthAppSettings getConfig();
}
