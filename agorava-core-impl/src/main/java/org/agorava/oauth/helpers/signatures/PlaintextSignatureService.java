/*
 * Copyright 2013-2020 Agorava
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

package org.agorava.oauth.helpers.signatures;

import org.agorava.api.exception.OAuthSignatureException;
import org.agorava.api.service.OAuthEncoder;
import org.agorava.api.service.Preconditions;
import org.agorava.api.service.SignatureService;
import org.agorava.api.service.SignatureType;

import static org.agorava.api.service.SignatureType.Type.PLAINTEXT;

/**
 * plaintext implementation of {@code SignatureService}
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 * @author Werner Keil
 * @see SignatureService 
 */

@SignatureType(PLAINTEXT)
public class PlaintextSignatureService implements SignatureService {
    private static final String METHOD = "PLAINTEXT";

    /**
     * {@inheritDoc}
     */
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            Preconditions.checkEmptyString(apiSecret, "Api secret cant be null or empty string");
            return OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret);
        } catch (Exception e) {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getSignatureMethod() {
        return METHOD;
    }
}

