/*
 * Copyright 2014 Agorava
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

package org.agorava.cdi.config;

import org.agorava.api.exception.AgoravaException;
import org.apache.deltaspike.core.spi.config.ConfigSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author Antoine Sabot-Durand
 */
public class AgoravaPropertyFileConfigSource implements ConfigSource {

    private final ResourceBundle resourceBundle;

    private final String bundleName;

    @Override
    public int getOrdinal() {
        return 50;
    }

    public AgoravaPropertyFileConfigSource(String bundleName) {
        this.bundleName = bundleName;
        resourceBundle = retrievePropertyBundle(bundleName);

    }

    public static ResourceBundle retrievePropertyBundle(String bundleName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream is = cl.getResourceAsStream(bundleName + ".properties");
        try {
            return new PropertyResourceBundle(is);
        } catch (IOException e) {
            throw new AgoravaException("Didn't found Agorava properties file", e);
        }
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }

    @Override
    public String getPropertyValue(String key) {
        return resourceBundle.containsKey(key) ? resourceBundle.getString(key) : null;
    }

    @Override
    public String getConfigName() {
        return bundleName;
    }

    @Override
    public boolean isScannable() {
        return false;
    }
}
