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

import org.agorava.core.api.oauth.OAuthAppSettings;
import org.agorava.core.oauth.SimpleOAuthAppSettingsBuilder;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.Producer;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author Antoine Sabot-Durand
 */
class OAuthAppSettingsProducerDecorator implements Producer<OAuthAppSettings> {

    private Producer<OAuthAppSettings> oldProducer;
    private Annotation qual;

    OAuthAppSettingsProducerDecorator(Producer<OAuthAppSettings> oldProducer, Annotation qual) {
        this.oldProducer = oldProducer;
        this.qual = qual;
    }

    @Override
    public OAuthAppSettings produce(CreationalContext<OAuthAppSettings> ctx) {
        OAuthAppSettings settings = oldProducer.produce(ctx);
        OAuthAppSettings newSettings = new SimpleOAuthAppSettingsBuilder().readFromSettings(settings).
                qualifier(qual).
                name(AgoravaExtension.getServicesToQualifier().inverse().get(qual)).build();
        ctx.push(newSettings);
        return newSettings;
    }

    @Override
    public void dispose(OAuthAppSettings instance) {
        oldProducer.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return oldProducer.getInjectionPoints();
    }
}
