package cz.numsolution.cfdpal.model;

import java.util.Locale;

import cz.numsolution.cfdpal.Utils;

/**
 * Created by Tomas Antecky on 31.3.18.
 */

public final class CellHeightCalculation implements Calculation {
    private final double mVelocity;
    private final double mDensity;
    private final double mViscosity;
    private final double mLength;
    private final double mYplus;

    private double mReynoldsNumber;
    private double mCellHeight;

    public CellHeightCalculation(String velocity, String density,
                                 String viscosity, String length, String yplus) {
        this(Double.valueOf(velocity), Double.valueOf(density), Double.valueOf(viscosity),
                Double.valueOf(length), Double.valueOf(yplus));
    }

    public CellHeightCalculation() {
        this(20, 1.225,
                1.8e-5, 1, 50);
    }

    public CellHeightCalculation(double velocity, double density,
                                 double viscosity, double length, double yplus) {
        mVelocity = velocity;
        mDensity = density;
        mViscosity = viscosity;
        mLength = length;
        mYplus = yplus;
    }

    public double getVelocity() {
        return mVelocity;
    }

    public double getDensity() {
        return mDensity;
    }

    public double getViscosity() {
        return mViscosity;
    }

    public double getLength() {
        return mLength;
    }

    public double getYplus() {
        return mYplus;
    }

    public double getReynoldsNumber() {
        return mReynoldsNumber;
    }

    public double getCellHeight() {
        return mCellHeight;
    }

    @Override
    public void calculate() {
        mReynoldsNumber = mVelocity * mDensity * mLength / mViscosity;
        double cf = Math.pow(2.0 * Math.log10(mReynoldsNumber) - 0.65, -2.3);
        double tauw = cf * 0.5 * mDensity * mVelocity * mVelocity;
        double ustar = Math.sqrt(tauw / mDensity);
        mCellHeight = mYplus * mViscosity / (mDensity * ustar);
    }

    @Override
    public String resultsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "Reynolds number: %.1f", mReynoldsNumber));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "First cell height: %.4e (m)", mCellHeight));
        return sb.toString();

    }

    @Override
    public String inputValuesToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "Velocity: %s (m/s)", mVelocity));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Density: %s (kg/m3)", mDensity));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Viscosity: %s (kg/ms)", mViscosity));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Length: %s (m)", mLength));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "y+: %s", mYplus));
        return sb.toString();
    }
}
