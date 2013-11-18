package com.github.countrycode.reverse;

class Country {

    private final String code;
    private final Geometry geometry;

    public Country(String code, Geometry geometry) {
        this.code = code;
        this.geometry = geometry;
    }

    public String getCode() {
        return code;
    }

    public boolean contains(double lat, double lon) {
        return geometry.contains(lat, lon);
    }

}
