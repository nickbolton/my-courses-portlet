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

import java.util.Map;

import net.unicon.mycourses.services.MockSakaiService;
import net.unicon.mycourses.services.SakaiService;

import org.springframework.mock.web.portlet.MockRenderRequest;
import org.springframework.mock.web.portlet.MockRenderResponse;
import org.springframework.web.portlet.ModelAndView;

public class TestMyCoursesController extends TestCase {

    public void testMyCoursesController() throws Exception {

        MyCoursesController controller = (MyCoursesController)myCoursesPortletContext.getBean("myCoursesController");

    	MockRenderRequest request = new MockRenderRequest();
    	MockRenderResponse response = new MockRenderResponse();

        SakaiService sakaiService = new MockSakaiService();      
        controller.setSakaiSiteService(sakaiService);
        
        SakaiServiceContextFactory contextFactory = new MockSakaiServiceContextFactory();
        controller.setSakaiServiceContextFactory(contextFactory);
           	
		ModelAndView mav = controller.handleRenderRequest(request, response);
		assertNotNull(mav);
		
		logger.info("view: " + mav.getViewName());
		
		Map model = mav.getModel();

		assertTrue(model.containsKey("model"));
        
        //TODO: check for sites, terms, default term, etc...

    }
}
