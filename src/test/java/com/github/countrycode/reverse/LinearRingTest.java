package com.github.countrycode.reverse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class LinearRingTest {

    private LinearRing ring;

    @Before
    public void setup() throws ParseException {
        List<Point> points = ImmutableList
                .<Point> builder()
                    .add(new Point(1, 3.5))
                    .add(new Point(2, 1))
                    .add(new Point(4, 1.5))
                    .add(new Point(4.5, 4.5))
                    .add(new Point(1, 3.5))
                    .build();
        ring = new LinearRing(points);
    }

    @Test
    public void insideRing() {
        assertTrue(ring.contains(4, 3));
    }

    @Test
    public void outsideRing() {
        assertFalse(ring.contains(2, 4));
    }

}
