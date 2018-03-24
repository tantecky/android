package cz.numsolution.cfdpal.view;

/**
 * Created by Tomas Antecky on 21. 3. 2018.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;
import cz.numsolution.cfdpal.interactor.SelectionInteractorImpl;
import cz.numsolution.cfdpal.presenter.SelectionPresenter;
import cz.numsolution.cfdpal.presenter.SelectionPresenterImpl;

public class SelectionFragment extends Fragment implements SelectionView {

    private static final String TAG = "SelectionFragment";

    private SelectionPresenter mPresenter;
    private Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SelectionPresenterImpl(this, new SelectionInteractorImpl());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        mPresenter.onResume();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDestroyView();
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({R.id.buttonHeight, R.id.buttonQuantities, R.id.buttonGrid})
    public void onButtonClick(Button button) {
        Utils.showToast(this.getContext(), button.getText());
    }

    @Override
    public void onCalculationSelected() {

    }
}
