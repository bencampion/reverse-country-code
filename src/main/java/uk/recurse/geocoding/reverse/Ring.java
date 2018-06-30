package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.stream.Stream;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
class Ring implements Geometry {

    private final float[] latitude;
    private final float[] longitude;
    private final BoundingBox boundingBox;

    @JsonCreator
    Ring(Point... points) {
        latitude = new float[points.length];
        longitude = new float[points.length];
        for (int i = 0; i < points.length; i++) {
            latitude[i] = points[i].latitude();
            longitude[i] = points[i].longitude();
        }
        boundingBox = new BoundingBox(Stream.of(points));
    }

    @Override
    public boolean contains(float lat, float lon) {
        return boundingBox.contains(lat, lon) && pnpoly(lat, lon);
    }

    @Override
    public Country getCountry(float lat, float lon) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BoundingBox boundingBox() {
        return boundingBox;
    }

    @Override
    public Stream<Geometry> flatten(Country country) {
        throw new UnsupportedOperationException();
    }

    // algorithm notes: https://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
    private boolean pnpoly(float lat, float lon) {
        boolean contains = false;
        for (int i = 0, j = latitude.length - 1; i < latitude.length; j = i++) {
            if (((latitude[i] > lat) != (latitude[j] > lat))
                    && (lon < (longitude[j] - longitude[i])
                    * (lat - latitude[i])
                    / (latitude[j] - latitude[i])
                    + longitude[i])) {
                contains = !contains;
            }
        }
        return contains;
    }
}
