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
import org.agorava.core.api.oauth.OAuthAppSettings;

import java.util.ResourceBundle;

/**
 * This Builder creates a {@link OAuthAppSettings} from a {@link ResourceBundle}
 * The bundle name is "agorava" ("agorava.properties" filename) by default but can be customized through {@link #bundleName(String)}
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
 */
public class PropertyOAuthAppSettingsBuilder extends SimpleOAuthAppSettingsBuilder {

    protected String bundleName = "agorava";
    protected String prefix;
    protected static final String[] bindingKeys = {API_KEY, API_SECRET};
    protected static final String[] optionalKeys = {NAME, SCOPE, CALLBACK};

    public static final String BUNDLE_NAME = "bundleName";
    public static final String PREFIX = "prefix";


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
        this.prefix = prefix;
        return this;
    }


    /**
     * {@inheritDoc} <p/>
     * This implementation will build the {@link OAuthAppSettings} from a {@link ResourceBundle}
     * <p/>
     * It'll first try to load all binding (mandatory) fields from the bundle by looking for the key prefix.fieldName (or fieldName if prefix is empty)
     * <p/>
     * In a second time it'll check if optional fields are present in the bundle (with the same key construction) and load them if they are.
     * If they are not present it'll try to load them without prefix
     *
     * @return the built OAuthAppSettings
     * @throws java.util.MissingResourceException
     *                          if the bundle can't be open
     * @throws AgoravaException if a binding field is missing in the bundle
     */
    @Override
    public OAuthAppSettings build() {
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