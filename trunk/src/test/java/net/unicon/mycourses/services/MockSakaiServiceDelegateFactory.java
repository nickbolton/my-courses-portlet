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

public class MockSakaiServiceDelegateFactory implements SakaiServiceDelegateFactory {

   
    private boolean calledALoginService;
    private SakaiServiceContext aSiteService_ContextArg;
    private boolean calledASiteService;
    private SakaiServiceContext aLoginService_ContextArg;
    private boolean calledIsLoginServiceValid;
    private SakaiLoginService isLoginServiceValidForContextWith_ServiceArg;
    private SakaiServiceContext isLoginServiceValidForContextWith_ContextArg;
    private boolean calledIsSiteServiceValid;
    private SakaiSiteService isSiteServiceValidForContextWith_ServiceArg;
    private SakaiServiceContext isSiteServiceValidForContextWith_ContextArg;
    
    
    public SakaiLoginService aLoginService(SakaiServiceContext context) {
        calledALoginService = true;
        aLoginService_ContextArg = context;
        
        return new MockSakaiLoginService();
    }

    public SakaiSiteService aSiteService(SakaiServiceContext context) {
        calledASiteService = true;
        aSiteService_ContextArg = context;
        return new MockSakaiSiteService();
    }

    public boolean isLoginServiceValidForContext(SakaiLoginService loginService, SakaiServiceContext context) {
        calledIsLoginServiceValid = true;
        isLoginServiceValidForContextWith_ServiceArg = loginService;
        isLoginServiceValidForContextWith_ContextArg = context;
        return true;
    }

    public boolean isSiteServiceValidForContext(SakaiSiteService siteService, SakaiServiceContext context) {
        calledIsSiteServiceValid = true;
        isSiteServiceValidForContextWith_ServiceArg = siteService;
        isSiteServiceValidForContextWith_ContextArg = context;
        return true;
    }

    public boolean calledALoginService() {
        return calledALoginService;
    }

    public void calledALoginService(boolean calledALoginService) {
        this.calledALoginService = calledALoginService;
    }

    public boolean calledASiteService() {
        return calledASiteService;
    }

    public void calledASiteService(boolean calledASiteService) {
        this.calledASiteService = calledASiteService;
    }

    public boolean isCalledIsLoginServiceValid() {
        return calledIsLoginServiceValid;
    }

    public void setCalledIsLoginServiceValid(boolean calledIsLoginServiceValid) {
        this.calledIsLoginServiceValid = calledIsLoginServiceValid;
    }

    public boolean isCalledIsSiteServiceValid() {
        return calledIsSiteServiceValid;
    }

    public void setCalledIsSiteServiceValid(boolean calledIsSiteServiceValid) {
        this.calledIsSiteServiceValid = calledIsSiteServiceValid;
    }

    public SakaiServiceContext getIsLoginServiceValidForContextWith_ContextArg() {
        return isLoginServiceValidForContextWith_ContextArg;
    }

    public void setIsLoginServiceValidForContextWith_ContextArg(
            SakaiServiceContext isLoginServiceValidForContextWith_ContextArg) {
        this.isLoginServiceValidForContextWith_ContextArg = isLoginServiceValidForContextWith_ContextArg;
    }

    public SakaiLoginService getIsLoginServiceValidForContextWith_ServiceArg() {
        return isLoginServiceValidForContextWith_ServiceArg;
    }

    public void setIsLoginServiceValidForContextWith_ServiceArg(
            SakaiLoginService isLoginServiceValidForContextWith_ServiceArg) {
        this.isLoginServiceValidForContextWith_ServiceArg = isLoginServiceValidForContextWith_ServiceArg;
    }

    public SakaiServiceContext getIsSiteServiceValidForContextWith_ContextArg() {
        return isSiteServiceValidForContextWith_ContextArg;
    }

    public void setIsSiteServiceValidForContextWith_ContextArg(
            SakaiServiceContext isSiteServiceValidForContextWith_ContextArg) {
        this.isSiteServiceValidForContextWith_ContextArg = isSiteServiceValidForContextWith_ContextArg;
    }

    public SakaiSiteService getIsSiteServiceValidForContextWith_ServiceArg() {
        return isSiteServiceValidForContextWith_ServiceArg;
    }

    public void setIsSiteServiceValidForContextWith_ServiceArg(
            SakaiSiteService isSiteServiceValidForContextWith_ServiceArg) {
        this.isSiteServiceValidForContextWith_ServiceArg = isSiteServiceValidForContextWith_ServiceArg;
    }

    public SakaiServiceContext getALoginService_ContextArg() {
        return aLoginService_ContextArg;
    }

    public void setALoginService_ContextArg(
            SakaiServiceContext loginService_ContextArg) {
        aLoginService_ContextArg = loginService_ContextArg;
    }

    public SakaiServiceContext getASiteService_ContextArg() {
        return aSiteService_ContextArg;
    }

    public void setASiteService_ContextArg(
            SakaiServiceContext siteService_ContextArg) {
        aSiteService_ContextArg = siteService_ContextArg;
    }

    
    
}
