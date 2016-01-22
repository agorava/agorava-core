/*
 * Copyright 2016 Agorava
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

package org.agorava.oauth.settings;

import org.agorava.AgoravaConstants;
import org.agorava.AgoravaContext;
import org.agorava.api.exception.AgoravaException;
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.oauth.application.OAuthAppSettingsBuilder;
import org.agorava.api.oauth.application.Param;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * {@inheritDoc}
 *
 * @author Antoine Sabot-Durand
 * @author Werner Keil
 */
public class SimpleOAuthAppSettingsBuilder implements OAuthAppSettingsBuilder {

    private String name;

    private String apiKey;

    private String apiSecret;

    private String callback;

    private String scope = "";
    
    private String enable = "";

    private Annotation qualifier;

    @Override
    public OAuthAppSettingsBuilder params(Param[] params) {
        for (Param param : params) {
            invokeSetter(param.name(), param.value());
        }
        return this;
    }

    @Override
    public OAuthAppSettingsBuilder qualifier(Annotation qualifier) {
        this.qualifier = qualifier;
        return this;
    }

    @Override
    public SimpleOAuthAppSettingsBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public SimpleOAuthAppSettingsBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    @Override
    public SimpleOAuthAppSettingsBuilder apiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    /**
     * {@inheritDoc}
     * This implementation add a check to callback. If it's not 'oob' and doesn't start with 'http://'
     * it'll try to get the path of the current application to dynamically create a return URL when
     * connection on Oauth service is over.
     * As some service build the Callback URL from domain name configured when the OAuth application is created (for security
     * reasons), whe should make the distinction between relative URL that should be prefixed here (without a beginning "/")
     * and relative url that will prefixed by remote service (with a beginning "/")
     *
     * @param callback a callback url or 'oob' if the application is not on the web
     * @return this builder
     */
    @Override
    public SimpleOAuthAppSettingsBuilder callback(String callback) {
        if (!"oob".equals(callback) && !callback.startsWith("http://") && !callback.startsWith("https://") && !callback
                .startsWith("/"))
            this.callback = AgoravaContext.webAbsolutePath + "/" + callback;
        else
            this.callback = callback;
        return this;
    }

    @Override
    public SimpleOAuthAppSettingsBuilder scope(String scope) {
        this.scope = scope;
        return this;
    }

    @Override
    public OAuthAppSettings build() {
        if (callback == null)
            callback(AgoravaConstants.CALLBACK_URL);
        return new OAuthAppSettingsImpl(name, apiKey, apiSecret, callback, scope, qualifier, enable);
    }

    @Override
    public OAuthAppSettingsBuilder readFromSettings(OAuthAppSettings settings) {
        apiKey(settings.getApiKey()).
                apiSecret(settings.getApiSecret()).
                callback(settings.getCallback()).
                scope(settings.getScope()).
                qualifier(settings.getQualifier()).name(settings.getSocialMediaName());

        return this;
    }

    /**
     * This reflection method tries to call a field setter in the builder from its name
     * It's useful when settings are reading from a text source.
     *
     * @param k     name of the param
     * @param value value to set in the param
     * @throws AgoravaException if the setter can't be found or invoked
     */
    void invokeSetter(String k, String value) {
        Method setter;
        Class<? extends OAuthAppSettingsBuilder> clazz = getClass();
        try {
            setter = clazz.getMethod(k, String.class);
            setter.invoke(this, value);
        } catch (Exception e) {
            throw new AgoravaException("Unable to invoke setter for " + k + " in class " + clazz, e);
        }

    }
}
