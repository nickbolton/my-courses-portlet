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

import java.util.ArrayList;
import java.util.Collection;

import net.unicon.sakai.portalservices.api.Site;
import net.unicon.sakai.portalservices.api.Term;

public class MockSakaiService implements SakaiService {

    private Collection sites;
    private Collection terms;
    
    public Collection getActiveSiteEnrollment(SakaiServiceContext userContext)
            throws SakaiServiceException {
        sites = new ArrayList();
        sites.add(new Site("siteA", "/url/to/siteA", "course", new Term("termA", "termA", false), "instructorA"));
        sites.add(new Site("siteB", "/url/to/siteB", "project", new Term("termB", "termB", false), "instructorB"));
        sites.add(new Site("siteC", "/url/to/siteC", "home", new Term("termB", "termB", false), "instructorC"));
        return sites;
    }

    public Collection getActiveSiteEnrollmentByTerm(SakaiServiceContext userContext, String termCode) throws SakaiServiceException {
        sites = new ArrayList();
        sites.add(new Site("siteA", "/url/to/siteA", "course", new Term(termCode, termCode, false), "instructorA"));
        sites.add(new Site("siteB", "/url/to/siteB", "project", new Term(termCode, termCode, false), "instructorB"));
        sites.add(new Site("siteC", "/url/to/siteC", "home", new Term(termCode, termCode, false), "instructorC"));
        return sites;
    }
    
    public Collection getActiveSiteEnrollmentByType(SakaiServiceContext userContext, String type) throws SakaiServiceException {
        sites = new ArrayList();
        sites.add(new Site("siteB", "/url/to/siteD", type, new Term("termD","termD",false), "instructorD"));
        sites.add(new Site("siteB", "/url/to/siteE", type, new Term("termE","termE",false), "instructorE"));
        return sites;
    }

    public Collection getTerms(SakaiServiceContext userContext) throws SakaiServiceException {
        terms = new ArrayList();
        terms.add(new Term("termA", "termA", true));
        terms.add(new Term("termB", "termB", false));
        terms.add(new Term("termC", "termC", false));
        return terms;
    }

    public String getMembershipToolPageURL(SakaiServiceContext userContext) throws SakaiServiceException {
        // TODO Auto-generated method stub
        return null;
    }

}
