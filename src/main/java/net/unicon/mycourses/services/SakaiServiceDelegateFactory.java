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

import net.unicon.sakai.portalservices.api.SakaiLoginService;
import net.unicon.sakai.portalservices.api.SakaiSiteService;

/**
 * Layer of indirection for manufacturing or otherwise
 * allocating SakaiXxxService instances to a client.
 * 
 * 
 * @author dmccallum
 *
 */
public interface SakaiServiceDelegateFactory {
    
    /**
     * Return an instance of SakaiSiteService
     * 
     * @param context may contain configuration to be applied
     *   to this Factory or the generated SakaiSiteService
     * @return
     */
    public SakaiSiteService aSiteService(SakaiServiceContext context)
    throws SakaiServiceConfigurationException;
    
    /**
     * Return an instance of SakaiLoginService
     * 
     * @param context may contain configuration to be applied
     *   to this Factory or the generated SakaiLoginService
     * @return
     */
    public SakaiLoginService aLoginService(SakaiServiceContext context)
    throws SakaiServiceConfigurationException;
    
    /**
     * Indicates if a given SakaiSiteService can saitify a requested
     * configuration represented by a SakaiServiceContext
     * 
     * @param siteService SakaiSiteService
     * @param context a SakaiServiceContext
     * @return true if the specified Context can be satisfied by
     *   the specified SakaiSiteService
     */
    public boolean isSiteServiceValidForContext(SakaiSiteService siteService, SakaiServiceContext context);
    
    /**
     * Indicates if a given SakaiLoginService can saitify a requested
     * configuration represented by a SakaiServiceContext
     * 
     * @param loginService a SakaiLoginService
     * @param context a SakaiServiceContext
     * @return true if the specified Context can be satisfied by
     *   the specified SakaiLoginService
     */
    public boolean isLoginServiceValidForContext(SakaiLoginService loginService, SakaiServiceContext context);
    
}
