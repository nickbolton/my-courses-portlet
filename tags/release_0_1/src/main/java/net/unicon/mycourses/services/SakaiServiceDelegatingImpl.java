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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.unicon.sakai.portalservices.api.LoginResult;
import net.unicon.sakai.portalservices.api.SakaiLoginService;
import net.unicon.sakai.portalservices.api.SakaiSiteService;
import net.unicon.sakai.portalservices.api.Site;
import net.unicon.sakai.portalservices.api.SiteQueryResult;
import net.unicon.sakai.portalservices.api.StringQueryResult;
import net.unicon.sakai.portalservices.api.Term;
import net.unicon.sakai.portalservices.api.TermQueryResult;

/**
 * Implements the SakaiSiteService contract by delegating
 * calls to more fine-grained service interfaces defined
 * by remote Sakai APIs.
 * 
 * 
 * @author dmccallum
 *
 */
public class SakaiServiceDelegatingImpl implements SakaiService {
    
    public static final String DEFAULT_CONFIG_SCOPE_ID = 
        "SakaiServiceDelegatingImpl.DEFAULT_CONFIG_SCOPE_ID";
           
    /** Map between "config scope ID's" (Strings) and SakaiLoginServices */
    private Map loginServiceDelegates;
    /** Map between "config scope ID's" (Strings) and SakaiSiteServicess */
    private Map siteServiceDelegates;
    /** Strategy for generating new SakaiXxxService instances */
    private SakaiServiceDelegateFactory delegateFactory;
    
    private SiteServiceFactoryInvoker siteServiceFactoryInvoker;
    private LoginServiceFactoryInvoker loginServiceFactoryInvoker;
    
    public SakaiServiceDelegatingImpl() {
        siteServiceDelegates = new HashMap();
        loginServiceDelegates = new HashMap();
    }
    
    /** {@inheritDoc }*/
    public Collection getActiveSiteEnrollment(SakaiServiceContext userContext) 
    throws SakaiServiceException {
        
        GetActiveSiteEnrollmentInvoker call = 
            new GetActiveSiteEnrollmentInvoker(getSiteServiceDelegate(userContext), userContext);
        
        executeSiteServiceCall(call, userContext);
        
        SiteQueryResult result = call.getResult();
        
        return siteResultCollection(result);
        
    }
    
    /** {@inheritDoc }*/
    public Collection getActiveSiteEnrollmentByTerm(SakaiServiceContext userContext, String termCode)
    throws SakaiServiceException {
        
        GetActiveSiteEnrollmentByTermInvoker call = 
            new GetActiveSiteEnrollmentByTermInvoker(getSiteServiceDelegate(userContext), 
                    userContext, termCode);
        
        executeSiteServiceCall(call, userContext);
        
        SiteQueryResult result = call.getResult();
        
        return siteResultCollection(result);
        
    }
    
    /** {@inheritDoc }*/
    public Collection getActiveSiteEnrollmentByType(SakaiServiceContext userContext, String typeCode)
    throws SakaiServiceException {
        
        GetActiveSiteEnrollmentByTypeInvoker call = 
            new GetActiveSiteEnrollmentByTypeInvoker(getSiteServiceDelegate(userContext), 
                    userContext, typeCode);
        
        executeSiteServiceCall(call, userContext);
        
        SiteQueryResult result = call.getResult();
        
        return siteResultCollection(result);
        
    }
    
    /** {@inheritDoc }*/
    public Collection getTerms(SakaiServiceContext userContext)
    throws SakaiServiceException {
        
        GetTermsInvoker call = 
            new GetTermsInvoker(getSiteServiceDelegate(userContext), userContext);
        
        executeSiteServiceCall(call, userContext);
        
        TermQueryResult result = call.getResult();
        
        return termResultCollection(result);
        
    }
    
    /** {@inheritDoc }*/
    public String getMembershipToolPageURL(SakaiServiceContext userContext)
    throws SakaiServiceException {
        
        GetMembershipToolPageURLInvoker call = new GetMembershipToolPageURLInvoker(
                getSiteServiceDelegate(userContext), userContext);

        executeSiteServiceCall(call, userContext);

        StringQueryResult result = call.getResult();
        String[] stringArrayResult = result.getQueryResults();

        return stringResultFirstElement(result);
        
    }
    
    
    
    /**
     * Abstracts the lazy login algorithm surrounding calls to SakaiSiteService. 
     * 
     * @param delegateInvoker a command object which will invoke a method
     *   on the appropriately pre-selected SakaiSiteService delegate
     * @param userContext useful for determining whether the user is logged in
     *   or not. Will cache a Sakai session ID upon a successful login.
     */
    protected void executeSiteServiceCall(SakaiSiteServiceInvoker delegateInvoker, SakaiServiceContext userContext) 
    throws SakaiServiceException {

        boolean isLoggedIn = userContext.hasSakaiSession();
        
        if ( !(isLoggedIn) ) {
            
            login(userContext);
            
        }
        
        try {
            
            delegateInvoker.exec();
            
        } catch ( SakaiLoginException e ) {
            
            // one retry
            userContext.setSakaiSessionID(null);
            login(userContext);
            delegateInvoker.resetExec();
            delegateInvoker.exec();
            
        }
        
        
    }
    
    

    
    
