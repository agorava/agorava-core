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

import org.agorava.core.api.SocialMediaAware;
import org.agorava.core.api.rest.RestResponse;
import org.agorava.core.api.rest.RestService;
import org.agorava.core.api.rest.RestVerb;

import java.util.Map;

/**
 * Implementation of this interface is used to manage a generic OAuth Service
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthService extends RestService, SocialMediaAware {

    /**
     * @return the access token for the OAuth service
     */
    public OAuthToken getAccessToken();

    /**
     * Returns the url to the OAuth service to ask an authorization to access the service.
     *
     * @return the REST URL to use request access
     */
    public String getAuthorizationUrl();

    /**
     * Access to OAuth verifier
     *
     * @return the OAUth verifier
     */
    public String getVerifier();

    /**
     * Initialize the OAuth access token after the service gave an authorization with the Verifier
     */
    public void initAccessToken();

    /**
     * Send an OAuth request signed without any parameter
     *
     * @param verb a REST verb
     * @param uri  the REST address of the request
     * @return an HttpResponse containing the response. It could be in various format (json, xml, string)
     */
    public RestResponse sendSignedRequest(RestVerb verb, String uri);

    /**
     * Send an OAuth request signed with a list a parameter
     *
     * @param verb   a REST verb
     * @param uri    the REST address of the request
     * @param params a Map of key value parameters to send in the header of the request
     * @return an HttpResponse containing the response. It could be in various format (json, xml, string)
     */
    public RestResponse sendSignedRequest(RestVerb verb, String uri, Map<String, ?> params);

    /**
     * Send an OAuth request signed with a single parameter
     *
     * @param verb  a REST verb
     * @param uri   the REST address of the request
     * @param key   name of the parameter
     * @param value value of the parameter
     * @return an HttpResponse containing the response. It could be in various format (json, xml, string)
     */
    public RestResponse sendSignedRequest(RestVerb verb, String uri, String key, Object value);

    /**
     * Used to initialize verifier code returned by OAuth service
     *
     * @param verifierStr
     */
    public void setVerifier(String verifierStr);

    /**
     * Initialize and set an OAuth access token from its public and private keys
     *
     * @param token  public key
     * @param secret secret keys
     */
    public void setAccessToken(String token, String secret);

    /**
     * Set the Access Token with for an OAuth access
     *
     * @param token the token to set
     */
    public void setAccessToken(OAuthToken token);

    /**
     * Send an OAuth request signed with an XML Paylad as content
     *
     * @param verb    the REST verb of the request
     * @param uri     the url of the remote request
     * @param payload the content of the XML payload to send to the service
     * @return an HttpResponse containing the response. It could be in various format (json, xml, string)
     */
    public RestResponse sendSignedXmlRequest(RestVerb verb, String uri, String payload);

    /**
     * @return the session settings of the given service
     */
    public OAuthSession getSession();

    /**
     * Signs and sends a simple request
     *
     * @param request
     * @return an HttpResponse containing the response. It could be in various format (json, xml, string)
     */
    public RestResponse sendSignedRequest(OAuthRequest request);

}
