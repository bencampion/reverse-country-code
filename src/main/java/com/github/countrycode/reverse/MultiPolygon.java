package com.github.countrycode.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class MultiPolygon implements Geometry {

    private final String id;
    private final List<Polygon> polygons;
    private final BoundingBox boundingBox;

    public MultiPolygon(String id, Collection<Polygon> polygons) {
        this.id = id;
        this.polygons = new ArrayList<Polygon>(polygons);
        BoundingBox.Builder builder = new BoundingBox.Builder();
        for (Polygon p : polygons) {
            builder.addBox(p.getBoundingBox());
        }
        boundingBox = builder.build();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean contains(double lat, double lon) {
        return boundingBox.contains(lat, lon) && inPolys(lat, lon);
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
