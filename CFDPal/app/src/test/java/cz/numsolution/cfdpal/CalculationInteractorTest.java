package cz.numsolution.cfdpal;

import org.junit.Before;
import org.junit.Test;

import cz.numsolution.cfdpal.interactor.CalculationInteractor;
import cz.numsolution.cfdpal.interactor.CalculationInteractorImpl;
import cz.numsolution.cfdpal.model.CellHeightCalculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public class CalculationInteractorTest {


    private CalculationInteractor mInteractor;

    @Before
    public void setUp() throws Exception {
        mInteractor = new CalculationInteractorImpl();
    }

    @Test
    public void getDefaultCellHeightCalculation() {
        CellHeightCalculation calculation = mInteractor.getDefaultCellHeightCalculation();

        assertThat(calculation.getVelocity(), is(closeTo(20.0, Utils.EPS)));
    }
}