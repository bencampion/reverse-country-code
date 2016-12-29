package uk.recurse.geocoding.reverse;

import org.testng.annotations.Test;

import java.util.stream.IntStream;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

public class RTreeTest {

    @Test
    public void pageSize() {
        Geometry[] rectangles = rectangles(16);

        Geometry[] rTree = RTree.sortTileRecursive(rectangles);

        assertSame(rTree, rectangles);
    }

    @Test
    public void pageSizePlusOne() {
        Geometry[] rectangles = rectangles(17);

        Geometry[] rTree = RTree.sortTileRecursive(rectangles);

        assertEquals(rTree.length, 2);
        assertEquals(rTree[0].boundingBox().latitude(), 4.0f);
        assertEquals(rTree[0].boundingBox().longitude(), 4.0f);
        assertEquals(rTree[1].boundingBox().latitude(), 12.5f);
        assertEquals(rTree[1].boundingBox().longitude(), 12.5f);
    }

    @Test
    public void pageSizeTimesTwo() {
        Geometry[] rectangles = rectangles(32);

        Geometry[] rTree = RTree.sortTileRecursive(rectangles);

        assertEquals(rTree.length, 2);
        assertEquals(rTree[0].boundingBox().latitude(), 7.5f);
        assertEquals(rTree[0].boundingBox().longitude(), 7.5f);
        assertEquals(rTree[1].boundingBox().latitude(), 23.5f);
        assertEquals(rTree[1].boundingBox().longitude(), 23.5f);
    }

    private Geometry[] rectangles(int n) {
        return IntStream.iterate(0, i -> i + 1)
                .mapToObj(i -> new Point(i, i))
                .map(p -> new Ring(new Point[]{p}))
                .limit(n)
                .toArray(Geometry[]::new);
    }
}
