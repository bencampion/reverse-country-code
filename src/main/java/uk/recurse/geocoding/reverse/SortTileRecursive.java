package uk.recurse.geocoding.reverse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

// algorithm paper: http://www.dtic.mil/dtic/tr/fulltext/u2/a324493.pdf
class SortTileRecursive {

    private static final int PAGE_SIZE = 16;
    private static final Comparator<Geometry> X_COORDINATE =
            Comparator.comparingDouble(geometry -> geometry.boundingBox().centroidLongitude());
    private static final Comparator<Geometry> Y_COORDINATE =
            Comparator.comparingDouble(geometry -> geometry.boundingBox().centroidLatitude());

    static MultiPolygon pack(Stream<? extends Geometry> rectangles) {
        List<Geometry> packed = pack(rectangles.collect(toList()));
        return new MultiPolygon(packed.toArray(new Geometry[0]));
    }

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
                nodes.add(new MultiPolygon(run.toArray(new Geometry[0])));
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
