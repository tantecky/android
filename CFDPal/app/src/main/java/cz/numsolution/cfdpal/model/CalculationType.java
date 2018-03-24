package cz.numsolution.cfdpal.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Tomas Antecky on 24.3.18.
 */


@IntDef({CalculationType.UNKNOWN, CalculationType.CELL_HEIGHT,
        CalculationType.TURBULENT_QUANTITIES, CalculationType.GRID_CONVERGENCE})
@Retention(RetentionPolicy.SOURCE)
public @interface CalculationType {
    int UNKNOWN = 0;
    int CELL_HEIGHT = 1;
    int TURBULENT_QUANTITIES = 2;
    int GRID_CONVERGENCE = 3;
}
