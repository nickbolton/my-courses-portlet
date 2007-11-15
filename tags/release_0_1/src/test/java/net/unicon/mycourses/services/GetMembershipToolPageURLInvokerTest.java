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
import net.unicon.sakai.portalservices.api.StringQueryResult;

public class GetMembershipToolPageURLInvokerTest extends TestCase {

    private String userID = "sakaiuserID";
    private SakaiServiceContext context;
    
    protected void setUp() throws Exception {
        super.setUp();
        context = new SakaiServiceContextImpl();
        context.setUserID(userID);
        context.setSecret("sakaisecret");
        context.setSakaiSessionID("sakaisessionID");
        context.setConfigScopeID("configscopeID");
    }
    
    /**
     * Verifies a search for a User's Sites returns the 
     * Sites provided by an underlying delegate.
     */
    public void testExec_ReturnsUserMembershipToolURL() {
        
        final String url = 
            "http://localhost:8080/portal/site/~" + userID + "/page/page-id-123";       
        
        MockSakaiSiteService delegate = new MockSakaiSiteService() {
            
            public StringQueryResult getMembershipToolPageURL(String sessionID) {
                logGetMembershipToolPageURL(sessionID);
                
                StringQueryResult result = new StringQueryResult();
                result.setQueryResults(new String[] { url });
                return result;
            }
            
        };
        
        GetMembershipToolPageURLInvoker cmd = 
            new GetMembershipToolPageURLInvoker(delegate, context);
        
        cmd.exec();
        
        String[] foundURLs = cmd.getResult().getQueryResults();
        assertEquals(1, foundURLs.length);
        assertEquals(url, foundURLs[0]);
        
    }
    
    /**
     * Verifies a search for a page containing the membership tool 
     * delegates a call to an underlying service object, passing 
     * the correct parameters
     *
     */
    public void testExec_DelegatesToSakaiSiteService() {
        
        MockSakaiSiteService delegate = new MockSakaiSiteService();
        GetMembershipToolPageURLInvoker cmd = 
            new GetMembershipToolPageURLInvoker(delegate, context);
        
        cmd.exec();
        
        assertTrue(delegate.calledGetMembershipToolPageURL());
        assertEquals(delegate.getLastGetMembershipToolPageURLCall_SessionIDArg(), 
                context.getSakaiSessionID());
        
    }
    
    
    /**
     * Verifies that GetMembershipToolPageURLInvoker.exec() raises a 
     * SakaiQueryException if the underlying SakaiSiteService returns
     * a SiteQueryResult with an appropriate error code. Also ensures that 
     * error code is available in the SakaiQueryException.
     *
     */
    public void testExec_RaisesSakaiQueryExceptions() {
        
        // we chose a meaningless error code b/c we
        // expect exec() to throw SiteQueryException as
        // the default exception type
        final String errCode = "a-site-query-error-code";
        SakaiSiteService delegate = new MockSakaiSiteService() {
            public StringQueryResult getMembershipToolPageURL(String sessionID) {
                logGetMembershipToolPageURL(sessionID);               
                return new StringQueryResult(errCode);
            }
        };
        
        GetMembershipToolPageURLInvoker cmd = 
            new GetMembershipToolPageURLInvoker(delegate, context);
        
        try {
            cmd.exec();
            fail("expected a SakaiQueryException");
        } catch ( SakaiQueryException e ) {
            assertEquals(errCode,e.getErrorCode());
        }
        
    }
    
    /**
     * Verifies that GetMembershipToolPageURLInvoker.exec() raises a 
     * SakaiLoginException if the underlying SakaiSiteService returns
     * a SiteQueryResult with an appropriate error code. Also ensures that 
     * error code is available in the SakaiQueryException.
     *
     */
    public void testExec_RaisesSakaiLoginExceptions() {
        
        // we chose a meaningless error code b/c we
        // expect exec() to throw SiteQueryException as
        // the default exception type
        final String errCode = SakaiSiteServiceErrorCodes.INVALID_SESSION_ID;
        SakaiSiteService delegate = new MockSakaiSiteService() {
            public StringQueryResult getMembershipToolPageURL(String sessionID) {
                logGetMembershipToolPageURL(sessionID);               
                return new StringQueryResult(errCode);
            }
        };
        
        GetMembershipToolPageURLInvoker cmd = 
            new GetMembershipToolPageURLInvoker(delegate, context);
        
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
