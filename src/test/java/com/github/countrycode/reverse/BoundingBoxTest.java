package com.github.countrycode.reverse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BoundingBoxTest {

    private BoundingBox box;

    @Before
    public void setup() throws ParseException {
        box = new BoundingBox(new Point(4.5, 4.5), new Point(1, 1));
    }

    @Test
    public void onLowerCorner() {
        assertTrue(box.contains(1, 1));
    }

    @Test
    public void outsideLowerCorner() {
        assertFalse(box.contains(0.5, 0.5));
    }

    @Test
    public void onUpperCorner() {
        assertTrue(box.contains(4.5, 4.5));
    }

    @Test
    public void outsideUpperCorner() {
        assertFalse(box.contains(5, 5));
    }
}
