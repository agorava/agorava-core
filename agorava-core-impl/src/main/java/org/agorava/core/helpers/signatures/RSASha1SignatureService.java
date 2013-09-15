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

package org.agorava.core.helpers.signatures;

import org.agorava.core.api.atinject.SignatureType;
import org.agorava.core.api.exception.OAuthSignatureException;
import org.agorava.core.api.service.SignatureService;
import org.apache.commons.codec.binary.Base64;

import java.security.PrivateKey;
import java.security.Signature;

import static org.agorava.core.api.atinject.SignatureType.Type.RSASHA1;

/**
 * A signature service that uses the RSA-SHA1 algorithm.
 */
@SignatureType(RSASHA1)
public class RSASha1SignatureService implements SignatureService {
    private static final String METHOD = "RSA-SHA1";

    private static final String RSA_SHA1 = "SHA1withRSA";

    private PrivateKey privateKey;

    public RSASha1SignatureService(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * {@inheritDoc}
     */
    public String getSignature(String baseString, String apiSecret, String tokenSecret) {
        try {
            Signature signature = Signature.getInstance(RSA_SHA1);
            signature.initSign(privateKey);
            signature.update(baseString.getBytes());
            return new String(Base64.encodeBase64(signature.sign(), false));
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