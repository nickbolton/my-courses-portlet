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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.portlet.GenericPortlet;
import javax.portlet.PortalContext;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class HelloWorldPortlet extends GenericPortlet {

    public void doView(RenderRequest request, RenderResponse response)
            throws PortletException, IOException {

        
        /*
         * var undefined;
         * 
         * if ( helloWorldCounter == undefined ) {
         *   var helloWorldCounter = 0;
         * }
         * 
         * hellowWorldCounter++;
         * 
         * alert(helloWorldCounter);
         * 
         */
        String displayText = "A Hello World Portlet";
        response.setContentType(request.getResponseContentType());
        PrintWriter writer = response.getWriter();
        writer.write(displayText);
        
        writer.write("<script language=\"JavaScript\" type=\"text/javascript\">");
        writer.write("var undefined;");
        writer.write("if ( helloWorldCounter == undefined ) {");
        writer.write("var helloWorldCounter = 0;");
        writer.write("}");
        writer.write("helloWorldCounter++;");
        writer.write("alert('Hello World Counter: ' + helloWorldCounter);");
        
        writer.write("</script>");
        writer.write("");
        
        writer.write("Context path: " + request.getContextPath() + "<br>");
        writer.write("Scheme: " + request.getScheme() + "<br>");
        writer.write("<br>");
        writer.write("Properties: <br>");
        for ( Enumeration e = request.getPropertyNames(); e.hasMoreElements(); ) {
            String key = (String)e.nextElement();
            Object value = request.getProperty(key);
            writer.write(key + " : " + value + "<br>");
        }
        
        writer.write("<br>");
        writer.write("Attributes: <br>");
        for ( Enumeration e = request.getAttributeNames(); e.hasMoreElements(); ) {
            String key = (String)e.nextElement();
            Object value = request.getAttribute(key);
            writer.write(key + " : " + value + "<br>");
        }
        writer.write("<br>");
        writer.write("Parameters: <br>");
        for ( Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
            String key = (String)e.nextElement();
            String[] values = request.getParameterValues(key);
            writer.write(key + " : values: <br>");
            for ( int p = 0; p < values.length; p++ ) {
                writer.write(values[p] + "<br>");
            }
            writer.write("<br>");
        }
        writer.write("<br>");
        writer.write("Preferences: <br>");
        PortletPreferences prefs = request.getPreferences();
        for ( Enumeration e = prefs.getNames(); e.hasMoreElements(); ) {
            String key = (String)e.nextElement();
            String[] values = prefs.getValues(key, null);
            writer.write(key + " : values: <br>");
            for ( int p = 0; p < values.length; p++ ) {
                writer.write(values[p] + "<br>");
            }
            writer.write("<br>");
        }
        writer.write("<br>");
        PortletSession session = request.getPortletSession(false);
        if ( session == null ) {
            writer.write("No Session<br>");
        } else {
            writer.write("<br>");
            writer.write("Portlet Session Attributes: <br>");
            for ( Enumeration e = session.getAttributeNames(PortletSession.PORTLET_SCOPE); e.hasMoreElements(); ) {
                String key = (String)e.nextElement();
                Object value = session.getAttribute(key, PortletSession.PORTLET_SCOPE);
                writer.write(key + " : " + value + "<br>");
            }
            
            writer.write("Application Session Attributes: <br>");
            for ( Enumeration e = session.getAttributeNames(PortletSession.APPLICATION_SCOPE); e.hasMoreElements(); ) {
                String key = (String)e.nextElement();
                Object value = session.getAttribute(key, PortletSession.APPLICATION_SCOPE);
                writer.write(key + " : " + value + "<br>");
            }
        }
        writer.write("<br>");
        writer.write("PortletContext: <br>");
        session = request.getPortletSession(true);
        PortletContext context = session.getPortletContext();
        writer.write("portlet session ID: " + session.getId() + "<br>");
        writer.write("context name: " + context.getPortletContextName() + "<br>");
        writer.write("PortletContext Attributes: <br>");
        for ( Enumeration e = context.getAttributeNames(); e.hasMoreElements(); ) {
            String key = (String)e.nextElement();
            Object value = context.getAttribute(key);
            writer.write(key + " : " + value + "<br>");
        }
        writer.write("<br>");
        writer.write("PortalContext: <br>");
        PortalContext portalContext = request.getPortalContext();
        writer.write("portal info: " + portalContext.getPortalInfo() + "<br>");
        writer.write("PortalContext Properties: <br>");
        for ( Enumeration e = portalContext.getPropertyNames(); e.hasMoreElements(); ) {
            String key = (String)e.nextElement();
            Object value = portalContext.getProperty(key);
            writer.write(key + " : " + value + "<br>");
        }
        writer.flush();
        
        
        
    }

}
