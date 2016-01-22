/*
 * Copyright 2013-2016 Agorava
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

import org.agorava.api.exception.AgoravaException;
import org.agorava.api.oauth.application.OAuthAppSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * This Builder creates a {@link OAuthAppSettings} from a {@link ResourceBundle}
 * The bundle name is "agorava" ("agorava.properties" filename) by default but can be customized through {@link #bundleName
 * (String)}
 * keys in the file must be :
 * <ul>
 * <li>apiKey</li>
 * <li>apiSecret</li>
 * <li>callback</li>
 * <li>scope</li>
 * </ul>
 * <p/>
 * You can configure a prefix for the key with {@link #prefix(String)}. By default the prefix is empty
 *
 * @author Antoine Sabot-Durand
 * @author Werner Keil
 */
public class PropertyOAuthAppSettingsBuilder extends SimpleOAuthAppSettingsBuilder {

    /**
     * Global constant to give type safe name for the bundleName property when used in {@link org.agorava.api.oauth
     * .application.OAuthApplication}
     */
    public static final String BUNDLE_NAME = "bundleName";

    /**
     * Global constant to give type safe name for the prefix property when used in {@link org.agorava.api.oauth
     * .application.OAuthApplication}
     */
    public static final String PREFIX = "prefix";

    private static final String[] bindingKeys = {API_KEY, API_SECRET};

    private static final String[] optionalKeys = {NAME, SCOPE, CALLBACK};

    private String bundleName = "agorava";

    private String prefix = "";

    /**
     * Set the bundle name to read settings from.
     * If it's not used, default bundle name will be 'agorava'
     *
     * @param bundleName name of the bundle
     * @return this builder
     */
    public PropertyOAuthAppSettingsBuilder bundleName(String bundleName) {
        this.bundleName = bundleName;
        return this;
    }

    /**
     * Set the prefix for the keys in the bundle
     * If it's not used, default prefix will be empty
     *
     * @param prefix to add to keys
     * @return this builder
     */
    public PropertyOAuthAppSettingsBuilder prefix(String prefix) {
        this.prefix = prefix.trim();
        return this;
    }

    public static ResourceBundle retrievePropertyBundle(String bundleName) {
            ClassLoader cl=Thread.currentThread().getContextClassLoader();
            InputStream is =cl.getResourceAsStream(bundleName+".properties");
        if(is==null) {
            throw new AgoravaException("Property file : " +bundleName+".properties, not found");
        }
            try {
                return new PropertyResourceBundle(is);
            } catch (IOException e) {
                throw new AgoravaException("Didn't found Agorava properties file",e);
            }
        }
    
    /**
     * {@inheritDoc} <p/>
     * This implementation will build the {@link OAuthAppSettings} from a {@link ResourceBundle}
     * <p/>
     * It'll first try to load all binding (mandatory) fields from the bundle by looking for the key prefix.fieldName (or
     * fieldName if prefix is empty)
     * <p/>
     * In a second time it'll check if optional fields are present in the bundle (with the same key construction) and load
     * them if they are.
     * If they are not present it'll try to load them without prefix
     *
     * @return the built OAuthAppSettings
     * @throws java.util.MissingResourceException if the bundle can't be open
     * @throws AgoravaException                   if a binding field is missing in the bundle
     */
    @Override
    public OAuthAppSettings build() {
        String key;
        String value;
        ResourceBundle rb = retrievePropertyBundle(bundleName);
        String pref = "".equals(prefix) ? "" : prefix + ".";

        for (String k : bindingKeys) {
            key = pref + k;
            if (!rb.containsKey(key)) {
                throw new AgoravaException("Unable to find binding key: " + key + " in bundle " + bundleName + " to build " +
                        "settings");
            }
            value = rb.getString(key);

            invokeSetter(k, value);
        }

        for (String k : optionalKeys) {
            key = pref + k;
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
