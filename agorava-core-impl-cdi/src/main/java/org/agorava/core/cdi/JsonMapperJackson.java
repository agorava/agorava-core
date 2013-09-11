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

package org.agorava.core.cdi;

import org.agorava.core.api.JsonMapper;
import org.agorava.core.api.exception.AgoravaException;
import org.agorava.core.api.exception.AgoravaRestException;
import org.agorava.core.api.rest.Response;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.READ_ENUMS_USING_TO_STRING;
import static org.codehaus.jackson.map.SerializationConfig.Feature.WRITE_ENUMS_USING_TO_STRING;

/**
 * {@inheritDoc}
 * This CDI implentation uses an Jackson {@link ObjectMapper}.
 *
 * @author Antoine Sabot-Durand
 */
@ApplicationScoped
public class JsonMapperJackson implements JsonMapper {

    private static final long serialVersionUID = -2012295612034078749L;

    @Produces
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    @Any
    protected Instance<Module> moduleInstances;

    @Override
    public <T> T mapToObject(Response resp, Class<T> clazz) {

        String msg = resp.getBody();
        if (resp.getCode() != 200) {
            throw new AgoravaRestException(resp.getCode(), resp.getUrl(), msg);
        }
        try {
            return objectMapper.readValue(msg, clazz);
        } catch (IOException e) {
            throw new AgoravaException("Unable to map Json response for this response " + msg, e);
        }
    }

    @Override
    public String objectToJsonString(Object obj) {
        try {
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
        objectMapper.enable(WRITE_ENUMS_USING_TO_STRING).setSerializationInclusion(Inclusion.NON_NULL);
        objectMapper.enable(READ_ENUMS_USING_TO_STRING);
        for (Module module : moduleInstances) {
            registerModule(module);
        }

    }
}
