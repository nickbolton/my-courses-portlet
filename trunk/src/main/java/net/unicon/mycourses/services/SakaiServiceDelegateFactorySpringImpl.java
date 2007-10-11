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

import net.unicon.sakai.portalservices.api.SakaiLoginService;
import net.unicon.sakai.portalservices.api.SakaiSiteService;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Retrieves Sakai services from Spring. Reacts to runtime changes
 * in service WSDL locations by retrieving new instances from
 * Spring. As such, be sure beans ID'd by
 * {@link net.unicon.sakai.portalservices.SakaiSiteService} and
 * {@link net.unicon.sakai.portlservices.SakaiLoginService} are tagged as
 * prototypes.
 * 
 * @author dmccallum
 *
 */
public class SakaiServiceDelegateFactorySpringImpl implements
        SakaiServiceDelegateFactory, ApplicationContextAware {

    public static final String SITE_SERVICE_WSDL_LOCATION = 
        "net.unicon.mycourses.preferenceskeys.SITE_SERVICE_WSDL_LOCATION";
    
    public static final String LOGIN_SERVICE_WSDL_LOCATION = 
        "net.unicon.mycourses.preferenceskeys.LOGIN_SERVICE_WSDL_LOCATION";
    
    public static final String SITE_SERVICE_BEAN_ID = 
        "net.unicon.sakai.portalservices.api.SakaiSiteService";
    
    public static final String LOGIN_SERVICE_BEAN_ID = 
        "net.unicon.sakai.portalservices.api.SakaiLoginService";
    
    private String siteServiceWsdlLocationKey = SITE_SERVICE_WSDL_LOCATION;
    private String loginServiceWsdlLocationKey = LOGIN_SERVICE_WSDL_LOCATION;
    private ApplicationContext springContext;
    
    /**
     * {@inheritDoc}
     */
    public SakaiSiteService aSiteService(SakaiServiceContext context) 
    throws SakaiServiceConfigurationException {
        
        //TODO reduce dupl with aLoginService
        
        String wsdlLocation = context.getOption(siteServiceWsdlLocationKey);
           
        SakaiSiteService delegate = 
            (SakaiSiteService) newServiceDelegate(SITE_SERVICE_BEAN_ID,wsdlLocation);
        
        ConfiguredSakaiSiteService wrapper = new ConfiguredSakaiSiteService();
        wrapper.setDelegateWsdlLocation(wsdlLocation);
        wrapper.setDelegateSiteService(delegate);
        return wrapper;
        
    }
    
    /**
     * {@inheritDoc}
     */
    public SakaiLoginService aLoginService(SakaiServiceContext context) 
    throws SakaiServiceConfigurationException {
        
        //TODO reduce dupl with aSiteService
        
        String wsdlLocation = context.getOption(loginServiceWsdlLocationKey);
        
        SakaiLoginService delegate = 
            (SakaiLoginService)newServiceDelegate(LOGIN_SERVICE_BEAN_ID,wsdlLocation);
        
        ConfiguredSakaiLoginService wrapper = new ConfiguredSakaiLoginService();
        wrapper.setDelegateWsdlLocation(wsdlLocation);
        wrapper.setDelegateLoginService(delegate);
        return wrapper;
        
    }
    
    

    
    /** 
     * Tests if the given Context specifies the same SakaiSiteService WSDL 
     * location as currently associated with this Wrapper. Matches null
     * values. This allows us to implement default WSDL locations defined
     * in Spring config files.
     * 
     * <p>Returns false if the specified SakaiSiteService is not an
     * instance of ConfiguredSakaiSiteService since we cannot know
     * the targeted WSDL otherwise.
     * 
     * @param siteService must be a ConfiguredSakaiSiteService
     * @param context a SakaiServiceContext possibly containing a 
     *   new WSDL location for a SakaiSiteService
     * @return true if the Context and this Wrapper specify the same
     *   SakaiSiteService WSDL location or if the Context does not
     *   specify a WSDL location.
     */
    public boolean isSiteServiceValidForContext(SakaiSiteService siteService,
            SakaiServiceContext context) {
        
        if ( !(siteService instanceof ConfiguredSakaiSiteService) ) {
            return false;
        }
        
        String rqstWsdlLocation = context.getOption(siteServiceWsdlLocationKey);
        if (rqstWsdlLocation == null) {
            return true;
        }
        
        String delegateWsdlLocation = 
            ((ConfiguredSakaiSiteService)siteService).getDelegateWsdlLocation();
        
        return rqstWsdlLocation.equals(delegateWsdlLocation);
        
    }

    /** 
     * Tests if the given Context specifies the same SakaiLoginService WSDL 
     * location as currently associated with this Wrapper. Matches null
     * values. This allows us to implement default WSDL locations defined
     * in Spring config files.
     * 
     * <p>Returns false if the specified SakaiLoginService is not an
     * instance of ConfiguredSakaiLoginService since we cannot know
     * the targeted WSDL otherwise.
     * 
     * @param loginService must be a ConfiguredSakaiLoginService
     * @param context a SakaiServiceContext possibly containing a 
     *   new WSDL location for a SakaiLoginService
     * @return true if the Context and this Wrapper specify the same
     *   SakaiLoginService WSDL location or if the Context does not
     *   specify a WSDL location.
     */
    public boolean isLoginServiceValidForContext(SakaiLoginService loginService,
            SakaiServiceContext context) {
        
        if ( !(loginService instanceof ConfiguredSakaiLoginService) ) {
            return false;
        }
        
        String rqstWsdlLocation = context.getOption(loginServiceWsdlLocationKey);
        if (rqstWsdlLocation == null) {
            return true;
        }
        
        String delegateWsdlLocation = 
            ((ConfiguredSakaiLoginService)loginService).getDelegateWsdlLocation();
        
        return rqstWsdlLocation.equals(delegateWsdlLocation);
        
    }
    
    /**
     * Assign the key used to retrieve the SakaiSiteService's WSDL
     * location from a given SakaiServiceContext.
     * 
     * @param siteServiceWsdlOptionKey
     */
    public void setSiteServiceWsdlOptionKey(String siteServiceWsdlOptionKey) {
        this.siteServiceWsdlLocationKey = siteServiceWsdlOptionKey;
    }
    
    /**
     * Assign the key used to retrieve the SakaiSiteService's WSDL
     * location from a given SakaiServiceContext.
     * 
     * @param loginServiceWsdlOptionKey
     */
    public void setLoginServiceWsdlOptionKey(String loginServiceWsdlOptionKey) {
        this.loginServiceWsdlLocationKey = loginServiceWsdlOptionKey;
    }
    
    /**
     * {@inheritDoc}
     */
    public void setApplicationContext(ApplicationContext applicationContext) 
        throws BeansException {
        this.springContext = applicationContext;
    }
    
    /**
     * Create a service delegate object using the specified bean ID.
     * To function properly, the specified bean ID must reference
     * a prototype DisguisedJaxRpcPortProxyFactoryBean.
     * 
     * @return a service delegate
     * @param beanID a Spring bean identifier
     * @param wsdlLocation assigned to the Spring-managed JAX-RPC
     *   mechanism. ignored if null.
     */
    protected Object newServiceDelegate(String beanID, String wsdlLocation) 
      throws SakaiServiceConfigurationException {
        
        try {
            DisguisedJaxRpcPortProxyFactoryBean jaxrpc = (DisguisedJaxRpcPortProxyFactoryBean) springContext
                    .getBean(beanID);

            if (wsdlLocation != null) {

                jaxrpc.setWsdlDocumentUrl(springContext.getResource(
                        wsdlLocation).getURL());

            }

            return jaxrpc.getServiceProxy();
        } catch (Exception e) {
            throw new SakaiServiceConfigurationException("Failed to reconfigure SakaiService delegate [beanID: " +
                    beanID + "][wsdlLocation: " + wsdlLocation + "]", e); // TODO an error code?
                                                            
        }
        
    }

}
