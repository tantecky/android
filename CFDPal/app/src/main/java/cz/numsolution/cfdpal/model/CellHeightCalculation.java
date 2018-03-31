package cz.numsolution.cfdpal.model;

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
}
