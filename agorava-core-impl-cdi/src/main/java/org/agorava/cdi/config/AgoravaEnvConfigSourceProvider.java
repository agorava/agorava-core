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

import org.apache.deltaspike.core.spi.config.ConfigSource;
import org.apache.deltaspike.core.spi.config.ConfigSourceProvider;
import org.apache.deltaspike.core.util.PropertyFileUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Antoine Sabot-Durand
 */
public class AgoravaEnvConfigSourceProvider implements ConfigSourceProvider {

    private static final Logger LOG = Logger.getLogger(AgoravaEnvConfigSourceProvider.class.getName());

    private List<ConfigSource> configSources = new ArrayList<ConfigSource>();

    AgoravaEnvConfigSourceProvider(String propertyFileName) {
        try {
            Enumeration<URL> propertyFileUrls = PropertyFileUtils.resolvePropertyFiles(propertyFileName);

            while (propertyFileUrls.hasMoreElements()) {
                URL propertyFileUrl = propertyFileUrls.nextElement();
                LOG.log(Level.INFO,
                        "Custom config found by DeltaSpike. Name: ''{0}'', URL: ''{1}''",
                        new Object[] { propertyFileName, propertyFileUrl });
                configSources.add(new PropertyFileConfigSource(propertyFileUrl));
            }
        } catch (IOException ioe) {
            throw new IllegalStateException("problem while loading DeltaSpike property files", ioe);
        }

    }

    @Override
    public List<ConfigSource> getConfigSources() {
        return configSources;
    }
}
