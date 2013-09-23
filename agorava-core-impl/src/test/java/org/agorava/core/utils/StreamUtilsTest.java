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

package org.agorava.core.utils;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class StreamUtilsTest {

    @Test
    public void shouldCorrectlyDecodeAStream() {
        String value = "expected";
        InputStream is = new ByteArrayInputStream(value.getBytes());
        String decoded = StreamUtils.getStreamContents(is);
        assertEquals("expected", decoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailForNullParameter() {
        InputStream is = null;
        StreamUtils.getStreamContents(is);
        fail("Must throw exception before getting here");
    }
}
