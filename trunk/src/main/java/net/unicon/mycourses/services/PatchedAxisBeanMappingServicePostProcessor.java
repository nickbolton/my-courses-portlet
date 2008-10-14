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

import java.util.Properties;

import org.springframework.remoting.jaxrpc.support.AxisBeanMappingServicePostProcessor;

public class PatchedAxisBeanMappingServicePostProcessor extends AxisBeanMappingServicePostProcessor {

    /**
     * Overriden due to bug in spring 2.0-rc2 -- ignores setBeanMappings(Properties)
     * if the beanMappings member is set to null. 
     * 
     * <p>Our override forces that map to initialize, then calls the super impl,
     * unless the specified beanMappingProps is null, in which
     * case we force the internal bean mapping map to null.
     * 
     * <p>see http://springframework.cvs.sourceforge.net/springframework/spring/src/org/springframework/remoting/jaxrpc/support/AxisBeanMappingServicePostProcessor.java?revision=1.1&view=markup
     */
    public void setBeanMappings(Properties beanMappingProps) {
        
        if ( beanMappingProps == null ) {
            setBeanClasses(null); // forces internal map back to null
            return;
        } else {
            setBeanClasses(new Class[0]);
        }
        
        super.setBeanMappings(beanMappingProps);
        
    }
    
    
}
