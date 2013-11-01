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

package org.agorava.helpers.signatures;

import org.agorava.api.exception.OAuthSignatureException;
import org.agorava.api.service.OAuthEncoder;
import org.agorava.api.service.Preconditions;
import org.agorava.api.service.SignatureService;
import org.agorava.api.service.SignatureType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import static org.agorava.api.service.SignatureType.Type.HMACSHA1;

/**
 * HMAC-SHA1 implementation of {@SignatureService}
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 */

@SignatureType(HMACSHA1)
public class HMACSha1SignatureService implements SignatureService {
    private static final String EMPTY_STRING = "";

    private static final String CARRIAGE_RETURN = "\r\n";

    private static final String UTF8 = "UTF-8";

    private static final String HMAC_SHA1 = "HmacSHA1";

    private static final String METHOD = "HMAC-SHA1";

    /**
     * {@inheritDoc}
     */
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            Preconditions.checkEmptyString(baseString, "Base string cant be null or empty string");
            Preconditions.checkEmptyString(apiSecret, "Api secret cant be null or empty string");
            return doSign(baseString, OAuthEncoder.encode(apiSecret) + '&' + OAuthEncoder.encode(tokenSecret));
        } catch (Exception e) {
            throw new OAuthSignatureException(baseString, e);
        }
    }

    private String doSign(String toSign, String keyString) throws Exception {
        SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8), HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(key);
        byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
        return new String(DatatypeConverter.printBase64Binary(bytes).replace(CARRIAGE_RETURN, EMPTY_STRING));
    }

    /**
     * {@inheritDoc}
     */
    public String getSignatureMethod() {
        return METHOD;
    }
}
