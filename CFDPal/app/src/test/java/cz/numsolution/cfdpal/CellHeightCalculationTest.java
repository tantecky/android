package cz.numsolution.cfdpal;

import org.junit.Before;
import org.junit.Test;

import cz.numsolution.cfdpal.interactor.CalculationInteractor;
import cz.numsolution.cfdpal.interactor.CalculationInteractorImpl;
import cz.numsolution.cfdpal.model.CellHeightCalculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class CellHeightCalculationTest {


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