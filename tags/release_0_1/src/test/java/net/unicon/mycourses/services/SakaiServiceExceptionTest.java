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

import junit.framework.TestCase;

public class SakaiServiceExceptionTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }
    
    public void testGetMessage_MessageAndErrorCode() {
        String msg = "msg";
        String errCode = "err-code";
        SakaiServiceException e = new SakaiServiceException(msg, errCode);
        assertEquals(msg + " [errorCode: " + errCode + "]", e.getMessage());
    }
    
    public void testGetMessage_NoErrorCode() {
        String msg = "msg";
        SakaiServiceException e = new SakaiServiceException(msg, (String)null);
        assertEquals(msg, e.getMessage());
    }
    
    public void testGetMessage_ErrorCodeOnly() {
        String errCode = "err-code";
        SakaiServiceException e = new SakaiServiceException(null, errCode);
        assertEquals("[errorCode: " + errCode + "]", e.getMessage());
    }
    
    public void testGetMessage_NoText() {
        SakaiServiceException e = new SakaiServiceException();
        assertNull(e.getMessage());
    }
    
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
}
