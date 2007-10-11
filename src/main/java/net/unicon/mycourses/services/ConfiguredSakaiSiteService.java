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
import net.unicon.sakai.portalservices.api.SiteQueryResult;
import net.unicon.sakai.portalservices.api.StringQueryResult;
import net.unicon.sakai.portalservices.api.TermQueryResult;

/**
 * Associates a SakaiSiteService instance with a WSDL location. 
 * 
 * @author dmccallum
 * @see ConfiguredSakaiLoginService
 *
 */
class ConfiguredSakaiSiteService implements
        SakaiSiteService {
    
    private SakaiSiteService delegateSiteService;
    
    /** wsdl location associated with the currently assigned SakaiSiteService */
    private String delegateWsdlLocation;

    /**
     * Assign a SakaiSiteService delegate
     * 
     * @param service a SakaiSiteService
     */
    public void setDelegateSiteService(SakaiSiteService service) {
        this.delegateSiteService = service;
    }
    
    /**
     * Return the SakaiSiteService delegate associated with this
     * object.
     * 
     * @return a SakaiSiteService
     */
    public SakaiSiteService getDelegateSiteService() {
        return delegateSiteService;
    }
    
    /**
     * Associate a WSDL location with this SakaiSiteService
     * 
     * @param location a WSDL location
     */
    public void setDelegateWsdlLocation(String location) {
        this.delegateWsdlLocation = location;
    }
    
    /**
     * Get the WSDL location associated with this SakaiSiteService
     *
     * @return a WSDL location
     */
    public String getDelegateWsdlLocation() {
        return this.delegateWsdlLocation;
    }

    /**
     * A passthrough the the underlying SakaiSiteService delegate.
     */
    public SiteQueryResult getActiveSiteEnrollment(String sessionID) {
        return this.delegateSiteService.getActiveSiteEnrollment(sessionID);
    }

    /**
     * A passthrough the the underlying SakaiSiteService delegate.
     */
    public SiteQueryResult getActiveSiteEnrollmentByTerm(String sessionID, String term) {
        return this.delegateSiteService.getActiveSiteEnrollmentByTerm(sessionID, term);
    }
    
    /**
     * A passthrough the the underlying SakaiSiteService delegate.
     */
    public SiteQueryResult getActiveSiteEnrollmentByType(String sessionID, String type) {
        return this.delegateSiteService.getActiveSiteEnrollmentByType(sessionID, type);
    }

    /**
     * A passthrough the the underlying SakaiSiteService delegate.
     */
    public TermQueryResult getTerms(String sessionID) {
        return this.delegateSiteService.getTerms(sessionID);
    }

    /**
     * A passthrough the the underlying SakaiSiteService delegate.
     */
    public StringQueryResult getMembershipToolPageURL(String sessionID) {
        return this.delegateSiteService.getMembershipToolPageURL(sessionID);
    }

}
