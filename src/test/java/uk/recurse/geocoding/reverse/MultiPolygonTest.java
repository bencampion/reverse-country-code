package uk.recurse.geocoding.reverse;

import org.testng.annotations.Test;
import uk.recurse.geocoding.reverse.MultiPolygon.SortTileRecursive;

import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

public class MultiPolygonTest {

    @Test
    public void given16Rectangles_sortTileRecursiveReturnsSameInput() {
        List<Geometry> rectangles = rectangles(16);

        List<Geometry> rTree = SortTileRecursive.pack(rectangles);

        assertSame(rTree, rectangles);
    }

    @Test
    public void given17Rectangles_sortTileRecursiveReturnsTwoRectangles() {
        List<Geometry> rectangles = rectangles(17);

        List<Geometry> rTree = SortTileRecursive.pack(rectangles);

        assertEquals(rTree.size(), 2);
        assertEquals(rTree.get(0).boundingBox().latitude(), 4.0f);
        assertEquals(rTree.get(0).boundingBox().longitude(), 4.0f);
        assertEquals(rTree.get(1).boundingBox().latitude(), 12.5f);
        assertEquals(rTree.get(1).boundingBox().longitude(), 12.5f);
    }

    @Test
    public void given32Rectangles_sortTileRecursiveReturnsTwoRectangles() {
        List<Geometry> rectangles = rectangles(32);

        List<Geometry> rTree = SortTileRecursive.pack(rectangles);

        assertEquals(rTree.size(), 2);
        assertEquals(rTree.get(0).boundingBox().latitude(), 7.5f);
        assertEquals(rTree.get(0).boundingBox().longitude(), 7.5f);
        assertEquals(rTree.get(1).boundingBox().latitude(), 23.5f);
        assertEquals(rTree.get(1).boundingBox().longitude(), 23.5f);
    }

    private List<Geometry> rectangles(int n) {
        return IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> new Point(i, i))
                .map(p -> new Ring(new Point[]{p}))
                .limit(n)
                .collect(toList());
    }
}
