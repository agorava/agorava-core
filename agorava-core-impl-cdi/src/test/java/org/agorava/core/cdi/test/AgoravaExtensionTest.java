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

package org.agorava.core.cdi.test;

import junit.framework.Assert;
import org.agorava.core.api.oauth.OAuthAppSettings;
import org.agorava.core.api.oauth.OAuthProvider;
import org.agorava.core.api.oauth.OAuthService;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * @author Antoine Sabot-Durand
 */

@RunWith(Arquillian.class)
public class AgoravaExtensionTest extends AgoravaTestDeploy {

    @Inject
    @FakeService
    OAuthProvider fakeProvider;

    @Inject
    @FakeService2
    OAuthProvider fakeProvider2;

    @Inject
    @FakeService
    OAuthAppSettings settings1;


    @Inject
    @FakeService2
    OAuthAppSettings settings2;

    @Inject
    @FakeService
    OAuthService service;

    @Test
    public void testFakeProvider1Version() {
        Assert.assertEquals(fakeProvider.getVersion(), "1.0");
    }

    @Test
    public void testFakeProvider2Version() {
        Assert.assertEquals(fakeProvider2.getVersion(), "2.0");
    }

    @Test
    public void testFakeSettings1Qual() {
        Assert.assertEquals(settings1.getQualifier(), FakeServiceLiteral.INSTANCE);
    }

    @Test
    public void testFakeSettings2Qual() {
        Assert.assertEquals(settings2.getQualifier(), FakeService2Literal.INSTANCE);
    }
}
