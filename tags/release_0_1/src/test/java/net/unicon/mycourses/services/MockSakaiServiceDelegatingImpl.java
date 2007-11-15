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


public class MockSakaiServiceDelegatingImpl extends SakaiServiceDelegatingImpl {
    
    volatile private boolean blockGetServiceDelegateUnguardedCalls;
    private int completedGetServiceDelegateUnguardedCalls;
    private int attemptedGetServiceDelegateUnguardedCalls;
    private long blockingGetServiceDelegateUnguardedCallSleepTime = 50;
    private boolean calledGetServiceDelegate;
    private SakaiServiceContext getServiceDelegateWith_ContextArg;
    private Map getServiceDelegateWithDelegate_MapArg;
    private DelegateFactoryInvoker getServiceDelegateWith_FactoryInvokerArg; 
    private SakaiSiteServiceInvoker executeSiteServiceCall_CommandArg;
    
    public SakaiSiteServiceInvoker getExecuteSiteServiceCall_CommandArg() {
        return executeSiteServiceCall_CommandArg;
    }
    
    protected void executeSiteServiceCall(SakaiSiteServiceInvoker delegateInvoker, SakaiServiceContext userContext) {
        executeSiteServiceCall_CommandArg = delegateInvoker;
        super.executeSiteServiceCall(delegateInvoker, userContext);
    }
    
    protected Object getServiceDelegateUnguarded(String scrubbedConfigScopeID, 
            SakaiServiceContext context, Map delegateMap, DelegateFactoryInvoker pointers) {
        
        synchronized(this) {
            attemptedGetServiceDelegateUnguardedCalls++;
        }
        while ( blockGetServiceDelegateUnguardedCalls ) {
            try {
                Thread.currentThread().sleep(blockingGetServiceDelegateUnguardedCallSleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        synchronized (this) {
            completedGetServiceDelegateUnguardedCalls++;
        }
        return super.getServiceDelegateUnguarded(scrubbedConfigScopeID, context, delegateMap, pointers);
        
    }
    
    protected Object getServiceDelegate(SakaiServiceContext context, Map delegateMap, DelegateFactoryInvoker pointers) {
        
        calledGetServiceDelegate = true;
        getServiceDelegateWith_ContextArg = context;
        getServiceDelegateWithDelegate_MapArg = delegateMap;
        getServiceDelegateWith_FactoryInvokerArg = pointers;
        
        return super.getServiceDelegate(context, delegateMap, pointers);
        
    }

    public boolean calledGetServiceDelegate() {
        return calledGetServiceDelegate;
    }

    public void calledGetServiceDelegate(boolean calledGetServiceDelegate) {
        this.calledGetServiceDelegate = calledGetServiceDelegate;
    }

    public SakaiServiceContext getGetServiceDelegateWith_ContextArg() {
        return getServiceDelegateWith_ContextArg;
    }

    public void setGetServiceDelegateWith_ContextArg(
            SakaiServiceContext getServiceDelegateWith_ContextArg) {
        this.getServiceDelegateWith_ContextArg = getServiceDelegateWith_ContextArg;
    }

    public DelegateFactoryInvoker getGetServiceDelegateWith_FactoryInvokerArg() {
        return getServiceDelegateWith_FactoryInvokerArg;
    }

    public void setGetServiceDelegateWith_FactoryInvokerArg(
            DelegateFactoryInvoker getServiceDelegateWith_FactoryInvokerArg) {
        this.getServiceDelegateWith_FactoryInvokerArg = getServiceDelegateWith_FactoryInvokerArg;
    }

    public Map getGetServiceDelegateWithDelegate_MapArg() {
        return getServiceDelegateWithDelegate_MapArg;
    }

    public void setGetServiceDelegateWithDelegate_MapArg(
            Map getServiceDelegateWithDelegate_MapArg) {
        this.getServiceDelegateWithDelegate_MapArg = getServiceDelegateWithDelegate_MapArg;
    }

    public int getAttemptedGetServiceDelegateUnguardedCalls() {
        return attemptedGetServiceDelegateUnguardedCalls;
    }

    public void setAttemptedGetServiceDelegateUnguardedCalls(
            int attemptedGetServiceDelegateUnguardedCalls) {
        this.attemptedGetServiceDelegateUnguardedCalls = attemptedGetServiceDelegateUnguardedCalls;
    }

    public boolean isBlockGetServiceDelegateUnguardedCalls() {
        return blockGetServiceDelegateUnguardedCalls;
    }

    public void setBlockGetServiceDelegateUnguardedCalls(
            boolean blockGetServiceDelegateUnguardedCalls) {
        this.blockGetServiceDelegateUnguardedCalls = blockGetServiceDelegateUnguardedCalls;
    }

    public long getBlockingGetServiceDelegateUnguardedCallSleepTime() {
        return blockingGetServiceDelegateUnguardedCallSleepTime;
    }

    public void setBlockingGetServiceDelegateUnguardedCallSleepTime(
            long blockingGetServiceDelegateUnguardedCallSleepTime) {
        this.blockingGetServiceDelegateUnguardedCallSleepTime = blockingGetServiceDelegateUnguardedCallSleepTime;
    }

    public int getCompletedGetServiceDelegateUnguardedCalls() {
        return completedGetServiceDelegateUnguardedCalls;
    }

    public void setCompletedGetServiceDelegateUnguardedCalls(
            int completedGetServiceDelegateUnguardedCalls) {
        this.completedGetServiceDelegateUnguardedCalls = completedGetServiceDelegateUnguardedCalls;
    }

    

}
