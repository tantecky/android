package cz.numsolution.cfdpal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.numsolution.cfdpal.R;
import cz.numsolution.cfdpal.Utils;

/**
 * Created by Tomas Antecky on 23. 4. 2018.
 */
public class QuantityInput extends ConstraintLayout {

    public static final String TAG = "QuantityInput";

    @BindView(R.id.tvName)
    TextView mName;
    @BindView(R.id.tvUnits)
    TextView mUnits;
    @BindView(R.id.etValue)
    TextView mValue;

    public CharSequence getName() {
        return mName.getText();
    }

    public CharSequence getValue() {
        return mValue.getText();
    }

    public void setValue(CharSequence value) {
        mValue.setText(value);
    }

    public QuantityInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.QuantityInput, 0, 0);

        mName.setText(a.getString(R.styleable.QuantityInput_name));
        mUnits.setText(a.getString(R.styleable.QuantityInput_units));
        mValue.setText(a.getString(R.styleable.QuantityInput_value));
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.quantity_input, this);

        ButterKnife.bind(this, view);
    }
}
