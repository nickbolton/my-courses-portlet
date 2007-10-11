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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import net.unicon.mycourses.services.SakaiService;
import net.unicon.mycourses.services.SakaiServiceContext;
import net.unicon.mycourses.services.SakaiServiceException;
import net.unicon.sakai.portalservices.api.Term;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

public class MyCoursesController extends AbstractController implements InitializingBean {

    public static final String PROJECT_SITE_TYPE = "project";
    public static final String COURSE_SITE_TYPE = "course";
    public static final String BASE_URL = "net.unicon.mycourses.preferenceskeys.SAKAI_BASE_URL";
    public static final String DEFAULT_SYSTEM_ERROR = "default-system-error"; 
    public static final String LIST_VIEW = "mcList";
    public static final String HELP_VIEW = "mcHelp";
    public static final String DEFAULT_TERM_SELECTION = "default_term";
    
    public static int portletUID = 0;
      
    private SakaiService sakaiSiteService;
    private SakaiServiceContextFactory sakaiServiceContextFactory;
    
    public void afterPropertiesSet() throws Exception {
        if (this.sakaiSiteService == null)
            throw new IllegalArgumentException("A sakaiService is required");
    }
    
	public ModelAndView handleRenderRequestInternal(RenderRequest request, RenderResponse response) throws Exception {
        
        synchronized (this) {
            if (portletUID == Integer.MAX_VALUE) {
                portletUID = 0;
            } else {
                portletUID++;
            }
            //System.out.println("!!! portletUID: " + portletUID);
        }
        
        
        
        Map model = new HashMap();
        String view = LIST_VIEW;
        PortletMode mode = request.getPortletMode();
        //WindowState state = request.getWindowState();
        
        String baseURL = null;
        String worksiteURL = "/login";
        PortletPreferences prefs = request.getPreferences();
        baseURL = prefs.getValue(BASE_URL, null);
        
        PortletURL refreshURL = response.createRenderURL();
        refreshURL.setParameter("randomValue", "" + System.currentTimeMillis() + portletUID);
        model.put("refreshURL", refreshURL);
        
        if (mode.toString().equals("view")) {

            Collection sites = new ArrayList();
            Collection terms = null;
            String membershipURL = null;
            String errorMessage = null;
            
            String selectedTerm = request.getParameter("selectedTerm");
            
            if (DEFAULT_TERM_SELECTION.equals(selectedTerm)) {
            	
            	// Set to null so the default term would be used
            	selectedTerm = null;
            }
                   
            try {
               
                SakaiServiceContext context = sakaiServiceContextFactory.contextFor(request);
                terms = sakaiSiteService.getTerms(context);
                
                if (selectedTerm == null) {
                    Iterator termsIt = terms.iterator();
                    while (termsIt.hasNext()) {
                        Term term = (Term)termsIt.next();
                        if (term.getIsDefault()) {
                            selectedTerm = term.getId();
                            break;
                        }
                    }
                }
                
                if (selectedTerm == null) {
                    sites = sakaiSiteService.getActiveSiteEnrollment(context);
                } else {
                    sites = sakaiSiteService.getActiveSiteEnrollmentByTerm(context, selectedTerm);
                    sites.addAll(sakaiSiteService.getActiveSiteEnrollmentByType(context, PROJECT_SITE_TYPE));
                }
                
                membershipURL = sakaiSiteService.getMembershipToolPageURL(context);
                
            } catch (SakaiServiceException sse) {
                logger.error("Sakai Service Exception", sse);
                sse.printStackTrace();
                errorMessage = sse.getErrorCode();
            } catch (RuntimeException rte) {
                logger.error("System Error", rte);
                rte.printStackTrace();
                errorMessage = DEFAULT_SYSTEM_ERROR;
            }
            
            if (errorMessage != null) {
                model.put("errorMessage", errorMessage);
            }
            if (membershipURL == null) {
                membershipURL = worksiteURL;
            }
            
            model.put("worksiteURL", worksiteURL);
            model.put("portletUID", Integer.valueOf(portletUID));
            model.put("membershipURL", membershipURL);
            model.put("baseURL", baseURL);
            model.put("selectedTerm", selectedTerm);
            model.put("terms", terms);
            model.put("sites", sites);
            model.put("siteCourse", COURSE_SITE_TYPE);
            model.put("siteProject", PROJECT_SITE_TYPE);
            
            view = LIST_VIEW;
        } else if (mode.toString().equals("help")) {
            view = HELP_VIEW;
        }
        
        return new ModelAndView(view, "model", model);
	}

    public void setSakaiSiteService(SakaiService sakaiSiteService) {
        this.sakaiSiteService = sakaiSiteService;
    }

    public void setSakaiServiceContextFactory(SakaiServiceContextFactory sakaiServiceContextFactory) {
        this.sakaiServiceContextFactory = sakaiServiceContextFactory;
    }
}
