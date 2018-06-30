package uk.recurse.geocoding.reverse;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Information about a country.
 */
public class Country {

    private final String iso;
    private final String iso3;
    private final int isoNumeric;
    private final String name;
    private final float area;
    private final int population;
    private final String continent;
    private final List<Locale> locales;

    private Country(String[] row) {
        iso = row[0];
        iso3 = row[1];
        isoNumeric = Integer.parseInt(row[2]);
        name = row[4];
        area = Float.parseFloat(row[6]);
        population = Integer.parseInt(row[7]);
        continent = row[8];
        locales = Stream.of(row[15].split(","))
                .map(Locale::forLanguageTag)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    static Map<String, Country> load(Reader reader) {
        return new BufferedReader(reader).lines()
                .filter(line -> !line.startsWith("#"))
                .map(line -> line.split("\t"))
                .collect(toMap(row -> row[16], Country::new));
    }

    /**
     * Returns the ISO two letter country code.
     *
     * @return ISO 3166-1 alpha-2 code
     */
    public String iso() {
        return iso;
    }

    /**
     * Returns the ISO three letter country code.
     *
     * @return ISO 3166-1 alpha-3 code
     */
    public String iso3() {
        return iso3;
    }

    /**
     * Returns the ISO three digit county code.
     *
     * @return ISO 3166-1 numeric code
     */
    public int isoNumeric() {
        return isoNumeric;
    }

    /**
     * Returns the country name.
     *
     * @return country name in English
     */
    public String name() {
        return name;
    }

    /**
     * Returns the country size.
     *
     * @return area in sq km
     */
    public float area() {
        return area;
    }

    /**
     * Returns the country population.
     *
     * @return population (estimate)
     */
    public int population() {
        return population;
    }

    /**
     * Returns the continent the country is in.
     *
     * @return two letter continent code
     */
    public String continent() {
        return continent;
    }

    /**
     * Returns the country locales.
     *
     * @return locales ordered by the number of speakers
     */
    public List<Locale> locales() {
        return locales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Country country = (Country) o;
        return Objects.equals(iso, country.iso);
    }

    @Override
    public int hashCode() {
        return iso.hashCode();
    }

    @Override
    public String toString() {
        return iso + " (" + name + ")";
    }
}
