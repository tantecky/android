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

import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;
import cz.numsolution.cfdpal.presenter.SelectionPresenter;


public class SelectionFragment extends Fragment {

    private static final String TAG = "SelectionFragment";

    private SelectionPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_selection, container, false);
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
    }
}
