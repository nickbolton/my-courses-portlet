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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletSession;

/**
 * 
 * 
 * @author dmccallum
 *
 */
public class MockPortletSession implements PortletSession {

    private Map portletAttributes;
    private Map applicationAttributes;
    
    public MockPortletSession() {
        portletAttributes = new HashMap();
        applicationAttributes = new HashMap();
    }
    
    public Object getAttribute(String arg0) {
        return getAttribute(arg0, PortletSession.PORTLET_SCOPE);
    }

    public Object getAttribute(String arg0, int arg1) {
        
        switch (arg1) {

        case PortletSession.PORTLET_SCOPE:
            return portletAttributes.get(arg0);
        case PortletSession.APPLICATION_SCOPE:
            return applicationAttributes.get(arg0);
        default:
            throw new IllegalArgumentException("unrecognized scope [" + arg1
                    + "]");

        }
    }

    public Enumeration getAttributeNames() {
        return getAttributeNames(PortletSession.PORTLET_SCOPE);
    }

    public Enumeration getAttributeNames(int arg0) {
        
        switch (arg0) {

        case PortletSession.PORTLET_SCOPE:
            final Iterator portletAttributeNameIterator = 
                portletAttributes.keySet().iterator();
            
            return new Enumeration() {
                
                public boolean hasMoreElements() {
                    return portletAttributeNameIterator.hasNext();
                }
                
                public Object nextElement() {
                    return portletAttributeNameIterator.next();
                }
                
            };
        case PortletSession.APPLICATION_SCOPE:
            final Iterator applicationAttributeNameIterator = 
                applicationAttributes.keySet().iterator();
            
            return new Enumeration() {
                
                public boolean hasMoreElements() {
                    return applicationAttributeNameIterator.hasNext();
                }
                
                public Object nextElement() {
                    return applicationAttributeNameIterator.next();
                }
                
            };
        default:
            throw new IllegalArgumentException("unrecognized scope [" + arg0
                    + "]");
        
        }

        
        
    }

    public long getCreationTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getId() {
        // TODO Auto-generated method stub
        return null;
    }

    public long getLastAccessedTime() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getMaxInactiveInterval() {
        // TODO Auto-generated method stub
        return 0;
    }

    public PortletContext getPortletContext() {
        // TODO Auto-generated method stub
        return null;
    }

    public void invalidate() {
        // TODO Auto-generated method stub
        
    }

    public boolean isNew() {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeAttribute(String arg0) {
        removeAttribute(arg0, PortletSession.PORTLET_SCOPE);
        
    }

    public void removeAttribute(String arg0, int arg1) {
        
        switch (arg1) {

        case PortletSession.PORTLET_SCOPE:
            portletAttributes.remove(arg0);
            break;
        case PortletSession.APPLICATION_SCOPE:
            applicationAttributes.remove(arg0);
            break;
        default:
            throw new IllegalArgumentException("unrecognized scope [" + arg1
                    + "]");
        
        }
        
    }

    public void setAttribute(String arg0, Object arg1) {
        setAttribute(arg0, arg1, PortletSession.PORTLET_SCOPE);     
    }

    public void setAttribute(String arg0, Object arg1, int arg2) {
        switch (arg2) {

        case PortletSession.PORTLET_SCOPE:
            portletAttributes.put(arg0, arg1);
            break;
        case PortletSession.APPLICATION_SCOPE:
            applicationAttributes.put(arg0, arg1);
            break;
        default:
            throw new IllegalArgumentException("unrecognized scope [" + arg2
                    + "]");
        
        }
        
    }

    public void setMaxInactiveInterval(int arg0) {
        // TODO Auto-generated method stub
        
    }

}
