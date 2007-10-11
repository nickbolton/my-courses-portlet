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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.unicon.sakai.portalservices.api.SakaiSiteService;
import net.unicon.sakai.portalservices.api.Site;
import net.unicon.sakai.portalservices.api.SiteQueryResult;
import net.unicon.sakai.portalservices.api.StringQueryResult;
import net.unicon.sakai.portalservices.api.Term;
import net.unicon.sakai.portalservices.api.TermQueryResult;

public class MockSakaiSiteService implements SakaiSiteService {
    
    private Map sites;
    private int getActiveSiteEnrollmentCallCount;
    private String lastGetActiveSiteEnrollmentCall_SessionIDArg;
    
    private int getActiveSiteEnrollmentByTermCallCount;
    private String lastGetActiveSiteEnrollmentByTermCall_SessionIDArg;
    private String lastGetActiveSiteEnrollmentByTermCall_TermArg;
    
    private int getActiveSiteEnrollmentByTypeCallCount;
    private String lastGetActiveSiteEnrollmentByTypeCall_SessionIDArg;
    private String lastGetActiveSiteEnrollmentByTypeCall_TypeArg;
    
    private int getTermsCallCount;
    private String lastGetTermsCall_SessionIDArg;
    private Collection seedTerms;
    private SessionToUserMapper sessionMapper;
    /** String-to-String. UserIDs-to-URLs */
    private Map membershipToolPageURLs;
    private int getMembershipToolPageURLCallCount;
    private String lastGetMembershipToolPageURLCall_SessionIDArg;
    
    /**
     * package private constructor
     *
     */
    public MockSakaiSiteService() {
        sites = new HashMap();
        seedTerms = new ArrayList();
        membershipToolPageURLs = new HashMap();
    }
    
    /** Assign a reference to the SakaiLoginService used in concert with 
     * this MockSakaiSiteService. We need this so we can look up users
     * by session ID.
     *
     */
    public void setSessionToUserMapper(SessionToUserMapper sessionMapper) {
        this.sessionMapper = sessionMapper;
    }
    
    /**
     * Logs call and returns current internally cached Sites
     * corresponding to the specified session ID
     */
    public SiteQueryResult getActiveSiteEnrollment(String sessionID) {
        logGetActiveSiteEnrollmentCall(sessionID);
        
        if ( sessionMapper == null ) {
            return new SiteQueryResult();
        }
        
        String userID = sessionMapper.getUserIDForSessionID(sessionID);
        Collection sitesCollection = getSites(userID);
        Site[] sitesArray = 
            sitesCollection == null ? null : (Site[])sitesCollection.toArray(new Site[sitesCollection.size()]);
        SiteQueryResult result = new SiteQueryResult();
        result.setQueryResults(sitesArray);
        return result;
    }
    
    /**
     * Logs call and returns current internally cached Sites
     * corresponding to the specified session ID and term
     */
    public SiteQueryResult getActiveSiteEnrollmentByTerm(String sessionID, String term) {
        
        logGetActiveSiteEnrollmentByTermCall(sessionID, term);
        
        Site[] sites = getActiveSiteEnrollment(sessionID).getQueryResults();
        
        if ( sites == null || sites.length == 0 ) {
            
            return new SiteQueryResult();
            
        } else {
            ArrayList matches = new ArrayList();
            for (int i = 0; i < sites.length; i++) {
                Site site = sites[i];
		Term siteTerm = site.getTerm();
		String siteTermId = siteTerm == null ? null : siteTerm.getId();
		boolean match = false;
		if ( (term == null || siteTermId == null) && term == siteTermId ) {
			match = true;
		} else if ( term.equals(siteTermId) ) {
			match = true;
		}
		if ( match ) {
			matches.add(site);
		}
            }
            return new SiteQueryResult((Site[]) matches
                    .toArray(new Site[matches.size()]));
        }
    }
    
