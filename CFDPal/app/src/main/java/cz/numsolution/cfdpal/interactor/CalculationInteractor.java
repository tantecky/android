package cz.numsolution.cfdpal.interactor;

import cz.numsolution.cfdpal.model.CellHeightCalculation;
import cz.numsolution.cfdpal.model.GridConvergenceCalculation;
import cz.numsolution.cfdpal.model.TurbulentQuantitiesCalculation;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public interface CalculationInteractor {

    CellHeightCalculation getDefaultCellHeightCalculation();

    TurbulentQuantitiesCalculation getDefaultTurbulentQuantitiesCalculation();

    GridConvergenceCalculation getDefaultGridConvergenceCalculation();

    void calculateCellHeight(OnCalculationListener listener, String velocity, String density,
                             String viscosity, String length, String yplus);

    void calculateTurbulentQuantities(OnCalculationListener listener, String velocity,
                                      String length, String intensity);

    void calculateGridConvergence(OnCalculationListener listener,
                                  String grid1, String quantity1,
                                  String grid2, String quantity2,
                                  String grid3, String quantity3);

    boolean isValid(OnCalculationListener listener, String velocity, String density,
                    String viscosity, String length, String yplus);

    boolean isValid(OnCalculationListener listener, String velocity,
                    String length, String intensity);

    boolean isValid(OnCalculationListener listener,
                    String grid1, String quantity1,
                    String grid2, String quantity2,
                    String grid3, String quantity3
    );

    interface OnCalculationListener {
        void onCalculationError(String problematicTag, String message);

        void onCalculationSuccess(String inputValues, String results);
    }
}
