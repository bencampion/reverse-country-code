package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class MultiPolygon implements Geometry {

    private final Geometry[] geometries;
    private final BoundingBox boundingBox;

    @JsonCreator
    MultiPolygon(@JsonProperty("coordinates") Ring[][] rings) {
        this(Stream.of(rings).map(Polygon::new));
    }

    MultiPolygon(Stream<? extends Geometry> geometries) {
        this(SortTileRecursive.pack(geometries.collect(toList())));
    }

    private MultiPolygon(List<Geometry> geometries) {
        this.geometries = geometries.toArray(new Geometry[geometries.size()]);
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

    // algorithm paper: http://www.dtic.mil/dtic/tr/fulltext/u2/a324493.pdf
    static class SortTileRecursive {

        private static final int PAGE_SIZE = 16;
        private static final Comparator<Geometry> X_COORDINATE =
                Comparator.comparingDouble(geometry -> geometry.boundingBox().longitude());
        private static final Comparator<Geometry> Y_COORDINATE =
                Comparator.comparingDouble(geometry -> geometry.boundingBox().latitude());

        static List<Geometry> pack(List<Geometry> rectangles) {
            int leafPages = ceilDiv(rectangles.size(), PAGE_SIZE);
            if (leafPages <= 1) {
                return rectangles;
            }
            int verticalSlices = ceilSqrt(leafPages);
            List<Geometry> nodes = new ArrayList<>(leafPages);
            rectangles.sort(X_COORDINATE);
            for (List<Geometry> verticalSlice : partition(rectangles, verticalSlices)) {
                verticalSlice.sort(Y_COORDINATE);
                int runs = ceilDiv(verticalSlice.size(), PAGE_SIZE);
                for (List<Geometry> run : partition(verticalSlice, runs)) {
                    nodes.add(new MultiPolygon(run));
                }
            }
            return pack(nodes);
        }

        private static <T> List<List<T>> partition(List<T> list, int n) {
            int size = ceilDiv(list.size(), n);
            List<List<T>> partitions = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                int start = i * size;
                int end = Math.min(start + size, list.size());
                partitions.add(list.subList(start, end));
            }
            return partitions;
        }

        private static int ceilDiv(double x, double y) {
            return (int) Math.ceil(x / y);
        }

        private static int ceilSqrt(double a) {
            return (int) Math.ceil(Math.sqrt(a));
        }
    }
}
