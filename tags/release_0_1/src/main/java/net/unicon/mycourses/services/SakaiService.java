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

import java.util.Collection;

import net.unicon.sakai.portalservices.api.SiteQueryResult;
import net.unicon.sakai.portalservices.api.TermQueryResult;

public interface SakaiService {

    /**
     * Retrieves Collection of Sites in which a Sakai user is actively
     * enrolled. Attempts to create a Sakai session for the user 
     * associated with the specified SakaiServiceContext if that
     * Context has not already been assigned a Sakai session ID.
     * 
     * @param userContext used to establish or reconnect to a
     *   Sakai session
     * @return a Collection of Sites
     * @throws SakaiServiceException if the service invocation
     *   fails
     * 
     */
    public Collection getActiveSiteEnrollment(SakaiServiceContext userContext) 
    throws SakaiServiceException;
    
    /**
     * Retrieves Collection of Sites in which a Sakai user is actively
     * enrolled in the given term. Attempts to create a Sakai session for the user 
     * associated with the specified SakaiServiceContext if that
     * Context has not already been assigned a Sakai session ID.
     * 
     * @param userContext used to establish or reconnect to a
     *   Sakai session.
     * @param termID a Term identifier. Term enum can be
     *   retrieved from {@link #getTerms(SakaiServiceContext)}
     * @return a Collection of Sites
     * @throws SakaiServiceException if the service invocation
     *   fails
     * 
     * 
     */
    public Collection getActiveSiteEnrollmentByTerm(SakaiServiceContext userContext, String termCode)
    throws SakaiServiceException;
    
    /**
     * Retrieves Collection of Sites of a given type in which a Sakai user 
     * is actively enrolled of the g. Attempts to create a Sakai session for 
     * the user associated with the specified SakaiServiceContext if that
     * Context has not already been assigned a Sakai session ID.
     * 
     * @param userContext used to establish or reconnect to a
     *   Sakai session.
     * @param siteType a Site type identifier.
     * @return a Collection of Sites
     * @throws SakaiServiceException if the service invocation
     *   fails
     * 
     * 
     */
    public Collection getActiveSiteEnrollmentByType(SakaiServiceContext userContext, String siteType)
    throws SakaiServiceException;

    /**
     * Retieves the enumeration of Terms known to the Sakai host.
     * Attempts to create a Sakai session for the user 
     * associated with the specified SakaiServiceContext if that
     * Context has not already been assigned a Sakai session ID.
     * 
     * @param userContext used to establish or reconnect to a
     *   Sakai session
     * @return a Collection of Terms.
     * @throws SakaiServiceException if the service invocation
     *   fails
     * 
     */
    public Collection getTerms(SakaiServiceContext userContext)
    throws SakaiServiceException;
    
    /**
     * Retrieves a String representation of a URL at which
     * the Sakai "membership tool" can be viewed for the current
     * user. This is typically implemented to search for a page
     * on the current user's MyWorkspace site. The URL references
     * that page, not the tool placement.
     * 
     * <p>Returns null if a relevant URL cannot be located. The
     * returned URL will typically be absolute and include the
     * target host name.
     * 
     * @param userContext used to establish or reconnect to a
     *   Sakai session
     * @return a String representation of a page URL
     * @throws SakaiServiceException if the service invocation
     *   fails
     * 
     */
    public String getMembershipToolPageURL(SakaiServiceContext userContext)
    throws SakaiServiceException;
    

    

}