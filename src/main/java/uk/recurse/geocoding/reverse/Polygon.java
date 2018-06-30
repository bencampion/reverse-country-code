package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

class Polygon implements Geometry {

    private final Ring ring;
    private final Geometry holes;
    private final Country country;

    @JsonCreator
    Polygon(@JsonProperty("coordinates") Ring[] rings) {
        this(rings[0], SortTileRecursive.pack(Stream.of(rings).skip(1)), null);
    }

    private Polygon(Ring ring, Geometry holes, Country country) {
        this.ring = ring;
        this.holes = holes;
        this.country = country;
    }

    @Override
    public boolean contains(float lat, float lon) {
        return ring.contains(lat, lon) && !holes.contains(lat, lon);
    }

    @Override
    public Country getCountry(float lat, float lon) {
        return contains(lat, lon) ? country : null;
    }

    @Override
    public BoundingBox boundingBox() {
        return ring.boundingBox();
    }

    @Override
    public Stream<Geometry> flatten(Country country) {
        return Stream.of(new Polygon(ring, holes, country));
    }
}
