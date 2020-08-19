package cz.numsolution.cfdpal.model;

import java.util.Locale;

import cz.numsolution.cfdpal.Utils;

/**
 * Created by Tomas Antecky on 18. 8. 2020.
 */

public final class TurbulentQuantitiesCalculation implements Calculation {
    private final double mVelocity;
    private final double mIntensity;
    private final double mLength;

    private double mKineticEnergy;
    private double mDisRate;
    private double mSpecificDisRate;
    private double mModViscosity;

    public TurbulentQuantitiesCalculation(String velocity, String intensity, String length) {
        this(Double.valueOf(velocity), Double.valueOf(intensity), Double.valueOf(length));
    }

    public TurbulentQuantitiesCalculation() {
        this(20, 5, 1);
    }

    public TurbulentQuantitiesCalculation(double velocity, double intensity, double length) {
        mVelocity = velocity;
        mIntensity = intensity;
        mLength = length;
    }

    public double getVelocity() {
        return mVelocity;
    }

    public double getIntensity() {
        return mIntensity;
    }

    public double getLength() {
        return mLength;
    }

    public double getKineticEnergy() {
        return mKineticEnergy;
    }

    public double getDisRate() {
        return mDisRate;
    }

    public double getSpecificDisRate() {
        return mSpecificDisRate;
    }

    public double getModViscosity() {
        return mModViscosity;
    }

    @Override
    public void calculate() {
        double intensity = mIntensity / 100.0;
        mKineticEnergy = 3.0 * 0.5 * mVelocity * mVelocity * intensity * intensity;
        final double cmu = 0.09;
        double length = 0.07 * mLength / Math.pow(cmu, 3.0 / 4.0);
        mDisRate = Math.pow(mKineticEnergy, 3.0 / 2.0) / length;
        mSpecificDisRate = Math.sqrt(mKineticEnergy) / (cmu * length);
        mModViscosity = cmu * Math.sqrt(3.0 / 2.0) * mVelocity * intensity * length;

    }

    @Override
    public String resultsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "Turbulent kinetic energy: %.4e (m2/s2)",
                mKineticEnergy));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Turbulent dissipation rate: %.4e (m2/s3)",
                mDisRate));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Specific dissipation rate: %.4e (1/s)",
                mSpecificDisRate));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Modified turbulent viscosity: %.4e (m2/s)",
                mModViscosity));
        return sb.toString();
    }

    @Override
    public String inputValuesToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "Velocity: %s (m/s)", mVelocity));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Turbulence intensity: %s (%%)", mIntensity));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Characteristic length: %s (m)", mLength));
        return sb.toString();
    }
}