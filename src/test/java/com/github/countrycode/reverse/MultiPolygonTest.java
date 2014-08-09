package com.github.countrycode.reverse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

@RunWith(MockitoJUnitRunner.class)
public class MultiPolygonTest {

    private MultiPolygon multiPolygon;
    @Mock
    private Polygon polygon1;
    @Mock
    private Polygon polygon2;

    @Before
    public void setup() {
        when(polygon1.contains(4, 3.5)).thenReturn(true);
        when(polygon1.getBoundingBox()).thenReturn(
                new BoundingBox(new Point(4.5, 4), new Point(2, 3)));
        when(polygon2.contains(1, 3)).thenReturn(true);
        when(polygon2.getBoundingBox()).thenReturn(
                new BoundingBox(new Point(4.5, 3.5), new Point(1, 1)));
        multiPolygon = new MultiPolygon(ImmutableList.of(polygon1, polygon2));
    }

    @Test
    public void in1() {
        assertTrue(multiPolygon.contains(4, 3.5));
    }

    @Test
    public void in2() {
        assertTrue(multiPolygon.contains(1, 3));
    }

    @Test
    public void out() {
        assertFalse(multiPolygon.contains(5, 1));
    }

    @Test
    public void hole() {
        assertFalse(multiPolygon.contains(2, 2.5));
    }

}
