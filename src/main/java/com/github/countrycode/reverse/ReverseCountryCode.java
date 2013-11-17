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

    private final List<Geometry> geometries = new ArrayList<Geometry>();

    /**
     * Creates a new reverse country code object. This is an expensive operation
     * as the country boundary data is parsed each time the class instantiated.
     */
    public ReverseCountryCode() {
        this(ReverseCountryCode.class.getResourceAsStream(PROPERTIES_FILE));
    }

    private ReverseCountryCode(InputStream is) {
        Properties polys = new Properties();
        try {
            polys.load(is);
            load(polys);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void load(Properties polys) {
        try {
            for (String id : polys.stringPropertyNames()) {
                String wkt = polys.getProperty(id);
                WktParser parser = new WktParser(new StringReader(wkt));
                geometries.add(parser.parse(id));
            }
        } catch (TokenMgrError e) {
            throw new IllegalArgumentException(e);
        } catch (ParseException e) {
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
        for (Geometry g : geometries) {
            if (g.contains(lat, lon)) {
                return g.getId();
            }
        }
        return null;
    }

}
