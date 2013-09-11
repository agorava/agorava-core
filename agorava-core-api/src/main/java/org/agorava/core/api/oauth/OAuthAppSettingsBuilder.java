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

import java.lang.annotation.Annotation;

/**
 * Builder for an {@link OAuthAppSettings} can be initialized directly from
 * fields name or from an Array of {@link Param} in case of Annotation configuration.
 * <p/>
 * For this last purpose, the interface {@link OAuthAppSettingsBuilder} contains constants
 * for fields name.
 *
 * @author Antoine Sabot-Durand
 */
public interface OAuthAppSettingsBuilder {

    //TODO: switch to OAuthConstants
    public static final String NAME = "name";
    public static final String API_KEY = "apiKey";
    public static final String API_SECRET = "apiSecret";
    public static final String CALLBACK = "callback";
    public static final String SCOPE = "scope";


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
     * Load builder params with an array of {@link Param}. This method can be used when params for {@link OAuthAppSettings}
     * are defined through an annotation.
     *
     * @param params parameters collection to configure the builder
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder params(Param[] params);


    /**
     * Builds the {@link OAuthAppSettings}
     *
     * @return the application settings
     */
    OAuthAppSettings build();

    /**
     * Set builder params from an existing {@link OAuthAppSettings}
     *
     * @param settings
     * @return the current OAuthAppSettingsBuilder
     */
    OAuthAppSettingsBuilder readFromSettings(OAuthAppSettings settings);


}