    /**
     * Assign the objects to which login queries will be delegated.
     * Nulls accepted. Map should be String -&gt; SakaiLoginServiceWapper
     * (not validated).
     * 
     * <p>Not a good idea to call this once this SakaiServiceDelegateImpl
     * has been pressed into (multi-threaded) service.
     * 
     * @param delegates Map of "config scope IDs" (Strings) to
     *   SakaiLoginServices
     */
    public void setLoginServiceDelegates(Map delegates) {
        loginServiceDelegates = delegates;
    }
    
    /**
     * Assign the objects to which Site queries will be delegated.
     * Nulls accepted. Map should be String -&gt; SakaiSiteServiceWapper
     * (not validated).
     * 
     * <p>Not a good idea to call this once this SakaiServiceDelegateImpl
     * has been pressed into (multi-threaded) service.
     * 
     * @param delegates Map of "config scope IDs" (Strings) to
     *   SakaiLoginServices
     */
    public void setSiteServiceDelegates(Map delegates) {
        siteServiceDelegates = delegates;
    }
    
    /**
     * Assign the object responsible for instantiating new
     * SakaiSiteService and SakaiLoginService objects.
     * 
     * @param factory
     */
    public void setServiceDelegateFactory(SakaiServiceDelegateFactory factory) {
        this.delegateFactory = factory;
        siteServiceFactoryInvoker = new SiteServiceFactoryInvoker(this.delegateFactory);
        loginServiceFactoryInvoker = new LoginServiceFactoryInvoker(this.delegateFactory);
    }
    
    /**
     * Logs in the specified Context. Does not guard against
     * already logged-in Contexts.
     * 
     * <p>See {@link net.unicon.sakai.portalservices.api.SakaiLoginServiceErrorCodes}
     * for a soft enum of possible error codes.
     * 
     * @param userContext a SakaiServiceContext
     * @throws SakaiLoginException if a Sakai session cannot be allocated
     *   for the specified user
     */
    protected void login(SakaiServiceContext userContext) throws SakaiLoginException {
        
        SakaiLoginService delegate = getLoginServiceDelegate(userContext);
        
        LoginResult result = 
            delegate.loginPortalUser(userContext.getUserID(), 
                userContext.getSecret());
        
        if ( result.hasErrorCode() ) {
            throw new SakaiLoginException("Failed to login Sakai portal user [user: " + 
                    userContext.getUserID()  + "][config scope: " + userContext.getConfigScopeID() + "]",
                    result.getErrorCode());
        } else {
            userContext.setSakaiSessionID(result.getSessionId());
        }
                
    }
    
    
    /**
     * Ensures empty and null Site[]s are handled consistently when
     * translating to a Collection.
     * 
     * @param src the source object.
     */
    protected Collection siteResultCollection(SiteQueryResult src) {
        
        Site[] srcSites = src.getQueryResults();
        
        if ( srcSites == null || srcSites.length == 0 ) {
            return new ArrayList();
        }
        
        return resultCollection(srcSites);
        
    }
    
    /**
     * Ensures empty and null Term[]s are handled consistently when
     * translating to a Collection.
     * 
     * @param src the source object.
     */
    protected Collection termResultCollection(TermQueryResult src) {
        
        Term[] srcTerms = src.getQueryResults();
        
        return resultCollection(srcTerms);
        
    }
    
    /**
     * Ensures empty and null String[]s are handled consistently when
     * extracting the first element.
     * 
     * @param src the source object.
     */
    protected String stringResultFirstElement(StringQueryResult src) {
        
        String[] stringArrayResult = src.getQueryResults();

        return stringArrayResult == null || stringArrayResult.length == 0 ? null
                : stringArrayResult[0];
        
    }
    
    protected Collection resultCollection(Object[] src) {
        
        if ( src == null || src.length == 0 ) {
            return new ArrayList();
        }
        
        //TODO some sort of problem with the list returned by Arrays.asList(). What was it?
        return new ArrayList(Arrays.asList(src));
        
    }
    
    /**
     * Lazy getter for a concrete SakaiSiteService. Includes
     * logic for allocating a new instance in the event that
     * a previouslly allocated instance's configuration has
     * become stale. Specifically, will allocate a new
     * instance in the specified Context's configuration scope if
     * the Context indicates that the service's WSDL location
     * has changed.
     * 
     * <p>If the specified Context has an empty or null config
     * scope ID, will return a default SakaiSiteService instance.
     * 
     * @return a concrete SakaiSiteService for the specified
     *   Context's configuration scope
     */
    protected SakaiSiteService getSiteServiceDelegate(SakaiServiceContext context) 
    throws SakaiServiceConfigurationException {
        
        return 
        (SakaiSiteService) 
        getServiceDelegate(context, getSiteServiceDelegates(), siteServiceFactoryInvoker);
        
    }
    
