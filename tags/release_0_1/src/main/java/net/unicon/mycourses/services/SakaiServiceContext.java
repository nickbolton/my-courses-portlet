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

import java.util.Map;

/**
 * Keeps state for SakaiService implementations. 
 * 
 * <p>An instance should correspond to an underlying Sakai session
 * and should typically be released on user logout. 
 * 
 * <p>In addition to maintaining user login state, it also
 * provides clients with access to runtime-configurable
 * preferences. These preferences are declared statically
 * at this time as an attempt to avoid completely ad-hoc and
 * usage such as what you might find in Sakai Velocity context maps.
 * 
 * @author dmccallum
 *
 */
public interface SakaiServiceContext {
         
    /**
     * Name-value pairs representing current runtime configuration options.
     * 
     * @return a String-to-Object map
     */
    public Map getOptions();
    
    /**
     * Assign a runtime configuration. Completely overwrites
     * any existing map.
     * 
     * @param options a String-to-String map
     */
    public void setOptions(Map options);
    
    /**
     * Assign a runtime configuration option, silently overwriting
     * any prexisting mapping.
     * 
     * @param name the option's name. nulls not allowed
     * @param value the option's value. nulls allowed
     */
    public void setOption(String name, String value);
    
    /**
     * Retrieve a runtime configuration option.
     * 
     * @param name the option's name
     * @return the option's value, or null
     */
    public String getOption(String name);
    
    /**
     * Tests if this Context is associated with a Sakai session.
     * If this returns true, getSakaiSession() should return null
     * and vice-versa.
     * 
     * @return false unless this Context is associated with a
     *   Sakai session.
     */
    public boolean hasSakaiSession();
    
    /**
     * Retrieve the Sakai session ID, if any, associated with this
     * Context. Null return value indicates no session-context
     * binding.
     * 
     * @return Sakai-native session ID or null if no session associated
     *   with this context\
     */
    public String getSakaiSessionID();
    
    /**
     * Set the Sakai session ID for this context. Use
     * nulls to clear current session ID, indicating logout.
     * 
     * @param sakaiSessionID a Sakai Session identifier
     */
    public void setSakaiSessionID(String sakaiSessionID);
    
    /**
     * Return the token used to authenticate the current user
     * against Sakai.
     * 
     * @return an authentication token
     */
    public String getSecret();
    
    /**
     * Assign the token used to authenticate the current user
     * against Sakai.
     * 
     * @param secret
     */
    public void setSecret(String secret);
    
    /**
     * Return the Sakai user ID associated with this Context.
     * 
     * @return the current user ID
     */
    public String getUserID();
    
    /**
     * Assign a Sakai user ID to this Context.
     * 
     * @param userID a Sakai user ID
     */
    public void setUserID(String userID);
    
    /**
     * Return the configuration scope identifier associated with this
     * Context. Configuration scope sematics may depend on the 
     * deployment context, but is generally used to isolate
     * heavy-weight resource configuration within client-specific
     * contextual boundaries. For example, web service client-side
     * proxies might be cached on a per-Channel basis in a uPortal
     * environment, even though those Channels all delegate to a
     * single Portlet instance.
     * 
     * @return
     */
    public String getConfigScopeID();
    
    /**
     * Assign the configuration scope identifier associated with this
     * Context.
     * 
     * @see #getConfigScopeID()
     * @param configScopeID
     */
    public void setConfigScopeID(String configScopeID);;
    

}
