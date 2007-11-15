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

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.ReadOnlyException;

import junit.framework.TestCase;
import net.unicon.mycourses.services.SakaiServiceContext;
import net.unicon.mycourses.services.SakaiServiceContextImpl;

public class SakaiServiceContextFactorySessionAwareImplTest extends TestCase {

    private MockSakaiServiceContextFactorySessionAwareImpl factory;
    private MockPortletRequest portletRqst;
    private Map userInfo;
    private String configScopeID = "some-config-scope-id";
    private String portalSecret = "portal-secret";
    private String userID = "user-id";
    private PortletPreferences prefs;
    
    protected void setUp() throws Exception {
        super.setUp();
        factory = new MockSakaiServiceContextFactorySessionAwareImpl();
        portletRqst = new MockPortletRequest();
        userInfo = new HashMap();
        userInfo.put(factory.getUserIDKey(), userID);
        portletRqst.setAttribute(PortletRequest.USER_INFO, userInfo);
        prefs = portletRqst.getPreferences();
        prefs.setValue(factory.getConfigScopeIDPreferenceKey(), configScopeID);
        prefs.setValue(factory.getPortalSecretPreferenceKey(), portalSecret);
    }
    
    /**
     * Verifies that the Factory returns the same SakaiServiceContext
     * stored in the current PortletSession, if any.
     * 
     * Note that there may be multiple SakaiServiceContexts per
     * PortletSession, even within the same PortletSession.PORTLET_SCOPE.
     * These SakaiServiceContexts are stored in a Map keyed by a
     * "configuration scope ID". 
     *
     */
    public void testContextFor_ReusesContextInSession() throws ReadOnlyException {
        
        Map contexts = new HashMap();
        SakaiServiceContext ctx = new SakaiServiceContextImpl();
        contexts.put(configScopeID, ctx);
        PortletSession session = portletRqst.getPortletSession();
        session.setAttribute(factory.getContextsSessionKey(), contexts, PortletSession.PORTLET_SCOPE);
        assertSame(ctx, factory.contextFor(portletRqst));
        
    }
    
    /**
     * Verifies that the Factory does not search into the application-scoped
     * PortletSession.
     */
    public void testContextFor_SearchesForContextInPortletScopedSession() throws ReadOnlyException {
        
        
        Map contexts = new HashMap();
        SakaiServiceContext ctx = new SakaiServiceContextImpl();
        contexts.put(configScopeID, ctx);
        PortletSession session = portletRqst.getPortletSession();
        //      place context in the "wrong" scope
        session.setAttribute(factory.getContextsSessionKey(), contexts, PortletSession.APPLICATION_SCOPE);
        assertNotSame(ctx, factory.contextFor(portletRqst));
        
    }
    
    /**
     * Verifies that the Factory lazily creates a Map for SakaiServiceContexts
     * and places that Map in the PortletSession.
     *
     */
    public void testContextFor_LazilyInitializesSessionContextMap() {
        
        PortletSession session = portletRqst.getPortletSession();
        // ensure the fixture isn't interfering
        assertNull(session.getAttribute(factory.getContextsSessionKey()));
        factory.contextFor(portletRqst);
        assertNotNull(session.getAttribute(factory.getContextsSessionKey()));
        
    }
    
    
    /**
     * Verifies that the Factory lazily creates a new SakaiServiceContext
     * and places it in the PortletSession's Map of the same.
     *
     */
    public void testContextFor_CachesNewContextInSession() {
        
        PortletSession session = portletRqst.getPortletSession();
        //      ensure the fixture isn't interfering
        assertNull(session.getAttribute(factory.getContextsSessionKey()));
        SakaiServiceContext factoryReturnedContext = factory.contextFor(portletRqst);
        Map contextsMap = 
            (Map)session.getAttribute(factory.getContextsSessionKey());
        assertEquals(1, contextsMap.size());
        SakaiServiceContext cachedContext = (SakaiServiceContext) contextsMap.get(configScopeID);
        assertNotNull(cachedContext);
        assertSame(factoryReturnedContext, cachedContext);
        
    }
    
    /**
     * Verifies that the factory passes the SakaiServiceContext
     * returned from contextFor() to configureContext() before
     * it is returned. Also ensures the PortletRequest is passed
     * to configureContext().
     * 
     * <p>This enables us to test configureContext() directly.
     *
     */
    public void testContextFor_PassesContextAndRequestToConfigurator() {
        
      SakaiServiceContext context = factory.contextFor(portletRqst);
      assertSame(context, factory.getLastConfigureContextCall_ContextArg());
      assertSame(portletRqst, factory.getLastConfigureContextCall_RequestArg());
      
    }
    
