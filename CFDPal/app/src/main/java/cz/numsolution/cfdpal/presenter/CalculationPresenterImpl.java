package cz.numsolution.cfdpal.presenter;

import cz.numsolution.cfdpal.interactor.CalculationInteractor;
import cz.numsolution.cfdpal.model.CalculationType;
import cz.numsolution.cfdpal.model.CellHeightCalculation;
import cz.numsolution.cfdpal.view.CalculationView;

/**
 * Created by Tomas Antecky on 27.3.18.
 */
public final class CalculationPresenterImpl implements CalculationPresenter,
        CalculationInteractor.OnCalculationListener {
    private CalculationView mView;
    private CalculationInteractor mInteractor;
    @CalculationType
    int mCalcType;

    public CalculationPresenterImpl(CalculationView view,
                                    @CalculationType int calcType,
                                    CalculationInteractor interactor) {

        if (calcType == CalculationType.UNKNOWN) {
            throw new AssertionError();
        }

        mView = view;
        mInteractor = interactor;
        mCalcType = calcType;
    }

    @Override
    public void onCalculationError(String problematicTag, String message) {
        mView.setError(problematicTag, message);
    }

    @Override
    public void onCalculationSuccess(String results) {
        mView.showResults(results);
    }

    @Override
    public void onDestroyView() {
        mView = null;
        mInteractor = null;
    }

    @Override
    public void onCreateView() {
        setDefaultInputValues();
    }

    @Override
    public void onCalculationClick() {
        switch (mCalcType) {
            case CalculationType.CELL_HEIGHT:
                mInteractor.calculateCellHeight(this, mView.getVelocity(),
                        mView.getDensity(), mView.getViscosity(),
                        mView.getLength(), mView.getYplus());
                break;
            default:
                break;

        }
    }

    private void setDefaultInputValues() {
        switch (mCalcType) {
            case CalculationType.CELL_HEIGHT:
                CellHeightCalculation calculation = mInteractor.getDefaultCellHeightCalculation();
                mView.setInputValues(String.valueOf(calculation.getVelocity()),
                        String.valueOf(calculation.getDensity()),
                        String.valueOf(calculation.getViscosity()),
                        String.valueOf(calculation.getLength()),
                        String.valueOf(calculation.getYplus()));
                break;
            default:
                throw new AssertionError();

        }
    }
}
