package cz.numsolution.cfdpal.interactor;

import cz.numsolution.cfdpal.Utils;
import cz.numsolution.cfdpal.model.CellHeightCalculation;
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
}
