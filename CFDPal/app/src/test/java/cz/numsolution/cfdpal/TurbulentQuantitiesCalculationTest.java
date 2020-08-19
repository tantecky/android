package cz.numsolution.cfdpal;

import org.junit.Before;
import org.junit.Test;

import cz.numsolution.cfdpal.model.CellHeightCalculation;
import cz.numsolution.cfdpal.model.TurbulentQuantitiesCalculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public class TurbulentQuantitiesCalculationTest {


    private TurbulentQuantitiesCalculation mCalculation;

    @Before
    public void setUp() throws Exception {
        mCalculation = new TurbulentQuantitiesCalculation();
    }

    @Test
    public void calculate() {
        mCalculation.calculate();

        assertThat(mCalculation.getKineticEnergy(), is(closeTo(1.50000e+0,
                Utils.EPS)));
        assertThat(mCalculation.getDisRate(), is(closeTo(4.31242e+0,
                Utils.EPS)));
        assertThat(mCalculation.getSpecificDisRate(), is(closeTo(3.19438e+1,
                Utils.EPS)));
        assertThat(mCalculation.getModViscosity(), is(closeTo(4.69574e-2,
                Utils.EPS)));
    }
}