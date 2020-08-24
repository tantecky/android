package cz.numsolution.cfdpal.model;

import java.util.Locale;

import androidx.arch.core.util.Function;
import cz.numsolution.cfdpal.Utils;

/**
 * Created by Tomas Antecky on 19. 8. 2020.
 */

public final class GridConvergenceCalculation implements Calculation {

    public static final String TAG = "GridConvergenceCalculation";

    private final double mGrid1;
    private final double mQuantity1;
    private final double mGrid2;
    private final double mQuantity2;
    private final double mGrid3;
    private final double mQuantity3;

    private double mOrder;
    private double mExtrapolated;
    private double mIndex21;
    private double mIndex32;
    private double mLower;
    private double mUpper;

    private double f32, f21, eps32, eps21, s, r21, r32;

    // in percent
    private static double gci(double eps, double p, double r) {
        return 1.25 * Math.abs(eps) / (Math.pow(r, p) - 1.0);
    }

    public GridConvergenceCalculation(String grid1, String quantity1,
                                      String grid2, String quantity2,
                                      String grid3, String quantity3) {
        this(Double.valueOf(grid1), Double.valueOf(quantity1),
                Double.valueOf(grid2), Double.valueOf(quantity2),
                Double.valueOf(grid3), Double.valueOf(quantity3)
        );

    }

    public GridConvergenceCalculation() {
        this(4000, 0.9705,
                2000, 0.96854,
                1000, 0.96178);
    }

    public GridConvergenceCalculation(double grid1, double quantity1,
                                      double grid2, double quantity2,
                                      double grid3, double quantity3) {
        mGrid1 = grid1;
        mQuantity1 = quantity1;
        mGrid2 = grid2;
        mQuantity2 = quantity2;
        mGrid3 = grid3;
        mQuantity3 = quantity3;
    }

    public int getGrid1() {
        return (int) mGrid1;
    }

    public double getQuantity1() {
        return mQuantity1;
    }

    public int getGrid2() {
        return (int) mGrid2;
    }

    public double getQuantity2() {
        return mQuantity2;
    }

    public int getGrid3() {
        return (int) mGrid3;
    }

    public double getQuantity3() {
        return mQuantity3;
    }

    public double getOrder() {
        return mOrder;
    }

    public double getExtrapolated() {
        return mExtrapolated;
    }

    public double getIndex21() {
        return mIndex21 * 100.0;
    }

    public double getIndex32() {
        return mIndex32 * 100.0;
    }

    public double getLower() {
        return mLower;
    }

    public double getUpper() {
        return mUpper;
    }

    @Override
    public void calculate() {
        double length1 = Math.cbrt(1.0 / mGrid1);
        double length2 = Math.cbrt(1.0 / mGrid2);
        double length3 = Math.cbrt(1.0 / mGrid3);

        r21 = length2 / length1;
        r32 = length3 / length2;

        double f1 = mQuantity1;
        double f2 = mQuantity2;
        double f3 = mQuantity3;

        f32 = f3 - f2;
        f21 = f2 - f1;

        eps32 = f32 / f3;
        eps21 = f21 / f2;

        s = f32 / f21 > 0.0
                ? 1.0
                : -1.0;

        Utils.logD(TAG, String.format("r21 %e r32 %e", r21, r32));
        Utils.logD(TAG, String.format("f21 %e f32 %e", f21, f32));
        Utils.logD(TAG, String.format("eps21 %e eps32 %e", eps21, eps32));
        Utils.logD(TAG, String.format("s %e", s));

        Function<Double, Double> fce;

        if (r21 != r32) {
            fce = (Double p) -> p - Math.abs(
                    Math.log(Math.abs(f32 / f21))
                            + (Math.log((Math.pow(r21, p) - s) / (Math.pow(r32, p) - s)))
            ) / Math.log(r21);
        } else {
            fce = (Double p) -> p - Math.abs(
                    Math.log(Math.abs(f32 / f21))
            ) / Math.log(r21);
        }

        mOrder = Utils.secant(fce, 2);
        mExtrapolated = (Math.pow(r21, mOrder) * f1 - f2) /
                (Math.pow(r21, mOrder) - 1.0);

        mIndex21 = gci(eps21, mOrder, r21);
        mIndex32 = gci(eps32, mOrder, r32);

        mLower = mExtrapolated * (1.0 - mIndex21);
        mUpper = mExtrapolated * (1.0 + mIndex21);
    }

    @Override
    public String resultsToString() {
        if (!(mOrder > 0.0)) {
            return "Unable to find order of convergence";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "Extrapolated quantity: %.4e", mExtrapolated));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "95%% interval: %.3e,\u00A0%.3e",
                mLower, mUpper));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Grid 3/2 convergence index: %.3f%%",
                getIndex32()));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Grid 1/2 convergence index: %.3f%%",
                getIndex21()));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Order of convergence: %.4f", mOrder));


        return sb.toString();
    }

    @Override
    public String inputValuesToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.US, "Grid 1 cell count: %s", getGrid1()));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Grid 1 quantity: %s", mQuantity1));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Grid 2 cell count: %s", getGrid2()));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Grid 2 quantity: %s", mQuantity2));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Grid 3 cell count: %s", getGrid3()));
        sb.append(Utils.LINE_SEPARATOR);
        sb.append(String.format(Locale.US, "Grid 3 quantity: %s", mQuantity3));
        return sb.toString();
    }
}
