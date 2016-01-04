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

package org.agorava.api.storage;

import java.io.Serializable;
import java.util.Collection;

import org.agorava.api.function.Identifiable;

/**
 * A repository containing a collection of element that can be iterated
 *
 * @param <T> the type of element contained in repo
 * @author Antoine Sabot-Durand
 */
public interface Repository<T extends Identifiable> extends Serializable, Iterable<T> {

    /**
     * @return the current element
     */
    T getCurrent();

    /**
     * @return all elements in this Repository
     */
    Collection<T> getAll();

    /**
     * Set the current element
     *
     * @param element to set as current
     */
    void setCurrent(T element);

    /**
     * Look for element with the given id, set it as current and return it
     *
     * @param id od the element to set as current
     * @return the element with the given id
     * @throws IllegalArgumentException if no element with the given id is found
     */
    T setCurrent(String id) throws IllegalArgumentException;

    /**
     * Return element with the given id
     *
     * @param id a String id for the element
     * @return the element with the given id or null if it doesn't exist
     */
    T get(String id);


    /**
     * Remove the current Element
     *
     * @return true if it was removed correctly
     */
    boolean removeCurrent();

    /**
     * Remove the element with the given id
     *
     * @param id of the element to remove
     * @return true if it was removed correctly
     */
    boolean remove(String id);


    /**
     * Remove the given element
     *
     * @param element to remove
     * @return true if it was removed correctly
     */
    boolean remove(T element);


    /**
     * Add the given element
     *
     * @param element to add
     */
    void add(T element);

    /**
     * Remove all the elements in this repository
     */
    void clear();
}
