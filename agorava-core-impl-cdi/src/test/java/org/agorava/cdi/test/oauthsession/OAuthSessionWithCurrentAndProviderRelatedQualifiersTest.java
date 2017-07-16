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

package org.agorava.cdi.test.oauthsession;

import junit.framework.Assert;
import org.agorava.cdi.test.AgoravaArquillianCommons;
import org.agorava.cdi.test.FakeProvider;
import org.agorava.cdi.test.FakeService;
import org.agorava.cdi.test.FakeServiceLiteral;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;


/**
 * @author Antoine Sabot-Durand
 */

@RunWith(Arquillian.class)
public class OAuthSessionWithCurrentAndProviderRelatedQualifiersTest extends AgoravaArquillianCommons {


    @Deployment
    @ShouldThrowException(OAuthSessionWithCurrentAndProviderRelatedQualifiersException.class)
    public static Archive<?> createTestArchive() throws FileNotFoundException {

        WebArchive ret = getBasicArchive()
                .addClasses(
                        FakeService.class,
                        FakeServiceLiteral.class,
                        FakeProvider.class,

                        FaultyResolver.class)
                .addAsResource("agorava-faulty-resolver.properties", "agorava.properties");
        ;

        return ret;
    }

    @Test
    public void dummyTest() {
        Assert.assertTrue(true);
    }

}
