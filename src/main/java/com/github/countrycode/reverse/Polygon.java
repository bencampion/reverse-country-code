package com.github.countrycode.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

class Polygon implements Geometry {

    private final LinearRing ring;
    private final Iterable<LinearRing> holes;

    public Polygon(LinearRing ring) {
        this(ring, Collections.<LinearRing> emptyList());
    }

    public Polygon(LinearRing ring, Collection<LinearRing> holes) {
        this.ring = ring;
        this.holes = new ArrayList<>(holes);
    }

    @Override
    public boolean contains(double lat, double lon) {
        return ring.contains(lat, lon) && !inHoles(lat, lon);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ring.getBoundingBox();
    }

    private boolean inHoles(double lat, double lon) {
        for (LinearRing hole : holes) {
            if (hole.contains(lat, lon)) {
                return true;
            }
        }
        return false;
    }

}
