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

package org.agorava.oauth.helpers.extractors;

import org.agorava.api.exception.OAuthParametersMissingException;
import org.agorava.api.extractor.ExtractorType;
import org.agorava.api.extractor.StringExtractor;
import org.agorava.api.oauth.OAuthRequest;
import org.agorava.api.service.OAuthEncoder;
import org.agorava.api.service.Preconditions;

import java.util.Map;

import static org.agorava.api.extractor.ExtractorType.Type.HEADER;

/**
 * Extracts HEADER. Conforms to OAuth 1.0a
 *
 * @author Pablo Fernandez
 */
@ExtractorType(HEADER)
public class HeaderExtractor implements StringExtractor {
    private static final String PARAM_SEPARATOR = ", ";

    private static final String PREAMBLE = "OAuth ";

    /**
     * {@inheritDoc}
     */
    @Override
    public String extract(OAuthRequest request) {
        checkPreconditions(request);
        Map<String, String> parameters = request.getOauthParameters();
        StringBuffer header = new StringBuffer(parameters.size() * 20);
        header.append(PREAMBLE);
        for (String key : parameters.keySet()) {
            if (header.length() > PREAMBLE.length()) {
                header.append(PARAM_SEPARATOR);
            }
            header.append(String.format("%s=\"%s\"", key, OAuthEncoder.encode(parameters.get(key))));
        }
        return header.toString();
    }

    private void checkPreconditions(OAuthRequest request) {
        Preconditions.checkNotNull(request, "Cannot extract a header from a null object");

        if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0) {
            throw new OAuthParametersMissingException(request);
        }
    }

}
