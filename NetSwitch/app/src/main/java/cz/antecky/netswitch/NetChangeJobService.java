package cz.antecky.netswitch;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import cz.antecky.netswitch.ui.NetSwitchWidget;

public final class NetChangeJobService extends JobService {
    private final static String TAG = "NetChangeJobService";

    public final static int MOBILE_DATA_CHANGED = 1;
    public final static int WIFI_CHANGED = 2;

    public static void schedule(Context context, int id) {
        Utils.logD(TAG, "schedule");
        JobScheduler js = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo job = new JobInfo.Builder(id, new ComponentName(context, NetChangeJobService.class))
                .setMinimumLatency(3000)
                .build();

        js.schedule(job);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Utils.logD(TAG, "onStartJob");
        switch (params.getJobId()) {
            case MOBILE_DATA_CHANGED:
                NetSwitchWidget.requestUpdate(this,
                        false, null);
                break;
            case WIFI_CHANGED:
                NetSwitchWidget.requestUpdate(this,
                        null, false);
                break;
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Utils.logD(TAG, "onStopJob");

        return true;
    }
}
