package cz.numsolution.cfdpal.interactor;

import cz.numsolution.cfdpal.Utils;
import cz.numsolution.cfdpal.model.CellHeightCalculation;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public final class CalculationInteractorImpl implements CalculationInteractor {
    public CellHeightCalculation getDefaultCellHeightCalculation() {
        return new CellHeightCalculation();
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
}
