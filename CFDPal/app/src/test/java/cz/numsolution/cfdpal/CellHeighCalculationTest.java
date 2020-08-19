package cz.numsolution.cfdpal;

import org.junit.Before;
import org.junit.Test;

import cz.numsolution.cfdpal.model.CellHeightCalculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public class CellHeighCalculationTest {


    private CellHeightCalculation mCalculation;

    @Before
    public void setUp() throws Exception {
        mCalculation = new CellHeightCalculation();
    }

    @Test
    public void calculate() {
        mCalculation.calculate();

        assertThat(mCalculation.getReynoldsNumber(), is(closeTo(1361111.11111,
                Utils.EPS)));
        assertThat(mCalculation.getCellHeight(), is(closeTo(8.71933e-4,
                Utils.EPS)));
    }
}