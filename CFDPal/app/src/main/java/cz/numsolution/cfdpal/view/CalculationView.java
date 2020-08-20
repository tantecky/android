package cz.numsolution.cfdpal.view;

import cz.numsolution.cfdpal.model.CalculationType;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public interface CalculationView {
    void setInputValues(String velocity, String density,
                        String viscosity, String length, String yplus);

    void setInputValues(String velocity, String length, String intensity);

    void setInputValues(String grid1, String quantity1,
                        String grid2, String quantity2,
                        String grid3, String quantity3);

    void onCalculationClick();

    void onResetClick();

    void showResults(String inputValues, String results);

    void setError(String problematicVariable, String message);

    void showHideInputs(@CalculationType int calcType);

    String getVelocity();

    String getDensity();

    String getViscosity();

    String getLength();

    String getYplus();

    String getIntensity();

    String getGrid1();
    String getGrid2();
    String getGrid3();

    String getQuantity1();
    String getQuantity2();
    String getQuantity3();

}
