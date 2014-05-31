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
import org.agorava.AgoravaConstants;
import org.agorava.api.atinject.Current;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.service.OAuthLifeCycleService;
import org.agorava.api.storage.GlobalRepository;
import org.agorava.api.storage.UserSessionRepository;
import org.apache.deltaspike.core.api.common.DeltaSpike;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Antoine Sabot-Durand
 */

@RunWith(Arquillian.class)
public class RequestResolverTest extends AgoravaArquillianCommons {

    private static final String SESSION1_ID = "session1";
    private static final String SESSION2_ID = "session2";
    private static final String USER_REPO_ID = "userRepoId";
    @Inject
    @DeltaSpike
    HttpServletRequest request;

    @Inject
    OAuthLifeCycleService lfs;

    @Inject
    @Current
    UserSessionRepository repo;

    @Inject
    GlobalRepository glob;

    @Inject
    @Current
    OAuthSession currentSession;


    private static OAuthSession session1;

    private static OAuthSession session2;

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
                .addAsResource("agorava-request-resolver.properties", "agorava.properties");

        return ret;
    }

    @BeforeClass
    public static void initSessions() {
        UserSessionRepository r = GlobalRepository.getInstance().createNew(USER_REPO_ID);

        session1 = new OAuthSession.Builder().qualifier(FakeServiceLiteral.INSTANCE).repo(r).id(SESSION1_ID).build();
        r.add(session1);

        session2 = new OAuthSession.Builder().qualifier(FakeService2Literal.INSTANCE).repo(r).id(SESSION2_ID).build();
        r.add(session2);
        r.setCurrent(session2);
    }


    @Test
    public void addedSessionShouldBeAvailableInGlobalRepo() {
        Assert.assertEquals(session1, glob.getOauthSession(SESSION1_ID));
    }

    @Test
    public void oauthSessionShouldBeNullWithoutRequestParam() {
        Assert.assertEquals(OAuthSession.NULL, lfs.getCurrentSession());
    }

    @Test
    public void currentSessionShouldBeDrivenByRequestAndSessionIdParam() {
        when(request.getParameter(AgoravaConstants.SESSIONID_PARAM)).thenReturn(SESSION1_ID);
        when(request.getParameter(AgoravaConstants.REPOID_PARAM)).thenReturn(USER_REPO_ID);
        Assert.assertEquals(session1, lfs.getCurrentSession());
    }

    @Test
    public void currentSessionShouldBeConsistent() {
        when(request.getParameter(AgoravaConstants.SESSIONID_PARAM)).thenReturn(SESSION2_ID);
        Assert.assertEquals(session2, currentSession);
        Assert.assertEquals(session2, lfs.getCurrentSession());
    }

    @Test
    public void currentSessionShouldFallBackToRepoCurrentWithoutSessionIdParam() {
        when(request.getParameter(AgoravaConstants.REPOID_PARAM)).thenReturn(USER_REPO_ID);
        Assert.assertEquals(session2, lfs.getCurrentSession());
    }


}
