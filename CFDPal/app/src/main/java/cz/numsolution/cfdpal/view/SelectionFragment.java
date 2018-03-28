package cz.numsolution.cfdpal.view;

/**
 * Created by Tomas Antecky on 21. 3. 2018.
 */

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;
import cz.numsolution.cfdpal.model.CalculationType;


public class SelectionFragment extends Fragment {

    private static final String TAG = "SelectionFragment";

    private Unbinder mUnbinder;

    @BindView(R.id.btnHeight)
    Button mHeight;
    @BindView(R.id.btnQuantities)
    Button mQuantities;
    @BindView(R.id.btnGrid)
    Button mGrid;

    @BindView(R.id.tvWeb)
    TextView mWeb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selection, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mHeight.setTag(CalculationType.CELL_HEIGHT);
        mQuantities.setTag(CalculationType.TURBULENT_QUANTITIES);
        mGrid.setTag(CalculationType.GRID_CONVERGENCE);

        mWeb.setPaintFlags(mWeb.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @OnClick({R.id.btnHeight, R.id.btnQuantities, R.id.btnGrid})
    public void onButtonClick(Button button) {
        @CalculationType int calcType = (int) button.getTag();
        CalculationActivity.start(getContext(), calcType, button.getText());

    }

    @OnClick({R.id.imLogo, R.id.tvWeb})
    public void onLinkClick(View v) {
        Utils.openUrl(this.getContext(), getString(R.string.web_url));

    }
}
