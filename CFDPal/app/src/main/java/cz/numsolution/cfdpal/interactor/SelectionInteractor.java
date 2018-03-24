package cz.numsolution.cfdpal.interactor;

/**
 * Created by Tomas Antecky on 24.3.18.
 */

public interface SelectionInteractor {
    interface OnSelectionListener {
        void onSelectionError();
        void onSelectionSuccess();
    }

    void selectCalculation(CharSequence buttonText, OnSelectionListener listener);
}
