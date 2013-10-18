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

package org.agorava.web.spi;

import org.agorava.api.exception.AgoravaException;
import org.agorava.api.service.SessionService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Antoine Sabot-Durand
 */
@WebServlet(value = "/callback", name = "OAuthCallBackCdi")
public  class OAuthCallbackServlet extends HttpServlet {

    @Inject
    SessionService sessionService;

    protected void renderResponse(HttpServletRequest req, HttpServletResponse resp) {
           ServletOutputStream os = null;
           try {
               os = resp.getOutputStream();
               os.println("<script type=\"text/javascript\">");
               os.println("function moveOn() {\n" +
                       "            window.opener.location.reload();\n" +
                       "            window.close();\n" +
                       "        }\n" +
                       "moveOn();");
               os.println("</script>");
               os.flush();
               os.close();
           } catch (IOException e) {
               throw new AgoravaException(e);
           }

       }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String verifier = req.getParameter(sessionService.getCurrentService().getVerifierParamName());
        sessionService.completeSession(verifier);
        renderResponse(req, resp);
    }
}
