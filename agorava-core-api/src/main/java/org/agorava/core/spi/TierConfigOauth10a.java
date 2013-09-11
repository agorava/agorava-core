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

package org.agorava.core.spi;

import org.agorava.core.api.ExtractorKind;
import org.agorava.core.api.OAuthVersion;
import org.agorava.core.api.extractors.BaseStringExtractor;
import org.agorava.core.api.extractors.HeaderExtractor;
import org.agorava.core.api.extractors.TokenExtractor;
import org.agorava.core.api.oauth.SignatureType;
import org.agorava.core.api.oauth.Token;
import org.agorava.core.api.rest.Verb;
import org.agorava.core.api.services.HMACSha1SignatureService;
import org.agorava.core.api.services.SignatureService;
import org.agorava.core.api.services.TimestampService;
import org.agorava.core.api.services.TimestampServiceImpl;

import javax.inject.Inject;

import static org.agorava.core.api.ExtractorKind.Kind.BASIC;
import static org.agorava.core.api.OAuthVersion.ONE;

/**
 * Default implementation of the OAuth protocol, version 1.0a
 * <p/>
 * This class is meant to be extended by concrete implementations of the API,
 * providing the endpoints and endpoint-http-verbs.
 * <p/>
 * If your Api adheres to the 1.0a protocol correctly, you just need to extend
 * this class and define the getters for your endpoints.
 * <p/>
 * If your Api does something a bit different, you can override the different
 * extractors or services, in order to fine-tune the process. Please read the
 * javadocs of the interfaces to get an idea of what to do.
 *
 * @author Pablo Fernandez
 */
public abstract class TierConfigOauth10a implements TierConfigOauth {

    @Inject
    @ExtractorKind(BASIC)
    TokenExtractor tokenExtractor;


    @Inject
    BaseStringExtractor baseStringExtractor;

    @Inject
    HeaderExtractor headerExtractor;

    public TokenExtractor getAccessTokenExtractor() {
        return tokenExtractor;
    }

    /**
     * Returns the base string extractor.
     *
     * @return base string extractor
     */
    public BaseStringExtractor getBaseStringExtractor() {
        return baseStringExtractor;
    }

    /**
     * Returns the header extractor.
     *
     * @return header extractor
     */
    public HeaderExtractor getHeaderExtractor() {
        return headerExtractor;
    }

    /**
     * Returns the request token extractor.
     *
     * @return request token extractor
     */
    public TokenExtractor getRequestTokenExtractor() {
        return tokenExtractor;
    }

    /**
     * Returns the signature service.
     *
     * @return signature service
     */
    public SignatureService getSignatureService() {
        return new HMACSha1SignatureService();
    }

    /**
     * Returns the timestamp service.
     *
     * @return timestamp service
     */
    public TimestampService getTimestampService() {
        return new TimestampServiceImpl();
    }

    /**
     * Returns the verb for the access token endpoint (defaults to POST)
     *
     * @return access token endpoint verb
     */
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

    /**
     * Returns the verb for the request token endpoint (defaults to POST)
     *
     * @return request token endpoint verb
     */
    public Verb getRequestTokenVerb() {
        return Verb.POST;
    }

    /**
     * Returns the URL that receives the request token requests.
     *
     * @return request token URL
     */
    public abstract String getRequestTokenEndpoint();

    /**
     * Returns the URL that receives the access token requests.
     *
     * @return access token URL
     */
    public abstract String getAccessTokenEndpoint();

    /**
     * Returns the URL where you should redirect your users to authenticate
     * your application.
     *
     * @param requestToken the request token you need to authorize
     * @return the URL where you should redirect your users
     */
    public abstract String getAuthorizationUrl(Token requestToken);

    @Override
    public SignatureType getSignatureType() {
        return SignatureType.Header;
    }

    @Override
    public OAuthVersion getOAuthVersion() {
        return ONE;
    }
}
