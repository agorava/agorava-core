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

import org.agorava.api.service.TimestampService;

import java.util.Random;

/**
 * Implementation of {@link org.agorava.api.service.TimestampService} using plain java classes.
 *
 * @author Pablo Fernandez
 * @author Antoine Sabot-Durand
 */
public class TimestampServiceImpl implements TimestampService {
    private Timer timer;

    /**
     * Default constructor.
     */
    public TimestampServiceImpl() {
        timer = new Timer();
    }

    /**
     * {@inheritDoc}
     */
    public String getNonce() {
        Long ts = getTs();
        return String.valueOf(ts + timer.getRandomInteger());
    }

    /**
     * {@inheritDoc}
     */
    public String getTimestampInSeconds() {
        return String.valueOf(getTs());
    }

    private Long getTs() {
        return timer.getMilis() / 1000;
    }

    void setTimer(Timer timer) {
        this.timer = timer;
    }

    /**
     * Inner class that uses {@link System} for generating the timestamps.
     *
     * @author Pablo Fernandez
     */
    static class Timer {
        Long getMilis() {
            return System.currentTimeMillis();
        }

        Integer getRandomInteger() {
            return new Random().nextInt();
        }
    }

}
