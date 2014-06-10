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

package org.agorava.picketlink;

import org.agorava.AgoravaContext;
import org.agorava.spi.UserProfile;
import org.picketlink.idm.model.basic.User;

/**
 * @author Antoine Sabot-Durand
 */
public class AgoravaUser extends User {

    private final UserProfile profile;

    public AgoravaUser(UserProfile profile) {
        this.profile = profile;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public String getProviderName() {
        return AgoravaContext.getServiceFromClass(profile.getQualifier());
    }


    @Override
    public String getLoginName() {
        return profile.getLoginName();
    }

    @Override
    public String getId() {
        return profile.getId();
    }

    @Override
    public String getFirstName() {
        return profile.getFirstName();
    }

    @Override
    public String getLastName() {
        return profile.getLastName();
    }

    @Override
    public String getEmail() {
        return profile.getEmail();
    }

    @Override
    public int hashCode() {
        return profile.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return profile.equals(obj);
    }

    @Override
    public void setFirstName(String firstName) {
        throw new UnsupportedOperationException("Changing social profile first name is not allowed");
    }

    @Override
    public void setLastName(String lastName) {
        throw new UnsupportedOperationException("Changing social profile last name is not allowed");
    }

    @Override
    public void setEmail(String email) {
        throw new UnsupportedOperationException("Changing social profile email is not allowed");
    }
}
