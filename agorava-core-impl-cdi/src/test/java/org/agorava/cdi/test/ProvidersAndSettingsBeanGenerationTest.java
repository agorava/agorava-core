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

package org.agorava.cdi.test;

import junit.framework.Assert;
import org.agorava.api.oauth.OAuthService;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.agorava.api.oauth.OAuth.OAuthVersion.ONE;
import static org.agorava.api.oauth.OAuth.OAuthVersion.TWO_DRAFT_11;

import java.io.FileNotFoundException;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * @author Antoine Sabot-Durand
 */

@RunWith(Arquillian.class)
public class ProvidersAndSettingsBeanGenerationTest extends AgoravaArquillianCommons {

    @Deployment
    public static Archive<?> createTestArchive() throws FileNotFoundException {

        WebArchive ret = getBasicArchive()
                .addClasses(AgoravaTestsProducers.class,
                        FakeProvider.class,
                        FakeProvider2.class,
                        FakeService.class,
                        FakeService2.class,
                        FakeServiceLiteral.class,
                        FakeService2Literal.class)
                .addAsWebInfResource("agorava.properties");

        return ret;
    }


    @Inject
    @FakeService
    OAuthService fakeProvider1;

    @Inject
    @FakeService2
    OAuthService fakeProvider2;

    @Inject
    @FakeService
    OAuthAppSettings settings1;


    @Inject
    @FakeService2
    OAuthAppSettings settings2;

    @Inject
    @FakeService
    OAuthService service;

    @Inject
    @FakeService2
    OAuthService service2;

    @Inject
    @Any
    Instance<OAuthSession> sessions;


    @Test
    public void providersShouldHaveRightOauthVersion() {
        Assert.assertEquals("Bad OAuth Version for fakeProvider1", ONE, fakeProvider1.getVersion());
        Assert.assertEquals("Bad OAuth Version for fakeProvider2", TWO_DRAFT_11, fakeProvider2.getVersion());
    }

    @Test
    public void settingsShouldHaveRightQualifier() {
        Assert.assertEquals("Bad qualifier for settings1", FakeServiceLiteral.INSTANCE, settings1.getQualifier());
        Assert.assertEquals("Bad qualifier for settings2", FakeService2Literal.INSTANCE, settings2.getQualifier());
    }

    
    
    /*@Test(expected = NullPointerException.class)
    public void testServiceWithCurrentSession() {
        Assert.assertNotNull(service.getSession());

    }

    @Test(expected = NullPointerException.class)
    public void testServiceWithoutCurrentSession() {
        service2.getSession();
    }


    @Test(expected = NullPointerException.class)
    public void testIsInjectified() {
        Assert.assertEquals(session.hashCode(), service.getSession().hashCode());
    }*/
}
