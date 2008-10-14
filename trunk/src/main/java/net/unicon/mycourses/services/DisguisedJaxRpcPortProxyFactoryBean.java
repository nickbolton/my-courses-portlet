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

import javax.xml.rpc.ServiceException;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.remoting.jaxrpc.JaxRpcPortClientInterceptor;

/**
 * "Disguised" because it does the same thing as JaxRpcPortProxyFactoryBean
 * but "sneaks" past the BeanFactory so the client can control when the
 * service proxy is created. getObject() has been changed to getServiceProxy().
 * 
 * <p>Typical usage defines this bean as a prototype with configuration that
 * may change following retrieval from the BeanFactory.
 * 
 * @author dmccallum
 *
 */
public class DisguisedJaxRpcPortProxyFactoryBean extends
        JaxRpcPortClientInterceptor {

    private Object serviceProxy;

    public void afterPropertiesSet() {
        if (getServiceInterface() == null) {
            // Use JAX-RPC port interface (a traditional RMI interface)
            // as service interface if none explicitly specified.
            if (getPortInterface() != null) {
                setServiceInterface(getPortInterface());
            } else {
                throw new IllegalArgumentException(
                        "serviceInterface is required");
            }
        }
        super.afterPropertiesSet();
        this.serviceProxy = ProxyFactory.getProxy(getServiceInterface(), this);
    }

    public Object getServiceProxy() {
        return this.serviceProxy;
    }


}
