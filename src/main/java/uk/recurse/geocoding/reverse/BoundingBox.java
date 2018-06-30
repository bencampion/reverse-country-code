package uk.recurse.geocoding.reverse;

import java.util.DoubleSummaryStatistics;
import java.util.stream.Stream;

class BoundingBox {

    private final Point max;
    private final Point min;

    BoundingBox(Geometry[] geometries) {
        this(Stream.of(geometries).map(Geometry::boundingBox).flatMap(BoundingBox::points));
    }

    BoundingBox(Stream<Point> points) {
        DoubleSummaryStatistics lat = new DoubleSummaryStatistics();
        DoubleSummaryStatistics lon = new DoubleSummaryStatistics();
        points.forEach(point -> {
            lat.accept(point.latitude());
            lon.accept(point.longitude());
        });
        max = new Point((float) lat.getMax(), (float) lon.getMax());
        min = new Point((float) lat.getMin(), (float) lon.getMin());
    }

    boolean contains(float lat, float lon) {
        return lat <= max.latitude() && lon <= max.longitude()
                && lat >= min.latitude() && lon >= min.longitude();
    }

    float centroidLatitude() {
        return (max.latitude() + min.latitude()) / 2;
    }

    float centroidLongitude() {
        return (max.longitude() + min.longitude()) / 2;
    }

    private Stream<Point> points() {
        return Stream.of(max, min);
    }
}
