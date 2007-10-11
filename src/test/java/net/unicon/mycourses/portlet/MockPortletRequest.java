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

import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;

public class MockPortletRequest implements PortletRequest {

    private MockPortletSession session;
    private MockPorletPreferences prefs;
    private Map attributes;
    
    public MockPortletRequest() {
        prefs = new MockPorletPreferences();
        attributes = new HashMap();
    }
    
    public Object getAttribute(String arg0) {
        return attributes.get(arg0);
    }

    public Enumeration getAttributeNames() {
        
        final Iterator i = attributes.keySet().iterator();
        
        return new Enumeration() {
            
            public boolean hasMoreElements() {
                return i.hasNext();
            }
            
            public Object nextElement() {
                return i.next();
            }
            
        };
        
    }

    public String getAuthType() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getContextPath() {
        // TODO Auto-generated method stub
        return null;
    }

    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getLocales() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getParameter(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public Map getParameterMap() {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getParameterNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public String[] getParameterValues(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public PortalContext getPortalContext() {
        // TODO Auto-generated method stub
        return null;
    }

    public PortletMode getPortletMode() {
        // TODO Auto-generated method stub
        return null;
    }

    public PortletSession getPortletSession() {
        return getPortletSession(true);
    }

    public PortletSession getPortletSession(boolean arg0) {
        if ( session == null && arg0) {
            session = new MockPortletSession();
        }
        return session;
    }

    public PortletPreferences getPreferences() {
        return prefs;
    }

    public Enumeration getProperties(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getProperty(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getPropertyNames() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRemoteUser() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getRequestedSessionId() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getResponseContentType() {
        // TODO Auto-generated method stub
        return null;
    }

    public Enumeration getResponseContentTypes() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getScheme() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getServerName() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getServerPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Principal getUserPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

    public WindowState getWindowState() {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isPortletModeAllowed(PortletMode arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isRequestedSessionIdValid() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isSecure() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isUserInRole(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isWindowStateAllowed(WindowState arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeAttribute(String arg0) {
        attributes.remove(arg0);

    }

    public void setAttribute(String arg0, Object arg1) {
        attributes.put(arg0,arg1);

    }

}
