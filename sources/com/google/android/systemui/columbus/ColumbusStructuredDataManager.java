package com.google.android.systemui.columbus;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.settings.UserTracker;
import com.google.android.setupcompat.partnerconfig.ResourceEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/* compiled from: ColumbusStructuredDataManager.kt */
/* loaded from: classes.dex */
public final class ColumbusStructuredDataManager {
    public final Set<String> allowPackageList;
    public final Executor bgExecutor;
    public final ContentResolver contentResolver;
    public final Object lock = new Object();
    public JSONArray packageStats = fetchPackageStats();
    public final UserTracker userTracker;

    public static JSONObject makeJSONObject$default(ColumbusStructuredDataManager columbusStructuredDataManager, String str, int i, long j, int i2) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            j = 0;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(ResourceEntry.KEY_PACKAGE_NAME, str);
        jSONObject.put("shownCount", i);
        jSONObject.put("lastDeny", j);
        return jSONObject;
    }

    public final JSONArray fetchPackageStats() {
        JSONArray jSONArray;
        synchronized (this.lock) {
            String stringForUser = Settings.Secure.getStringForUser(this.contentResolver, "columbus_package_stats", this.userTracker.getUserId());
            if (stringForUser == null) {
                stringForUser = "[]";
            }
            try {
                jSONArray = new JSONArray(stringForUser);
            } catch (JSONException e) {
                Log.e("Columbus/StructuredData", "Failed to parse package counts", e);
                jSONArray = new JSONArray();
            }
        }
        return jSONArray;
    }

    public final long getLastDenyTimestamp(String str) {
        synchronized (this.lock) {
            int length = this.packageStats.length();
            int i = 0;
            while (i < length) {
                int i2 = i + 1;
                JSONObject jSONObject = this.packageStats.getJSONObject(i);
                if (Intrinsics.areEqual(str, jSONObject.getString(ResourceEntry.KEY_PACKAGE_NAME))) {
                    return jSONObject.getLong("lastDeny");
                }
                i = i2;
            }
            return 0L;
        }
    }

    public final int getPackageShownCount(String str) {
        synchronized (this.lock) {
            int length = this.packageStats.length();
            int i = 0;
            while (i < length) {
                int i2 = i + 1;
                JSONObject jSONObject = this.packageStats.getJSONObject(i);
                if (Intrinsics.areEqual(str, jSONObject.getString(ResourceEntry.KEY_PACKAGE_NAME))) {
                    return jSONObject.getInt("shownCount");
                }
                i = i2;
            }
            return 0;
        }
    }

    public final void storePackageStats() {
        synchronized (this.lock) {
            Settings.Secure.putStringForUser(this.contentResolver, "columbus_package_stats", this.packageStats.toString(), this.userTracker.getUserId());
        }
    }

    public ColumbusStructuredDataManager(Context context, UserTracker userTracker, Executor executor) {
        this.userTracker = userTracker;
        this.bgExecutor = executor;
        this.contentResolver = context.getContentResolver();
        String[] stringArray = context.getResources().getStringArray(2130903087);
        this.allowPackageList = SetsKt__SetsKt.setOf(Arrays.copyOf(stringArray, stringArray.length));
        UserTracker.Callback columbusStructuredDataManager$userTrackerCallback$1 = new UserTracker.Callback() { // from class: com.google.android.systemui.columbus.ColumbusStructuredDataManager$userTrackerCallback$1
            @Override // com.android.systemui.settings.UserTracker.Callback
            public final void onUserChanged(int i) {
                ColumbusStructuredDataManager columbusStructuredDataManager = ColumbusStructuredDataManager.this;
                synchronized (columbusStructuredDataManager.lock) {
                    columbusStructuredDataManager.packageStats = columbusStructuredDataManager.fetchPackageStats();
                }
            }
        };
        BroadcastReceiver columbusStructuredDataManager$broadcastReceiver$1 = new BroadcastReceiver() { // from class: com.google.android.systemui.columbus.ColumbusStructuredDataManager$broadcastReceiver$1
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context2, Intent intent) {
                List list;
                String dataString;
                if (intent == null || (dataString = intent.getDataString()) == null) {
                    list = null;
                } else {
                    list = StringsKt__StringsKt.split$default(dataString, new String[]{":"});
                }
                if (list != null) {
                    if (list.size() != 2) {
                        Log.e("Columbus/StructuredData", Intrinsics.stringPlus("Unexpected package name tokens: ", CollectionsKt___CollectionsKt.joinToString$default(list, ",", null, null, null, 62)));
                        return;
                    }
                    String str = (String) list.get(1);
                    int i = 0;
                    if (!intent.getBooleanExtra("android.intent.extra.REPLACING", false) && ColumbusStructuredDataManager.this.allowPackageList.contains(str)) {
                        ColumbusStructuredDataManager columbusStructuredDataManager = ColumbusStructuredDataManager.this;
                        Objects.requireNonNull(columbusStructuredDataManager);
                        synchronized (columbusStructuredDataManager.lock) {
                            int length = columbusStructuredDataManager.packageStats.length();
                            while (i < length) {
                                int i2 = i + 1;
                                if (Intrinsics.areEqual(str, columbusStructuredDataManager.packageStats.getJSONObject(i).getString(ResourceEntry.KEY_PACKAGE_NAME))) {
                                    columbusStructuredDataManager.packageStats.remove(i);
                                    columbusStructuredDataManager.storePackageStats();
                                    return;
                                }
                                i = i2;
                            }
                        }
                    }
                }
            }
        };
        userTracker.addCallback(columbusStructuredDataManager$userTrackerCallback$1, executor);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        context.registerReceiver(columbusStructuredDataManager$broadcastReceiver$1, intentFilter);
    }
}
