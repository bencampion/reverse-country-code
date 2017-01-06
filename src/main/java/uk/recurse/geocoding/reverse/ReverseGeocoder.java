package uk.recurse.geocoding.reverse;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * A reverse geocoder that converts latitude and longitude coordinates to a country.
 */
public class ReverseGeocoder {

    private final FeatureCollection featureCollection;

    /**
     * Creates a new reverse geocoder. This is an expensive operation as the country boundary data
     * is parsed each time the class constructed.
     */
    public ReverseGeocoder() {
        Class<?> cls = ReverseGeocoder.class;
        try (
                InputStream countryInfo = cls.getResourceAsStream("/countryInfo.txt");
                InputStream shapes = cls.getResourceAsStream("/shapes_simplified_low.json")
        ) {
            featureCollection = load(countryInfo, shapes);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private FeatureCollection load(InputStream countryInfo, InputStream shapes) throws IOException {
        Map<String, Country> countries = Country.load(new InputStreamReader(countryInfo, UTF_8));
        InjectableValues injectables = new InjectableValues.Std().addValue(Map.class, countries);
        return new ObjectMapper()
                .readerFor(FeatureCollection.class)
                .with(injectables)
                .readValue(shapes);
    }

    /**
     * Converts a coordinate into a country.
     *
     * @param lat degrees latitude
     * @param lon degrees longitude
     * @return the country at the given coordinate
     */
    public Optional<Country> getCountry(double lat, double lon) {
        return getCountry((float) lat, (float) lon);
    }

    /**
     * Converts a coordinate into a country.
     *
     * @param lat degrees latitude
     * @param lon degrees longitude
     * @return the country at the given coordinate
     */
    public Optional<Country> getCountry(float lat, float lon) {
        return Optional.ofNullable(featureCollection.getCountry(lat, lon));
    }

    /**
     * Returns all the countries recognised by the reverse geocoder.
     *
     * @return stream of countries
     */
    public Stream<Country> countries() {
        return featureCollection.countries();
    }

}
