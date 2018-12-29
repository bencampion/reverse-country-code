package uk.recurse.geocoding.reverse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ReverseGeocoderTest {

    private static ReverseGeocoder geocoder;

    @BeforeAll
    static void setup() {
        geocoder = new ReverseGeocoder();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/baselineCities.csv")
    void reverseGeocoding(float lat, float lon, String expectedIso) {
        String actualIso = geocoder.getCountry(lat, lon)
                .map(Country::iso)
                .orElseGet(() -> fail("Country not found"));

        assertEquals(expectedIso, actualIso, "lat=" + lat + " lon=" + lon);
    }

    @Test
    void streaming() {
        assertEquals(247, geocoder.countries().count());
    }
}
