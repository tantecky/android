package cz.numsolution.cfdpal.model;

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
        return mIndex21;
    }

    public double getIndex32() {
        return mIndex32;
    }

    public double getLower() {
        return mLower;
    }

    public double getUpper() {
        return mUpper;
    }

    @Override
    public void calculate() {

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
