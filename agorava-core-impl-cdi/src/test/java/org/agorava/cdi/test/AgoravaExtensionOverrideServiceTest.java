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

package org.agorava.cdi.test;

import junit.framework.Assert;
import org.agorava.api.atinject.Current;
import org.agorava.api.oauth.OAuthService;
import org.agorava.api.oauth.OAuthSession;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * @author Antoine Sabot-Durand
 */

@RunWith(Arquillian.class)
public class AgoravaExtensionOverrideServiceTest extends AgoravaTestDeploy {

    @Inject
    @FakeService
    OAuthService service;

    @Inject
    @FakeService
    @Current
    OAuthSession session;

    @Test
    public void testIsServiceOverrided() throws Exception {
        Assert.assertEquals(service.getVerifier(), "FAKE");

    }

    @Test
    public void testIsInjectified() {
        Assert.assertEquals(session.hashCode(), service.getSession().hashCode());
    }
}