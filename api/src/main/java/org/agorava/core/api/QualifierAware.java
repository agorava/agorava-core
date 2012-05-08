/*******************************************************************************
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
 ******************************************************************************/
/**
 * 
 */
package org.agorava.core.api;

import java.lang.annotation.Annotation;

/**
 * @author Antoine Sabot-Durand
 * 
 */
public interface QualifierAware {

    /**
     * Returns the Qualifier used for this social network
     * 
     * @return Annotation being a Qualifier
     */
    public Annotation getQualifier();

}
