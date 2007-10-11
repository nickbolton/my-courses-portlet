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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import net.unicon.mycourses.services.SakaiServiceContext;
import net.unicon.mycourses.services.SakaiServiceContextImpl;

/**
 * A session-aware SakaiServiceContextFactory implementation. Also
 * implements logic for mapping portlet preferences into generated
 * SakaiServiceContexts and special handling for certain preferences.
 * 
 * @author dmccallum
 *
 */
public class SakaiServiceContextFactorySessionAwareImpl implements
        SakaiServiceContextFactory {

    public static final String DEFAULT_SAKAI_SERVICE_CONTEXTS_SESSION_KEY = 
        "net.unicon.mycourses.sessionkeys.SAKAI_SERVICE_CONTEXTS";
    public static final String DEFAULT_SAKAI_SERVICE_CONFIG_SCOPE_ID_PREFERENCE_KEY = 
        "net.unicon.mycourses.preferenceskeys.CONFIGURATION_SCOPE_ID";
    public static final String DEFAULT_SAKAI_PORTAL_SECRET_PREFERENCE_KEY = 
        "net.unicon.mycourses.preferenceskeys.SAKAI_PORTAL_SECRET";
    public static final String DEFAULT_USER_ID_KEY = 
        "net.unicon.mycourses.userinfokeys.USER_ID";
    public static final String DEFAULT_CONFIG_SCOPE_ID = 
        "SakaiServiceContextFactorySessionAwareImpl.DEFAULT_CONFIG_SCOPE_ID";
    
    private String contextsSessionKey = DEFAULT_SAKAI_SERVICE_CONTEXTS_SESSION_KEY;
    private String configScopeIDPreferenceKey = DEFAULT_SAKAI_SERVICE_CONFIG_SCOPE_ID_PREFERENCE_KEY;
    private String portalSecretPreferenceKey = DEFAULT_SAKAI_PORTAL_SECRET_PREFERENCE_KEY;
    /** used as a key into the USER_INFO Map returned from PortletRequest attributes */
    private String userIDKey = DEFAULT_USER_ID_KEY;
    
    public SakaiServiceContext contextFor(PortletRequest request) {
        
        PortletSession session = request.getPortletSession();
        Map contexts = (Map)session.getAttribute(contextsSessionKey);
        
        if ( contexts == null ) {
            contexts = new Hashtable(); // TODO: can we use HashMap safely in a PortletSession attribute?
            session.setAttribute(contextsSessionKey, contexts);
        }
        
        String currentConfigScopeID = 
            request.getPreferences().getValue(configScopeIDPreferenceKey, null);
        
        // apply default, if necessary
        currentConfigScopeID = scrubConfigScopeID(currentConfigScopeID);
        
        //TODO any concern about abandoned contexts?
        
        SakaiServiceContext context = (SakaiServiceContext)contexts.get(currentConfigScopeID);
        if ( context == null ) {
            context = new SakaiServiceContextImpl();
            // only assign config scope id to newly created Contexts
            context.setConfigScopeID(currentConfigScopeID);
            contexts.put(currentConfigScopeID, context);
        }
        
        configureContext(context, request);
        
        return context;
        
    }
    
    protected void configureContext(SakaiServiceContext context, PortletRequest request) {
        
        PortletPreferences prefs = request.getPreferences();
         
        // portal secret
        String secret = prefs.getValue(portalSecretPreferenceKey, null);
        context.setSecret(secret);
        
        // user id
        Map userInfo = (Map) request.getAttribute(PortletRequest.USER_INFO);
        
	String newUserID = userInfo == null ? request.getRemoteUser() : (String) userInfo.get(userIDKey);
	
        String oldUserID = context.getUserID();
        if ( !(newUserID == oldUserID) ) {
            
            if ( newUserID == null || !(newUserID.equals(oldUserID)) ) {
                context.setUserID(newUserID);
                context.setSakaiSessionID(null);
            }
            
        }
        
        // options
        
        // PortletPreferences stored as String[]'s but we want simple Strings
        Map newOptions = new HashMap();
        for ( Enumeration e = prefs.getNames(); e.hasMoreElements(); ) {
            String name = (String)e.nextElement();
            newOptions.put(name, prefs.getValue(name, null));
        }
        context.setOptions(newOptions);
        
    }
    
    /**
     * Converts null, empty and whitespace config scope IDs to a default value,
     * otherwise returns a trimmed String.
     * 
     * <p>Something nearly identical exists in 
     * {@link net.unicon.mycourses.services.SakaiServiceDelegatingImpl}.
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

    public String getContextsSessionKey() {
        return contextsSessionKey;
    }

    public void setContextsSessionKey(String contextsSessionKey) {
        this.contextsSessionKey = contextsSessionKey;
    }

    public String getConfigScopeIDPreferenceKey() {
        return configScopeIDPreferenceKey;
    }

    public void setConfigScopeIDPreferenceKey(String configScopeIDPreferenceKey) {
        this.configScopeIDPreferenceKey = configScopeIDPreferenceKey;
    }

    public String getPortalSecretPreferenceKey() {
        return portalSecretPreferenceKey;
    }

    public void setPortalSecretPreferenceKey(String portalSecretPreferenceKey) {
        this.portalSecretPreferenceKey = portalSecretPreferenceKey;
    }

    public String getUserIDKey() {
        return userIDKey;
    }

    public void setUserIDKey(String userIDKey) {
        this.userIDKey = userIDKey;
    }

}
