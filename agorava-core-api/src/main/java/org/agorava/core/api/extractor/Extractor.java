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

package org.agorava.core.api.extractor;

/**
 * Generic Extractor, root of all Extractors hierarchy
 *
 * @param <T> type of data extracted
 * @param <F> type of data from which we extract
 * @author Antoine Sabot-Durand
 */
public interface Extractor<T, F> {

    /**
     * Extract data from a given object
     *
     * @param from data from which we should extract
     * @return extracted data
     */
    T extract(F from);

}
