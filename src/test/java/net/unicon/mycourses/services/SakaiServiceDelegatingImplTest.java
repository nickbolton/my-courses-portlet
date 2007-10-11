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
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import net.unicon.sakai.portalservices.api.LoginResult;
import net.unicon.sakai.portalservices.api.SakaiLoginService;
import net.unicon.sakai.portalservices.api.SakaiSiteService;
import net.unicon.sakai.portalservices.api.SakaiSiteServiceErrorCodes;
import net.unicon.sakai.portalservices.api.Site;
import net.unicon.sakai.portalservices.api.SiteQueryResult;
import net.unicon.sakai.portalservices.api.Term;

public class SakaiServiceDelegatingImplTest extends TestCase {

    private MockSakaiServiceDelegatingImpl service;
    private MockSakaiLoginService loginDelegate1;
    private MockSakaiSiteService sitesDelegate1;
    private MockSakaiLoginService loginDelegate2;
    private MockSakaiSiteService sitesDelegate2;
    private SakaiServiceContext userContext1;
    private SakaiServiceContext userContext2;
    private MockSakaiServiceDelegateFactory delegateFactory;
    private Collection sites1;
    private Collection sites2;
    private Map siteMap1;
    private Map siteMap2;
    private Map siteDelegatesMap;
    private Map loginDelegatesMap;
    private String pass = "sakaisecret";
    private String userID1 = "sakaiuserID1";
    private String userID2 = "sakaiuserID2";
    private String configScopeID1 = "configScopeID1";
    private String configScopeID2 = "configScopeID2";
    private Collection terms;
    private String membershipToolPageURL1 = 
        "http://localhost:8080/portal/site/~" + userID1 + "/page/page-id-1";
    private String membershipToolPageURL2 = 
        "http://localhost:8080/portal/site/~" + userID2 + "/page/page-id-2";
    private Map membershipToolPageURLMap1;
    private Map membershipToolPageURLMap2;
    
    public SakaiServiceDelegatingImplTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        
        // TODO: do we really need all this 1&2 dupl?
        
        pass = "sakaisecret";
        userID1 = "sakaiuserID1";
        userID2 = "sakaiuserID2";
        configScopeID1 = "configScopeID1";
        configScopeID2 = "configScopeID2";
        
        userContext1 = new SakaiServiceContextImpl();
        userContext1.setUserID(userID1);
        userContext1.setConfigScopeID(configScopeID1);
        userContext2 = new SakaiServiceContextImpl();
        userContext2.setUserID(userID2);
        userContext2.setConfigScopeID(configScopeID2);
        
        sites1 = new ArrayList();
        sites1.add(new Site("siteA", "/url/to/siteA", "course", new Term("termA","termA",false), "instructorA"));
        sites1.add(new Site("siteB", "/url/to/siteB", "project", new Term("termB","termB",false), "instructorB"));
        sites1.add(new Site("siteC", "/url/to/siteC", "home", new Term("termC","termC",false), "instructorC"));
        
        sites2 = new ArrayList();
        sites2.add(new Site("siteD", "/url/to/siteD", "course", new Term("termD","termD",false), "instructorD"));
        sites2.add(new Site("siteE", "/url/to/siteE", "project", new Term("termE","termE",false), "instructorE"));
        sites2.add(new Site("siteF", "/url/to/siteF", "home", new Term("termF","termF",false), "instructorF"));
        
        siteMap1 = new HashMap();
        siteMap2 = new HashMap();
        siteMap1.put(userContext1.getUserID(), sites1);
        siteMap2.put(userContext2.getUserID(), sites2);
        
        terms = new ArrayList();
        terms.add(new Term("termA", "termA", false));
        terms.add(new Term("termB", "termB", false));
        terms.add(new Term("termC", "termC", false));
        terms.add(new Term("termD", "termD", false));
        terms.add(new Term("termE", "termE", false));
        terms.add(new Term("termF", "termF", false));
      
        membershipToolPageURLMap1 = new HashMap();
        membershipToolPageURLMap1.put(userContext1.getUserID(), membershipToolPageURL1);
        membershipToolPageURLMap2 = new HashMap();
        membershipToolPageURLMap2.put(userContext2.getUserID(), membershipToolPageURL2);
        
        sitesDelegate1 = new MockSakaiSiteService();
        sitesDelegate1.setSites(siteMap1);
        loginDelegate1 = new MockSakaiLoginService();
        sitesDelegate1.setSessionToUserMapper(loginDelegate1);
        sitesDelegate1.setSeedTerms(terms);
        sitesDelegate1.setMembershipToolPageURLs(membershipToolPageURLMap1);
        
        sitesDelegate2 = new MockSakaiSiteService();
        sitesDelegate2.setSites(siteMap2);
        loginDelegate2 = new MockSakaiLoginService();
        sitesDelegate2.setSessionToUserMapper(loginDelegate2);
        sitesDelegate2.setSeedTerms(terms);
        sitesDelegate2.setMembershipToolPageURLs(membershipToolPageURLMap2);
    
