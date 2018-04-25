package cz.numsolution.cfdpal.view;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.Unbinder;
import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;
import cz.numsolution.cfdpal.interactor.CalculationInteractorImpl;
import cz.numsolution.cfdpal.model.CalculationType;
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

    @BindView(R.id.etVelocity)
    TextInputEditText mVelocity;
    @BindView(R.id.etDensity)
    TextInputEditText mDensity;
    @BindView(R.id.etViscosity)
    TextInputEditText mViscosity;
    @BindView(R.id.etLength)
    TextInputEditText mLength;
    @BindView(R.id.etYplus)
    TextInputEditText mYplus;

    @BindView(R.id.qVelocity)
    QuantityInput qVelocity;

    List<TextInputLayout> mTextInputLayouts;
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

        mPresenter.onCreateView();
        mTextInputLayouts = findAll(TextInputLayout.class);
        mQuantityInputs = this.<QuantityInput>findAll(QuantityInput.class);
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
        mVelocity.setText(velocity);
        mDensity.setText(density);
        mViscosity.setText(viscosity);
        mLength.setText(length);
        mYplus.setText(yplus);

        qVelocity.setValue(velocity);
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

        for (TextInputLayout til : mTextInputLayouts) {
            Object tag = til.getTag();

            if (tag != null) {
                String problematicTag = ((String) tag);
                if (problematicVariable.contentEquals(problematicTag)) {
                    til.setErrorEnabled(true);
                    til.setError(message);
                }
            }
        }

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
    public String getVelocity() {
        return mVelocity.getText().toString();
    }

    @Override
    public String getDensity() {
        return mDensity.getText().toString();
    }

    @Override
    public String getViscosity() {
        return mViscosity.getText().toString();
    }

    @Override
    public String getLength() {
        return mLength.getText().toString();
    }

    @Override
    public String getYplus() {
        return mYplus.getText().toString();
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
        for (TextInputLayout til : mTextInputLayouts) {
            til.setErrorEnabled(false);
            til.setError(null);
        }

        for (QuantityInput input : mQuantityInputs) {
            input.setError(null);
        }
    }
}
