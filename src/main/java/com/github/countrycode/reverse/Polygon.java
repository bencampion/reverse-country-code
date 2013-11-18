package com.github.countrycode.reverse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class Polygon implements Geometry {

    private final double[] latitude;
    private final double[] longitude;
    private final List<Polygon> holes;
    private final BoundingBox boundingBox;

    public Polygon(List<Point> ring) {
        this(ring, Collections.<Polygon> emptyList());
    }

    public Polygon(List<Point> ring, Collection<Polygon> holes) {
        latitude = new double[ring.size()];
        longitude = new double[ring.size()];
        populateArrays(ring);
        boundingBox = createBoundingBox(ring);
        this.holes = new ArrayList<Polygon>(holes);
    }

    private void populateArrays(List<Point> ring) {
        for (int i = 0; i < ring.size(); i++) {
            Point point = ring.get(i);
            latitude[i] = point.getLatitude();
            longitude[i] = point.getLongitude();
        }
    }

    private BoundingBox createBoundingBox(List<Point> ring) {
        BoundingBox.Builder builder = new BoundingBox.Builder();
        for (Point point : ring) {
            builder.addPoint(point);
        }
        return builder.build();
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
        for (int i = 0, j = longitude.length - 1; i < longitude.length; j = i++) {
            if (((latitude[i] > lat) != (latitude[j] > lat))
                    && (lon < (longitude[j] - longitude[i])
                            * (lat - latitude[i]) / (latitude[j] - latitude[i])
                            + longitude[i])) {
                contains = !contains;
            }
        }
        return contains;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
