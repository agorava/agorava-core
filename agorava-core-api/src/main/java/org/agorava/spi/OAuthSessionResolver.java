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

package org.agorava.spi;

import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.storage.UserSessionRepository;

import java.io.Serializable;

/**
 * @author Antoine Sabot-Durand
 */
public interface OAuthSessionResolver extends Serializable {

    OAuthSession getCurrentSession(UserSessionRepository repository);
}
