package com.github.countrycode.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class MultiPolygon implements Geometry {

    private final String id;
    private final List<Polygon> polygons;
    private double north = Double.NEGATIVE_INFINITY;
    private double south = Double.POSITIVE_INFINITY;
    private double east = Double.POSITIVE_INFINITY;
    private double west = Double.NEGATIVE_INFINITY;

    public MultiPolygon(String id, Collection<Polygon> polygons) {
        this.id = id;
        this.polygons = new ArrayList<Polygon>(polygons.size());
        for (Polygon p : polygons) {
            this.polygons.add(p);
            north = Math.max(p.getNorth(), north);
            south = Math.min(p.getSouth(), south);
            east = Math.min(p.getEast(), east);
            west = Math.max(p.getWest(), west);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean contains(double lat, double lon) {
        return inBox(lat, lon) && inPolys(lat, lon);
    }

    private boolean inBox(double lat, double lon) {
        return lat <= north && lat >= south && lon >= east && lon <= west;
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
