package cz.numsolution.cfdpal;

/**
 * Created by Tomas Antecky on 21. 3. 2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.arch.core.util.Function;

public final class Utils {

    public static final double EPS = 1e-4;
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void showToast(Context context, final CharSequence message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void logE(final String tag, final String message) {
        Log.e(tag, message);
    }

    public static void logD(final String tag, final String message) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void openUrl(Context context, final String url) {
        context.startActivity(new Intent(Intent.ACTION_VIEW,
                Uri.parse(url)));
    }

    public static boolean isPositiveNumber(String value) {
        return isPositiveNumber(value, 0.0);
    }

    public static boolean isPositiveNumber(String value, double limit) {
        try {
            return value != null && Double.valueOf(value) > limit;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isTwoPane(Activity activity) {
        return activity.findViewById(R.id.fragment_container_master) != null;
    }

    public static double secant(Function<Double,Double> f, double init) {
        double f0;
        double f1;
        double x0 = init;
        double x1 = init * (1 + 1e-4) + 1e-4;
        double x = x1;

        for (int i = 0; i < 50; i++) {

            f0 = f.apply(x0);
            f1 = f.apply(x1);

            if (Math.abs(f0 - f1) < 1e-8) {
                return x;
            }

            x = x1 - f1 * (x1 - x0) / (f1 - f0);

            x0 = x1;
            x1 = x;
        }

        return 0;

    }
}