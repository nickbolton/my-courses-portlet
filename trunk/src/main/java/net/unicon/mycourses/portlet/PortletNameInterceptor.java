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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.HandlerInterceptor;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.context.PortletApplicationObjectSupport;
import org.springframework.web.portlet.context.PortletContextAware;

public class PortletNameInterceptor extends PortletApplicationObjectSupport
        implements HandlerInterceptor, PortletContextAware {

    PortletContext portletContext;
    
	public void setPortletConfig(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

	public boolean preHandle(PortletRequest request, PortletResponse response, Object handler)
			throws Exception {
        logger.info("portletContextName : " +(portletContext == null ? "<no PortletContext!>" : portletContext.getPortletContextName()));
        return true;
	}

	public void postHandle(
			RenderRequest request, RenderResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}

	public void afterCompletion(
			PortletRequest request, PortletResponse response, Object handler, Exception ex)
			throws Exception {
	}

    public boolean preHandleAction(ActionRequest arg0, ActionResponse arg1, Object arg2) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    public void afterActionCompletion(ActionRequest arg0, ActionResponse arg1, Object arg2, Exception arg3) throws Exception {
        // TODO Auto-generated method stub
        
    }

    public boolean preHandleRender(RenderRequest arg0, RenderResponse arg1, Object arg2) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    public void postHandleRender(RenderRequest arg0, RenderResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
        // TODO Auto-generated method stub
        
    }

    public void afterRenderCompletion(RenderRequest arg0, RenderResponse arg1, Object arg2, Exception arg3) throws Exception {
        // TODO Auto-generated method stub
        
    }

}
