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

package org.agorava.cdi.decorators;

import org.agorava.api.event.OAuthComplete;
import org.agorava.api.event.SocialEvent;
import org.agorava.api.oauth.OAuthService;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

/**
 * @author Antoine Sabot-Durand
 */
@Decorator
public abstract class OAuthServiceDecorator implements OAuthService {

    @Inject
    @Any
    @Delegate
    OAuthService delegate;

    @Inject
    Event<OAuthComplete> completeEvt;

    @Override
    public void initAccessToken() {
        delegate.initAccessToken();
        completeEvt.fire(new OAuthComplete(SocialEvent.Status.SUCCESS, "", delegate.getSession()));

    }
}
