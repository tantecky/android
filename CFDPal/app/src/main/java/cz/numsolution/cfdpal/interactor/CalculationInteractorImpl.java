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
            listener.onCalculationSuccess(calculation.getResults());
        }

    }

    @Override
    public boolean isValid(OnCalculationListener listener, String velocity,
                           String density, String viscosity, String length,
                           String yplus) {
        if (!Utils.isPositiveNumber(velocity)) {
            listener.onCalculationError("velocity",
                    "has to be positive number");
            return false;
        }

        return true;
    }
}
