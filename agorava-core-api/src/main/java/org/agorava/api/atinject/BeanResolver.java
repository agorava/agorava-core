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

package org.agorava.api.atinject;

/**
 *
 * Provide an agnostic way to resolve a bean accross all JSR 330 engine
 *
 * @author Antoine Sabot-Durand
 */
public abstract class BeanResolver {

    /**
     * The current Resolver. Implementation should initialize this at startup
     */
    protected static BeanResolver instance;

    /**
     * @return the current resulover
     */
    public static BeanResolver getInstance() {
        return instance;
    }

    /**
     *
     * Resolve a bean by its name
     *
     * @param name of the bean
     * @return the bean value
     * @throws java.lang.IllegalStateException if no bean is found
     */
    public abstract Object resolve(String name) throws IllegalStateException;

    /**
     * Resolve a bean by its class
     *
     * @param clazz class to look for
     * @param <T> final class of bean value
     * @return the bean value
     * @throws java.lang.IllegalStateException if no bean is found
     */
    public abstract <T> T resolve(Class<T> clazz) throws IllegalStateException;


    /**
     * Rsolve bean by its class. Resolution can be optional
     *
     * @param name of the bean
     * @param optional if true and no bean found return null
     * @return the bean value or null if no bean found and optional is set to true
     * @throws IllegalStateException if no bean found and optional is set to false
     */
    public abstract Object resolve(String name, boolean optional) throws IllegalStateException;

    /**
     * @param clazz    class to look for
     * @param optional if true and no bean found return null
     * @param <T>      final class of bean value
     * @return the bean value
     * @throws IllegalStateException if no bean found and optional is set to false
     */
    public abstract <T> T resolve(Class<T> clazz, boolean optional) throws IllegalStateException;
}
