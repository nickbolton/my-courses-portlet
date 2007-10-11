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

import net.unicon.sakai.portalservices.api.SakaiSiteService;
import net.unicon.sakai.portalservices.api.SakaiSiteServiceErrorCodes;
import net.unicon.sakai.portalservices.api.SiteQueryResult;

/**
 * Command object which invokes getActiveSiteEnrollment() on a SakaiSiteService
 * provided at construction.
 */
class GetActiveSiteEnrollmentInvoker implements SakaiSiteServiceInvoker {
    
    private SakaiSiteService delegate;
    private SakaiServiceContext context;
    private SiteQueryResult result;
    
    /**
     * Create a GetActiveSiteEnrollmentInvoker, caching the specified
     * SakaiSiteService and SakaiServiceContext.
     * 
     * @param delegate the SakaiSiteService which this invoker will target
     * @param context retrieves the current Sakai sessionID from this object
     */
    protected GetActiveSiteEnrollmentInvoker(SakaiSiteService delegate, SakaiServiceContext context) {
        
        this.context = context;
        this.delegate = delegate;
        
    }
    
    /**
     * Invokes SakaiSiteService.getActiveSiteEnrollment() and caches the result,
     * raising exceptions as a side-effect of recieving results with error codes.
     * 
     * @throws SakaiLoginException if returned result has an error code matching
     *   SakaiSiteServiceErrorCodes.INVALID_SESSION_ID
     * @throws SakaiQueryException if returned result has an error code not matching
     *   SakaiSiteServiceErrorCodes.INVALID_SESSION_ID
     */
    public void exec() throws SakaiLoginException, SakaiQueryException {
            
        result = delegate.getActiveSiteEnrollment(context.getSakaiSessionID());
        
        if ( result.hasErrorCode() ) {
            
            if ( SakaiSiteServiceErrorCodes.INVALID_SESSION_ID.equals(result.getErrorCode()) ) {
                throw new SakaiLoginException("Failed to acquire session for retrieving Sakai Site enrollment list [user: " + 
                        context.getUserID()  + "][config scope: " + context.getConfigScopeID() + "]",
                        SakaiSiteServiceErrorCodes.INVALID_SESSION_ID);
            } else {
                
                throw new SakaiQueryException("Failed to retrieve Sakai Site enrollment list [user: " + 
                        context.getUserID()  + "][config scope: " + context.getConfigScopeID() + "]",
                        result.getErrorCode());
                
            }
                
        }
          
    }
    
    public SiteQueryResult getResult() {
        return result;
    }
    
    public SakaiSiteService getSakaiSiteService() {
        return delegate;
    }
    
    public SakaiServiceContext getUserContext() {
        return context;
    }
    
    public void resetExec() {
        
        result = null;
        
    }
    
}