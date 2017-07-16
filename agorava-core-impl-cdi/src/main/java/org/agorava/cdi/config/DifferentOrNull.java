/*
 * Copyright 2014 Agorava
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

package org.agorava.cdi.config;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.deltaspike.core.api.interpreter.ExpressionInterpreter;

/**
 * @author Antoine Sabot-Durand
 */
public class DifferentOrNull implements ExpressionInterpreter<String, Boolean> {


    protected String getConfiguredValue(String key) {
        return ConfigResolver.getProjectStageAwarePropertyValue(key);
    }

    @Override
    public Boolean evaluate(String expression) {
        String[] elts = expression.split(",");
        if (elts.length != 2)
            throw new IllegalArgumentException("Expression " + expression + " has a wrong format. Should be 'key,value'");
        String value = getConfiguredValue(elts[0]);
        if (value == null)
            return true;
        else
            return !value.equals(elts[1].trim());
    }
}
