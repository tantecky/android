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
    private final static int ID = 1;
    private final static String TAG = "NetChangeJobService";

    public static void schedule(Context context) {
        Log.v(TAG, "schedule");
        JobScheduler js = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo job = new JobInfo.Builder(ID, new ComponentName(context, NetChangeJobService.class))
                .setMinimumLatency(3000)
                .build();

        js.schedule(job);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.v(TAG, "onStartJob");
        NetSwitchWidget.requestUpdate(this);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.v(TAG, "onStopJob");

        return true;
    }
}
