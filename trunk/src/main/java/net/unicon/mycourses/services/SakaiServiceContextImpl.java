/*
 * Copyright (C) 2007 Unicon, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this distribution.  It is also available here:
 * http://www.fsf.org/licensing/licenses/gpl.html
 *
 * As a special exception to the terms and conditions of version
 * 2 of the GPL, you may redistribute this Program in connection
 * with Free/Libre and Open Source Software ("FLOSS") applications
 * as described in the GPL FLOSS exception.  You should have received
 * a copy of the text describing the FLOSS exception along with this
 * distribution.
 */

package net.unicon.mycourses.services;

import java.util.Map;
import java.util.HashMap;

public class SakaiServiceContextImpl implements SakaiServiceContext {

    private Map options;
    private String sessionID;
    private String secret;
    private String userID;
    private String configScopeID;
    
    public SakaiServiceContextImpl() {
        options = new HashMap();
    }
    
    public Map getOptions() {
        return options;
    }

    public void setOptions(Map options) {
        this.options = options;
    }

    public void setOption(String name, String value) {
        if ( options == null ) {
            options = new HashMap();
        }
        options.put(name, value);
    }
    
    public String getOption(String name) {
        if ( options == null ) {
            return null;
        }
        return (String)options.get(name);
    }
    
    public boolean hasSakaiSession() {
        return this.sessionID != null;
    }

    public String getSakaiSessionID() {
        return sessionID;
    }

    public void setSakaiSessionID(String sakaiSessionID) {
        this.sessionID = sakaiSessionID;  
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getConfigScopeID() {
        return this.configScopeID;
    }

    public void setConfigScopeID(String configScopeID) {
        this.configScopeID = configScopeID;
    }

    
    
}
