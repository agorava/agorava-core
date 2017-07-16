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

import org.agorava.cdi.config.AgoravaDefaultConfigSourceProvider;
import org.agorava.cdi.extensions.AgoravaExtension;
import org.apache.deltaspike.core.spi.config.ConfigSourceProvider;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filter;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;

import java.io.FileNotFoundException;
import javax.enterprise.inject.spi.Extension;


public class AgoravaArquillianCommons {

    public static WebArchive getBasicArchive() throws FileNotFoundException {

        WebArchive ret = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addAsLibraries(getAgoravaAllArchive())
                .addAsLibraries(getMavenArchives())
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return ret;
    }

    public static JavaArchive[] getMavenArchives() {
        return Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.apache.deltaspike.core:deltaspike-core-impl")
                .withTransitivity().as(JavaArchive.class);
    }

    public static JavaArchive getAgoravaAllArchive() {
        return ShrinkWrap.create(JavaArchive.class, "all-agorava.jar")
                .addPackages(true, new Filter<ArchivePath>() {
                    @Override
                    public boolean include(ArchivePath path) {
                        return !((path.get().contains("test") || path.get().contains("servlet")));
                    }
                }, "org.agorava")
                .addAsServiceProvider(Extension.class, AgoravaExtension.class)
                .addAsServiceProvider(ConfigSourceProvider.class, AgoravaDefaultConfigSourceProvider.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }
}
