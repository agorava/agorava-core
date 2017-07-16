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

package org.agorava.api.extractor;

import org.agorava.api.oauth.OAuthRequest;

/**
 * Extracts an OAuth 1.0a url-encoded base string from the {@link OAuthRequest}.
 *
 * @author Antoine Sabot-Durand
 */
public interface StringExtractor extends Extractor<String, OAuthRequest> {

}
