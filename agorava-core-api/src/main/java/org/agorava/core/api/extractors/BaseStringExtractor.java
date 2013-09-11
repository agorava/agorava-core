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

package org.agorava.core.api.extractors;

import org.agorava.core.api.oauth.OAuthRequest;

/**
 * Simple command object that extracts a base string from a {@link org.agorava.core.api.model.OAuthRequest}
 * Extracts an url-encoded base string from the {@link org.agorava.core.api.model.OAuthRequestImpl}.
 * <p/>
 * See <a href="http://oauth.net/core/1.0/#anchor14">the oauth spec</a> for more info on this.
 *
 * @author Pablo Fernandez
 */
public interface BaseStringExtractor extends Extractor<String, OAuthRequest> {

}
