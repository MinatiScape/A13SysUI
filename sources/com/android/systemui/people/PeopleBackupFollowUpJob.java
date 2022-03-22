package com.android.systemui.people;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.people.IPeopleManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.ServiceManager;
import android.preference.PreferenceManager;
import android.util.Log;
import com.android.systemui.people.widget.PeopleBackupHelper;
import com.android.systemui.people.widget.PeopleTileKey;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/* loaded from: classes.dex */
public class PeopleBackupFollowUpJob extends JobService {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Context mContext;
    public IPeopleManager mIPeopleManager;
    public JobScheduler mJobScheduler;
    public final Object mLock = new Object();
    public PackageManager mPackageManager;
    public static final long JOB_PERIODIC_DURATION = Duration.ofHours(6).toMillis();
    public static final long CLEAN_UP_STORAGE_AFTER_DURATION = Duration.ofHours(48).toMillis();

    @Override // android.app.job.JobService
    public final boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    @Override // android.app.job.JobService
    public final boolean onStartJob(JobParameters jobParameters) {
        boolean z;
        synchronized (this.mLock) {
            try {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor edit = defaultSharedPreferences.edit();
                SharedPreferences sharedPreferences = getSharedPreferences("shared_follow_up", 0);
                SharedPreferences.Editor edit2 = sharedPreferences.edit();
                HashMap processFollowUpFile = processFollowUpFile(sharedPreferences, edit2);
                long j = jobParameters.getExtras().getLong("start_date");
                long currentTimeMillis = System.currentTimeMillis();
                boolean z2 = true;
                if (!processFollowUpFile.isEmpty()) {
                    if (currentTimeMillis - j > CLEAN_UP_STORAGE_AFTER_DURATION) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z) {
                        z2 = false;
                    }
                }
                if (z2) {
                    cancelJobAndClearRemainingWidgets(processFollowUpFile, edit2, defaultSharedPreferences);
                }
                edit.apply();
                edit2.apply();
            } catch (Throwable th) {
                throw th;
            }
        }
        PeopleBackupHelper.updateWidgets(this.mContext);
        return false;
    }

    public final HashMap processFollowUpFile(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        HashMap hashMap = new HashMap();
        for (Map.Entry<String, ?> entry : sharedPreferences.getAll().entrySet()) {
            String key = entry.getKey();
            if (PeopleBackupHelper.isReadyForRestore(this.mIPeopleManager, this.mPackageManager, PeopleTileKey.fromString(key))) {
                editor.remove(key);
            } else {
                try {
                    hashMap.put(entry.getKey(), (Set) entry.getValue());
                } catch (Exception unused) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Malformed entry value: ");
                    m.append(entry.getValue());
                    Log.e("PeopleBackupFollowUpJob", m.toString());
                }
            }
        }
        return hashMap;
    }

    public void setManagers(Context context, PackageManager packageManager, IPeopleManager iPeopleManager, JobScheduler jobScheduler) {
        this.mContext = context;
        this.mPackageManager = packageManager;
        this.mIPeopleManager = iPeopleManager;
        this.mJobScheduler = jobScheduler;
    }

    public final void cancelJobAndClearRemainingWidgets(HashMap hashMap, SharedPreferences.Editor editor, SharedPreferences sharedPreferences) {
        for (Map.Entry entry : hashMap.entrySet()) {
            PeopleTileKey fromString = PeopleTileKey.fromString((String) entry.getKey());
            if (!PeopleTileKey.isValid(fromString)) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Malformed peopleTileKey in follow-up file: ");
                m.append((String) entry.getKey());
                Log.e("PeopleBackupFollowUpJob", m.toString());
            } else {
                try {
                    for (String str : (Set) entry.getValue()) {
                        try {
                            int parseInt = Integer.parseInt(str);
                            PeopleSpaceUtils.removeSharedPreferencesStorageForTile(this.mContext, fromString, parseInt, sharedPreferences.getString(String.valueOf(parseInt), ""));
                        } catch (NumberFormatException e) {
                            Log.e("PeopleBackupFollowUpJob", "Malformed widget id in follow-up file: " + e);
                        }
                    }
                } catch (Exception e2) {
                    Log.e("PeopleBackupFollowUpJob", "Malformed widget ids in follow-up file: " + e2);
                }
            }
        }
        editor.clear();
        this.mJobScheduler.cancel(74823873);
    }

    @Override // android.app.Service
    public final void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
        this.mPackageManager = getApplicationContext().getPackageManager();
        this.mIPeopleManager = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
        this.mJobScheduler = (JobScheduler) this.mContext.getSystemService(JobScheduler.class);
    }
}