    /**
     * Lazy getter for a concrete SakaiLoginService. Includes
     * logic for allocating a new instance in the event that
     * a previouslly allocated instance's configuration has
     * become stale. Specifically, will allocate a new
     * instance in the specified Context's configuration scope if
     * the Context indicates that the service's WSDL location
     * has changed.
     * 
     * <p>If the specified Context has an empty or null config
     * scope ID, will return a default SakaiSiteService instance.
     * 
     * @return a concrete SakaiLoginService for the specified
     *   Context's configuration scope
     */
    protected SakaiLoginService getLoginServiceDelegate(SakaiServiceContext context) 
    throws SakaiServiceConfigurationException {
        
        return (SakaiLoginService) getServiceDelegate(context, 
                getLoginServiceDelegates(), loginServiceFactoryInvoker);
    }
    
   
    /**
     * Retrieves a delegate service object. Responsible for determinig whether or
     * not a candidate object cached under the current "config scope ID" can 
     * satisfy the specified SakaiServiceContext. If not, a new delegate object
     * will be created and cached under that "config scope ID". As implemented,
     * the underlying SakaiServiceDelegateFactory is responsible for determining
     * when a new delegate object is required
     * 
     * @param context the current request context
     * @param delegateMap a map of cached service delegates
     * @param pointers abstracts details of invoking delegate type-specific
     *   methods on the current SakaiServiceDelegateFactory
     * @return
     */
    protected Object getServiceDelegate(SakaiServiceContext context, Map delegateMap, DelegateFactoryInvoker pointers) 
    throws SakaiServiceConfigurationException {
        
        String configScopeID = scrubConfigScopeID(context.getConfigScopeID());
        
        // trying to avoid two threads "simultaneously" discovering an
        // invalid service and generating duplicate replacement services
        synchronized (delegateMap) {
            
            return getServiceDelegateUnguarded(configScopeID, context, delegateMap, pointers);

        }
        
    }
    
    /**
     * Performs service delegate lookup and possible replacement without
     * any regard for thread safety issues.
     * 
     * <p>This method exists primarily for white-box concurrency testing purposes.
     * It is probably too coarse grained for less muscular lock management
     * strategies.
     * 
     * @param scrubbedConfigScopeID client must have already sanitized and/or defaulted
     *   the config scope ID
     * @param context the client execution context
     * @param delegateMap a map of existing service delegate objects, keyed by config
     *   scope id
     * @param pointers invocation mechanisms for SakaiServiceDelegateFactory methods
     * @return
     */
    protected Object getServiceDelegateUnguarded(String scrubbedConfigScopeID, 
            SakaiServiceContext context, Map delegateMap, DelegateFactoryInvoker pointers) 
    throws SakaiServiceConfigurationException {
        
        Object delegate = delegateMap.get(scrubbedConfigScopeID);

        if (delegate == null
                || !(pointers.isServiceValidForContext(delegate, context))) {

            delegate = pointers.aService(context);
            delegateMap.put(scrubbedConfigScopeID, delegate);

        }

        return delegate;
        
    }
    
    /**
     * Converts null, empty and whitespace config scope IDs to a default value,
     * otherwise returns a trimmed String.
     * 
     * 
     * @param configScopeID
     */
    protected String scrubConfigScopeID(String configScopeID) {
        
        if ( configScopeID == null ) {
            return DEFAULT_CONFIG_SCOPE_ID;
        }
        
        String trimmed = configScopeID.trim();
        
        return trimmed.length() == 0 ? DEFAULT_CONFIG_SCOPE_ID : configScopeID;
        
    }
    
    /**
     * Exposes internal SakaiLoginService Map to subclasses.
     * Lazily initializes the returned Map member.
     * 
     * @return
     */
    protected Map getLoginServiceDelegates() {
        if ( loginServiceDelegates == null ) {
            loginServiceDelegates = new HashMap();
        }
        return loginServiceDelegates;
    }
    
    /**
     * Exposes internal SakaiSiteService Map to subclasses.
     * Lazily initializes the returned Map member.
     * 
     * @return
     */
    protected Map getSiteServiceDelegates() {
        if ( siteServiceDelegates == null ) {
            siteServiceDelegates = new HashMap();
        }
        return siteServiceDelegates;
    }
    
    /**
     * Exposes cahced SakaiLoginService FactoryInvoker to package and subclasses
     * 
     * @return
     */
    protected DelegateFactoryInvoker getLoginServiceFactoryInvoker() {
        return loginServiceFactoryInvoker;
    }
    
    /**
     * Exposes cahced SakaiSiteService FactoryInvoker to package and subclasses
     * 
     * @return
     */
    protected DelegateFactoryInvoker getSiteServiceFactoryInvoker() {
        return siteServiceFactoryInvoker;
    }
    
    
    
    
    

}
