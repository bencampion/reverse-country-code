package uk.recurse.geocoding.reverse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.testng.Assert.assertEquals;

public class ReverseGeocoderIT {

    private ReverseGeocoder geocoder;

    @DataProvider
    public Iterator<Object[]> cities() {
        InputStream in = ReverseGeocoderIT.class.getResourceAsStream("/baselineCities.txt");
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

    public static void main(String[] args) throws IOException {
        System.out.println("Generating baseline cities...");
        ReverseGeocoder geocoder = new ReverseGeocoder();
        int pass = 0;
        int fail = 0;
        try (
                InputStream in = ReverseGeocoderIT.class.getResourceAsStream("/cities15000.txt");
                Writer baseline = Files.newBufferedWriter(Paths.get(args[0],"baselineCities.txt"))
        ) {
            Stream<String> stream = new BufferedReader(new InputStreamReader(in, UTF_8)).lines();
            for (Iterator<String> lines = stream.iterator(); lines.hasNext();) {
                String line = lines.next();
                String[] row = line.split("\t");
                float lat = Float.parseFloat(row[4]);
                float lon = Float.parseFloat(row[5]);
                String iso = row[8];
                Optional<String> output = geocoder.getCountry(lat, lon).map(Country::iso);
                if (output.filter(iso::equals).isPresent()) {
                    baseline.write(String.join("\t", row[4], row[5], output.get()));
                    baseline.write('\n');
                    pass++;
                } else {
                    String format = "Failure: %s, %s (lat=%f lon=%f) [%s]\n";
                    System.out.printf(format, row[1], iso, lat, lon, output.orElse("empty"));
                    fail++;
                }
            }
        }
        double percent = (double) pass / (pass + fail);
        System.out.printf("\nPassed=%d Failed=%d (%f%%)\n", pass, fail, percent * 100);
    }
}
