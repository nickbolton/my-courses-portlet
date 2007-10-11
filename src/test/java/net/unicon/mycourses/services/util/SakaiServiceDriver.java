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

package net.unicon.mycourses.services.util;

import java.util.Collection;

import net.unicon.mycourses.services.SakaiService;
import net.unicon.mycourses.services.SakaiServiceContext;
import net.unicon.mycourses.services.SakaiServiceContextImpl;
import net.unicon.sakai.portalservices.api.Term;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Utility for configuring up and exercising a SakaiService. Uses
 * a Spring ApplicationContext to perform the configuration.
 * 
 * TODO: turn this into a functional test case?
 * 
 * @author dmccallum
 *
 */
public class SakaiServiceDriver {

    private SakaiService service;
    private String secret;
    private String userName;
    
    public void exec() {
        //for ( int i = 0; i < 2; i++) {
        SakaiServiceContext context = new SakaiServiceContextImpl();
        context.setSecret(secret);
        context.setUserID(userName);
        context.setConfigScopeID("some-random-value");
        System.out.println("Getting active enrollment: ");
        System.out.println(service.getActiveSiteEnrollment(context));
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Getting terms: ");
        Collection terms = service.getTerms(context);
        System.out.println(terms);
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Getting active enrollment by term: ");
        //String termID = ((Term)terms.iterator().next()).getName();
        System.out.println(service.getActiveSiteEnrollmentByTerm(context, "SUMMER 2006"));
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("Getting active enrollment by term: ");
        //String termID = ((Term)terms.iterator().next()).getName();
        System.out.println(service.getActiveSiteEnrollmentByType(context, "project"));
        //}
    }
    
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public SakaiService getService() {
        return service;
    }

    public void setService(SakaiService service) {
        this.service = service;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static void main(String[] args) {
        
        
        ApplicationContext context = new ClassPathXmlApplicationContext("sakaiServiceDriverApplicationContext.xml");
        SakaiServiceDriver driver = 
            (SakaiServiceDriver)context.getBean("net.unicon.mycourses.services.util.SakaiServiceDriver");
        driver.exec();
        
        
    }
    
}
