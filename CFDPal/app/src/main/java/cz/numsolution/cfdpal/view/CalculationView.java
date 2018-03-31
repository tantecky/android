package cz.numsolution.cfdpal.view;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public interface CalculationView {
    void setInputValues(String velocity, String density,
                        String viscosity, String length, String yplus);
    void onCalculationClick();

}
