package cz.numsolution.cfdpal.presenter;

import cz.numsolution.cfdpal.interactor.CalculationInteractor;
import cz.numsolution.cfdpal.model.CalculationType;
import cz.numsolution.cfdpal.model.CellHeightCalculation;
import cz.numsolution.cfdpal.model.GridConvergenceCalculation;
import cz.numsolution.cfdpal.model.TurbulentQuantitiesCalculation;
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
    public void onCalculationSuccess(String inputValues, String results) {
        mView.showResults(inputValues, results);
    }

    @Override
    public void onDestroyView() {
        mView = null;
        mInteractor = null;
    }

    @Override
    public void onCreateView() {
        setDefaultInputValues();
        mView.showHideInputs(mCalcType);
    }

    @Override
    public void onCalculationClick() {
        switch (mCalcType) {
            case CalculationType.CELL_HEIGHT:
                mInteractor.calculateCellHeight(this, mView.getVelocity(),
                        mView.getDensity(), mView.getViscosity(),
                        mView.getLength(), mView.getYplus());
                break;
            case CalculationType.TURBULENT_QUANTITIES:
                mInteractor.calculateTurbulentQuantities(this, mView.getVelocity(),
                        mView.getLength(), mView.getIntensity());
                break;
            case CalculationType.GRID_CONVERGENCE:
                mInteractor.calculateGridConvergence(this,
                        mView.getGrid1(), mView.getQuantity1(),
                        mView.getGrid2(), mView.getQuantity2(),
                        mView.getGrid3(), mView.getQuantity3()

                );
                break;
            default:
                throw new AssertionError();

        }
    }

    @Override
    public void onResetClick() {
        setDefaultInputValues();
    }

    @Override
    public void onMoreInfoClick() {
        String url = null;
        switch (mCalcType) {
            case CalculationType.CELL_HEIGHT:
                url = "https://www.cfd-online.com/Wiki/Y_plus_wall_distance_estimation";
                break;
            case CalculationType.TURBULENT_QUANTITIES:
                url = "https://www.cfd-online.com/Wiki/Turbulence_intensity";
                break;
            case CalculationType.GRID_CONVERGENCE:
                url = "https://www.grc.nasa.gov/www/wind/valid/tutorial/spatconv.html";
                break;
            default:
                throw new AssertionError();
        }
        mView.openUrl(url);
    }

    private void setDefaultInputValues() {
        switch (mCalcType) {
            case CalculationType.CELL_HEIGHT: {
                CellHeightCalculation calculation = mInteractor.getDefaultCellHeightCalculation();
                mView.setInputValues(String.valueOf(calculation.getVelocity()),
                        String.valueOf(calculation.getDensity()),
                        String.valueOf(calculation.getViscosity()),
                        String.valueOf(calculation.getLength()),
                        String.valueOf(calculation.getYplus()));
                break;
            }
            case CalculationType.TURBULENT_QUANTITIES: {
                TurbulentQuantitiesCalculation calculation = mInteractor.getDefaultTurbulentQuantitiesCalculation();
                mView.setInputValues(String.valueOf(calculation.getVelocity()),
                        String.valueOf(calculation.getLength()),
                        String.valueOf(calculation.getIntensity()));
                break;
            }
            case CalculationType.GRID_CONVERGENCE: {
                GridConvergenceCalculation calculation = mInteractor.getDefaultGridConvergenceCalculation();
                mView.setInputValues(
                        String.valueOf(calculation.getGrid1()),
                        String.valueOf(calculation.getQuantity1()),
                        String.valueOf(calculation.getGrid2()),
                        String.valueOf(calculation.getQuantity2()),
                        String.valueOf(calculation.getGrid3()),
                        String.valueOf(calculation.getQuantity3())
                );
                break;
            }
            default:
                throw new AssertionError();

        }
    }
}
