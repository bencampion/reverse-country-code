package uk.recurse.geocoding.reverse;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

class SortTileRecursiveTest {

    @Test
    void given16Rectangles_sortTileRecursiveReturnsSameInput() {
        List<Geometry> rectangles = rectangles(16);

        List<Geometry> rTree = SortTileRecursive.pack(rectangles);

        assertSame(rectangles, rTree);
    }

    @Test
    void given17Rectangles_sortTileRecursiveReturnsTwoRectangles() {
        List<Geometry> rectangles = rectangles(17);

        List<Geometry> rTree = SortTileRecursive.pack(rectangles);

        assertAll(
                () -> assertEquals(2, rTree.size()),
                () -> assertEquals(4.0f, rTree.get(0).boundingBox().centroidLatitude()),
                () -> assertEquals(4.0f, rTree.get(0).boundingBox().centroidLongitude()),
                () -> assertEquals(12.5f, rTree.get(1).boundingBox().centroidLatitude()),
                () -> assertEquals(12.5f, rTree.get(1).boundingBox().centroidLongitude())
        );
    }

    @Test
    void given32Rectangles_sortTileRecursiveReturnsTwoRectangles() {
        List<Geometry> rectangles = rectangles(32);

        List<Geometry> rTree = SortTileRecursive.pack(rectangles);

        assertAll(
                () -> assertEquals(2, rTree.size()),
                () -> assertEquals(7.5f, rTree.get(0).boundingBox().centroidLatitude()),
                () -> assertEquals(7.5f, rTree.get(0).boundingBox().centroidLongitude()),
                () -> assertEquals(23.5f, rTree.get(1).boundingBox().centroidLatitude()),
                () -> assertEquals(23.5f, rTree.get(1).boundingBox().centroidLongitude())
        );
    }

    private List<Geometry> rectangles(int n) {
        return IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> new Point(i, i))
                .map(Ring::new)
                .limit(n)
                .collect(toList());
    }
}