    /**
     * Sometimes a configuration scope identifier may not be available
     * in the PortletPreferences when the Factory is invoked. In these
     * cases the Factory should fall back on a default configuration scope.
     *
     */
    public void testContextFor_AssignsDefaultConfigScopeIDToContext() throws ReadOnlyException {
        
        prefs.setValue(factory.getConfigScopeIDPreferenceKey(), null);
        SakaiServiceContext context = factory.contextFor(portletRqst);
        assertEquals(factory.DEFAULT_CONFIG_SCOPE_ID, context.getConfigScopeID());
        
    }
    
    /**
     * Sometimes a configuration scope identifier may not be available
     * in the PortletPreferences when the Factory is invoked. In these
     * cases the Factory should fall back on a default configuration scope.
     *
     */
    public void testContextFor_CachesContextUnderDefaultConfigScopeID() throws ReadOnlyException {
        
        prefs.setValue(factory.getConfigScopeIDPreferenceKey(), null);
        SakaiServiceContext contextReturnedFromFactory = factory.contextFor(portletRqst);
        Map contextsInSession = 
            (Map)portletRqst.getPortletSession().getAttribute(factory.getContextsSessionKey());
        SakaiServiceContext contextInSession = 
            (SakaiServiceContext)contextsInSession.get(factory.DEFAULT_CONFIG_SCOPE_ID);
        assertEquals(contextReturnedFromFactory, contextInSession);
        
    }
    
    /**
     * Verifies that the Factory correctly assigns the config scope ID to a
     * lazily created SakaiServiceContext.
     *
     */
    public void testContextFor_AssignsConfigScopeIDToContext() {
        
        SakaiServiceContext context = factory.contextFor(portletRqst);
        assertEquals(configScopeID, context.getConfigScopeID());
       
    }
    
    /**
     * Verifies that the Factory correctly assigns the portal secret to a
     * lazily created SakaiServiceContext.
     *
     */
    public void testConfigureContext_AssignsPortalSecretToContext() {
        
        SakaiServiceContextImpl context = new SakaiServiceContextImpl();
        factory.configureContext(context, portletRqst);
        assertEquals(portalSecret, context.getSecret());
       
    }
    
    /**
     * Verifies that the Factory correctly assigns the portal secret to an
     * existing SakaiServiceContext.
     *
     */
    public void testConfigureContext_OverwritesExistingContextPortalSecret() {
        
        SakaiServiceContextImpl context = new SakaiServiceContextImpl();
        context.setSecret("some-random-value-to-be-overwritten");
        factory.configureContext(context, portletRqst);
        assertEquals(portalSecret, context.getSecret());
          
    }
    
    /**
     * Verifies that configureContext() retrieves the userID from
     * PortletRequest attributes and assigns it to the configured
     * SakaiServiceContext.
     *
     */
    public void testConfigureContext_AssignsUserIDToContext() {
        SakaiServiceContextImpl context = new SakaiServiceContextImpl();
        factory.configureContext(context, portletRqst);
        assertEquals(userID, context.getUserID());
    }
    
    /**
     * A Sakai session cannot possibly be valid for a given SakaiServiceContext
     * if the associated userID changes. This verifies that a userID change
     * wipes out the current Sakai session ID.
     */
    public void testConfigureContext_ClearsSakaiSessionIDIfUserIDChanges() {
        SakaiServiceContextImpl context = new SakaiServiceContextImpl();
        context.setUserID("some-user-id-value-to-be-cleared");
        context.setSakaiSessionID("some-session-id-value-to-be-cleared");
        factory.configureContext(context, portletRqst);
        assertEquals(userID, context.getUserID());
        assertNull(context.getSakaiSessionID());
    }
    
    /**
     * Verifies that received PortletPreferences completely replace existing
     * SakaiServiceContext options.
     *
     */
    public void testConfigureContext_ReceivedPreferencesObliterateExistingContextOptions() {
        SakaiServiceContextImpl context = new SakaiServiceContextImpl();
        Map options = new HashMap();
        options.put("some-random-key1", "some-random-value1");
        options.put("some-random-key2", "some-random-value2");
        context.setOptions(options);
        factory.configureContext(context, portletRqst);
        assertNull(context.getOption("some-random-key1"));
        assertNull(context.getOption("some-random-key2"));
    }
    
    public void testConfigureContext_StringArrayPreferencesConvertedToSimpleStrings() 
    throws ReadOnlyException {
        SakaiServiceContextImpl context = new SakaiServiceContextImpl();
        prefs.setValues("pref1", new String[0]);
        prefs.setValues("pref2", new String[] { "string1" });
        factory.configureContext(context, portletRqst);
        
        factory.configureContext(context, portletRqst);
        
        assertNull(null, context.getOption("pref1"));
        assertEquals("string1", context.getOption("pref2"));
    }
    

    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
