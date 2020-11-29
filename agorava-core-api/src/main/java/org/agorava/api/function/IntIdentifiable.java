/*
 * Copyright 2016-2020 Agorava
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

package org.agorava.api.function;

import java.io.Serializable;

/**
 * Provides <code>int</code> id to implementations.
 * 
 * <p>There is no requirement that a distinct result be returned each
 * time the supplier is invoked, unless implementing classes enforce it.
 * 
 * <p>This is a <a href="http://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html#package.description">functional interface</a>
 * whose functional method is {@link #getId()}.
 * 
 * @author Werner Keil
 * @version 1.0 $Date: 2020/11/29 $
 * @see Identifiable
 */
public interface IntIdentifiable extends Serializable {
    /**
     * @return an Id
     */
    public int getId();
}