package cz.numsolution.cfdpal.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.numsolution.cfdpal.R;
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
        mPresenter.onCalculationClick();
        AlertDialog alertDialog = new AlertDialog.Builder(this.getContext()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();
    }

}
