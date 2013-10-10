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

package org.agorava.cdi.extensions;

import org.agorava.api.oauth.application.OAuthAppSettings;
import org.agorava.api.oauth.application.OAuthAppSettingsBuilder;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.Producer;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import static org.agorava.utils.AgoravaContext.getQualifierToService;


/**
 * @author Antoine Sabot-Durand
 */
class OAuthAppSettingsProducerWithBuilder implements Producer<OAuthAppSettings> {

    private OAuthAppSettingsBuilder builder;

    private Annotation qual;


    OAuthAppSettingsProducerWithBuilder(OAuthAppSettingsBuilder builder, Annotation qual) {
        this.builder = builder;
        this.qual = qual;
    }

    @Override
    public OAuthAppSettings produce(CreationalContext<OAuthAppSettings> ctx) {
        builder.name(getQualifierToService().get(qual));
        OAuthAppSettings newSettings = builder.build();
        ctx.push(newSettings);
        return newSettings;
    }

    @Override
    public void dispose(OAuthAppSettings instance) {
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return new HashSet<InjectionPoint>();
    }
}
