package cz.numsolution.cfdpal.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Array;
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

    List<TextInputLayout> mTextInputLayouts;

    public CalculationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        @CalculationType int calcType = getArguments().getInt(EXTRA_CALCULATION_TYPE,
                CalculationType.UNKNOWN);
        mPresenter = new CalculationPresenterImpl(this, calcType,
                new CalculationInteractorImpl());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculation, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mPresenter.onCreateView();
        mTextInputLayouts = findAll();
        clearAllErrors();

        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroyView();
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void setInputValues(String velocity, String density, String viscosity,
                               String length, String yplus) {
        mVelocity.setText(velocity);
        mDensity.setText(density);
        mViscosity.setText(viscosity);
        mLength.setText(length);
        mYplus.setText(yplus);
    }

    @OnClick(R.id.btnCalculate)
    @Override
    public void onCalculationClick() {
        clearAllErrors();
        mPresenter.onCalculationClick();
        Utils.logD(TAG, "onCalculationClick");
    }

    @Override
    public void showResults(String results) {
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
        alertDialog.setTitle("Results");
        alertDialog.setMessage(results);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
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
                    break;
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

    private List<TextInputLayout> findAll() {
        int count = mRoot.getChildCount();
        List<TextInputLayout> tils = new ArrayList<TextInputLayout>(count);

        for (int i = 0; i < count; i++) {
            View v = mRoot.getChildAt(i);
            if (v instanceof TextInputLayout) {
                tils.add((TextInputLayout) v);
            }
        }

        return tils;
    }

    private void clearAllErrors() {
        for (TextInputLayout til : mTextInputLayouts) {
            til.setErrorEnabled(false);
            til.setError(null);
        }

    }
}
