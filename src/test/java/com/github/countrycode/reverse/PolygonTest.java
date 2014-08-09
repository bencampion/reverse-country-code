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
public class PolygonTest {

    private Polygon polygon;
    @Mock
    private LinearRing ring;
    @Mock
    private LinearRing hole;

    @Before
    public void setup() {
        when(ring.contains(4, 3)).thenReturn(true);
        when(ring.contains(3, 3)).thenReturn(true);
        when(hole.contains(3, 3)).thenReturn(true);
        polygon = new Polygon(ring, ImmutableList.of(hole));
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
