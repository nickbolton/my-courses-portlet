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

import net.unicon.sakai.portalservices.api.SakaiSiteServiceErrorCodes;
import net.unicon.sakai.portalservices.api.SiteQueryResult;

public class MockSakaiSiteServiceInvalidSessionIdImpl extends
        MockSakaiSiteService {
    
    private int getActiveSiteEnrollmentAttempts_ReturnErrorQuota = 0;
    private int getActiveSiteEnrollmentAttempts_ReturnErrorCount = 0;
    
    public SiteQueryResult getActiveSiteEnrollment(String sessionID) {
        if ( getActiveSiteEnrollmentAttempts_ReturnErrorCount 
                < getActiveSiteEnrollmentAttempts_ReturnErrorQuota ) {
            logGetActiveSiteEnrollmentCall(sessionID);
            getActiveSiteEnrollmentAttempts_ReturnErrorCount++;
            return new SiteQueryResult(SakaiSiteServiceErrorCodes.INVALID_SESSION_ID);
        }
        return super.getActiveSiteEnrollment(sessionID);
    }

    public int getGetActiveSiteEnrollmentAttempts_ReturnErrorCount() {
        return getActiveSiteEnrollmentAttempts_ReturnErrorCount;
    }

    public void setGetActiveSiteEnrollmentAttempts_ReturnErrorCount(
            int getActiveSiteEnrollmentAttempts_ReturnErrorCount) {
        this.getActiveSiteEnrollmentAttempts_ReturnErrorCount = getActiveSiteEnrollmentAttempts_ReturnErrorCount;
    }

    public int getGetActiveSiteEnrollmentAttempts_ReturnErrorQuota() {
        return getActiveSiteEnrollmentAttempts_ReturnErrorQuota;
    }

    public void setGetActiveSiteEnrollmentAttempts_ReturnErrorQuota(
            int getActiveSiteEnrollmentAttempts_ReturnErrorQuota) {
        this.getActiveSiteEnrollmentAttempts_ReturnErrorQuota = getActiveSiteEnrollmentAttempts_ReturnErrorQuota;
    }

}
