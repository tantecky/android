package cz.numsolution.cfdpal.interactor;

import cz.numsolution.cfdpal.model.CellHeightCalculation;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public interface CalculationInteractor {

    CellHeightCalculation getDefaultCellHeightCalculation();

    void calculateCellHeight(OnCalculationListener listener, String velocity, String density,
                             String viscosity, String length, String yplus);

    boolean isValid(OnCalculationListener listener, String velocity, String density,
                    String viscosity, String length, String yplus);


    interface OnCalculationListener {
        void onCalculationError(String problematicVariable, String message);

        void onCalculationSuccess(String results);
    }
}
