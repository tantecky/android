package cz.numsolution.cfdpal.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;
import cz.numsolution.cfdpal.interactor.CalculationInteractorImpl;
import cz.numsolution.cfdpal.model.CalculationType;
import cz.numsolution.cfdpal.model.CellHeightCalculation;
import cz.numsolution.cfdpal.presenter.CalculationPresenter;
import cz.numsolution.cfdpal.presenter.CalculationPresenterImpl;

public class CalculationFragment extends Fragment implements CalculationView {

    private static final String TAG = "CalculationFragment";
    private static final String EXTRA_CALCULATION_TYPE = "CalculationType";

    public static CalculationFragment newInstance(@CalculationType int calcType) {
        CalculationFragment fragment = new CalculationFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(EXTRA_CALCULATION_TYPE, calcType);
        fragment.setArguments(bundle);
        return fragment;
    }

    private CalculationPresenter mPresenter;
    private Unbinder mUnbinder;

    @BindView(R.id.root)
    ViewGroup mRoot;

    @BindView(R.id.scrollView)
    ScrollView mScrollView;

    @BindView(R.id.qVelocity)
    QuantityInput qVelocity;
    @BindView(R.id.qDensity)
    QuantityInput qDensity;
    @BindView(R.id.qViscosity)
    QuantityInput qViscosity;
    @BindView(R.id.qLength)
    QuantityInput qLength;
    @BindView(R.id.qYplus)
    QuantityInput qYplus;
    @BindView(R.id.qIntensity)
    QuantityInput qIntensity;

    List<QuantityInput> mQuantityInputs;

    public CalculationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @CalculationType int calcType = getArguments().getInt(EXTRA_CALCULATION_TYPE,
                CalculationType.UNKNOWN);
        mPresenter = new CalculationPresenterImpl(this, calcType,
                new CalculationInteractorImpl());

        Utils.logD(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculation, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mQuantityInputs = findAll(QuantityInput.class);
        mPresenter.onCreateView();
        clearAllErrors();

        Utils.logD(TAG, "onCreateView");
        mScrollView.smoothScrollTo(0, 0);

        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroyView();
        super.onDestroyView();
        mUnbinder.unbind();

        Utils.logD(TAG, "onDestroyView");
    }

    @Override
    public void setInputValues(String velocity, String density, String viscosity,
                               String length, String yplus) {
        qVelocity.setValue(velocity);
        qDensity.setValue(density);
        qViscosity.setValue(viscosity);
        qLength.setValue(length);
        qYplus.setValue(yplus);
    }

    @Override
    public void setInputValues(String velocity, String length, String intensity) {
        qVelocity.setValue(velocity);
        qLength.setValue(length);
        qIntensity.setValue(intensity);
    }

    @OnClick(R.id.btnCalculate)
    @Override
    public void onCalculationClick() {

        clearAllErrors();
        mPresenter.onCalculationClick();
        Utils.logD(TAG, "onCalculationClick");
    }

    @OnClick(R.id.btnReset)
    @Override
    public void onResetClick() {
        mPresenter.onResetClick();
        clearAllErrors();
    }

    @Override
    public void showResults(String inputValues, String results) {
        ResultsFragment fragment = ResultsFragment.newInstance();
        fragment.setInputValues(inputValues);
        fragment.setResults(results);
        fragment.show(getFragmentManager(), ResultsFragment.TAG);
    }

    @Override
    public void setError(String problematicVariable, String message) {

        for (QuantityInput input : mQuantityInputs) {
            Object tag = input.getTag();

            if (tag != null) {
                String problematicTag = ((String) tag);
                if (problematicVariable.contentEquals(problematicTag)) {
                    input.setError(message);
                }
            }
        }
    }

    @Override
    public void showHideInputs(@CalculationType int calcType) {
        for (QuantityInput input : mQuantityInputs) {
            input.setVisibility(View.GONE);
        }

        switch (calcType) {
            case CalculationType.CELL_HEIGHT:
                qVelocity.setVisibility(View.VISIBLE);
                qDensity.setVisibility(View.VISIBLE);
                qViscosity.setVisibility(View.VISIBLE);
                qLength.setVisibility(View.VISIBLE);
                break;
            case CalculationType.TURBULENT_QUANTITIES:
                qVelocity.setVisibility(View.VISIBLE);
                qLength.setVisibility(View.VISIBLE);
                qIntensity.setVisibility(View.VISIBLE);
                break;
            case CalculationType.GRID_CONVERGENCE:
                break;
            default:
                throw new AssertionError();

        }

    }

    @Override
    public String getVelocity() {
        return qVelocity.getValue();
    }

    @Override
    public String getDensity() {
        return qDensity.getValue();
    }

    @Override
    public String getViscosity() {
        return qViscosity.getValue();
    }

    @Override
    public String getLength() {
        return qLength.getValue();
    }

    @Override
    public String getYplus() {
        return qYplus.getValue();
    }

    @Override
    public String getIntensity() {
        return qIntensity.getValue();
    }

    private <T> List<T> findAll(Class<T> type) {
        int count = mRoot.getChildCount();
        List<T> inputs = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            View v = mRoot.getChildAt(i);
            if (type.isInstance(v)) {
                inputs.add((T) v);
            }

        }

        return inputs;
    }

    private void clearAllErrors() {

        for (QuantityInput input : mQuantityInputs) {
            input.setError(null);
        }
    }
}
