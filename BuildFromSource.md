# Introduction #

This wiki page will grow to describe how to build this portlet from source code. **It is not currently feasible to build this portlet from source code.**  Making this code available as Free and Open Source Software is a step in a larger process to make all the dependencies of this portlet available so that anyone can build it from source.  This documentation will be updated when that has been achieved.


# Steps #

Building from source is not feasible at this time.  Making this feasible is a prominent to-do item.

## Download Maven 2 ##
[Download Maven2](http://maven.apache.org/) if you haven't already.

## Sakai web services dependency ##
This project depends on Unicon-developed Sakai web services.
**Neither binaries nor source code are yet publicly available for those services.**

The plan is to work to make that code available through the Sakai Project (whether via contrib or in another way) and/or to refactor this portlet to use services that are publicly available in the Sakai Project.

In the meantime: incremental progress towards "open sourcing" this projectlet.  At this juncture, at least this portlet source code is available, such that one could begin to envision adding functionality or implementing against other services or even other LMSs.

## Use Maven to package MyCourses ##
If the directory into which you checked out the source is $MYCOURSES\_HOME, run:

```
    % cd $MYCOURSES_HOME
    % mvn clean package
```

## Install into your portlet container of choice ##

The output of the build collects into $MYCOURSES\_HOME/target/mycourses-portlet.war. See
[the deployment instructions page](DeploymentInstructions.md) for deploying this war to your portlet container of choice (if your container of choice is uPortal or standalone Pluto).