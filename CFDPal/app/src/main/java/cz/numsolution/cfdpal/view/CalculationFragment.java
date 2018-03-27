package cz.numsolution.cfdpal.view;


import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.interactor.CalculationInteractorImpl;
import cz.numsolution.cfdpal.presenter.CalculationPresenter;
import cz.numsolution.cfdpal.presenter.CalculationPresenterImpl;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalculationFragment extends Fragment implements CalculationView {

    private CalculationPresenter mPresenter;
    private Unbinder mUnbinder;

    @BindView(R.id.etDensity)
    TextInputEditText mDensity;


    public CalculationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CalculationPresenterImpl(this, new CalculationInteractorImpl());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculation, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

}
