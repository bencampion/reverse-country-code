package com.github.countrycode.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class Polygon implements Geometry {

    private final String id;
    private final double[] latitude;
    private final double[] lonitude;
    private final List<Polygon> holes;
    private double north = Double.NEGATIVE_INFINITY;
    private double south = Double.POSITIVE_INFINITY;
    private double east = Double.NEGATIVE_INFINITY;
    private double west = Double.POSITIVE_INFINITY;

    public Polygon(String id, List<double[]> ring) {
        this(id, ring, Collections.<Polygon> emptyList());
    }

    public Polygon(String id, List<double[]> ring, Collection<Polygon> holes) {
        this.id = id;
        int n = ring.size();
        latitude = new double[n];
        lonitude = new double[n];
        for (int i = 0; i < n; i++) {
            double x = ring.get(i)[0], y = ring.get(i)[1];
            latitude[i] = y;
            lonitude[i] = x;
            north = Math.max(y, north);
            south = Math.min(y, south);
            east = Math.max(x, east);
            west = Math.min(x, west);
        }
        this.holes = new ArrayList<Polygon>(holes);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean contains(double lat, double lon) {
        return inBox(lat, lon) && inPoly(lat, lon) && !inHoles(lat, lon);
    }

    private boolean inBox(double lat, double lon) {
        return lat <= north && lat >= south && lon <= east && lon >= west;
    }

    private boolean inHoles(double lat, double lon) {
        for (Polygon hole : holes) {
            if (hole.contains(lat, lon)) {
                return true;
            }
        }
        return false;
    }

    private boolean inPoly(double lat, double lon) {
        boolean contains = false;
        for (int i = 0, j = lonitude.length - 1; i < lonitude.length; j = i++) {
            if (((latitude[i] > lat) != (latitude[j] > lat))
                    && (lon < (lonitude[j] - lonitude[i]) * (lat - latitude[i])
                            / (latitude[j] - latitude[i]) + lonitude[i])) {
                contains = !contains;
            }
        }
        return contains;
    }

    public double getNorth() {
        return north;
    }

    public double getSouth() {
        return south;
    }

    public double getEast() {
        return east;
    }

    public double getWest() {
        return west;
    }

}
