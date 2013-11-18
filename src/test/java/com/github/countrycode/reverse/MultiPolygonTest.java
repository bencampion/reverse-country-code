package com.github.countrycode.reverse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class MultiPolygonTest {

    private Geometry polygon;

    @Before
    public void setup() throws ParseException {
        String wkt = "MULTIPOLYGON (((4 4, 2 4.5, 4.5 3, 4 4)), ((2 3.5, 4.5 2, 3 0.5, 1 1, 1 3, 2 3.5), (3 2, 2 2.5, 2 1.5, 3 2)))";
        WktParser parser = new WktParser(new StringReader(wkt));
        polygon = parser.parse();
    }

    @Test
    public void in1() {
        assertTrue(polygon.contains(4, 3.5));
    }

    @Test
    public void in2() {
        assertTrue(polygon.contains(1, 3));
    }

    @Test
    public void out() {
        assertFalse(polygon.contains(5, 1));
    }

    @Test
    public void hole() {
        assertFalse(polygon.contains(2, 2.5));
    }

}
