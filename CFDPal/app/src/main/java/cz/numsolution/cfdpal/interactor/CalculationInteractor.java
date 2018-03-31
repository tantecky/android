package cz.numsolution.cfdpal.interactor;

import cz.numsolution.cfdpal.model.CellHeightCalculation;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public interface CalculationInteractor {

    CellHeightCalculation getDefaultCellHeightCalculation();

    void calculateCellHeight(OnCalculationListener listiner, String velocity, String density,
                             String viscosity, String length, String yplus);


    interface OnCalculationListener {
        void onError();

        void onSuccess();
    }
}
