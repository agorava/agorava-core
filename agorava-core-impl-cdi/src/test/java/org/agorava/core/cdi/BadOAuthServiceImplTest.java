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

package org.agorava.core.cdi;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Ignore;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Antoine Sabot-Durand
 */
//@RunWith(Arquillian.class)
public class BadOAuthServiceImplTest {


    //@Deployment(testable = false)
    //@ShouldThrowException(AgoravaException.class) //FIXME:due to a bug in Arquillian this test cannot be performed -> https://issues.jboss.org/browse/ARQ-480
    public static Archive<?> createTestArchive() throws FileNotFoundException {

        WebArchive ret = ShrinkWrap
                .create(WebArchive.class, "test.war")
                .addPackages(true, "org.agorava")
                .addAsLibraries(new File("../agorava-core-api/target/agorava-core-api.jar"));
        return ret;
    }

    // @Test
    @Inject
    @Ignore //FIXME:disabled for the reason above
    public void testGetQualifier(@BadServiceQual
                                 IncludingBean2 bean2) {
        Assert.assertEquals(FakeServiceLiteral.INSTANCE, bean2.getService().getQualifier());
    }
}
