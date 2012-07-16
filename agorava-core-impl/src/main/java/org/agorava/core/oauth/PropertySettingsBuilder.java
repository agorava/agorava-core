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

import com.google.common.base.Joiner;
import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.oauth.ApplicationSettings;

import java.util.ResourceBundle;

/**
 * @author Antoine Sabot-Durand
 */
public class PropertySettingsBuilder extends SimpleSettingsBuilder {

    protected String bundleName = "agorava";
    protected String prefix;
    protected static final String[] bindingKeys = {API_KEY, API_SECRET};
    protected static final String[] optionalKeys = {NAME, SCOPE, CALLBACK};

    public static final String BUNDLE_NAME = "bundleName";
    public static final String PREFIX = "prefix";

    @Override
    public SimpleSettingsBuilder name(String name) {
        return super.name(name);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public PropertySettingsBuilder bundleName(String bundleName) {
        this.bundleName = bundleName;
        return this;
    }

    public PropertySettingsBuilder prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public ApplicationSettings build() {
        String key;
        String value;
        Joiner kj = Joiner.on('.').skipNulls();

        ResourceBundle rb = ResourceBundle.getBundle(bundleName);


        for (String k : bindingKeys) {
            key = kj.join(prefix, k);
            if (!rb.containsKey(key)) {
                throw new AgoravaException("Unable to find binding key: " + key + " in bundle " + bundleName + " to build settings");
            }
            value = rb.getString(key);

            invokeSetter(k, value);
        }

        for (String k : optionalKeys) {
            key = kj.join(prefix, k);
            if (rb.containsKey(key)) {
                value = rb.getString(key);
                invokeSetter(k, value);
            } else if (rb.containsKey(k)) {
                value = rb.getString(k);
                invokeSetter(k, value);
            }
        }

        return super.build();
    }


}