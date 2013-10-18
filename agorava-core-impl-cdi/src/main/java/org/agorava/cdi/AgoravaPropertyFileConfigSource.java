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

package org.agorava.cdi;

import org.apache.deltaspike.core.spi.config.ConfigSource;

import java.util.Map;
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
        resourceBundle = ResourceBundle.getBundle(bundleName);

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