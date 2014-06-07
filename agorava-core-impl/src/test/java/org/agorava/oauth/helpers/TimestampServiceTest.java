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

package org.agorava.oauth.helpers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimestampServiceTest {

    private TimestampServiceImpl service;

    private TimestampServiceImpl.Timer timerStub;

    @Before
    public void setup() {
        service = new TimestampServiceImpl();
        timerStub = new TimerStub();
        service.setTimer(timerStub);
    }

    @Test
    public void shouldReturnTimestampInSeconds() {
        String expected = "1000";
        assertEquals(expected, service.getTimestampInSeconds());
    }

    @Test
    public void shouldReturnNonce() {
        String expected = "1042";
        assertEquals(expected, service.getNonce());
    }

    private static class TimerStub extends TimestampServiceImpl.Timer {

        @Override
        public Long getMilis() {
            return 1000000L;
        }

        @Override
        public Integer getRandomInteger() {
            return 42;
        }
    }
}
