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

package org.agorava.spi;

import org.agorava.api.oauth.application.OAuthAppSettings;

/**
 * An interface to provide a way to tune {@link org.agorava.api.oauth.application.OAuthAppSettings}
 *
 * @author Antoine Sabot-Durand
 */
public interface AppSettingsTuner {

    /**
     * The tuning method
     *
     * @param toTune the setting to tune
     * @return the tuned settings
     */
    OAuthAppSettings tune(final OAuthAppSettings toTune);
}
