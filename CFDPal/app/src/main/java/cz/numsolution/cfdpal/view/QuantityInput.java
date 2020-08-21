package cz.numsolution.cfdpal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.numsolution.cfdpal.R;

/**
 * Created by Tomas Antecky on 23. 4. 2018.
 */
public class QuantityInput extends ConstraintLayout {

    private static final String EDIT_TEXT_KEY = "EDIT_TEXT_KEY";

    public static final String TAG = "QuantityInput";

    @BindView(R.id.tvName)
    TextView mName;
    @BindView(R.id.tvUnits)
    TextView mUnits;
    @BindView(R.id.etValue)
    EditText mValue;
    @BindView(R.id.tvError)
    TextView mError;

    public CharSequence getName() {
        return mName.getText();
    }

    public String getValue() {
        return mValue.getText().toString();
    }

    public void setValue(CharSequence value) {
        mValue.setText(value);
    }

    public void setError(String message) {
        if (message == null) {
            mError.setVisibility(GONE);
            mValue.setTextColor(getResources().getColor(R.color.black));
        } else {
            mError.setText(message);
            mValue.setTextColor(getResources().getColor(R.color.red));
            mError.setVisibility(VISIBLE);
        }
    }

    public QuantityInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.QuantityInput, 0, 0);

        mName.setText(a.getString(R.styleable.QuantityInput_name));
        mValue.setText(a.getString(R.styleable.QuantityInput_value));

        final String units = a.getString(R.styleable.QuantityInput_units);

        if (units.isEmpty()) {
            mUnits.setVisibility(GONE);
        } else {
            mUnits.setText(units);
        }

        final int inputType = a.getInteger(R.styleable.QuantityInput_android_inputType,
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_SIGNED
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mValue.setInputType(inputType);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.quantity_input, this);

        ButterKnife.bind(this, view);
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putString(EDIT_TEXT_KEY, getValue());
        return bundle;

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setValue(bundle.getString(EDIT_TEXT_KEY));
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }
}
