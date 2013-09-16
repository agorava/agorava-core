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
/**
 *
 */
package org.agorava.core.spi;

import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.api.rest.HttpParameters;

import javax.inject.Inject;
import java.util.Map;

/**
 * Abstract base class for all Tier high level REST API calls.
 * <p/>
 * Concrete class must implement some or API of the remote service
 *
 * @author Antoine Sabot-Durand
 */
public abstract class TierService {


    /**
     * Helps the service to build URI to call tier API
     */
    @Inject
    protected HttpParameters params;


    /**
     * Build an URI with a parameter
     *
     * @param uri   base url
     * @param key   param name
     * @param value param value
     * @return absolute uri to perform call to remote service API
     */
    protected String buildUri(String uri, String key, String value) {
        return params.add(key, value).asUrl(buildAbsoluteUri(uri));
    }


    /**
     * Build an URI with multiple parameters coming from a Map
     *
     * @param uri        base url
     * @param parameters map containing parameters
     * @return absolute uri to perform call to remote service API
     */
    protected String buildUri(String uri, Map<String, ? extends Object> parameters) {
        return params.addMap(parameters).asUrl(buildAbsoluteUri(uri));
    }

    /**
     * Build an URI. This method should be overrided to build complete URI from relative URI
     * By default it returns the value in parameter to work with absolute uri
     *
     * @param uri to transform in absolute version
     * @return absolute URI
     */
    protected String buildAbsoluteUri(String uri) {
        return uri;
    }


    /**
     * @return the underlying service to communicate with Tier Service
     */
    protected abstract OAuthService getService();

    /**
     * @return the current user session
     */
    protected OAuthSession getSession() {
        return getService().getSession();
    }

}
