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

package org.agorava.api.oauth.application;

import java.lang.annotation.Annotation;

/**
 * Builder for an {@link OAuthAppSettingsImpl} can be initialized directly from
 * fields name or from an Array of {@link Param} in case of Annotation configuration with {@link OAuthApplication}.
 * <p/>
 * For this last purpose, the interface {@link OAuthAppSettingsBuilder} contains constants
 * for fields name should the configuration being read from a text file.
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthAppSettingsBuilder {

    /**
     * key label for the name of the OAuth service
     */
    String NAME = "name";

    /**
     * key label for the consumer key of the OAuth application
     */
    String API_KEY = "apiKey";

    /**
     * key label for the consumer secret of the OAuth application
     */
    String API_SECRET = "apiSecret";

    /**
     * key label for the call back url to send to of the tier service
     */
    String CALLBACK = "callback";

    /**
     * key label for the scope of the OAuth application
     */
    String SCOPE = "scope";

    /**
     * Set the qualifier of the Social Media for which the settings are intended.
     *
     * @param qualifier the name of the related Social Media
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder qualifier(Annotation qualifier);

    /**
     * Set the name of the Social Media for which the settings are intended
     *
     * @param name the name of the related Social Media
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder name(String name);

    /**
     * Set the Social Media application consumer key
     *
     * @param apiKey the consumer (public) OAuth key
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder apiKey(String apiKey);

    /**
     * Set the Social Media application secret key
     *
     * @param apiSecret the secret (private) OAuth key
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder apiSecret(String apiSecret);

    /**
     * Set the Callback for the application
     *
     * @param callback a callback url or 'oob' if the application is not on the web
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder callback(String callback);

    /**
     * Set the OAuth 2.0 scope for the application
     *
     * @param scope list (string separated by commas) of authorized action defined by Social Media for the application
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder scope(String scope);

    /**
     * Load builder params with an array of {@link Param}. This method can be used when params for {@link OAuthAppSettingsImpl}
     * are defined through an annotation.
     *
     * @param params parameters collection to configure the builder
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder params(Param[] params);

    /**
     * Builds the {@link OAuthAppSettingsImpl}
     *
     * @return the application settings
     */
    OAuthAppSettings build();

    /**
     * Set builder params from an existing {@link OAuthAppSettingsImpl}
     *
     * @param settings settings to read from
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder readFromSettings(OAuthAppSettings settings);


}
