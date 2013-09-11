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
import org.agorava.core.utils.URLUtils;

import javax.inject.Inject;
import java.util.Map;

/**
 * Abstract base class for all API services class
 *
 * @author Antoine Sabot-Durand
 */
public abstract class AbstractApiService {

    @Inject
    URLUtils urlUtils;


    public String buildUri(String url, String key, String value) {
        return urlUtils.buildUri(buildUri(url), key, value);
    }

    public String buildUri(String url, Map<String, ? extends Object> parameters) {
        return urlUtils.buildUri(buildUri(url), parameters);
    }

    public String buildUri(String url) {
        return url;
    }

    public String buildUri(String url, Object pojo) {
        return urlUtils.buildUri(buildUri(url), pojo);
    }

    /**
     * @return the underlying OAuthProvider
     */
    public abstract OAuthService getService();

    public OAuthSession getSession() {
        return getService().getSession();
    }

}
