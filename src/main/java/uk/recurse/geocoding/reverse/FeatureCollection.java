package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
class FeatureCollection {

    private final Geometry world;
    private final Country[] countries;

    @JsonCreator
    FeatureCollection(@JsonProperty("features") Feature[] features) {
        world = SortTileRecursive.pack(Stream.of(features).flatMap(Feature::geometries));
        countries = Stream.of(features)
                .map(Feature::country)
                .toArray(Country[]::new);
    }

    Country getCountry(float lat, float lon) {
        return world.getCountry(lat, lon);
    }

    Stream<Country> countries() {
        return Stream.of(countries);
    }
}
