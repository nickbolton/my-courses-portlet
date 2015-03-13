# Introduction #

This page instructs on how to install this portlet once [built from source](BuildFromSource.md).

# Details #

## Requires Ant ##

Deploying mycourses-portlet.war requires Ant. [Download Ant](http://ant.apache.org/) if you haven't already. You'll also need either [uPortal](http://www.uportal.org/) or a
standalone [Pluto](http://portals.apache.org/pluto/) v1.0.1.

## build.properties required in source distro ##
If you're deploying mycourses-portlet.war from a source checkout do the following (assuming the directory into which you checked out the source is $MYCOURSES\_HOME):

```
    % cd $MYCOURSES_HOME/src/main/webapp/scripts
    % cp build.properties.sample build.properties
```

## build scripts required in binary distribution ##
If you're deploying mycourses-portlet.war from a binary distribution, you'll need
to extract the build scripts. Assuming $MYCOURSES\_WAR is your binary:

```
    % jar -xvf $MYCOURSES_WAR scripts
    % cd scripts
    % cp build.properties.sample build.properties
```

## Edit build.properties ##

Edit build.properties to fit your environment.

$MYCOURSES\_HOME/src/main/webapp/scripts/build.properties.samples and
${MYCOURSES\_WAR}!/scripts/build.properties.samples have examples. Usually,
the only property you need to edit are 'portal.tomcat.home' and 'portal.webapp.home'.
If you're deploying from a binary distribution, you'll need to specify the name and
location of your war file. For example:

```
    portlet.war.name=my-personal-war-file-name-without-the-war-extension
    portlet.war=path-to-my-war/${portlet.war.name}.war
```

## Deploy to your portlet container ##

### Deploying to Pluto ###
To deploy to Pluto run:

```
    % ant plutoDeploy
```


Alternatively, you can also use the Pluto portal's web UI to upload and deploy your .war.

This has the advantage of immediately offering you the ability to configure on-screen
portlet placement, but you will need to explicitly edit the deployed .war's web.xml file
before that on-screen placement will function as expected. Post-processing web.xml is
necessary because the Pluto Deployer will revert your web.xml to a 2.3 doctype, which
causes the servlet container to ignore JSP expressions. To fix this problem, you can run
this ant target:

```
    % ant plutoPostDeploy
```

Or you can edit the web.xml by hand. Open the file and remove the doctype:

```
    <!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
                         "http://java.sun.com/dtd/web-app_2_3.dtd">
```

Then change the web-app element to reference the 2.4 XMLSchema:

```
    <web-app version="2.4" xmlns="http://java.sun.com/xml/ns/j2ee" 
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
        xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd">
```

Note that the 'plutoPostDeployment' target is unecessary if you deployed using
'plutoDeploy'.

#### Register the portlet if you did not use the Pluto web UI ####

If you did not use the Pluto web UI to deploy your portlet, you will need to modify
pageregistry.xml in your Pluto installation. Specifically, given the following entry in
portletentityregistry.xml:

```
  <application id="mycourses-portlet">
    <definition-id>mycourses-portlet</definition-id>
    <portlet id="mycourses">
      <definition-id>mycourses-portlet.mycourses</definition-id>
    </portlet>
  </application>
```

add this to pageregistry.xml:

```
    <fragment name="mycourses" type="page">
        <navigation>
            <title>myCourses</title>
            <description>MyCourses Portlet</description>
        </navigation>

        <fragment name="row" type="row">
            <fragment name="col1" type="column">
                <fragment name="p1" type="portlet">
                    <property name="portlet" value="mycourses-portlet.mycourses"/>
                </fragment>
            </fragment>

        </fragment>

    </fragment>
```


## Deploying to uPortal ##
To deploy to uPortal run:

```
    % ant uPortalDeploy 
```