    /**
     * Logs call and returns current internally cached Sites
     * corresponding to the specified session ID and type
     */
    public SiteQueryResult getActiveSiteEnrollmentByType(String sessionID, String type) {
        
        logGetActiveSiteEnrollmentByTypeCall(sessionID, type);
        
        Site[] sites = getActiveSiteEnrollment(sessionID).getQueryResults();
        
        if ( sites == null || sites.length == 0 ) {
            
            return new SiteQueryResult();
            
        } else {
            ArrayList matches = new ArrayList();
            for (int i = 0; i < sites.length; i++) {
                Site site = sites[i];
		String siteType = site.getType();
		boolean match = false;
		if ( (type == null || siteType == null) && type == siteType ) {
			match = true;
		} else if ( type.equals(siteType) ) {
			match = true;
		}
		if ( match ) {
			matches.add(site);
		}
            }
            return new SiteQueryResult((Site[]) matches
                    .toArray(new Site[matches.size()]));
        }
    }
    
    public TermQueryResult getTerms(String sessionID) {
        logGetTermsCall(sessionID);
        return seedTerms == null ? 
                new TermQueryResult() : 
                    new TermQueryResult((Term[])seedTerms.toArray(new Term[seedTerms.size()]));
    }
    
    public StringQueryResult getMembershipToolPageURL(String sessionID) {
        logGetMembershipToolPageURL(sessionID);
        if ( sessionMapper == null ) {
            return new StringQueryResult();
        }
        
        String userID = sessionMapper.getUserIDForSessionID(sessionID);
        String url = getMembershipToolPageURLForUserID(userID);
        if ( url == null ) {
            return new StringQueryResult();
        } else {
            return new StringQueryResult(new String[] { url });
        }
    }
    
    protected String getMembershipToolPageURLForUserID(String userID) {
        return (String) membershipToolPageURLs.get(userID);
    }
    
    protected void logGetMembershipToolPageURL(String sessionID) {
        getMembershipToolPageURLCallCount++;
        lastGetMembershipToolPageURLCall_SessionIDArg = sessionID;
    }
    
    
    
    /**
     * Verifies at least one invokation of getActiveSiteEnrollment()
     * during this object's lifetime.
     * 
     * @return true if getActiveSiteEnrollment() has ever been called.
     */
    public boolean calledGetActiveSiteEnrollment() {
      return getActiveSiteEnrollmentCallCount > 0;    
    }
    
    /**
     * Verifies at least one invokation of getActiveSiteEnrollmentByTerm()
     * during this object's lifetime.
     * 
     * @return true if getActiveSiteEnrollmentByTerm() has ever been called.
     */
    public boolean calledGetActiveSiteEnrollmentByTerm() {
      return getActiveSiteEnrollmentByTermCallCount > 0;    
    }
    
    /**
     * Verifies at least one invokation of getActiveSiteEnrollmentByType()
     * during this object's lifetime.
     * 
     * @return true if getActiveSiteEnrollmentByType() has ever been called.
     */
    public boolean calledGetActiveSiteEnrollmentByType() {
      return getActiveSiteEnrollmentByTypeCallCount > 0;    
    }
    
    /**
     * Verifies at least one invokation of getTerms()
     * during this object's lifetime.
     * 
     * @return true if getTerms() has ever been called.
     */
    public boolean calledGetTerms() {
      return getTermsCallCount > 0;    
    }
    
    /**
     * Verifies at least one invokation of getMembershipToolPageURL()
     * during this object's lifetime.
     * 
     * @return true if getMembershipToolPageURL() has ever been called.
     */
    public boolean calledGetMembershipToolPageURL() {
        return getMembershipToolPageURLCallCount > 0;
    }
    
    /**
     * 
     * @param userID
     * @return
     */
    public String getLastGetActiveSiteEnrollmentCall_SessionIDArg() {
        return lastGetActiveSiteEnrollmentCall_SessionIDArg;
    }
    
    public int getGetActiveSiteEnrollmentCallCount() {
        return getActiveSiteEnrollmentCallCount;
    }
    
    /**
     * Override default internal Site collection
     * 
     * @param sites maps user IDs to Site Collections
     */
    public void setSites(Map sites) {
        this.sites = sites;
    }
    
