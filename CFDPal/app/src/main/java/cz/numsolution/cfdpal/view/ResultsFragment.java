package cz.numsolution.cfdpal.view;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
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

    @BindView(R.id.tvVersion)
    public TextView mTextView;

    private Unbinder mUnbinder;
    private String mResults;
    private String mInputValues;

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

    @OnClick(R.id.btnOk)
    public void onOkClick() {
        this.dismiss();
    }

    @OnClick(R.id.btnShare)
    public void onShareClick() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                mInputValues + Utils.LINE_SEPARATOR + mResults);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Results"));

    }

    public void setResults(String results) {
        mResults = results;
    }

    public void setInputValues(String inputValues) {
        mInputValues = inputValues;

    }
}
