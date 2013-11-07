package com.github.countrycode.reverse;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReverseCountryCode {

    private final String PROPERTIES_FILE = "/polygons.properties";

    private List<Geometry> geometries = new ArrayList<Geometry>();

    public ReverseCountryCode() {
        Properties polys = new Properties();
        InputStream is = getClass().getResourceAsStream(PROPERTIES_FILE);
        try {
            polys.load(is);
            for (String id : polys.stringPropertyNames()) {
                String wkt = polys.getProperty(id);
                WktParser parser = new WktParser(new StringReader(wkt));
                geometries.add(parser.parse(id));
            }
        } catch (TokenMgrError e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
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

    public String getCountryCode(double x, double y) {
        for (Geometry g : geometries) {
            if (g.contains(x, y)) {
                return g.getId();
            }
        }
        return null;
    }

}
