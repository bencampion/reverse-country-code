package com.github.countrycode.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class MultiPolygon implements Geometry {

    private final List<Polygon> polygons;
    private final BoundingBox boundingBox;

    public MultiPolygon(Collection<Polygon> polygons) {
        this.polygons = new ArrayList<>(polygons);
        boundingBox = new BoundingBox.Builder().addGeometries(polygons).build();
    }

    @Override
    public boolean contains(double lat, double lon) {
        return boundingBox.contains(lat, lon) && inPolys(lat, lon);
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    private boolean inPolys(double lat, double lon) {
        for (Polygon p : polygons) {
            if (p.contains(lat, lon)) {
                return true;
            }
        }
        return false;
    }

}
