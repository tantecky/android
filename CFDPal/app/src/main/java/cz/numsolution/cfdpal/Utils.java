package cz.numsolution.cfdpal;

/**
 * Created by Tomas Antecky on 21. 3. 2018.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public final class Utils {

    public static final double EPS = 1e-5;


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
        try {
            return value != null && Double.valueOf(value) > 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}