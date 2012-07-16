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

import org.agorava.core.api.exception.AgoravaException;

import java.lang.reflect.Method;

/**
 * Abstract builder for {@link ApplicationSettings}
 *
 * @author Antoine Sabot-Durand
 */
public abstract class SettingsBuilder {

    public static final String NAME = "name";
    public static final String API_KEY = "apiKey";
    public static final String API_SECRET = "apiSecret";
    public static final String CALLBACK = "callback";
    public static final String SCOPE = "scope";
    private Param[] params;

    /**
     * Load builder params with an array of {@link Param}. This method can be used when params for {@link ApplicationSettings}
     * are defined through an annotation.
     *
     * @param params
     * @return the current SettingsBuilder
     */
    public SettingsBuilder params(Param[] params) {
        Class clazz = this.getClass();
        for (Param param : params) {
            invokeSetter(param.name(), param.value());
        }
        return this;
    }

    /**
     * The name of the Social Media for which the settings are intended
     *
     * @param name
     * @return the current SettingsBuilder
     */
    public abstract SettingsBuilder name(String name);


    /**
     * Builds the {@link ApplicationSettings}
     *
     * @return the application settings
     */
    public abstract ApplicationSettings build();

    protected void invokeSetter(String k, String value) {
        Method setter;
        Class clazz = this.getClass();
        try {
            setter = clazz.getMethod(k, String.class);
            setter.invoke(this, value);
        } catch (Exception e) {
            throw new AgoravaException("Unable to invoke setter for " + k + " in class " + clazz, e);
        }

    }


}
