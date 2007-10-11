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

import java.util.HashMap;
import java.util.Map;

import net.unicon.sakai.portalservices.api.LoginResult;
import net.unicon.sakai.portalservices.api.SakaiLoginService;

public class MockSakaiLoginService implements SakaiLoginService, SessionToUserMapper {
    
    private String baseSessionID = "123456789abcdefghijklmnop";
    private String lastSessionIDIssued;
    /** Map from sessionIDs to User IDs. Keeps track of sessions issued. */
    private Map sessionIDsIssued = new HashMap();
    private int loginPortalUserCallCount;
    private String lastLoginPortalUserCall_UserIDArg;
    private String lastLoginPortalUserCall_PassArg;
    
    
    public LoginResult loginPortalUser(String user, String secret) {
        logLoginPortalUserCall(user,secret);
        LoginResult result = new LoginResult();
        lastSessionIDIssued = baseSessionID + loginPortalUserCallCount;
        result.setSessionId(lastSessionIDIssued);
        sessionIDsIssued.put(lastSessionIDIssued, user);
        return result;
    }
    
    public String getUserIDForSessionID(String sessionID) {
        return (String)sessionIDsIssued.get(sessionID);
    }
        
    public boolean calledLoginPortalUser() {
        return loginPortalUserCallCount > 0;
    }
    
    public String getLastSessionIDIssued() {
        return lastSessionIDIssued;
    }
    
    public int getLoginPortalUserCallCount() {
        return loginPortalUserCallCount;
    }

    public String getBaseSessionID() {
        return baseSessionID;
    }

    public void setBaseSessionID(String baseSessionID) {
        this.baseSessionID = baseSessionID;
    }

    public String getLastLoginPortalUserCall_PassArg() {
        return lastLoginPortalUserCall_PassArg;
    }

    public String getLastLoginPortalUserCall_UserIDArg() {
        return lastLoginPortalUserCall_UserIDArg;
    }
    
    protected void logLoginPortalUserCall(String user, String secret) {
        lastLoginPortalUserCall_UserIDArg = user;
        lastLoginPortalUserCall_PassArg = secret;
        loginPortalUserCallCount++;
    }

    
}
