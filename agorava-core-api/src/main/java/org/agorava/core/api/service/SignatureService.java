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

package org.agorava.core.api.service;

/**
 * Signs a base string, returning the OAuth signature
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 */
public interface SignatureService {
    /**
     * Returns the signature
     *
     * @param baseString  url-encoded string to sign
     * @param apiSecret   api secret for your app
     * @param tokenSecret token secret (empty string for the request token step)
     * @return signature
     */
    String getSignature(String baseString, String apiSecret, String tokenSecret);

    /**
     * @return signature method/algorithm label
     */
    String getSignatureMethod();
}
