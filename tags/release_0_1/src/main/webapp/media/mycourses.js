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

//MY Courses Javascript File
var tName = ".sites-table";
var mycourses = function() {
	var t = this;	
	var mcID = "";
	this.cPage = 0;
	this.perPage = 10;
	this.init = function(mcid) {
		mcID = mcid;
		t.filterList(mcCourse);
		$(mcID + ".tab-list").css("display","block");
		$(mcID + ".view-course").click(function() {t.filterList(mcCourse) }).set("href","#");
		$(mcID + ".view-project").click(function() {t.filterList(mcProject) }).set("href","#");
	}
	//filterList creates query string to return proper list
	this.filterList = function(fType, fMin, fMax) {
		t.viewList("tr." + fType, fType);
	}
	this.viewList = function(query, fType) {
		$(mcID + tName + " tbody tr:visible").hide();  //hide all rows
		if($(mcID + tName + " tbody " + query).size()) {
			$(mcID + ".emptyList").css("display","none");
			$(mcID + tName + " tbody " + query).show();  //show all rows that match list query
			$(mcID + tName + " tbody " + query).removeClass("shade").filter("tr:even").addClass("shade");  //stripe rows	
		} else {
			$(mcID + ".emptyList").css("display","block");
		}
		$(mcID + ".tab-list li").removeClass("active");
		if(query.indexOf(mcCourse)>=0) { 
			$(mcID + ".view-course").addClass("active");
			$(mcID + ".date-filter:hidden").show();
		} else if(query.indexOf(mcProject)>=0) {
			$(mcID + ".view-project").addClass("active");
			$(mcID + ".date-filter:visible").hide();
		} 
	}		
	this.setPage = function(newPage) {
		
	}
}