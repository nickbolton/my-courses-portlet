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

public class MockSakaiServiceContextFactorySessionAwareImpl 
extends SakaiServiceContextFactorySessionAwareImpl {

    private int configureContextCallCount;
    private SakaiServiceContext lastConfigureContextCall_ContextArg;
    private PortletRequest lastConfigureContextCall_RequestArg;
    
    protected void configureContext(SakaiServiceContext context, PortletRequest request) {
        logConfigureContextCall(context,request);
        super.configureContext(context,request);
    }
    
    protected void logConfigureContextCall(SakaiServiceContext context, PortletRequest request) {
        configureContextCallCount++;
        lastConfigureContextCall_ContextArg = context;
        lastConfigureContextCall_RequestArg = request;
    }

    public int getConfigureContextCallCount() {
        return configureContextCallCount;
    }


    public SakaiServiceContext getLastConfigureContextCall_ContextArg() {
        return lastConfigureContextCall_ContextArg;
    }


    public PortletRequest getLastConfigureContextCall_RequestArg() {
        return lastConfigureContextCall_RequestArg;
    }

    
}
