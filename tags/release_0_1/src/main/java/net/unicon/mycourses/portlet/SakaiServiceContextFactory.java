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

package net.unicon.mycourses.portlet;

import javax.portlet.PortletRequest;

import net.unicon.mycourses.services.SakaiServiceContext;

/**
 * Implementations generate concrete SakaiServiceContext
 * instances, initializing them with data derived from a
 * client-provided PortletRequest object. Implementations
 * may be session-aware. I.e. not all calls to 
 * {@link #contextFor(PortletRequest)} may result in a 
 * new SakaiServiceContext instance.
 * 
 * @author dmccallum
 *
 */
public interface SakaiServiceContextFactory {

    /**
     * Instantiate a SakaiServiceContext or otherwise
     * obtain a SakaiServiceContext reference and 
     * initialize it using objects reachable from the 
     * specified PortletRequest.
     * 
     * 
     * @param request
     * @return
     */
    public SakaiServiceContext contextFor(PortletRequest request);
    
}
