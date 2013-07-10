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

package org.agorava.core.api.exception;

import java.text.MessageFormat;

/**
 * Exception thrown when a problem occurred in Rest exchanges
 *
 * @author Antoine Sabot-Durand
 */
public class AgoravaRestException extends AgoravaException {

    private static final long serialVersionUID = -1780448283631156367L;

    private int code = 0;

    private String url = "";

    private String msg = "";

    public AgoravaRestException(int code, String url, String msg) {
        super(
                MessageFormat
                        .format("Remote service returned the error code {0} for the following URL : {1}\nThe following data was returned :\n{2}\n",
                                code, url, msg));
        this.code = code;
        this.url = url;
        this.msg = msg;
    }


    public AgoravaRestException(String url2, Exception e) {
        super(MessageFormat.format("Error occurred while connecting to {0}", url2), e);
        url = url2;
    }

    public int getCode() {
        return code;
    }

    public String getUrl() {
        return url;
    }

    public String getMsg() {
        return msg;
    }

}
