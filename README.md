QuickCrawl
==========

Aims to quickly create a pub crawl from 2 given locations.
The back-end is written in Java.
The front-end is in JSP, Javascript and some javascript google maps stuff.
All dev was done in eclipse.

How does it work?
=================

1. Given 2 geo-locations (a, b) it will first create an area where a and b become the 2 opposite corners to form a rectangle.

2. This rectangular space is the area we look for pubs and clubs. Using Yelp & Qype as data sources it downloads all the pubs and bars in the area.

3. The duplicate events are aggregated together so as to not offer duplicate pubs/clubs.

4. Crappy pubs and ones that aren't reviewed are removed, if possible.

5. Using a mixture of distance travelled, pub reviews & number of reviews [bayes] it will generate an optimal crawl.

6. Google maps displays the pub crawl.

Issues
======

1. You cannot create a pub crawl that goes in a circle. It only works if there is a start and end location and aims to create a straight line or near curved line.

2. The weightings given for (reviews and distance) work Ok for a city but fall down when trying to create a pub crawl in the sticks.

3. The distance between points (geo-located) does not take into account the curvature of the earth. e.g. 1 latitude degree becomes less and less (in terms of actual, physical distance) the further north you are.

4. The 'website' is pap. It's my first attempt at a website and it needs completely rewriting.

5. Bad data. Can't really do anything about idiot users who think Nando's comes under nightlife/drinking. It always gets highly rated and therefore appears in pub crawls, sigh.