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
    public void onError() {

    }

    @Override
    public void onSuccess() {

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
         //    mInteractor.calculateCellHeight(this);
                break;
            default:
                throw new AssertionError();

        }
    }

    private void setDefaultInputValues() {
        switch (mCalcType) {
            case CalculationType.CELL_HEIGHT:
                CellHeightCalculation calcualtion = mInteractor.getDefaultCellHeightCalculation();
                mView.setInputValues(String.valueOf(calcualtion.getVelocity()),
                        String.valueOf(calcualtion.getDensity()),
                        String.valueOf(calcualtion.getViscosity()),
                        String.valueOf(calcualtion.getLength()),
                        String.valueOf(calcualtion.getYplus()));
                break;
            default:
                throw new AssertionError();

        }
    }
}
