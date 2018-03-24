package cz.numsolution.cfdpal.interactor;
import cz.numsolution.cfdpal.model.CalculationType;


/**
 * Created by Tomas Antecky on 24.3.18.
 */

public interface SelectionInteractor {
    interface OnSelectionListener {
        void onSelectionError();
        void onSelectionSuccess(@CalculationType int calculationType);
    }

    void selectCalculation(int buttonTag, OnSelectionListener listener);
}
