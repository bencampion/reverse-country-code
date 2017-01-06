package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.util.stream.Stream;

@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({@Type(Polygon.class), @Type(MultiPolygon.class)})
interface Geometry {

    boolean contains(float lat, float lon);

    Country getCountry(float lat, float lon);

    BoundingBox boundingBox();

    Stream<Geometry> flatten(Country country);
}
