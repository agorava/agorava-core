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

package org.agorava.web;

import org.agorava.core.utils.AgoravaContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * This filter capture the web context and store it in a static field of AgoravaContext It's main use is for calculing Callback
 * URL in OAuth lifecycle
 *
 * @author Antoine Sabot-Durand
 */
@WebFilter({"/*"})
public class CaptureAbsolutePathFilter implements Filter {

    private boolean first = true;

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (first) {
            AgoravaContext.webAbsolutePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort()
                    + req.getServletContext().getContextPath();

            first = false;
        }

        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

}
