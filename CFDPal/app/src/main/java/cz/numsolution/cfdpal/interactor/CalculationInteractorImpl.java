package cz.numsolution.cfdpal.interactor;

import cz.numsolution.cfdpal.Utils;
import cz.numsolution.cfdpal.model.CellHeightCalculation;
import cz.numsolution.cfdpal.model.GridConvergenceCalculation;
import cz.numsolution.cfdpal.model.TurbulentQuantitiesCalculation;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public final class CalculationInteractorImpl implements CalculationInteractor {
    public CellHeightCalculation getDefaultCellHeightCalculation() {
        return new CellHeightCalculation();
    }

    @Override
    public TurbulentQuantitiesCalculation getDefaultTurbulentQuantitiesCalculation() {
        return new TurbulentQuantitiesCalculation();
    }

    @Override
    public GridConvergenceCalculation getDefaultGridConvergenceCalculation() {
        return new GridConvergenceCalculation();
    }

    @Override
    public void calculateCellHeight(OnCalculationListener listener,
                                    String velocity, String density, String viscosity,
                                    String length, String yplus) {

        if (isValid(listener, velocity, density, viscosity, length, yplus)) {
            CellHeightCalculation calculation = new CellHeightCalculation(
                    velocity, density, viscosity, length, yplus);
            calculation.calculate();
            listener.onCalculationSuccess(calculation.inputValuesToString(),
                    calculation.resultsToString());
        }
    }

    @Override
    public void calculateTurbulentQuantities(OnCalculationListener listener, String velocity,
                                             String length, String intensity) {
        if (isValid(listener, velocity, length, intensity)) {
            TurbulentQuantitiesCalculation calculation = new TurbulentQuantitiesCalculation(
                    velocity, intensity, length);
            calculation.calculate();
            listener.onCalculationSuccess(calculation.inputValuesToString(),
                    calculation.resultsToString());
        }
    }

    @Override
    public void calculateGridConvergence(OnCalculationListener listener,
                                         String grid1, String quantity1,
                                         String grid2, String quantity2,
                                         String grid3, String quantity3) {

        if (isValid(listener,
                grid1, quantity1,
                grid2, quantity2,
                grid3, quantity3)) {
            GridConvergenceCalculation calculation = new GridConvergenceCalculation(
                    grid1, quantity1,
                    grid2, quantity2,
                    grid3, quantity3);
            calculation.calculate();
            listener.onCalculationSuccess(calculation.inputValuesToString(),
                    calculation.resultsToString());
        }
    }

    @Override
    public boolean isValid(OnCalculationListener listener, String velocity,
                           String density, String viscosity, String length,
                           String yplus) {
        boolean valid = true;
        final String errorMessage = "has to be positive number";
        if (!Utils.isPositiveNumber(velocity)) {
            listener.onCalculationError("velocity", errorMessage);
            valid = false;
        }
        if (!Utils.isPositiveNumber(density)) {
            listener.onCalculationError("density", errorMessage);
            valid = false;
        }
        if (!Utils.isPositiveNumber(viscosity)) {
            listener.onCalculationError("viscosity", errorMessage);
            valid = false;
        }
        if (!Utils.isPositiveNumber(length)) {
            listener.onCalculationError("length", errorMessage);
            valid = false;
        }
        if (!Utils.isPositiveNumber(yplus)) {
            listener.onCalculationError("yplus", errorMessage);
            valid = false;
        }
        return valid;

    }

    @Override
    public boolean isValid(OnCalculationListener listener, String velocity, String length,
                           String intensity) {
        boolean valid = true;
        final String errorMessage = "has to be positive number";
        if (!Utils.isPositiveNumber(velocity)) {
            listener.onCalculationError("velocity", errorMessage);
            valid = false;
        }
        if (!Utils.isPositiveNumber(length)) {
            listener.onCalculationError("length", errorMessage);
            valid = false;
        }
        if (!Utils.isPositiveNumber(intensity)) {
            listener.onCalculationError("intensity", errorMessage);
            valid = false;
        }
        if (Utils.isPositiveNumber(intensity, 100.0)) {
            listener.onCalculationError("intensity",
                    "has to be less than 100");
            valid = false;
        }
        return valid;
    }

    @Override
    public boolean isValid(OnCalculationListener listener, String grid1, String quantity1,
                           String grid2, String quantity2,
                           String grid3, String quantity3) {
        boolean valid = true;
        final String errorMessage = "has to be positive number";
        if (!Utils.isPositiveNumber(grid1)) {
            listener.onCalculationError("grid1", errorMessage);
            valid = false;
        }
        if (!Utils.isPositiveNumber(grid2)) {
            listener.onCalculationError("grid2", errorMessage);
            valid = false;
        }
        if (!Utils.isPositiveNumber(grid3)) {
            listener.onCalculationError("grid3", errorMessage);
            valid = false;
        }

        int node1 = (int)Double.valueOf(grid1).doubleValue();
        int node2 = (int)Double.valueOf(grid2).doubleValue();
        int node3 = (int)Double.valueOf(grid3).doubleValue();

        if (!(node1 > node2)) {
            listener.onCalculationError("grid1", "has to be the finest grid");
            valid = false;
        }

        if (!(node2 > node3)) {
            listener.onCalculationError("grid3", "has to be the coarsest grid");
            valid = false;
        }

        return valid;
    }
}
