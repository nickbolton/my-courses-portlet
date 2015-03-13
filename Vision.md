# Sakai integration non-functional improvements #
## Make dependencies of this portlet publicly available ##

An important follow-on task here is to also open-source the Unicon-developed Sakai web services code upon which this portlet depends.  There is intent to get that done, it's just a matter of finding some follow-on time to do it.

## Realize alternative Sakai integration layer implementations ##

It may be that core Sakai Web Services have evolved since when this portlet was first designed such that custom services are no longer required.  More RESTful, even Proxy-CAS-ified-XML integration approaches would be a lighter and more accessible alternative.  There is room in the world for more implementations of the Sakai integration plugin or improvements to a single implementation to make it easier to adopt.

# Support additional course management systems #

The MyCourses Portlet could be expanded to support additional course management systems beyond [Sakai](http://www.unicon.net/sakai).  For instance, there are IChannels commonly used with uPortal implementing similar WebCT and Blackboard summary-with-links integrations.  Currently, these codebases are disparate, their UIs are disparate, and occasionally the WebCT and Blackboard channels feel fragile across versions.  It might be a more maintainable and synergistic world if there were a single modern best-practices Spring PortletMVC portlet implementing a usable and accessible UI with plugins available for the various versions of various course management systems in use by the higher education portal community.

That is, the effort to have a simple, beautiful, usable, accessible summary integration to a newly arrived course management system version could be reduced to implementing a simple plugin API.

# User Experience Improvements #

Possibly, use Fluid components where appropriate.

Provide a more accessible UI.

# Deeper Integration #

## Single Sign On ##

Currently users will experience SSO only when the portlet is used in concert with an enterprise SSO system such as the [Central Authentication Service](http://www.ja-sig.org/products/cas/) or PubCookie.  Integrations with particular course management systems and the link production in the UI could be enhanced to provide inherently single-on-signing links.

## Indicators ##
Indication of how recently the linked sites have changed, whether there are new messages, pulling up announcements -- at the cost of additional complexity in implementation and in user interface, richer dashboard functionality could be implemented.

Andrew has a random pet feature idea around subtle indications of recentness of changes borrowing cues from [tag clouds](http://www.unicon.net/support/knowledgebase/tagcloud), with more recently updated sites' hyperlinks appearing continuously **bolder** than staler sites.  Take that for whatever it's worth.

# Other ideas #

Other ideas for directions this portlet could go?  Comment!  Jump in!