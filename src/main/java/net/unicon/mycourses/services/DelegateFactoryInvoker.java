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

/**
 * Subclasses define command object which will typically invoke methods on an 
 * implicitly provided SakaiServiceDelegateFactory.
 */
interface DelegateFactoryInvoker {
    
    /**
     * Retrieve a new service object. Use the specified Context to
     * determine the current configuration scope, e.g. portlet
     * instance.
     * 
     * @param context typically used to retrieve the current
     *   configuration scope
     * @return a service object
     */
    Object aService(SakaiServiceContext context);
    
    /**
     * Determine if the specified service is appropriate for the
     * specified call Context. For example, may compare the 
     * service's target host with a target host preference stored
     * in the Context.
     * 
     * @param service the service object to evaluate
     * @param context the context against which to evaluate the service
     * @return true if the service is valid for the specified context
     */
    boolean isServiceValidForContext(Object service, SakaiServiceContext context);
    
}