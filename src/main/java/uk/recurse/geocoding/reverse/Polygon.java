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
        ring = rings[0];
        holes = new MultiPolygon(Stream.of(rings).skip(1));
        country = null;
    }

    private Polygon(Polygon polygon, Country country) {
        ring = polygon.ring;
        holes = polygon.holes;
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
        return Stream.of(new Polygon(this, country));
    }
}
