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

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ServiceException;

import junit.framework.TestCase;
import net.unicon.sakai.portalservices.api.Site;
import net.unicon.sakai.portalservices.api.SiteQueryResult;
import net.unicon.sakai.portalservices.api.Term;

import org.apache.axis.ConfigurationException;
import org.apache.axis.Message;
import org.apache.axis.MessageContext;
import org.apache.axis.client.AxisClient;
import org.apache.axis.configuration.BasicClientConfig;
import org.apache.axis.handlers.BasicHandler;

public class PatchedAxisBeanMappingServicePostProcessorTest extends TestCase {
    
    private PatchedAxisBeanMappingServicePostProcessor servicePostProcessor;
    
    protected void setUp() throws Exception {
        super.setUp();
        servicePostProcessor = new PatchedAxisBeanMappingServicePostProcessor();
    }
    
    
    
    /**
     * Verifies response deserialization.
     * 
     * TODO Should probably be refactored. It's a long test w/ lots of
     * fixture setup, and the mock Axis configuration will be useful
     * elsewhere.
     * 
     * @throws ConfigurationException
     * @throws ServiceException
     * @throws RemoteException
     */
    public void testResponseDeserialization() 
        throws ConfigurationException, ServiceException, RemoteException, ClassNotFoundException {
        
        String nameSpace = "http://sakai.unicon.net/sakai-portal-services/SakaiSiteService";
        String encodingStyle = "http://schemas.xmlsoap.org/soap/encoding/";
        
        Properties beanMappings = new Properties();
        beanMappings.put(Site.class.getName(), "Site");
        beanMappings.put(Term.class.getName(), "Term");
        beanMappings.put(SiteQueryResult.class.getName(), "SiteQueryResult");
        beanMappings.put(Site[].class.getName(), "ArrayOfSite");
        
        servicePostProcessor.setEncodingStyleUri(encodingStyle);
        servicePostProcessor.setTypeNamespaceUri(nameSpace);
        servicePostProcessor.setBeanMappings(beanMappings);
        
        final InputStream responseIn = 
                getClass().getClassLoader().
                getResourceAsStream("SakaiSiteService.getActiveEnrollment.successfulResponse.xml");
        
        try {
            
            BasicHandler mockHandler = new BasicHandler() {

                public void invoke(MessageContext ctx) {

                        Message response = new Message(responseIn);
                        response.setMessageType(Message.RESPONSE);
                        ctx.setResponseMessage(response);
                }

            };

            BasicClientConfig axisConfig = new BasicClientConfig();
            axisConfig.deployTransport(new QName("http"), mockHandler);

            AxisClient axisClient = new AxisClient(axisConfig);

            org.apache.axis.client.Service service = new org.apache.axis.client.Service(
                    axisConfig, axisClient);

            servicePostProcessor.postProcessJaxRpcService(service);

            Call call = service.createCall();

            call.setPortTypeName(new QName("SakaiSiteService"));
            call.setOperationName(new QName("getActiveSiteEnrollment"));
            call.setTargetEndpointAddress("http://doesnt.matter");

            // Note that you have to keep this synched up with the xml
            // response read in off the classpath above.
            Site[] expectedSites = new Site[] {
                    new Site("siteA", "/url/to/site/A", "course", new Term("termA","termA",false), "instructorA"),
                    new Site("siteB", "/url/to/site/B", "project", new Term("termB","termB",false), "instructorB"),
                    new Site("siteC", "/url/to/site/C", "home", new Term("termC","termC",false), "instructorC"),
            };
            
            SiteQueryResult expectedResult = 
                new SiteQueryResult(expectedSites);
            
            assertEquals(expectedResult, call.invoke(new Object[] { "123456789" }));
        
        } finally {
            
            try {
                responseIn.close();
            } catch ( Throwable t ) {
                //
            }
            
        }

        
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    

}
