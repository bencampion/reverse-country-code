package uk.recurse.geocoding.reverse;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.testng.Assert.assertEquals;


public class CountryTest {

    private static final String INPUT = "GB\tGBR\t826\tUK\tUnited Kingdom\tLondon\t244820\t62348447\tEU\t.uk\tGBP\tPound\t44\t@# #@@|@## #@@|@@# #@@|@@## #@@|@#@ #@@|@@#@ #@@|GIR0AA\t^((?:(?:[A-PR-UWYZ][A-HK-Y]\\d[ABEHMNPRV-Y0-9]|[A-PR-UWYZ]\\d[A-HJKPS-UW0-9])\\s\\d[ABD-HJLNP-UW-Z]{2})|GIR\\s?0AA)$\ten-GB,cy-GB,gd\t2635167\tIE";

    private Country uk;

    @BeforeClass
    public void setup() {
        uk = Country.load(new StringReader(INPUT)).get("2635167");
    }

    @Test
    public void iso() {
        assertEquals(uk.iso(), "GB");
    }

    @Test
    public void iso3() {
        assertEquals(uk.iso3(), "GBR");
    }

    @Test
    public void isoNumeric() {
        assertEquals(uk.isoNumeric(), 826);
    }

    @Test
    public void name() {
        assertEquals(uk.name(), "United Kingdom");
    }

    @Test
    public void area() {
        assertEquals(uk.area(), 244820f);
    }

    @Test
    public void population() {
        assertEquals(uk.population(), 62348447);
    }

    @Test
    public void continent() {
        assertEquals(uk.continent(), "EU");
    }

    @Test
    public void locales() {
        List<Locale> locales = Arrays.asList(
                new Locale("en","GB"),
                new Locale("cy", "GB"),
                new Locale("gd")
        );
        assertEquals(uk.locales(), locales);
    }
}