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

package net.unicon.mycourses.portlet.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/**
 * Run this following org.jasig.portal.container.deploy.Deployer to correct the
 * web.xml doc type.
 * 
 * @author dmccallum
 * 
 */
public class PortletDeploymentPostProcessor {

    private String webAppPath;

    public PortletDeploymentPostProcessor() {
        //
    }

    public String getWebAppPath() {
        return webAppPath;
    }

    public void setWebAppPath(String webAppPath) {
        this.webAppPath = webAppPath;
    }

    /**
     * Rewrites the web.xml located below the assigned webapp path.
     * Removes the assigned DocumentType and assigns a 2.4
     * XMLSchema.
     * 
     * @throws RuntimeException if the operation fails. Makes
     *   no attempt to restore the orginal web.xml if the failure
     *   occurs in the middle of serialization.
     */
    public void exec() {

        try {

            String webDotXMLPath = webAppPath + "/WEB-INF/web.xml";
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory
                    .newInstance();

            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(new File(webDotXMLPath));
            DocumentType docType = doc.getDoctype();
            if (docType != null) {
                doc.removeChild(docType);
            }
            Element webAppElement = (Element) doc.getFirstChild();
            webAppElement.setAttribute("version", "2.4");
            webAppElement.setAttribute("xmlns",
                    "http://java.sun.com/xml/ns/j2ee");
            webAppElement.setAttribute("xmlns:xsi",
                    "http://www.w3.org/2001/XMLSchema-instance");
            webAppElement
                    .setAttribute(
                            "xsi:schemaLocation",
                            "http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd");

            OutputFormat of = new OutputFormat();
            of.setIndenting(true);
            of.setIndent(4); // 2-space indention
            of.setLineWidth(16384);

            OutputStream outputStream = new FileOutputStream(webDotXMLPath);
            Writer writer = new OutputStreamWriter(outputStream, "utf-8");

            XMLSerializer serializer = new XMLSerializer(writer, of);
            serializer.serialize(doc);

            writer.close();

        } catch (Exception e) {
            throw new RuntimeException("Processing failed", e);
        }

    }

    /**
     * Takes one argument: a path to the (unpacked) web-archive you wish to
     * manipulate.
     * 
     */
    public static void main(String[] args) {
        
        if (args.length == 0) {
            System.out
                    .println("Usage: PortletDeploymentPostProcessor <path-to-web-app>");
        }

        PortletDeploymentPostProcessor processor = new PortletDeploymentPostProcessor();

        processor.setWebAppPath(args[0]);

        processor.exec();

    }

}
