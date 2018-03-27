package cz.numsolution.cfdpal.interactor;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public interface CalculationInteractor {
    interface OnCalculationListener {
        void onError();
        void onSuccess();
    }
}
