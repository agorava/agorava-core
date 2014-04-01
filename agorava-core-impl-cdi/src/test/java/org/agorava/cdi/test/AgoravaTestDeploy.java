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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: antoine
 * Date: 24/06/13
 * Time: 22:13
 * To change this template use File | Settings | File Templates.
 */
public class AgoravaTestDeploy {
    @Deployment
    public static Archive<?> createTestArchive() throws FileNotFoundException {
        JavaArchive testJar = ShrinkWrap.create(JavaArchive.class, "all-agorava.jar")
                .addPackages(true, new Filter<ArchivePath>() {
                    @Override
                    public boolean include(ArchivePath path) {
                        return !((path.get().contains("test") || path.get().contains("servlet")));
                    }
                }, "org.agorava")
                .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension")
                .addAsResource("META-INF/services/org.apache.deltaspike.core.spi.config.ConfigSourceProvider")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");

        JavaArchive[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.apache.deltaspike.core:deltaspike-core-impl")
                .withTransitivity().as(JavaArchive.class);

        WebArchive ret = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addClasses(AgoravaTestsProducers.class, FakeProvider.class, FakeProvider2.class,
                        FakeService.class, FakeService2.class,
                        FakeServiceLiteral.class, FakeService2Literal.class,
                        FakeUserProfileService.class)
                .addAsLibraries(testJar)
                .addAsLibraries(libs)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource("agorava.properties");
        //  .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension");

        System.out.println(System.getProperty("arquillian"));
        return ret;
    }
}
