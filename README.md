#Reverse Country Code [![Build Status](https://travis-ci.org/bencampion/reverse-country-code.svg?branch=master)](https://travis-ci.org/bencampion/reverse-country-code) [![Coverage Status](https://img.shields.io/coveralls/bencampion/reverse-country-code.svg)](https://coveralls.io/r/bencampion/reverse-country-code?branch=master)

Java library for converting latitude and longitude coordinates into ISO 3166-1 two letter country codes.

##Building

Build using Maven:

    mvn clean package

The compiled jar has no dependencies.

##Usage

The library is simple to use as there is only one public class and it has only one public method:

    ReverseCountryCode rcc = new ReverseCountryCode();
    String london = rcc.getCountryCode(51.507222, -0.1275);
    String jakarta = rcc.getCountryCode(-6.2, 106.8);
    String vaticanCity = rcc.getCountryCode(41.904, 12.453);
    System.out.println(london); // prints UK
    System.out.println(jakarta); // prints ID
    System.out.println(vaticanCity); // prints VA

##Dataset

This library uses the world borders dataset from [thematicmapping.org](http://thematicmapping.org/downloads/world_borders.php). The data is stored in a properties file within the jar file that maps country codes to Well Known Text format polygons and multi-polygons. The dataset was converted to Well Known Text format by [GIS Stack Exchange user, elrobis](http://gis.stackexchange.com/a/17441).

##Algorithms

Determining if a point lies with in a polygon is performed using the algorithm from [PNPOLY](http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html). Bounding box checks are used to eliminate polygons that definitely cannot contain the the point.

##License

This library is released under a [Creative Commons Attribution-Share Alike License](http://creativecommons.org/licenses/by-sa/3.0/).

