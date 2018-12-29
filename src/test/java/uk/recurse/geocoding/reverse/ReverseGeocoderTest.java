package uk.recurse.geocoding.reverse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.assertEquals;

public class ReverseGeocoderTest {

    private ReverseGeocoder geocoder;

    @DataProvider
    public Iterator<Object[]> cities() {
        InputStream in = ReverseGeocoderTest.class.getResourceAsStream("/baselineCities.txt");
        return new BufferedReader(new InputStreamReader(in, UTF_8)).lines()
                .map(line -> line.split("\t"))
                .map(row -> {
                    float lat = Float.parseFloat(row[0]);
                    float lon = Float.parseFloat(row[1]);
                    return new Object[] { lat, lon, row[2] };
                })
                .iterator();
    }

    @BeforeClass
    public void setup() {
        geocoder = new ReverseGeocoder();
    }

    @Test(dataProvider = "cities")
    public void reverseGeocoding(float lat, float lon, String expectedIso) {
        String actualIso = geocoder.getCountry(lat, lon).map(Country::iso).orElse("null");

        assertEquals(actualIso, expectedIso, "lat=" + lat + " lon=" + lon);
    }

    @Test
    public void streaming() {
        assertEquals(geocoder.countries().count(), 247);
    }
}
