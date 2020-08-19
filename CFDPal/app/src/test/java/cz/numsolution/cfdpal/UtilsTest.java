package cz.numsolution.cfdpal;

import org.junit.Test;

import androidx.arch.core.util.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void isPositiveNumber() {
        assertThat(Utils.isPositiveNumber(""), is(false));
        assertThat(Utils.isPositiveNumber(null), is(false));
        assertThat(Utils.isPositiveNumber("-1"), is(false));
        assertThat(Utils.isPositiveNumber("1"), is(true));
    }

    @Test
    public void secant() {
        Function<Double, Double> fce = (Double x) -> x * x - 4;
        assertThat(Utils.secant(fce, 10.0), is(closeTo(2.0, Utils.EPS)));
    }
}