package cz.numsolution.cfdpal.interactor;

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
        double vel = Double.valueOf(velocity);
        double den = Double.valueOf(density);
        double vis = Double.valueOf(viscosity);
        double len = Double.valueOf(length);
        double yp = Double.valueOf(yplus);
        CellHeightCalculation calculation = new CellHeightCalculation(vel, den, vis, len, yp);
        calculation.calculate();
        listener.onSuccess(calculation.getResults());
    }
}