        siteDelegatesMap = new HashMap();
        siteDelegatesMap.put(configScopeID1, sitesDelegate1);
        siteDelegatesMap.put(configScopeID2, sitesDelegate2);
        
        loginDelegatesMap = new HashMap();
        loginDelegatesMap.put(configScopeID1, loginDelegate1);
        loginDelegatesMap.put(configScopeID2, loginDelegate2);
        
        delegateFactory = new MockSakaiServiceDelegateFactory();
        service = new MockSakaiServiceDelegatingImpl();
        service.setServiceDelegateFactory(delegateFactory);
        service.setSiteServiceDelegates(siteDelegatesMap);
        service.setLoginServiceDelegates(loginDelegatesMap);
        
       
    }
    
    /**
     * Verifies that getActiveSiteEnrollment() passes the actual
     * service invocation as a command to an exection
     * algorithm implemented by executeSiteServiceCall()
     * 
     */
    public void testGetActiveSiteEnrollment_PassesCommandToExecutionAlgorithm() {
        
        service.getActiveSiteEnrollment(userContext1);
        
        GetActiveSiteEnrollmentInvoker cmd = 
            (GetActiveSiteEnrollmentInvoker)
            service.getExecuteSiteServiceCall_CommandArg();
        
        // check the args passed to the command
        assertSame(userContext1, cmd.getUserContext());
        assertSame(getSiteDelegateFixture(userContext1), cmd.getSakaiSiteService());
        
    }
    
    /**
     * Verifies that Collection returned from getActiveSiteEnrollment()
     * is the same Collection cached as the operation result in the
     * generated GetActiveSiteEnrollmentInvoker 
     */
    public void testGetActiveSiteEnrollment_ReturnsCommandResult() {
        
        Collection result = service.getActiveSiteEnrollment(userContext1);
        
        GetActiveSiteEnrollmentInvoker cmd = 
            (GetActiveSiteEnrollmentInvoker)
            service.getExecuteSiteServiceCall_CommandArg();
        
        assertEquals(service.siteResultCollection(cmd.getResult()), result);
        
    }
    
    /**
     * Verifies that if the delegate Site service returns no
     * Sites, the return value from getActiveSiteEnrollment()
     * is an empty Collection.
     * 
     */
    public void testGetActiveSiteEnrollment_EmptyResultSetMapsToEmptyCollection() {
        
        SakaiServiceContext userContext = new SakaiServiceContextImpl();
        userContext.setUserID("user-id-with-no-sites");
        
        // use known configscope, otherwise the underlying SakaiSiteService
        // will have no reference to a SessionToUserMapper
        userContext.setConfigScopeID("configScopeID1");
        
        Collection sites = service.getActiveSiteEnrollment(userContext);
        
        assertNotNull(sites);
        assertTrue(sites.isEmpty());
        
    }
    
    
    
    /**
     * Verifies that getActiveSiteEnrollmentByTerm() passes the actual
     * service invocation as a command to an exection
     * algorithm implemented by executeSiteServiceCall()
     * 
     */
    public void testGetActiveSiteEnrollmentByTerm_PassesCommandToExecutionAlgorithm() {
        
        String term = "termA";
        service.getActiveSiteEnrollmentByTerm(userContext1, term);
        
        GetActiveSiteEnrollmentByTermInvoker cmd = 
            (GetActiveSiteEnrollmentByTermInvoker)
            service.getExecuteSiteServiceCall_CommandArg();
        
        // check the args passed to the command
        assertSame(userContext1, cmd.getUserContext());
        assertSame(getSiteDelegateFixture(userContext1), cmd.getSakaiSiteService());
        assertSame(term, cmd.getTerm());
        
    }
    
    /**
     * Verifies that Collection returned from getActiveSiteEnrollmentByTerm()
     * is the same Collection cached as the operation result in the
     * generated GetActiveSiteEnrollmentInvoker 
     */
    public void testGetActiveSiteEnrollmentByTerm_ReturnsCommandResult() {
        
        String term = "termA";
        Collection result = service.getActiveSiteEnrollmentByTerm(userContext1, term);
        
        GetActiveSiteEnrollmentByTermInvoker cmd = 
            (GetActiveSiteEnrollmentByTermInvoker)
            service.getExecuteSiteServiceCall_CommandArg();
        
        assertEquals(service.siteResultCollection(cmd.getResult()), result);
        
    }
    
    /**
     * Verifies that if the delegate Site service returns no
     * Sites, the return value from getActiveSiteEnrollmentByTerm()
     * is an empty Collection.
     * 
     */
    public void testGetActiveSiteEnrollmentByTerm_EmptyResultSetMapsToEmptyCollection() {
        
        SakaiServiceContext userContext = new SakaiServiceContextImpl();
        userContext.setUserID("user-id-with-no-sites");
        
        // use known configscope, otherwise the underlying SakaiSiteService
        // will have no reference to a SessionToUserMapper
        userContext.setConfigScopeID("configScopeID1");
        
        String term = "termA";
        Collection sites = service.getActiveSiteEnrollmentByTerm(userContext, term);
        
        assertNotNull(sites);
        assertTrue(sites.isEmpty());
        
    }
    
    /**
     * Verifies that getActiveSiteEnrollmentByType() passes the actual
     * service invocation as a command to an exection
     * algorithm implemented by executeSiteServiceCall()
     * 
     */
    public void testGetActiveSiteEnrollmentByType_PassesCommandToExecutionAlgorithm() {
        
        String type = "project";
        service.getActiveSiteEnrollmentByType(userContext1, type);
        
        GetActiveSiteEnrollmentByTypeInvoker cmd = 
            (GetActiveSiteEnrollmentByTypeInvoker)
            service.getExecuteSiteServiceCall_CommandArg();
        
        // check the args passed to the command
        assertSame(userContext1, cmd.getUserContext());
        assertSame(getSiteDelegateFixture(userContext1), cmd.getSakaiSiteService());
        assertSame(type, cmd.getType());
        
    }
    
    /**
     * Verifies that Collection returned from getActiveSiteEnrollmentByType()
     * is the same Collection cached as the operation result in the
     * generated GetActiveSiteEnrollmentInvoker 
     */
    public void testGetActiveSiteEnrollmentByType_ReturnsCommandResult() {
        
        String type = "project";
        Collection result = service.getActiveSiteEnrollmentByType(userContext1, type);
        
        GetActiveSiteEnrollmentByTypeInvoker cmd = 
            (GetActiveSiteEnrollmentByTypeInvoker)
            service.getExecuteSiteServiceCall_CommandArg();
        
        assertEquals(service.siteResultCollection(cmd.getResult()), result);
        
    }
    
    /**
     * Verifies that if the delegate Site service returns no
     * Sites, the return value from getActiveSiteEnrollmentByTerm()
     * is an empty Collection.
     * 
     */
    public void testGetActiveSiteEnrollmentByType_EmptyResultSetMapsToEmptyCollection() {
        
        SakaiServiceContext userContext = new SakaiServiceContextImpl();
        userContext.setUserID("user-id-with-no-sites");
        
        // use known configscope, otherwise the underlying SakaiSiteService
        // will have no reference to a SessionToUserMapper
        userContext.setConfigScopeID("configScopeID1");
        
        String type = "project";
        Collection sites = service.getActiveSiteEnrollmentByType(userContext, type);
        
        assertNotNull(sites);
        assertTrue(sites.isEmpty());
        
    }
    
    
    /**
     * Verifies that getTerms() passes the actual
     * service invocation as a command to an exection
     * algorithm implemented by executeSiteServiceCall()
     * 
     */
    public void testTerms_PassesCommandToExecutionAlgorithm() {
        
        service.getTerms(userContext1);
        
        GetTermsInvoker cmd = 
            (GetTermsInvoker) service.getExecuteSiteServiceCall_CommandArg();
        
        // check the args passed to the command
        assertSame(userContext1, cmd.getUserContext());
        assertSame(getSiteDelegateFixture(userContext1), cmd.getSakaiSiteService());
        
    }
    
    /**
     * Verifies that Collection returned from getTerms()
     * is the same Collection cached as the operation result in the
     * generated GetTermsInvoker 
     */
    public void testGetTerms_ReturnsCommandResult() {
        
        Collection result = service.getTerms(userContext1);
        
        GetTermsInvoker cmd = 
            (GetTermsInvoker) service.getExecuteSiteServiceCall_CommandArg();
        
        assertEquals(service.termResultCollection(cmd.getResult()), result);
        
    }
    
    /**
     * Verifies that if the delegate SakaiSiteService returns no
     * Terms, the return value from getTerms()
     * is an empty Collection.
     * 
     */
    public void testGetTerms_EmptyResultSetMapsToEmptyCollection() {
        
        MockSakaiSiteService siteDelegate = 
            (MockSakaiSiteService) getSiteDelegateFixture(userContext1);
        siteDelegate.setSeedTerms(null);
        Collection terms = service.getTerms(userContext1);
        
        assertNotNull(terms);
        assertTrue(terms.isEmpty());
        
    }
    
    /**
     * Verifies that getMembershipToolPageURL() passes the actual
     * service invocation as a command to an exection
     * algorithm implemented by executeSiteServiceCall()
     * 
     */
    public void testGetMembershipToolPageURL_PassesCommandToExecutionAlgorithm() {
        
        service.getMembershipToolPageURL(userContext1);
        
        GetMembershipToolPageURLInvoker cmd = 
            (GetMembershipToolPageURLInvoker)
            service.getExecuteSiteServiceCall_CommandArg();
        
        // check the args passed to the command
        assertSame(userContext1, cmd.getUserContext());
        assertSame(getSiteDelegateFixture(userContext1), cmd.getSakaiSiteService());
        
    }
    
    /**
     * Verifies that String returned from getMembershipToolPageURL()
     * is the same String cached as the operation result in the
     * generated GetMembershipToolPageURLInvoker 
     */
    public void testGetMembershipToolPageURL_ReturnsCommandResult() {
        
        String result = service.getMembershipToolPageURL(userContext1);
        
        GetMembershipToolPageURLInvoker cmd = 
            (GetMembershipToolPageURLInvoker)
            service.getExecuteSiteServiceCall_CommandArg();
        
        assertEquals(service.stringResultFirstElement(cmd.getResult()), result);
        
    }
    
    /**
     * Verifies that if the delegate Site service returns no
     * Sites, the return value from getMembershipToolPageURL()
     * is an empty Collection.
     * 
     */
    public void testGetMembershipToolPageURL_EmptyResultSetMapsToNullString() {
        
        SakaiServiceContext userContext = new SakaiServiceContextImpl();
        userContext.setUserID("user-id-with-no-membership-tool-placement");
        
        // use known configscope, otherwise the underlying SakaiSiteService
        // will have no reference to a SessionToUserMapper
        userContext.setConfigScopeID("configScopeID1");
        
        String result = service.getMembershipToolPageURL(userContext);
        
        assertNull(result);
        
    }
    
    
    /**
     * Verifies a search for a User's Sites logs in to Sakai prior to executing the
     * Site query. Just asserts that the login mechanism is called with the
     * proper arguments. See {@link testLogin_CachesSakaiSessionID()} and
     * other testLogin_Xxx() methods for additional login behavior verification.
     *
     */
    public void testExecuteSiteServiceCall_DelegatesLazyLoginToSakaiLoginService() {
        
        userContext1.setSakaiSessionID(null);
        SakaiSiteService siteDelegate = getSiteDelegateFixture(userContext1);
        MockSakaiLoginService loginDelegate = (MockSakaiLoginService) getLoginDelegateFixture(userContext1);
        
        // doesnt actually matter which Call we pass in. Behavior should be the same for all
        GetActiveSiteEnrollmentInvoker cmd = 
            new GetActiveSiteEnrollmentInvoker(siteDelegate,userContext1);
        
        service.executeSiteServiceCall(cmd, userContext1);
        
        assertTrue(loginDelegate.calledLoginPortalUser());
        assertEquals(userContext1.getUserID(), loginDelegate.getLastLoginPortalUserCall_UserIDArg());
        assertEquals(userContext1.getSecret(), loginDelegate.getLastLoginPortalUserCall_PassArg());
        
    }
    
    /**
     * Verifies that an active enrollment query skips the
     * login process if a session is already associated
     * with the current context.
     */
    public void testExecuteSiteServiceCall_SkipsLoginIfContextHasSession() {
        
        userContext1.setSakaiSessionID("123456789");
        SakaiSiteService siteDelegate = getSiteDelegateFixture(userContext1);
        
        // doesnt actually matter which Call we pass in. Behavior should be the same for all
        GetActiveSiteEnrollmentInvoker cmd = 
            new GetActiveSiteEnrollmentInvoker(siteDelegate, userContext1);
        
        service.executeSiteServiceCall(cmd, userContext1);
        
        assertFalse(loginDelegate1.calledLoginPortalUser());
        
    }
    
    /**
     * Verifies that executeSiteServiceCall() raises a 
     * SakaiQueryException if the given Call object 
     * raises a SakaiQueryException. Also ensures that error code
     * is available in the SakaiQueryException.
     *
     */
    public void testExecuteSiteServiceCall_RaisesSakaiQueryExceptions() {
        
        
        final String errCode = "a-site-query-error-code";
        
        GetActiveSiteEnrollmentInvoker cmd = 
            new GetActiveSiteEnrollmentInvoker(null,null) { // args dont matter
            
            public void exec() {
                throw new SakaiQueryException("query failed", errCode);
            }
            
        };
        
        try {
            service.executeSiteServiceCall(cmd, userContext1);
            fail("expected a SakaiQueryException");
        } catch ( SakaiQueryException e ) {
            assertEquals(errCode,e.getErrorCode());
        }
    }
    
    
    /**
     * Verifies that executeSiteServiceCall() raises a SakaiLoginException 
     * caused by the underlying login() implementation. Also ensures that error code
     * is available in the SakaiLoginException.
     */
    public void testExecuteSiteServiceCall_RaisesSakaiLoginExceptions() {
        
        final String errCode = "a-login-error-code";
        SakaiServiceDelegatingImpl angryService = new SakaiServiceDelegatingImpl() {
            protected void login(SakaiServiceContext userContext) throws SakaiLoginException {
                throw new SakaiLoginException("login failed", errCode);
            }
        };
        
        SakaiSiteService siteDelegate = getSiteDelegateFixture(userContext1);
        GetActiveSiteEnrollmentInvoker cmd = // cmd impl doesnt matter
            new GetActiveSiteEnrollmentInvoker(siteDelegate, userContext1);
        
        try {
            angryService.executeSiteServiceCall(cmd, userContext1);
            fail("expected a SakaiLoginException");
        } catch ( SakaiLoginException e ) {
            assertEquals(errCode, e.getErrorCode());
        }
        
    }
    
    /**
     * Verifies that executeSiteServiceCall() only attempts one
     * login if passed a SakaiServiceContext without a Sakai session ID.
     *
     */
    public void testExecuteSiteServiceCall_NoRetryOnFailedInitialLogin() {
        
        final String errCode = "a-login-error-code";
        MockSakaiLoginService loginDelegate = new MockSakaiLoginService() {
            public LoginResult loginPortalUser(String user, String secret) {
                super.logLoginPortalUserCall(user, secret);
                LoginResult result = new LoginResult();
                result.setErrorCode(errCode);
                return result;
            }
        };
        
        loginDelegatesMap.put(configScopeID1, loginDelegate); // map already cached by service
        
        MockSakaiSiteService siteDelegate = 
            (MockSakaiSiteService)getSiteDelegateFixture(userContext1);
        GetActiveSiteEnrollmentInvoker cmd = 
            new GetActiveSiteEnrollmentInvoker(siteDelegate, userContext1);
        
        try {
            service.executeSiteServiceCall(cmd, userContext1);
            fail("expected a SakaiLoginException");
        } catch ( SakaiLoginException e ) {
            assertEquals(errCode,e.getErrorCode());
        }
        
        // only one login attempt
        assertEquals(1, loginDelegate.getLoginPortalUserCallCount()); 
        
        
        // never attempts to retrieve site list
        assertEquals(0, siteDelegate.getGetActiveSiteEnrollmentCallCount());
        
    }
    
  
    
    /**
     * Verifies that executeSiteServiceCall() retries a login
     * if the underlying service indicates the current session is
     * invalid. Specifically, verifies that only one retry is
     * attempted, even if a new session cannot ever be acquired.
     *
     */
    public void testExecuteSiteServiceCall_RetryLoginOnPermanentlyRevokedSession() {
        
        MockSakaiSiteServiceInvalidSessionIdImpl angrySitesDelegate1 = 
            new MockSakaiSiteServiceInvalidSessionIdImpl(); // by default will accept all session IDs
        
        MockSakaiLoginService loginDelegate = 
            (MockSakaiLoginService)getLoginDelegateFixture(userContext1);
        
        angrySitesDelegate1.setSessionToUserMapper(loginDelegate);
        
        GetActiveSiteEnrollmentInvoker cmd = 
            new GetActiveSiteEnrollmentInvoker(angrySitesDelegate1, userContext1);
        
        service.executeSiteServiceCall(cmd, userContext1); // acquire initial session
        
        // throw a fit on all following login attempts. this is intentional: we want to
        // make sure the client does not fall into an infinite loop if session
        // allocation fails indefintely on retry.
        angrySitesDelegate1.setGetActiveSiteEnrollmentAttempts_ReturnErrorQuota(Integer.MAX_VALUE);
        
        try {
          service.executeSiteServiceCall(cmd, userContext1);
          fail("Expected a SakaiLoginException");
        } catch ( SakaiLoginException e ) {
            // success
        }
        
        // initial login, plus retry attempt
        assertEquals(2, loginDelegate.getLoginPortalUserCallCount()); 
        
        
        // initial call to acquire session, second call notified of invalid session,
        // third call with new session
        assertEquals(3, angrySitesDelegate1.getGetActiveSiteEnrollmentCallCount());
        
    }
    
    /**
     * Verifies that executeSiteServiceCall() retries a login
     * if the underlying service indicates the current session is
     * invalid. Specifically, verifies that only one retry is
     * attempted, and if the retry is successful, it returns the
     * associated query results.
     * 
     * <p>Basically the same thing as 
     * testExecuteSiteServiceCall_RetryLoginOnPermanentlyRevokedSession(),
     * but with a limit on failed logins.
     */
    public void testExecuteSiteServiceCall_RetryOnTemporarilyRevokedSession() {
        
        MockSakaiSiteServiceInvalidSessionIdImpl angrySitesDelegate1 = 
            new MockSakaiSiteServiceInvalidSessionIdImpl(); // by default will accept all login attempts
        
        MockSakaiLoginService loginDelegate = 
            (MockSakaiLoginService)getLoginDelegateFixture(userContext1);
        
        angrySitesDelegate1.setSessionToUserMapper(loginDelegate);
        
        GetActiveSiteEnrollmentInvoker cmd = // cmd impl doesnt matter
            new GetActiveSiteEnrollmentInvoker(angrySitesDelegate1,userContext1);
        
        service.executeSiteServiceCall(cmd, userContext1); // acquire a session
        assertNotNull(userContext1.getSakaiSessionID());
        
        // throw a fit on the next login attempt, then revert to "normal"
        // behavior
        angrySitesDelegate1.setGetActiveSiteEnrollmentAttempts_ReturnErrorQuota(1);
        
        
        service.executeSiteServiceCall(cmd, userContext1); // note that we're reusing the same command

        // this exists b/c a bug actually existed that passed the userID rather
        // than the session ID on the retry
        assertEquals(userContext1.getSakaiSessionID(), 
                angrySitesDelegate1.getLastGetActiveSiteEnrollmentCall_SessionIDArg());
        
        // initial login, plus retry attempt
        assertEquals(2, loginDelegate.getLoginPortalUserCallCount()); 
        
        // initial call to acquire session, second call notified of invalid session,
        // third call with new session
        assertEquals(3, angrySitesDelegate1.getGetActiveSiteEnrollmentCallCount());
        
    }
    
    /**
     * Verifies that getActiveSiteEnrollment() accesses the SakaiSiteService
     * delegate map through the lazy initialization mechanism. Otherwise
     * getServiceDelegate() will blow up if no delegate map has been assigned.
     *
     */
    public void testExecuteSiteServiceCall_SelfEncapsulatedAccessToSiteServiceDelegates() {
        service.setSiteServiceDelegates(null);
        service.getActiveSiteEnrollment(userContext1); // will throw NullPointerException if accessing map field directly
    }
    
    /**
     * Verifies that getLoginServiceDelegates() and 
     * getSiteServiceDelegates() never return null. Note that it's still
     * probably not a good idea to call setLoginXxxServiceDelegates() once
     * a SakaiServiceDelegatingImpl has been pressed into multi-threaded
     * service.
     */
    public void testGetXxxServiceDelegates_LazilyInitializesDelegateMaps() {
        SakaiServiceDelegatingImpl cleanService = new SakaiServiceDelegatingImpl();
        assertNotNull(cleanService.getLoginServiceDelegates());
        assertNotNull(cleanService.getSiteServiceDelegates());
        cleanService.setLoginServiceDelegates(null);
        cleanService.setSiteServiceDelegates(null);
        assertNotNull(cleanService.getLoginServiceDelegates());
        assertNotNull(cleanService.getSiteServiceDelegates());
    }
    
    
    
    /**
     * Verifies that login() accesses the SakaiLoginService
     * delegate map through the lazy initialization mechanism. Otherwise
     * getServiceDelegate() will blow up if no delegate map has been assigned.
     *
     */
    public void testLogin_SelfEncapsulatedAccessToLoginServiceDelegates() {
        service.setLoginServiceDelegates(null);
        service.login(userContext1); // will throw NullPointerException if accessing map field directly
    }
    
    /**
     * Verifies that a Sakai login results in a cached session ID
     * in the current SakaiServiceContext.
     * 
     * <p>Note that we don't currently test that clients like
     * executeSiteServiceCall() retain the session ID. Would probably good
     * for completeness (see other cases where we're testing that a
     * result set makes it all the way up the call stack), but is 
     * otherwise so much paranoia.
     */
    public void testLogin_CachesSakaiSessionID() {
        
        userContext1.setSakaiSessionID(null); // just to be sure
        service.login(userContext1);
        assertNotNull(userContext1.getSakaiSessionID());
        assertEquals(loginDelegate1.getLastSessionIDIssued(), userContext1.getSakaiSessionID());
                
    }
    
    /**
     * Verify that the login() operation raises a SakaiLoginException
     * if the underlying SakaiLoginService returns an error code.
     * The SakaiLoginException must preserve that error code.
     *
     */
    public void testLogin_RaisesDelegateErrorCodeAsException() {
        final String errCode = "a-login-error-code";
        SakaiLoginService serviceDelegate = new MockSakaiLoginService() {
            public LoginResult loginPortalUser(String user, String secret) {
                LoginResult result = new LoginResult();
                result.setErrorCode(errCode);
                return result;
            }
        };
        loginDelegatesMap.put(configScopeID1, serviceDelegate); // map already cached by service
        try {
            service.login(userContext1);
            fail("expected a SakaiLoginException");
        } catch ( SakaiLoginException e ) {
            assertEquals(errCode,e.getErrorCode());
        }
    }
    
    
     
    /**
     * Verifies that getSiteServiceDelegate() invokes a helper
     * method that consolidates logic for service delegate
     * retrieval methods. This enables us to to consolidate
     * delegate retrieval logic tests (see 
     * testGetServiceDelegate_Xxx()).
     * 
     * @see #getLoginServiceDelegate_UsesHelperMethod()
     *
     */
    public void getSiteServiceDelegate_UsesHelperMethod() {
        service.getSiteServiceDelegate(userContext1);
        
        assertSame(userContext1, service.getGetServiceDelegateWith_ContextArg());
        assertSame(service.getSiteServiceDelegates(), service.getGetServiceDelegateWithDelegate_MapArg());
        assertSame(service.getSiteServiceFactoryInvoker(), service.getGetServiceDelegateWith_FactoryInvokerArg());
    }
    
    /**
     * Verifies that getLoginServiceDelegate() invokes a helper
     * method that consolidates logic for service delegate
     * retrieval methods. This enables us to to consolidate
     * delegate retrieval logic tests (see 
     * testGetServiceDelegate_Xxx()).
     * 
     * @see #getSiteServiceDelegate_UsesHelperMethod()
     *
     */
    public void getLoginServiceDelegate_UsesHelperMethod() {
        service.getLoginServiceDelegate(userContext1);
        
        SakaiLoginService expectedLoginDelegate = getLoginDelegateFixture(userContext1);
        
        assertSame(userContext1, service.getGetServiceDelegateWith_ContextArg());
        assertSame(service.getLoginServiceDelegates(), service.getGetServiceDelegateWithDelegate_MapArg());
        assertSame(service.getLoginServiceFactoryInvoker(), service.getGetServiceDelegateWith_FactoryInvokerArg());
        
    }
    
    
    
    /**
     * Verifies that SakaiSiteService lookup checks with the underlying
     * factory to determine if the currently cached service is valid
     * given the current SakaiServiceContext.
     *
     */
    public void testGetServiceDelegate_VerifiesDelegateAgainstContext() {
        
        // do not need to verify calls for SakaiSiteServices... getServiceDelegate()
        // handles all delegate types identically.
        
        getSiteServiceDelegateForContext(userContext1);
        
        SakaiSiteService expectedDelegate = getSiteDelegateFixture(userContext1);
        
        assertSame(expectedDelegate, delegateFactory.getIsSiteServiceValidForContextWith_ServiceArg());
        assertSame(userContext1, delegateFactory.getIsSiteServiceValidForContextWith_ContextArg());
        
    }
    
    /**
     * Verifies that each underlying service delegate is scoped to
     * unique keys.
     *
     */
    public void testGetServiceDelegate_ReturnsScopedInstance() {
        
        // userContext1 and 2 have different "config scope IDs"
        SakaiSiteService retrievedDelegate1 = getSiteServiceDelegateForContext(userContext1);
        
        SakaiSiteService expectedDelegate1 = getSiteDelegateFixture(userContext1);
        assertSame(expectedDelegate1, retrievedDelegate1);
        
        SakaiSiteService retrievedDelegate2 = getSiteServiceDelegateForContext(userContext2);
        
        SakaiSiteService expectedDelegate2 = getSiteDelegateFixture(userContext2);
        assertSame(expectedDelegate2, retrievedDelegate2);
        
    }
    
    /**
     * Verifies that SakaiSiteServices are lazily instantiated
     * upon receipt of an unrecognized scope ID
     *
     */
    public void testGetServiceDelegate_LazilyInstantiatesAndCachesScopedService() {
        
        SakaiServiceContext userContext = new SakaiServiceContextImpl();
        userContext.setUserID("sakaiuserID");
        userContext.setConfigScopeID("configscopeID");
        SakaiSiteService retrievedDelegate1 = getSiteServiceDelegateForContext(userContext);
        
        assertNotNull(retrievedDelegate1);
        assertTrue(delegateFactory.calledASiteService());
        assertSame(delegateFactory.getASiteService_ContextArg(), userContext);
        
        SakaiSiteService retrievedDelegate2 = getSiteServiceDelegateForContext(userContext);
        
        assertSame(retrievedDelegate1, retrievedDelegate2); // delegate should have been cached
        
    }
    
    /**
     * Verifies that SakaiSiteService allocation fails if the client does
     * not provide a scope ID. Ensures nulls, empty strings and whitespace
     * all treated identically.
     *
     */
    public void testGetServiceDelegate_DefaultScope() {
        
        assertRetrievesSingletonServiceDelegateForConfigScopeID(null);
        assertRetrievesSingletonServiceDelegateForConfigScopeID("");
        assertRetrievesSingletonServiceDelegateForConfigScopeID("  ");
        assertSame(getSiteServiceDelegateForConfigScopeID(null),getSiteServiceDelegateForConfigScopeID(""));
        assertSame(getSiteServiceDelegateForConfigScopeID(null),getSiteServiceDelegateForConfigScopeID("  "));
        
    }
    
    /**
     * Verifies that two threads cannot lookup (and possibly
     * replace) a SakaiSiteService under a given scope ID
     * at the same time.
     */
    public void testGetServiceDelegate_SingleThreadedAllocation() throws InterruptedException {
        
        service.setBlockGetServiceDelegateUnguardedCalls(true); // trap the next caller
        
        class Worker extends Thread {
            public void run() {
                getSiteServiceDelegateForContext(userContext1);
            }
        }
        
        Worker worker1 = new Worker();
        worker1.start();
        
        // try to ensure worker1 is actually blocking
        while ( service.getAttemptedGetServiceDelegateUnguardedCalls() < 1 ) {
            Thread.currentThread().sleep(50);
        }
        
        // start another thread trying to do the same thing.
        Thread worker2 = new Worker();
        worker2.start();
        
        // Wait a reasonable amount of time for that thread to start blocking.
        // on a mutex guarding the allocation logic. Is not guaranteed to 
        // start blocking by the time we wake up, but is highly likely.
        Thread.currentThread().sleep(1000);
        
        // check that only one unguarded call has been attempted.
        assertEquals(1, service.getAttemptedGetServiceDelegateUnguardedCalls());
        
        // release the blocking thread
        service.setBlockGetServiceDelegateUnguardedCalls(false);
        
        // try to give both threads a chance to finish up
        Thread.currentThread().sleep(service.getBlockingGetServiceDelegateUnguardedCallSleepTime() * 4);
        
        // check to see that both calls now completed
        assertEquals(2, service.getCompletedGetServiceDelegateUnguardedCalls());
        
        //TODO any point in determining whether the calls completed in the correct order?
        
        
    }
    
    /*
     * This is for performance profiling synchronization strategies
     * in getServiceDelegate. This test does not assert any post
     * conditions.
     
    public void testGetServiceDelegate_UnderLoad() throws InterruptedException {
        
        final int readerCnt = 9;
        final int readerSleep = 50;
        final int writerCnt = 1;
        final int writerSleep = 1000; // artificially high write rate
        final int execQuotaPerReader = 50;
        final int execQuotaPerWriter = execQuotaPerReader / 10;
        final Latch latch = new Latch();
        
        
        class Reader extends Thread {
            private int execs;
            
            public int getExecs() {
                return execs;
            }
            public void run() {
                try {
                    latch.acquire();
                    while (execs < execQuotaPerReader) {
                        getSiteServiceDelegateForContext(userContext1);
                        execs++;
                        Thread.currentThread().sleep(readerSleep);
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        
        class Writer extends Thread {
            volatile private boolean pleaseStop;
            private int execs;
            private int configScopeIDCnt = 100;
            
            public int getExecs() {
                return execs;
            }
            public void run() {
                try {
                    latch.acquire();
                    while (!(pleaseStop) && (execs < execQuotaPerWriter) ) {
                        getSiteServiceDelegateForConfigScopeID("configScopeID"
                                + configScopeIDCnt++);
                        execs++;
                        Thread.currentThread().sleep(writerSleep);

                    }
                } catch (InterruptedException e) {
                    return;
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
        
        Reader[] readers = new Reader[readerCnt];
        for ( int p = 0; p < readers.length; p++ ) {
            readers[p] = new Reader();
            readers[p].start();
        }
        
        Writer[] writers = new Writer[writerCnt];
        for ( int p = 0; p < writers.length; p++ ) {
            writers[p] = new Writer();
            writers[p].start();
        }
        
        long startTime = System.currentTimeMillis();
        latch.release();
        
        boolean allReadersDone = false;
        while (!(allReadersDone)) {
            allReadersDone = true;
            for ( int p = 0; p < readers.length; p++ ) {
                if ( readers[p].isAlive() ) {
                    allReadersDone = false;
                    break;
                }
            }
        }
        
        for ( int p = 0; p < writers.length; p++ ) {
            writers[p].pleaseStop = true;
        }
        
        
        long stopTime = System.currentTimeMillis();
        Thread.sleep(500);
        
        int reads = 0;
        for ( int p = 0; p < readers.length; p++ ) {
            reads += readers[p].getExecs();
        }
        int writes = 0;
        for ( int p = 0; p < writers.length; p++ ) {
            writes += writers[p].getExecs();
        }
        
        System.out.println("Total reads: " + reads);
        System.out.println("Total writes: " + writes);
        System.out.println("Execution time (millis): " + (stopTime - startTime));
        
        
        
    }
    */
    
    
    
    
    
    
    
    /**
     * Helper method for testing various inputs to getServiceDelegate().
     * Given a config scope ID, the underlying factory mechanism should return
     * the same delegate instance to each request referencing that ID.
     * 
     * @param configScopeID
     */
    protected void assertRetrievesSingletonServiceDelegateForConfigScopeID(String configScopeID) {
        SakaiSiteService delegate = getSiteServiceDelegateForConfigScopeID(configScopeID);
        assertNotNull(delegate);
        assertSame(delegate, getSiteServiceDelegateForConfigScopeID(configScopeID));
    }
    
    /**
     * Helper method for instantiating a SakaiServiceContext and passing it to 
     * getSiteServiceDelegate()
     * 
     * @param configScopeID
     * @return
     */
    protected SakaiSiteService getSiteServiceDelegateForConfigScopeID(String configScopeID) {
        SakaiServiceContext userContext = new SakaiServiceContextImpl();
        userContext.setUserID("sakaiuserID");
        userContext.setConfigScopeID(configScopeID);
        SakaiSiteService delegate = getSiteServiceDelegateForContext(userContext);
        return delegate;
    }
    
    protected SakaiSiteService getSiteServiceDelegateForContext(SakaiServiceContext context) {
        return 
        (SakaiSiteService)
        service.getServiceDelegate(context, 
                service.getSiteServiceDelegates(), service.getSiteServiceFactoryInvoker());
    }
    
    
    
    /**
     * Retrieve the SakaiLoginService fixture associated with the specified
     * SakaiServiceContext
     * 
     * @param context
     * @return
     */
    protected SakaiLoginService getLoginDelegateFixture(SakaiServiceContext context) {
        return (SakaiLoginService) service.getLoginServiceDelegates().get(userContext1.getConfigScopeID());
    }
    
    /**
     * Retrieve the SakaiSiteService fixture associated with the specified
     * SakaiServiceContext
     * 
     * @param context
     * @return
     */
    protected SakaiSiteService getSiteDelegateFixture(SakaiServiceContext context) {
        return (SakaiSiteService) service.getSiteServiceDelegates().get(context.getConfigScopeID());
    }
    
    
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}




