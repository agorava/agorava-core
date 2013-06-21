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
import org.agorava.core.api.oauth.OAuthService;
import org.agorava.core.api.oauth.OAuthSession;
import org.agorava.core.cdi.Current;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.FileNotFoundException;

/**
 * @author Antoine Sabot-Durand
 */

@RunWith(Arquillian.class)
public class AgoravaExtensionOverrideServiceTest {

    @Inject
    @FakeService
    OAuthService service;

    @Inject
    @FakeService
    @Current
    OAuthSession session;


    @Deployment
    public static Archive<?> createTestArchive() throws FileNotFoundException {
        JavaArchive testJar = ShrinkWrap.create(JavaArchive.class, "all-agorava.jar")

                .addPackages(true, new Filter<ArchivePath>() {
                    @Override
                    public boolean include(ArchivePath path) {
                        return !(path.get().contains("test"));
                    }
                }, "org.agorava")
                .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");


        WebArchive ret = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addClasses(AgoravaExtensionTestProducers.class, FakeRoot.class, FakeService.class, FakeService2.class, FakeServiceLiteral.class, FakeService2Literal.class, FakeServiceImpl.class)
                .addAsLibraries(testJar)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        System.out.println(System.getProperty("arquillian"));
        return ret;
    }


    @Test
    public void testIsServiceOverrided() throws Exception {
        Assert.assertEquals(service.getVerifier(), "FAKE");

    }

    @Test
    public void testIsInjectified() {
        Assert.assertEquals(session.hashCode(), service.getSession().hashCode());
    }
}
