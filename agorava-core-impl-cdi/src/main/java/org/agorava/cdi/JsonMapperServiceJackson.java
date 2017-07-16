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

package org.agorava.cdi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.agorava.api.exception.AgoravaException;
import org.agorava.api.exception.ResponseException;
import org.agorava.api.rest.Response;
import org.agorava.api.service.JsonMapperService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * {@inheritDoc}
 * This CDI implentation uses an Jackson {@link ObjectMapper}.
 *
 * @author Antoine Sabot-Durand
 */
@ApplicationScoped
public class JsonMapperServiceJackson implements JsonMapperService {

    private static final long serialVersionUID = -2012295612034078749L;

    @Produces
    @Singleton
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    @Any
    protected Instance<Module> moduleInstances;

    @Override
    public <T> T mapToObject(Response resp, Class<T> clazz) throws ResponseException {

        String msg = "";
        if (resp.getCode() != 200) {
            throw new ResponseException(resp);
        }
        try {
            msg = resp.getBody();
            if (clazz.equals(String.class))
                return (T) msg;
            return objectMapper.readValue(msg, clazz);
        } catch (IOException e) {
            throw new AgoravaException("Unable to map Json response for this response " + msg, e);
        }
    }

    @Override
    public String objectToJsonString(Object obj) {
        try {
            if (obj.getClass().equals(String.class))
                return (String) obj;
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new AgoravaException("Unable to map a " + obj.getClass().getName() + " to json", e);
        }
    }

    /**
     * Register a Jackson configuration {@link Module} to set special rules for de-serialization
     *
     * @param module to register
     */
    public void registerModule(Module module) {
        objectMapper.registerModule(module);
    }

    @PostConstruct
    protected void init() {
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING).setSerializationInclusion(JsonInclude.Include
                .NON_NULL);
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        for (Module module : moduleInstances) {
            registerModule(module);
        }

    }
}
