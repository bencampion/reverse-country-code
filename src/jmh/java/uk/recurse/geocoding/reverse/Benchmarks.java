package uk.recurse.geocoding.reverse;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;

@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class Benchmarks {

    private ReverseGeocoder geocoder;
    private List<Point> cities;
    private List<Point> random;

    @Setup
    public void prepare() throws IOException {
        geocoder = new ReverseGeocoder();
        cities = cityPoints();
        Collections.shuffle(cities, new Random(0));
        random = randomPoints();
    }

    private List<Point> cityPoints() throws IOException {
        try (InputStream in = Benchmarks.class.getResourceAsStream("/cities.tsv")) {
            return new BufferedReader(new InputStreamReader(in, UTF_8)).lines()
                    .map(line -> line.split("\t"))
                    .map(row -> {
                        float lat = Float.parseFloat(row[0]);
                        float lon = Float.parseFloat(row[1]);
                        return new Point(lat, lon);
                    })
                    .collect(toList());
        }
    }

    private List<Point> randomPoints() {
        List<Point> points = new ArrayList<>(cities.size());
        PrimitiveIterator.OfDouble lats = new Random(0).doubles(-90, 90).iterator();
        PrimitiveIterator.OfDouble lons = new Random(0).doubles(-180, 180).iterator();
        for (int i = 0; i < cities.size(); i++) {
            float lat = (float) lats.nextDouble();
            float lon = (float) lons.nextDouble();
            points.add(new Point(lat, lon));
        }
        return points;
    }

    @Benchmark
    public void cities(Blackhole bh) {
        for (Point point : cities) {
            bh.consume(geocoder.getCountry(point.latitude(), point.longitude()));
        }
    }

    @Benchmark
    public void randomLocations(Blackhole bh) {
        for (Point point : random) {
            bh.consume(geocoder.getCountry(point.latitude(), point.longitude()));
        }
    }
}
