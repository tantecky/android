package cz.numsolution.cfdpal.view;

import cz.numsolution.cfdpal.model.CalculationType;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public interface CalculationView {
    void setInputValues(String velocity, String density,
                        String viscosity, String length, String yplus);

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

}
