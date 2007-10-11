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

import net.unicon.sakai.portalservices.api.LoginResult;
import net.unicon.sakai.portalservices.api.SakaiLoginService;

/**
 * 
 * @author dmccallum
 * @see ConfiguredSakaiSiteService
 */
public class ConfiguredSakaiLoginService implements SakaiLoginService {

    private SakaiLoginService delegateLoginService;
    
    /** wsdl location associated with the currently assigned SakaiLoginService */
    private String delegateWsdlLocation;

    /**
     * Assign a SakaiLoginService delegate
     * 
     * @param service a SakaiLoginService
     */
    public void setDelegateLoginService(SakaiLoginService service) {
        this.delegateLoginService = service;
    }
    
    /**
     * Return the SakaiLoginService delegate associated with this
     * object.
     * 
     * @return a SakaiLoginService
     */
    public SakaiLoginService getDelegateLoginService() {
        return delegateLoginService;
    }
    
    /**
     * Associate a WSDL location with this SakaiLoginService
     * 
     * @param location a WSDL location
     */
    public void setDelegateWsdlLocation(String location) {
        this.delegateWsdlLocation = location;
    }
    
    /**
     * Get the WSDL location associated with this SakaiLoginService
     *
     * @return a WSDL location
     */
    public String getDelegateWsdlLocation() {
        return this.delegateWsdlLocation;
    }


    /**
     * A passthrough the the underlying SakaiLoginService delegate.
     */
    public LoginResult loginPortalUser(String user, String secret) {
        return this.delegateLoginService.loginPortalUser(user, secret);
    }
    
}
