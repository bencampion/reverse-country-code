package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

class MultiPolygon implements Geometry {

    private final Geometry[] geometries;
    private final BoundingBox boundingBox;

    @JsonCreator
    MultiPolygon(@JsonProperty("coordinates") Ring[][] rings) {
        this(Stream.of(rings).map(Polygon::new).toArray(Geometry[]::new));
    }

    MultiPolygon(Geometry[] geometries) {
        this.geometries = geometries;
        boundingBox = new BoundingBox(geometries);
    }

    @Override
    public boolean contains(float lat, float lon) {
        if (boundingBox.contains(lat, lon)) {
            for (Geometry geometry : geometries) {
                if (geometry.contains(lat, lon)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Country getCountry(float lat, float lon) {
        if (boundingBox.contains(lat, lon)) {
            for (Geometry geometry : geometries) {
                Country country = geometry.getCountry(lat, lon);
                if (country != null) {
                    return country;
                }
            }
        }
        return null;
    }

    @Override
    public BoundingBox boundingBox() {
        return boundingBox;
    }

    @Override
    public Stream<Geometry> flatten(Country country) {
        return Stream.of(geometries).flatMap(geometry -> geometry.flatten(country));
    }
}
