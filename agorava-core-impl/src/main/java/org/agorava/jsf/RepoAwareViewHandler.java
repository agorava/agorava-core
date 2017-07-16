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
import org.agorava.api.storage.UserSessionRepository;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Antoine Sabot-Durand
 */
public class RepoAwareViewHandler extends ViewHandlerWrapper {


    private final ViewHandler delegate;

    private BeanResolver resolver = BeanResolver.getInstance();


    public RepoAwareViewHandler(ViewHandler delegate) {
        this.delegate = delegate;
    }


    @Override
    public ViewHandler getWrapped() {
        return delegate;
    }


    @Override
    public String getActionURL(FacesContext facesContext, String viewId) {
        String actionUrl = super.getActionURL(facesContext, viewId);
        UserSessionRepository repo = (UserSessionRepository) resolver.resolve("currentRepo");


        return new FacesUrlTransformer(actionUrl).appendParamIfNecessary("repoid",
                repo.getId()).getUrl();
    }


    @Override
    public String getRedirectURL(FacesContext context, String viewId, Map<String, List<String>> parameters, boolean
            includeViewParams) {
        UserSessionRepository repo = (UserSessionRepository) resolver.resolve("currentRepo");
        List<String> values = new ArrayList<String>();
        values.add(repo.getId());
        parameters.put("repoid", values);
        return super.getRedirectURL(context, viewId, parameters, includeViewParams);
    }

    @Override
    public String getBookmarkableURL(FacesContext context, String viewId, Map<String, List<String>> parameters,
                                     boolean includeViewParams) {
        UserSessionRepository repo = (UserSessionRepository) resolver.resolve("currentRepo");
        List<String> values = new ArrayList<String>();
        values.add(repo.getId());
        parameters.put("repoid", values);
        return super.getBookmarkableURL(context, viewId, parameters, includeViewParams);
    }
}
