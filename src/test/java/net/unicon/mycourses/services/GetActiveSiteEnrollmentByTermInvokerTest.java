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

import junit.framework.TestCase;
import net.unicon.sakai.portalservices.api.SakaiSiteService;
import net.unicon.sakai.portalservices.api.SakaiSiteServiceErrorCodes;
import net.unicon.sakai.portalservices.api.Site;
import net.unicon.sakai.portalservices.api.SiteQueryResult;
import net.unicon.sakai.portalservices.api.Term;

public class GetActiveSiteEnrollmentByTermInvokerTest extends TestCase {

private SakaiServiceContext context;
    
    protected void setUp() throws Exception {
        super.setUp();
        context = new SakaiServiceContextImpl();
        context.setUserID("sakaiuserID");
        context.setSecret("sakaisecret");
        context.setSakaiSessionID("sakaisessionID");
        context.setConfigScopeID("configscopeID");
    }
    
    /**
     * Verifies a search for a User's Sites returns the 
     * Sites provided by an underlying delegate.
     * 
     * TODO abstract commonality with method of same name in 
     * GetActiveSiteEnrollmentInvokerTest
     */
    public void testExec_ReturnsUsersSites() {
        
        Term term = new Term("termA", "termA", false);
        
        final Site[] sites = new Site[] {
                new Site("siteA", "/url/to/siteA", "course", term, "instructorA"),
                new Site("siteB", "/url/to/siteB", "project", term, "instructorB"),
                new Site("siteC", "/url/to/siteC", "home", term, "instructorC")
        };
        
        MockSakaiSiteService delegate = new MockSakaiSiteService() {
            
            public SiteQueryResult getActiveSiteEnrollmentByTerm(String sessionID, String term) {
                logGetActiveSiteEnrollmentByTermCall(sessionID, term);
                
                SiteQueryResult result = new SiteQueryResult();
                result.setQueryResults(sites);
                return result;
            }
            
        };
        
        GetActiveSiteEnrollmentByTermInvoker cmd = 
            new GetActiveSiteEnrollmentByTermInvoker(delegate, context, term.getId());
        
        cmd.exec();
        
        Site[] foundSites = cmd.getResult().getQueryResults();
        assertSame(sites, foundSites);
        
    }
    
    /**
     * Verifies a search for a User's Sites delegates a call to 
     * an underlying service object, passing the correct parameters
     *
     * TODO abstract commonality with method of same name in 
     * GetActiveSiteEnrollmentInvokerTest
     */
    public void testExec_DelegatesToSakaiSiteService() {
        
        String term = "termA";
        MockSakaiSiteService delegate = new MockSakaiSiteService();
        GetActiveSiteEnrollmentByTermInvoker cmd = 
            new GetActiveSiteEnrollmentByTermInvoker(delegate, context, term);
        
        cmd.exec();
        
        assertTrue(delegate.calledGetActiveSiteEnrollmentByTerm());
        assertEquals(delegate.getLastGetActiveSiteEnrollmentByTermCall_SessionIDArg(), 
                context.getSakaiSessionID());
        assertEquals(delegate.getLastGetActiveSiteEnrollmentByTermCall_TermArg(), 
                term);
        
    }
    
    
    /**
     * Verifies that GetActiveSiteEnrollmentByTermInvoker.exec() raises a 
     * SakaiQueryException if the underlying SakaiSiteService returns
     * a SiteQueryResult with an appropriate error code. Also ensures that 
     * error code is available in the SakaiQueryException.
     *
     * TODO abstract commonality with method of same name in 
     * GetActiveSiteEnrollmentInvokerTest
     */
    public void testExec_RaisesSakaiQueryExceptions() {
        
        String term = "termA";
        
        // we chose a meaningless error code b/c we
        // expect exec() to throw SiteQueryException as
        // the default exception type
        final String errCode = "a-site-query-error-code";
        SakaiSiteService delegate = new MockSakaiSiteService() {
            public SiteQueryResult getActiveSiteEnrollmentByTerm(String sessionID, String term) {
                return new SiteQueryResult(errCode);
            }
        };
        
        GetActiveSiteEnrollmentByTermInvoker cmd = 
            new GetActiveSiteEnrollmentByTermInvoker(delegate, context, term);
        
        try {
            cmd.exec();
            fail("expected a SakaiQueryException");
        } catch ( SakaiQueryException e ) {
            assertEquals(errCode,e.getErrorCode());
        }
        
    }
    
    /**
     * Verifies that GetActiveSiteEnrollmentByTermInvoker.exec() raises a 
     * SakaiLoginException if the underlying SakaiSiteService returns
     * a SiteQueryResult with an appropriate error code. Also ensures that 
     * error code is available in the SakaiQueryException.
     *
     * TODO abstract commonality with method of same name in 
     * GetActiveSiteEnrollmentInvokerTest
     */
    public void testExec_RaisesSakaiLoginExceptions() {
        
        String term = "termA";
        
        // we chose a meaningless error code b/c we
        // expect exec() to throw SiteQueryException as
        // the default exception type
        final String errCode = SakaiSiteServiceErrorCodes.INVALID_SESSION_ID;
        SakaiSiteService delegate = new MockSakaiSiteService() {
            public SiteQueryResult getActiveSiteEnrollmentByTerm(String sessionID, String term) {
                return new SiteQueryResult(errCode);
            }
        };
        
        GetActiveSiteEnrollmentByTermInvoker cmd = 
            new GetActiveSiteEnrollmentByTermInvoker(delegate, context, term);
        
        try {
            cmd.exec();
            fail("expected a SakaiLoginException");
        } catch ( SakaiLoginException e ) {
            assertEquals(errCode,e.getErrorCode());
        }
        
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
