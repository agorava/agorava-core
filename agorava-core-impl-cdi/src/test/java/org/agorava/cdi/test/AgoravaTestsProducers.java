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

import org.agorava.AgoravaConstants;
import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.oauth.settings.PropertyOAuthAppSettingsBuilder;
import org.apache.deltaspike.core.api.common.DeltaSpike;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Antoine Sabot-Durand
 */
public class AgoravaTestsProducers {


    @ApplicationScoped
    @Produces
    @FakeService
    public OAuthAppSettings produceFirstSetting() {
        PropertyOAuthAppSettingsBuilder builder = new PropertyOAuthAppSettingsBuilder();
        return builder.build();
    }

    @ApplicationScoped
    @Produces
    @FakeService2
    public OAuthAppSettings produceSecondSetting() {
        PropertyOAuthAppSettingsBuilder builder = new PropertyOAuthAppSettingsBuilder();
        return builder.prefix("test").build();
    }

    @Produces
    @DeltaSpike
    @RequestScoped
    public HttpServletRequest produceMockedRequest() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(AgoravaConstants.REPOID_PARAM)).thenReturn("1234");
        when(req.getParameter(AgoravaConstants.SESSIONID_PARAM)).thenReturn("5678");
        return req;
    }
 /*   @ApplicationScoped
    @Produces
    @Current
    @FakeService
    public OAuthSession produceAppSession() {

        return new Builder().qualifier(FakeServiceLiteral.INSTANCE).build();
    }*/
}
