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

package org.agorava.core.oauth;

import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.oauth.OAuthAppSettings;
import org.agorava.core.api.oauth.OAuthAppSettingsBuilder;
import org.agorava.core.api.oauth.Param;
import org.agorava.core.utils.AgoravaContext;

import java.lang.reflect.Method;

/**
 * {@inheritDoc}
 *
 * @author Antoine Sabot-Durand
 */
public class SimpleOAuthAppSettingsBuilder implements OAuthAppSettingsBuilder {

    private String name;
    private String apiKey;
    private String apiSecret;
    private String callback = "oob";
    private String scope = "";


    @Override
    public OAuthAppSettingsBuilder params(Param[] params) {
        for (Param param : params) {
            invokeSetter(param.name(), param.value());
        }
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
     * connection on remote Media is over.
     *
     * @param callback a callback url or 'oob' if the application is not on the web
     * @return this builder
     */
    @Override
    public SimpleOAuthAppSettingsBuilder callback(String callback) {
        if (!"oob".equals(callback) && !callback.startsWith("http://") && !callback.startsWith("https://"))
            this.callback = AgoravaContext.webAbsolutePath + callback;
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
        return new OAuthAppSettingsImpl(name, apiKey, apiSecret, callback, scope);
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
        Class<? extends OAuthAppSettingsBuilder> clazz = this.getClass();
        try {
            setter = clazz.getMethod(k, String.class);
            setter.invoke(this, value);
        } catch (Exception e) {
            throw new AgoravaException("Unable to invoke setter for " + k + " in class " + clazz, e);
        }

    }
}
