package com.github.countrycode.reverse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Finds the two letter ISO country code (ISO 3166-1 alpha-2) for any given
 * point in degrees latitude and longitude.
 */
public class ReverseCountryCode {

    private static final String PROPERTIES_FILE = "/polygons.properties";

    private final List<Country> countries = new ArrayList<>();

    /**
     * Creates a new reverse country code object. This is an expensive operation
     * as the country boundary data is parsed each time the class instantiated.
     */
    public ReverseCountryCode() {
        Properties polys = new Properties();
        try (InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE)) {
            polys.load(is);
            load(polys);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void load(Properties polys) {
        try {
            for (String code : polys.stringPropertyNames()) {
                String wkt = polys.getProperty(code);
                WktParser parser = new WktParser(new StringReader(wkt));
                Geometry geometry = parser.parse();
                countries.add(new Country(code, geometry));
            }
        } catch (TokenMgrError | ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Returns the ISO country code of any given point.
     *
     * @param lat
     *            degrees latitude
     * @param lon
     *            degrees longitude
     * @return two letter ISO country code or null if the point is not in a
     *         country
     */
    public String getCountryCode(double lat, double lon) {
        for (Country c : countries) {
            if (c.contains(lat, lon)) {
                return c.getCode();
            }
        }
        return null;
    }

}
