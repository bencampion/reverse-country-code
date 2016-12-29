package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

class MultiPolygon implements Geometry {

    private final Geometry[] geometries;
    private final BoundingBox boundingBox;

    @JsonCreator
    MultiPolygon(@JsonProperty("coordinates") Ring[][] rings) {
        this(RTree.sortTileRecursive(Stream.of(rings)
                .map(Polygon::new)
                .toArray(Polygon[]::new)));
    }

    MultiPolygon(Geometry[] geometries) {
        this.geometries = geometries;
        boundingBox = new BoundingBox(this.geometries);
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
    public BoundingBox boundingBox() {
        return boundingBox;
    }
}
