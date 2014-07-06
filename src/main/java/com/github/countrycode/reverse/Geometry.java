package com.github.countrycode.reverse;

interface Geometry {

    boolean contains(double lat, double lon);

    BoundingBox getBoundingBox();

}
