package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@JsonIgnoreProperties(ignoreUnknown = true)
class FeatureCollection {

    private static final Comparator<Feature> POPULATION =
            Comparator.comparingInt(feature -> feature.country().population());

    private final Map<BoundingBox, Feature[]> featureMap;

    @JsonCreator
    FeatureCollection(@JsonProperty("features") Feature[] features) {
        featureMap = partitionByContinent(features);
    }

    private Map<BoundingBox, Feature[]> partitionByContinent(Feature[] features) {
        return Stream.of(features)
                .sorted(POPULATION.reversed()) // performance bias towards populous countries
                .collect(groupingBy(feature -> feature.country().continent()))
                .values()
                .stream()
                .map(continent -> continent.toArray(new Feature[0]))
                .collect(toMap(this::continentBoundingBox, Function.identity()));
    }

    private BoundingBox continentBoundingBox(Feature[] continent) {
        Geometry[] countries = Stream.of(continent)
                .map(Feature::geometry)
                .toArray(Geometry[]::new);
        return new BoundingBox(countries);
    }

    Feature getFeature(float lat, float lon) {
        for (Map.Entry<BoundingBox, Feature[]> entry: featureMap.entrySet()) {
            if (entry.getKey().contains(lat, lon)) {
                for (Feature feature : entry.getValue()) {
                    if (feature.contains(lat, lon)) {
                        return feature;
                    }
                }
            }
        }
        return null;
    }

    Stream<Feature> stream() {
         return featureMap.values().stream().flatMap(Stream::of);
    }
}
