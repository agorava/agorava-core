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

package org.agorava.jsf;

import org.agorava.api.atinject.BeanResolver;
import org.agorava.api.oauth.OAuthSession;
import org.agorava.api.storage.UserSessionRepository;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * @author Antoine Sabot-Durand
 */

@FacesConverter(forClass = OAuthSession.class)
public class SessionConverter implements Converter {

    private UserSessionRepository getRepo() {
        return (UserSessionRepository) BeanResolver.getInstance().resolve("currentRepo");
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return getRepo().get(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((OAuthSession) value).getId();
    }
}
