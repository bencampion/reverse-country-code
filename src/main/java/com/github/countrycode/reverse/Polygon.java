package com.github.countrycode.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class Polygon implements Geometry {

    private final List<Point> ring;
    private final List<Polygon> holes;
    private final BoundingBox boundingBox;

    public Polygon(List<Point> ring) {
        this(ring, Collections.<Polygon> emptyList());
    }

    public Polygon(List<Point> ring, Collection<Polygon> holes) {
        this.ring = new ArrayList<>(ring);
        this.holes = new ArrayList<>(holes);
        boundingBox = new BoundingBox.Builder().addPoints(ring).build();
    }

    @Override
    public boolean contains(double lat, double lon) {
        return boundingBox.contains(lat, lon) && inPoly(lat, lon)
                && !inHoles(lat, lon);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
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
        for (int i = 0, j = ring.size() - 1; i < ring.size(); j = i++) {
            final Point p_i = ring.get(i);
            final Point p_j = ring.get(j);
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
