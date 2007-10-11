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

package net.unicon.mycourses.portlet;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

public class MockPorletPreferences implements PortletPreferences {

    private Map prefs;
    
    public MockPorletPreferences() {
        prefs = new HashMap();
    }
    
    /**
     * Returns a defensive copy of internal Map
     */
    public Map getMap() {
        return Collections.unmodifiableMap(prefs);
    }

    public Enumeration getNames() {
        
        final Iterator i = prefs.keySet().iterator();
        
        return new Enumeration() {
            
            public boolean hasMoreElements() {
                return i.hasNext();
            }
            
            public Object nextElement() {
                return i.next();
            }
            
        };
        
    }

    public String getValue(String arg0, String arg1) {
        if ( arg0 == null ) {
            throw new IllegalArgumentException("null key");
        }
        String[] values = (String[]) prefs.get(arg0);
        return values == null || values.length == 0 || values[0] == null ? arg1 : values[0];
    }

    public String[] getValues(String arg0, String[] arg1) {
        if ( arg0 == null ) {
            throw new IllegalArgumentException("null key");
        }
        String[] values = (String[]) prefs.get(arg0);
        return values == null ? arg1 : values; //TODO should really be returning a defensive copy
    }

    public boolean isReadOnly(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    public void reset(String arg0) throws ReadOnlyException {
        prefs.remove(arg0);

    }

    public void setValue(String arg0, String arg1) throws ReadOnlyException {
        
      prefs.put(arg0, arg1 == null ? null : new String[] { arg1 });

    }

    public void setValues(String arg0, String[] arg1) throws ReadOnlyException {
        prefs.put(arg0, arg1);

    }

    public void store() throws IOException, ValidatorException {
        // TODO Auto-generated method stub

    }

}
