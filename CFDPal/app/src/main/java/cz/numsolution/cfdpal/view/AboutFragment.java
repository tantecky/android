package cz.numsolution.cfdpal.view;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;

public class AboutFragment extends AppCompatDialogFragment {

    public static final String TAG = "ResultsFragment";

    public static void show(FragmentManager fm) {
        AboutFragment fragment = AboutFragment.newInstance();
        fragment.show(fm, null);
    }

    private Unbinder mUnbinder;

    @BindView(R.id.tvWeb)
    public TextView mWeb;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        Dialog dialog = getDialog();
        dialog.setTitle(R.string.app_name);

        mWeb.setPaintFlags(mWeb.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

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

    @OnClick(R.id.tvWeb)
    public void onLinkClick() {
        Utils.openUrl(this.getContext(), getString(R.string.web_url));

    }

}
