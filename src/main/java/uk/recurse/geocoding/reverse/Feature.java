package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
class Feature {

    private final Country country;
    private final Geometry geometry;

    @JsonCreator
    Feature(
            @JacksonInject Map<String, Country> countries,
            @JsonProperty("properties") Map<String, String> properties,
            @JsonProperty("geometry") Geometry geometry
    ) {
        String id = properties.get("geoNameId");
        country = countries.get(id);
        this.geometry = geometry;
    }

    Country country() {
        return country;
    }

    Stream<Geometry> geometries() {
        return geometry.flatten(country);
    }
}
