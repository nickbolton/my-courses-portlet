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
 * "Layer supertype" exception for failures within the SakaiService
 * abstraction. Adds the capability for specifying an error code,
 * which is typically used to pass up the error code received from
 * the Sakai server.
 * 
 * @author dmccallum
 *
 */
public class SakaiServiceException extends RuntimeException {
    
    private String errorCode;
    
    public SakaiServiceException() {
        super();
    }
    
    public SakaiServiceException(String msg) {
        super(msg);
    }
    
    public SakaiServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public SakaiServiceException(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
    
    public SakaiServiceException(String msg, String errorCode, Throwable cause) {
        super(msg, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public String getMessage() {
        String baseMsg = super.getMessage();
        if ( baseMsg == null ) {
            return (this.errorCode == null) ? null : decoratedErrorCode();
        }
        return (this.errorCode == null) ? 
                baseMsg : baseMsg + " " + decoratedErrorCode();
    }
    
    private String decoratedErrorCode() {
        return "[errorCode: " + this.errorCode + "]";
    }

}
