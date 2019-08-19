# Reverse Country Code [![Build Status](https://travis-ci.org/bencampion/reverse-country-code.svg?branch=master)](https://travis-ci.org/bencampion/reverse-country-code) [![Coverage Status](https://img.shields.io/coveralls/bencampion/reverse-country-code.svg)](https://coveralls.io/r/bencampion/reverse-country-code?branch=master) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/uk.recurse/reverse-country-code/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.recurse/reverse-country-code)

A reverse geocoder that converts latitude and longitude coordinates to country information such as names, ISO codes and locales.

## Usage

```java
ReverseGeocoder geocoder = new ReverseGeocoder();
geocoder.getCountry(51.507222, -0.1275).ifPresent(country -> {
    System.out.println(country.iso());        // GB
    System.out.println(country.iso3());       // GBR
    System.out.println(country.isoNumeric()); // 826
    System.out.println(country.name());       // United Kingdom
    System.out.println(country.area());       // 244820.0
    System.out.println(country.population()); // 62348447
    System.out.println(country.continent());  // EU
    List<Locale> locales = country.locales();
    System.out.println(locales.get(0));       // en_GB
    System.out.println(locales.get(1));       // cy_GB
    System.out.println(locales.get(2));       // gd
});
```

## Dataset

Country information and boundary data comes from [GeoNames](http://download.geonames.org/export/dump/).

## Accuracy

97.92% when ran against the GeoNames data set for cities with a population > 15000. 

## Performance

~3 μs average to look up city from the GeoNames data set on a 13" MacBook Pro (i5-5257U) using a single thread. The retained heap size for `ReverseGeocoder` is ~7.5 MB.

## Algorithms

Country bounding boxes are loaded into [R-Trees](https://en.wikipedia.org/wiki/R-tree) using the [Sort-Tile-Recursive](http://www.dtic.mil/dtic/tr/fulltext/u2/a324493.pdf) algorithm. Determining if a point lies within a polygon is performed using the [PNPOLY](http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html) algorithm.

## Gradle tasks

Build jar:

    ./gradlew assemble

Download new Geonames data:

    ./gradlew updateCountryInfo updateShapes

Run benchmarks:

    ./gradlew jmh

Generate new baseline test data:

    ./gradlew generateBaseline
