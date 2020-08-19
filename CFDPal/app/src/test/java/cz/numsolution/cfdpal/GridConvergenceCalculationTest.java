package cz.numsolution.cfdpal;

import org.junit.Before;
import org.junit.Test;

import cz.numsolution.cfdpal.model.CellHeightCalculation;
import cz.numsolution.cfdpal.model.GridConvergenceCalculation;
import cz.numsolution.cfdpal.model.TurbulentQuantitiesCalculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public class GridConvergenceCalculationTest {


    private GridConvergenceCalculation mCalculation;

    @Before
    public void setUp() throws Exception {
        mCalculation = new GridConvergenceCalculation();
    }

    @Test
    public void calculate() {
        mCalculation.calculate();

        assertThat(mCalculation.getOrder(), is(closeTo(1.78617,
                Utils.EPS)));
        assertThat(mCalculation.getExtrapolated(), is(closeTo(0.97130,
                Utils.EPS)));
        assertThat(mCalculation.getIndex21(), is(closeTo(0.10329,
                Utils.EPS)));
        assertThat(mCalculation.getIndex32(), is(closeTo(0.35875,
                Utils.EPS)));
        assertThat(mCalculation.getLower(), is(closeTo(0.97030,
                Utils.EPS)));
        assertThat(mCalculation.getUpper(), is(closeTo(0.97230,
                Utils.EPS)));
    }
}