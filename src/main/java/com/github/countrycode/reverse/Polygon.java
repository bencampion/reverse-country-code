package com.github.countrycode.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class Polygon implements Geometry {

    private final double[] latitude;
    private final double[] lonitude;
    private final List<Polygon> holes;
    private final BoundingBox boundingBox;

    public Polygon(List<Point> ring) {
        this(ring, Collections.<Polygon> emptyList());
    }

    public Polygon(List<Point> ring, Collection<Polygon> holes) {
        int n = ring.size();
        latitude = new double[n];
        lonitude = new double[n];
        BoundingBox.Builder builder = new BoundingBox.Builder();
        for (int i = 0; i < n; i++) {
            double lat = ring.get(i).getLatitude();
            double lon = ring.get(i).getLongitude();
            latitude[i] = lat;
            lonitude[i] = lon;
            builder.addPoint(ring.get(i));
        }
        boundingBox = builder.build();
        this.holes = new ArrayList<Polygon>(holes);
    }

    @Override
    public boolean contains(double lat, double lon) {
        return boundingBox.contains(lat, lon) && inPoly(lat, lon)
                && !inHoles(lat, lon);
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

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
