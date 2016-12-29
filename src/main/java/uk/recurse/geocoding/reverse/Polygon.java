package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

class Polygon implements Geometry {

    private final Ring ring;
    private final Geometry[] holes;

    @JsonCreator
    Polygon(@JsonProperty("coordinates") Ring[] rings) {
        ring = rings[0];
        holes = RTree.sortTileRecursive(Arrays.copyOfRange(rings, 1, rings.length));
    }

    @Override
    public boolean contains(float lat, float lon) {
        return ring.contains(lat, lon) && !inHoles(lat, lon);
    }

    @Override
    public BoundingBox boundingBox() {
        return ring.boundingBox();
    }

    private boolean inHoles(float lat, float lon) {
        for (Geometry hole : holes) {
            if (hole.contains(lat, lon)) {
                return true;
            }
        }
        return false;
    }
}
