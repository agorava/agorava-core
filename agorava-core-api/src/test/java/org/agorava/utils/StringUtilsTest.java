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

package org.agorava.utils;

import org.agorava.api.service.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Antoine Sabot-Durand
 */
public class StringUtilsTest {

    static final List<String> myList = new ArrayList<String>() {{
        add("value1");
        add("value2");
        add("value3");
    }};


    @Test
    public void joinShouldWork() throws Exception {
        Assert.assertEquals(StringUtils.join(myList, ','), "value1,value2,value3");
    }

    @Test
    public void joinWithNullListShouldReturnEmptyString() throws Exception {
        Assert.assertEquals(StringUtils.join(null, ','), "");
    }

    @Test
    public void joinWithEmptyListShouldReturnEmptyString() throws Exception {
        Assert.assertEquals(StringUtils.join(null, ','), "");
    }
}
