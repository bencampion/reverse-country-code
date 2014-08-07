package com.github.countrycode.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class LinearRing implements Geometry {
    
    private final List<Point> points;
    private final BoundingBox boundingBox;

    public LinearRing(Collection<Point> points) {
        this.points = new ArrayList<>(points);
        boundingBox = new BoundingBox.Builder().addPoints(points).build();
    }

    @Override
    public boolean contains(double lat, double lon) {
        return boundingBox.contains(lat, lon) && pnpoly(lat, lon);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    private boolean pnpoly(double lat, double lon) {
        boolean contains = false;
        for (int i = 0, j = points.size() - 1; i < points.size(); j = i++) {
            final Point p_i = points.get(i);
            final Point p_j = points.get(j);
            if (((p_i.getLatitude() > lat) != (p_j.getLatitude() > lat))
                    && (lon < (p_j.getLongitude() - p_i.getLongitude())
                            * (lat - p_i.getLatitude())
                            / (p_j.getLatitude() - p_i.getLatitude())
                            + p_i.getLongitude())) {
                contains = !contains;
            }
        }
        return contains;
    }

}
