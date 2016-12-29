package uk.recurse.geocoding.reverse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class RTree {

    private static final int PAGE_SIZE = 16;
    private static final Comparator<Geometry> LATITUDE =
            Comparator.comparingDouble(geometry -> geometry.boundingBox().latitude());
    private static final Comparator<Geometry> LONGITUDE =
            Comparator.comparingDouble(geometry -> geometry.boundingBox().longitude());

    // algorithm paper: http://www.dtic.mil/dtic/tr/fulltext/u2/a324493.pdf
    static Geometry[] sortTileRecursive(Geometry[] rectangles) {
        double tiles = (double) rectangles.length / PAGE_SIZE;
        if (tiles <= 1) {
            return rectangles;
        }
        double slices = Math.ceil(Math.sqrt(tiles));
        return tile(Arrays.asList(rectangles), slices);
    }

    private static Geometry[] tile(List<Geometry> rectangles, double slices) {
        List<Geometry> tiles = new ArrayList<>();
        rectangles.sort(LONGITUDE);
        for (List<Geometry> slice : partition(rectangles, slices)) {
            slice.sort(LATITUDE);
            double pages = Math.ceil((double) slice.size() / PAGE_SIZE);
            for (List<Geometry> page : partition(slice, pages)) {
                tiles.add(new MultiPolygon(page.toArray(new Geometry[0])));
            }
        }
        return sortTileRecursive(tiles.toArray(new Geometry[0]));
    }

    private static <T> List<List<T>> partition(List<T> list, double n) {
        int size = (int) Math.ceil(list.size() / n);
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int start = i * size;
            int end = Math.min(start + size, list.size());
            partitions.add(list.subList(start, end));
        }
        return partitions;
    }
}
