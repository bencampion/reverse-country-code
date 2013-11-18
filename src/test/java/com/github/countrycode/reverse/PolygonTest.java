package com.github.countrycode.reverse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

public class PolygonTest {

    private Geometry polygon;

    @Before
    public void setup() throws ParseException {
        String wkt = "POLYGON ((3.5 1, 1 2, 1.5 4, 4.5 4.5, 3.5 1), (2 3, 3.5 3.5, 3 2, 2 3))";
        WktParser parser = new WktParser(new StringReader(wkt));
        polygon = parser.parse();
    }

    @Test
    public void in() {
        assertTrue(polygon.contains(4, 3));
    }

    @Test
    public void out() {
        assertFalse(polygon.contains(2, 4));
    }

    @Test
    public void hole() {
        assertFalse(polygon.contains(3, 3));
    }

}
