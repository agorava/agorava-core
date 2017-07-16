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

import org.apache.deltaspike.core.impl.config.PropertiesConfigSource;
import org.apache.deltaspike.core.util.PropertyFileUtils;

import java.net.URL;

/**
 * @author Antoine Sabot-Durand
 */
public class PropertyFileConfigSource extends PropertiesConfigSource {

    private String fileName;

    PropertyFileConfigSource(URL propertyFileUrl) {
        super(PropertyFileUtils.loadProperties(propertyFileUrl));
        fileName = propertyFileUrl.toExternalForm();
        initOrdinal(90);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigName() {
        return fileName;
    }
}
