package cz.numsolution.cfdpal.presenter;

import cz.numsolution.cfdpal.interactor.SelectionInteractor;
import cz.numsolution.cfdpal.model.CalculationType;
import cz.numsolution.cfdpal.view.SelectionView;

/**
 * Created by Tomas Antecky on 22. 3. 2018.
 */

public final class SelectionPresenterImpl implements SelectionPresenter,
        SelectionInteractor.OnSelectionListener {

    private SelectionView mView;
    private SelectionInteractor mInteractor;

    public SelectionPresenterImpl(SelectionView view, SelectionInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onSelectionButtonClick(int buttonId) {
        mInteractor.selectCalculation(buttonId, this);
    }

    @Override
    public void onSelectionError() {

    }

    @Override
    public void onSelectionSuccess(@CalculationType int calculationType) {

    }
}