    /**
     * Retrieve SiteQueryResult wrapping direct reference to internal Site
     * Collection for the specified user
     * 
     * @return
     */
    public Collection getSites(String forUserID) {
        Collection sitesCollection = (Collection)this.sites.get(forUserID);
        return sitesCollection;
    }
    
    protected void logGetActiveSiteEnrollmentCall(String forSessionID) {
        getActiveSiteEnrollmentCallCount++;
        lastGetActiveSiteEnrollmentCall_SessionIDArg = forSessionID;
    }
    
    protected void logGetActiveSiteEnrollmentByTermCall(String forSessionID, String forTerm) {
        getActiveSiteEnrollmentByTermCallCount++;
        lastGetActiveSiteEnrollmentByTermCall_SessionIDArg = forSessionID;
        lastGetActiveSiteEnrollmentByTermCall_TermArg = forTerm;
    }
    
    protected void logGetActiveSiteEnrollmentByTypeCall(String forSessionID, String forType) {
        getActiveSiteEnrollmentByTypeCallCount++;
        lastGetActiveSiteEnrollmentByTypeCall_SessionIDArg = forSessionID;
        lastGetActiveSiteEnrollmentByTypeCall_TypeArg = forType;
    }
    
    protected void logGetTermsCall(String forSessionID) {
        getTermsCallCount++;
        lastGetTermsCall_SessionIDArg = forSessionID;
    }
   
    public String getLastGetActiveSiteEnrollmentByTermCall_SessionIDArg() {
        return lastGetActiveSiteEnrollmentByTermCall_SessionIDArg;
    }

    public String getLastGetActiveSiteEnrollmentByTermCall_TermArg() {
        return lastGetActiveSiteEnrollmentByTermCall_TermArg;
    }

    public Collection getSeedTerms() {
        return seedTerms;
    }

    public void setSeedTerms(Collection terms) {
        this.seedTerms = terms;
    }

    public String getLastGetTermsCall_SessionIDArg() {
        return lastGetTermsCall_SessionIDArg;
    }

    public Map getMembershipToolPageURLs() {
        return membershipToolPageURLs;
    }

    public void setMembershipToolPageURLs(Map membershipToolPageURLs) {
        this.membershipToolPageURLs = membershipToolPageURLs;
    }

    public int getGetMembershipToolPageURLCallCount() {
        return getMembershipToolPageURLCallCount;
    }

    public String getLastGetMembershipToolPageURLCall_SessionIDArg() {
        return lastGetMembershipToolPageURLCall_SessionIDArg;
    }

    public int getGetActiveSiteEnrollmentByTypeCallCount() {
        return getActiveSiteEnrollmentByTypeCallCount;
    }

    public void setGetActiveSiteEnrollmentByTypeCallCount(
            int getActiveSiteEnrollmentByTypeCallCount) {
        this.getActiveSiteEnrollmentByTypeCallCount = getActiveSiteEnrollmentByTypeCallCount;
    }

    public String getLastGetActiveSiteEnrollmentByTypeCall_SessionIDArg() {
        return lastGetActiveSiteEnrollmentByTypeCall_SessionIDArg;
    }

    public void setLastGetActiveSiteEnrollmentByTypeCall_SessionIDArg(
            String lastGetActiveSiteEnrollmentByTypeCall_SessionIDArg) {
        this.lastGetActiveSiteEnrollmentByTypeCall_SessionIDArg = lastGetActiveSiteEnrollmentByTypeCall_SessionIDArg;
    }

    public String getLastGetActiveSiteEnrollmentByTypeCall_TypeArg() {
        return lastGetActiveSiteEnrollmentByTypeCall_TypeArg;
    }

    public void setLastGetActiveSiteEnrollmentByTypeCall_TypeArg(
            String lastGetActiveSiteEnrollmentByTypeCall_TypeArg) {
        this.lastGetActiveSiteEnrollmentByTypeCall_TypeArg = lastGetActiveSiteEnrollmentByTypeCall_TypeArg;
    }

    

    
    
    
    
    
    
}
