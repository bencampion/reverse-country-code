package uk.recurse.geocoding.reverse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.zip.ZipInputStream;

import static java.nio.charset.StandardCharsets.UTF_8;

public class BaselineGenerator {

    public static void main(String[] args) throws IOException {
        ReverseGeocoder geocoder = new ReverseGeocoder();
        int pass = 0;
        int fail = 0;
        try (
                InputStream in = new URL("https://download.geonames.org/export/dump/cities15000.zip").openStream();
                Writer baseline = Files.newBufferedWriter(Paths.get(args[0], "baselineCities.csv"))
        ) {
            ZipInputStream zipStream = new ZipInputStream(in);
            zipStream.getNextEntry();
            BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream, UTF_8));
            for (Iterator<String> lines = reader.lines().iterator(); lines.hasNext(); ) {
                String line = lines.next();
                String[] row = line.split("\t");
                float lat = Float.parseFloat(row[4]);
                float lon = Float.parseFloat(row[5]);
                String iso = row[8];
                Optional<String> output = geocoder.getCountry(lat, lon).map(Country::iso);
                if (output.filter(iso::equals).isPresent()) {
                    baseline.write(String.join(",", row[4], row[5], output.get()));
                    baseline.write('\n');
                    pass++;
                } else {
                    fail++;
                }
            }
        }
        double percent = (double) pass / (pass + fail);
        System.out.printf("\nPassed=%d Failed=%d (%f%%)\n", pass, fail, percent * 100);
    }
}
