/*
 * Copyright 2012 Agorava
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

package org.agorava.core.oauth;

import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.oauth.OAuthAppSettings;
import org.agorava.core.api.oauth.OAuthAppSettingsBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.MissingResourceException;

/**
 * @author Antoine Sabot-Durand
 */
public class PropertySettingsBuilderTest {
    @Test
    public void testDefaultBuild() {

        OAuthAppSettingsBuilder builderOAuthApp = new PropertyOAuthAppSettingsBuilder();
        OAuthAppSettings settings = builderOAuthApp.build();
        Assert.assertEquals(settings.getApiKey(), "dummy");
        Assert.assertEquals(settings.getApiSecret(), "dummySecret");
        Assert.assertEquals(settings.getScope(), "dummyScope");
        Assert.assertEquals(settings.getCallback(), "undefineddummyCallback");

    }

    @Test
    public void testDefaultBuildWithPrefix() {

        PropertyOAuthAppSettingsBuilder builder = new PropertyOAuthAppSettingsBuilder();
        builder.prefix("test");
        OAuthAppSettings settings = builder.build();
        Assert.assertEquals(settings.getApiKey(), "dummy2");
        Assert.assertEquals(settings.getApiSecret(), "dummySecret2");
    }

    @Test(expected = AgoravaException.class)
    public void testDefaultBuildWithIncompletePrefix() {

        PropertyOAuthAppSettingsBuilder builder = new PropertyOAuthAppSettingsBuilder();
        builder.prefix("test2");
        builder.build();
    }


    @Test(expected = AgoravaException.class)
    public void testDefaultBuildWithBadPrefix() {

        PropertyOAuthAppSettingsBuilder builder = new PropertyOAuthAppSettingsBuilder();
        builder.prefix("test3");
        builder.build();
    }

    @Test
    public void testOtherBundleBuild() {

        PropertyOAuthAppSettingsBuilder builder = new PropertyOAuthAppSettingsBuilder();
        builder.bundleName("agorava2");
        OAuthAppSettings settings = builder.build();
        Assert.assertEquals(settings.getApiKey(), "ymmud");
        Assert.assertEquals(settings.getApiSecret(), "ymmudSecret");
        Assert.assertEquals(settings.getScope(), "ymmudScope");
        Assert.assertEquals(settings.getCallback(), "undefinedymmudCallback");

    }


    @Test(expected = MissingResourceException.class)
    public void testBadBundleBuild() {

        PropertyOAuthAppSettingsBuilder builder = new PropertyOAuthAppSettingsBuilder();
        builder.bundleName("agorava3");
        builder.build();

    }
}
