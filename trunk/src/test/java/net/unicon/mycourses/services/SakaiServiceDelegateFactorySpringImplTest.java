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

import java.net.MalformedURLException;

import junit.framework.TestCase;
import net.unicon.sakai.portalservices.api.SakaiLoginService;
import net.unicon.sakai.portalservices.api.SakaiSiteService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SakaiServiceDelegateFactorySpringImplTest extends TestCase {

    private SakaiServiceDelegateFactorySpringImpl factory;
    private SakaiServiceDelegateFactorySpringImpl factoryWithAppContext;
    private SakaiServiceContext userContext1;
    private SakaiServiceContext userContext2;
    private ApplicationContext appContext;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        
        factory = new SakaiServiceDelegateFactorySpringImpl() {
            
            protected Object newServiceDelegate(String beanID, String wsdlLocation) {
                if (SITE_SERVICE_BEAN_ID.equals(beanID)) {
                    return new MockSakaiSiteService();
                } else if (LOGIN_SERVICE_BEAN_ID.equals(beanID)) {
                    return new MockSakaiLoginService();
                } else {
                    throw new IllegalArgumentException("unrecognized bean id [" + beanID + "]");
                }
            }
        };
        
        // we'll use a real app context to try to get a sense of the overhead involved
        String appContextLocation = "jaxRpcApplicationContext.xml"; //TODO a better name?
        //TODO should we be testing against our real app context config?
        appContext = new ClassPathXmlApplicationContext(appContextLocation);
        factoryWithAppContext = new SakaiServiceDelegateFactorySpringImpl();
        factoryWithAppContext.setApplicationContext(appContext);
        
        String userID1 = "sakaiuserID1";
        String configScopeID1 = "configScopeID1";
        String siteServiceWsdlLocation1 = "siteServiceWsdlLocation1";
        String loginServiceWsdlLocation1 = "loginServiceWsdlLocation1";
        String userID2 = "sakaiuserID2";
        String configScopeID2 = "configScopeID2";
        String siteServiceWsdlLocation2 = "siteServiceWsdlLocation2";
        String loginServiceWsdlLocation2 = "loginServiceWsdlLocation2";
        
        userContext1 = new SakaiServiceContextImpl();
        userContext1.setUserID(userID1);
        userContext1.setConfigScopeID(configScopeID1);
        userContext1.setOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION, 
                siteServiceWsdlLocation1);
        userContext1.setOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION, 
                loginServiceWsdlLocation1);
        
        userContext2 = new SakaiServiceContextImpl();
        userContext2.setUserID(userID2);
        userContext2.setConfigScopeID(configScopeID2);
        userContext2.setOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION, 
                siteServiceWsdlLocation2);
        userContext2.setOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION, 
                loginServiceWsdlLocation2);
    }
     
    
    public void testIsSiteServiceValidForContext_InvalidOnWSDLChange() {
        
        //TODO reduce dupl with testIsLoginServiceValidForContext_InvalidOnWSDLChange
        ConfiguredSakaiSiteService service = new ConfiguredSakaiSiteService();
        service.setDelegateWsdlLocation(userContext1.
                getOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION));
        service.setDelegateSiteService(new MockSakaiSiteService());
        
        assertFalse(factory.isSiteServiceValidForContext(service, userContext2));
        
    }
    
    
    public void testIsSiteServiceValidForContext_ValidOnNoWSDLChange() {
        
        //TODO reduce dupl with testIsLoginServiceValidForContext_ValidOnNoWSDLChange
        ConfiguredSakaiSiteService service = new ConfiguredSakaiSiteService();
        service.setDelegateWsdlLocation(userContext1.
                getOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION));
        service.setDelegateSiteService(new MockSakaiSiteService());
        
        //ensure distinct userContext's can be considered valid for the same service delegate 
        userContext2.setOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION, 
                userContext1.getOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION));
        
        assertTrue(factory.isSiteServiceValidForContext(service, userContext2));
        
    }
    
    /**
     * Verifies that if no WSDL location has been specified in the
     * SakaiServiceContext, the SakaiSiteService will be considered valid
     * only if it is also not associated with a WSDL location, i.e. has
     * been configured using a default value invisible to us.
     *
     */
    public void testIsSiteServiceValidForContext_NullWSDL() {
        
        // TODO reduce dupl with testIsLoginServiceValidForContext_NullWSDL()
        ConfiguredSakaiSiteService service = new ConfiguredSakaiSiteService();
        service.setDelegateWsdlLocation(userContext1.getOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION));
        service.setDelegateSiteService(new MockSakaiSiteService());
        
        //ensure distinct userContext's can be considered valid for the same service delegate 
        userContext2.setOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION, null);
        
        assertTrue(factory.isSiteServiceValidForContext(service, userContext2));
    }
    
    /**
     * Verify that if a service has been created using a user-specified
     * WSDL location, and we receive a subsequent request for a null
     * WSDL location, the original service will be invalidated. This mimics a situation
     * where a client wishes to revert to an unknown default WSDL location.
     *
     */
    public void testIsSiteServiceValidForContext_InvalidOnRevertToNullWSDL() {
        ConfiguredSakaiSiteService service = new ConfiguredSakaiSiteService();
        service.setDelegateWsdlLocation(userContext1.getOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION));
        service.setDelegateSiteService(new MockSakaiSiteService());
        
        //ensure distinct userContext's can be considered valid for the same service delegate 
        userContext2.setOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION, null);
        
        assertTrue(factory.isSiteServiceValidForContext(service, userContext2));
    }
    
    /**
     * Verifies that the ConfiguredSakaiSiteService returned from aSiteService()
     * contains a null WSDL location if the specified SakaiServiceContext contained 
     * a null WSDL location
     *
     */
    public void testASiteService_RetainsNullWSDL() {
        userContext1.setOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION, null);
        ConfiguredSakaiSiteService service = 
            (ConfiguredSakaiSiteService) factory.aSiteService(userContext1);
        assertNull(service.getDelegateWsdlLocation());
    }
    
    /**
     * Verifies that the ConfiguredSakaiLoginService returned from aLoginService()
     * contains the same WSDL location as specified by the SakaiServiceContext
     *
     */
    public void testASiteService_RetainsWSDL() {
        ConfiguredSakaiSiteService service = 
            (ConfiguredSakaiSiteService) factory.aSiteService(userContext1);
        assertEquals(userContext1.getOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION), 
                service.getDelegateWsdlLocation());
    }
    
    
    public void testIsLoginServiceValidForContext_InvalidOnWSDLChange() {
        
        //TODO reduce dupl with testALoginService_ReturnsNewInstanceOnWSDLChange
        ConfiguredSakaiLoginService service = new ConfiguredSakaiLoginService();
        service.setDelegateWsdlLocation(userContext1.
                getOption(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_WSDL_LOCATION));
        service.setDelegateLoginService(new MockSakaiLoginService());
        
        assertFalse(factory.isLoginServiceValidForContext(service, userContext2));
        
        
    }
    
    
    
    public void testIsLoginServiceValidForContext_ValidOnNoWSDLChange() {
        
        //TODO reduce dupl with testIsSiteServiceValidForContext_ValidOnNoWSDLChange
        ConfiguredSakaiLoginService service = new ConfiguredSakaiLoginService();
        service.setDelegateWsdlLocation(userContext1.
                getOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION));
        service.setDelegateLoginService(new MockSakaiLoginService());
        
        //ensure distinct userContext's can be considered valid for the same service delegate 
        userContext2.setOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION, 
                userContext1.getOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION));
        
        assertTrue(factory.isLoginServiceValidForContext(service, userContext2));
        
    }
    
    /**
     * Verifies that if no WSDL location has been specified in the
     * SakaiServiceContext, the SakaiLoginService will be considered valid
     * only if it is also not associated with a WSDL location, i.e. has
     * been configured using a default value invisible to us.
     *
     */
    public void testIsLoginServiceValidForContext_NullWSDL() {
        ConfiguredSakaiLoginService service = new ConfiguredSakaiLoginService();
        service.setDelegateWsdlLocation(null); // just to be sure
        service.setDelegateLoginService(new MockSakaiLoginService());
        
        //ensure distinct userContext's can be considered valid for the same service delegate 
        userContext1.setOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION, null);
        
        assertTrue(factory.isLoginServiceValidForContext(service, userContext1));
    }
    
    /**
     * Verify that if a service has been created using a user-specified
     * WSDL location, and we receive a subsequent request for a null
     * WSDL location, the original service will be invalidated. This mimics a situation
     * where a client wishes to revert to an unknown default WSDL location.
     *
     */
    public void testIsLoginServiceValidForContext_InvalidOnRevertToNullWSDL() {
        ConfiguredSakaiLoginService service = new ConfiguredSakaiLoginService();
        service.setDelegateWsdlLocation(userContext1.getOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION));
        service.setDelegateLoginService(new MockSakaiLoginService());
        
        //ensure distinct userContext's can be considered valid for the same service delegate 
        userContext2.setOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION, null);
        
        assertTrue(factory.isLoginServiceValidForContext(service, userContext2));
    }
    
    /**
     * Verifies that the ConfiguredSakaiLoginService returned from aLoginService()
     * contains a null WSDL location if the specified SakaiServiceContext contained 
     * a null WSDL location
     *
     */
    public void testALoginService_RetainsNullWSDL() {
        userContext1.setOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION, null);
        ConfiguredSakaiLoginService service = 
            (ConfiguredSakaiLoginService) factory.aLoginService(userContext1);
        assertNull(service.getDelegateWsdlLocation());
    }
    
    /**
     * Verifies that the ConfiguredSakaiLoginService returned from aLoginService()
     * contains the same WSDL location as specified by the SakaiServiceContext
     *
     */
    public void testALoginService_RetainsWSDL() {
        ConfiguredSakaiLoginService service = 
            (ConfiguredSakaiLoginService) factory.aLoginService(userContext1);
        assertEquals(userContext1.getOption(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_WSDL_LOCATION), 
                service.getDelegateWsdlLocation());
    }
    
    /**
     * Verifies that the ConfiguredSakaiLoginService returned from aLoginService()
     * actually contains a delegate service.
     *
     */
    public void testALoginService_CachesDelegateService() {
        ConfiguredSakaiLoginService service = 
            (ConfiguredSakaiLoginService) factory.aLoginService(userContext1);
        assertNotNull(service.getDelegateLoginService());
    }
    
    public void testALogin_RaisesConfigurationExceptions() {
        factory = new SakaiServiceDelegateFactorySpringImpl() {
            
            protected Object newServiceDelegate(String beanID, String wsdlLocation) {
                throw new SakaiServiceConfigurationException();
            }
        };
        try {
          factory.aLoginService(userContext1);
          fail("expected a SakaiServiceConfigurationException");
        } catch ( SakaiServiceConfigurationException e ) {
            // success
        }
    }
    
    public void testNewServiceDelegate_ReturnsSakaiSiteServiceFromSpring() throws MalformedURLException {
        Object delegate = 
            factoryWithAppContext.newServiceDelegate(SakaiServiceDelegateFactorySpringImpl.SITE_SERVICE_BEAN_ID, 
                    null);
        // just need to know if the delegate proxy is castable to a SakaiSiteService
        SakaiSiteService narrowedDelegate = (SakaiSiteService)delegate;
        
    }
    
    public void testNewServiceDelegate_ReturnsSakaiLoginServiceFromSpring() throws MalformedURLException {
        Object delegate = 
            factoryWithAppContext.newServiceDelegate(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_BEAN_ID , 
                    null);
        
        // just need to know if the delegate proxy is castable to a SakaiLoginService
        SakaiLoginService narrowedDelegate = (SakaiLoginService)delegate;
        
    }
    
    public void testALoginService_RaisesConfigurationExceptions() {
        factory = new SakaiServiceDelegateFactorySpringImpl() {
            
            protected Object newServiceDelegate(String beanID, String wsdlLocation) {
                throw new SakaiServiceConfigurationException();
            }
        };
        try {
          factory.aLoginService(userContext1);
          fail("expected a SakaiServiceConfigurationException");
        } catch ( SakaiServiceConfigurationException e ) {
            // success
        }
    }
    
    
    public void testASiteService_RaisesConfigurationExceptions() {
        factory = new SakaiServiceDelegateFactorySpringImpl() {
            
            protected Object newServiceDelegate(String beanID, String wsdlLocation) {
                throw new SakaiServiceConfigurationException();
            }
        };
        try {
          factory.aSiteService(userContext1);
          fail("expected a SakaiServiceConfigurationException");
        } catch ( SakaiServiceConfigurationException e ) {
            // success
        }
    }
    
    /**
     * Verifies that the ConfiguredSakaiLoginService returned from aLoginService()
     * actually contains a delegate service.
     *
     */
    public void testASiteService_CachesDelegateService() {
        ConfiguredSakaiSiteService service = 
            (ConfiguredSakaiSiteService) factory.aSiteService(userContext1);
        assertNotNull(service.getDelegateSiteService());
    }
    
    public void testNewServiceDelegate_WrapsSpringResourceExceptions() {
        try {
          factoryWithAppContext.newServiceDelegate(SakaiServiceDelegateFactorySpringImpl.LOGIN_SERVICE_BEAN_ID, 
                "no-way-this-exists");
          fail("expected a SakaiServiceConfigurationException");
        } catch ( SakaiServiceConfigurationException e ) {
            // we don't really care what it's wrapping
        }
    }
    
    
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
}
