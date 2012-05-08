/*******************************************************************************
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
 ******************************************************************************/

package org.agorava.core.cdi.scribe;

import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.oauth.OAuthProvider;
import org.agorava.core.api.oauth.OAuthRequest;
import org.agorava.core.api.oauth.OAuthServiceSettings;
import org.agorava.core.api.oauth.OAuthToken;
import org.agorava.core.api.rest.RestVerb;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

/**
 * @author Antoine Sabot-Durand
 * 
 */
public class OAuthProviderScribe implements OAuthProvider {

    private static final String SCRIBE_API_PREFIX = "org.scribe.builder.api.";
    private static final String SCRIBE_API_SUFFIX = "Api";

    private final org.scribe.oauth.OAuthService service;

    org.scribe.oauth.OAuthService getService() {
        if (service == null)
            throw new IllegalStateException("OAuthProvider can be used before it is initialized");
        return service;
    }

    @Override
    public OAuthToken getRequestToken() {
        return new OAuthTokenScribe("2.0".equals(getVersion()) ? null : getService().getRequestToken());
    }

    @Override
    public OAuthToken getAccessToken(OAuthToken requestToken, String verifier) {
        // TODO: catch exception from Scribe if the remote service is unavailabe
        return createToken(getService().getAccessToken(extractToken(requestToken), new Verifier(verifier)));
    }

    @Override
    public void signRequest(OAuthToken accessToken, OAuthRequest request) {
        getService().signRequest(extractToken(accessToken), ((OAuthRequestScribe) request).getDelegate());
    }

    @Override
    public String getVersion() {
        return getService().getVersion();
    }

    @Override
    public String getAuthorizationUrl(OAuthToken tok) {
        return getService().getAuthorizationUrl(extractToken(tok));
    }

    public OAuthProviderScribe(OAuthServiceSettings settings) {
        super();
        Class<? extends Api> apiClass = getApiClass(settings.getServiceName()); // TODO : should get API class differently !
        ServiceBuilder serviceBuilder = new ServiceBuilder().provider(apiClass).apiKey(settings.getApiKey())
                .apiSecret(settings.getApiSecret());
        if (settings.getCallback() != null && !("".equals(settings.getCallback())))
            serviceBuilder.callback(settings.getCallback());
        if (settings.getScope() != null && !("".equals(settings.getScope()))) {
            serviceBuilder.scope(settings.getScope());
        }
        service = serviceBuilder.build();
    }

    /**
     * @param serviceName
     * @return
     */
    @SuppressWarnings("unchecked")
    private Class<? extends Api> getApiClass(String serviceName) {
        String className = SCRIBE_API_PREFIX + serviceName + SCRIBE_API_SUFFIX;
        try {
            return (Class<? extends Api>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new AgoravaException("There no such Scribe Api class " + className, e);
        }
    }

    protected Token extractToken(OAuthToken tok) {
        return ((OAuthTokenScribe) tok).delegate;
    }

    @Override
    public OAuthRequest requestFactory(RestVerb verb, String uri) {
        return new OAuthRequestScribe(verb, uri);
    }

    @Override
    public OAuthToken tokenFactory(String token, String secret) {
        return new OAuthTokenScribe(token, secret);
    }

    private OAuthToken createToken(Token token) {
        return new OAuthTokenScribe(token);
    }
}
