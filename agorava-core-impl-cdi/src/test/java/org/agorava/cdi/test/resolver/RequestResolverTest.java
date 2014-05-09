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

package org.agorava.cdi.test.resolver;

import junit.framework.Assert;
import org.agorava.AgoravaConstants;
import org.agorava.api.atinject.Current;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.service.OAuthLifeCycleService;
import org.agorava.api.storage.UserSessionRepository;
import org.agorava.cdi.test.AgoravaTestDeploy;
import org.agorava.cdi.test.FakeService2Literal;
import org.agorava.cdi.test.FakeServiceLiteral;
import org.apache.deltaspike.core.api.common.DeltaSpike;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.when;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Antoine Sabot-Durand
 */

@RunWith(Arquillian.class)
public class RequestResolverTest extends AgoravaTestDeploy {

    @Inject
    @DeltaSpike
    HttpServletRequest request;

    @Inject
    OAuthLifeCycleService lfs;

    @Inject
    @Current
    Instance<UserSessionRepository> repos;


    @Test
    public void providerShouldBeDrivenByRequest() {
        OAuthSession session = lfs.buildSessionFor(FakeServiceLiteral.INSTANCE);
        lfs.buildSessionFor(FakeService2Literal.INSTANCE);
        when(request.getParameter(AgoravaConstants.SESSIONID_PARAM)).thenReturn(session.getId());
        Assert.assertEquals(session.getId(), lfs.getCurrentSession().getId());
        Assert.assertEquals(session.getRepo().getId(), repos.get().getId());

    }


}
