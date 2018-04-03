package cz.numsolution.cfdpal.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;

public class ResultsFragment extends AppCompatDialogFragment {

    public static final String TAG = "ResultsFragment";

    @BindView(R.id.tvResults)
    public TextView mTextView;

    private Unbinder mUnbinder;
    private String mResults;

    public static ResultsFragment newInstance() {
        return new ResultsFragment();
    }

    public ResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mTextView.setText(mResults);
        getDialog().setTitle("Results");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick(R.id.tvOk)
    public void onOkClick() {
        this.dismiss();
    }

    public void setResults(String results) {
        mResults = results;
    }
}
