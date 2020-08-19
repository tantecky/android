package cz.numsolution.cfdpal.model;

import androidx.arch.core.util.Function;
import cz.numsolution.cfdpal.Utils;

/**
 * Created by Tomas Antecky on 19. 8. 2020.
 */

public final class GridConvergenceCalculation implements Calculation {
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
        return 1.25 * Math.abs(eps) / (Math.pow(r, p) - 1.0) ;
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
        this(400, 0.9705,
                200, 0.96854,
                100, 0.96178);
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

    public double getGrid1() {
        return mGrid1;
    }

    public double getQuantity1() {
        return mQuantity1;
    }

    public double getGrid2() {
        return mGrid2;
    }

    public double getQuantity2() {
        return mQuantity2;
    }

    public double getGrid3() {
        return mGrid3;
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
        double element1Length = 1.0 / mGrid1;
        double element2Length = 1.0 / mGrid2;
        double element3Length = 1.0 / mGrid3;

        r21 = element2Length / element1Length;
        r32 = element3Length / element2Length;

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

        Function<Double, Double> fce = (Double p) -> p - Math.abs(Math.log(Math.abs(f32 / f21)) +
                (Math.log((Math.pow(r21, p) - s) / (Math.pow(r32, p) - s)))) / Math.log(r21);

        mOrder = Utils.secant(fce, 2);
        mExtrapolated = (Math.pow(r21, mOrder) * f1 - f2) /
                (Math.pow(r21, mOrder) - 1.0);

        mIndex21 = gci(eps21, mOrder, r21);
        mIndex32 = gci(eps32, mOrder, r32);


        mLower = mExtrapolated * (1.0 - mIndex21);
        mUpper = mExtrapolated * (1.0 + mIndex21 );
    }

    @Override
    public String resultsToString() {
        return null;
    }

    @Override
    public String inputValuesToString() {
        return null;
    }
}
