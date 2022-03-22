package com.android.settingslib.applications;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.Slog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class ServiceListing {
    public final boolean mAddDeviceLockedFlags;
    public final ContentResolver mContentResolver;
    public final Context mContext;
    public final String mIntentAction;
    public boolean mListening;
    public final String mNoun;
    public final String mPermission;
    public final String mSetting;
    public final String mTag;
    public final HashSet<ComponentName> mEnabledServices = new HashSet<>();
    public final ArrayList mServices = new ArrayList();
    public final ArrayList mCallbacks = new ArrayList();
    public final AnonymousClass1 mSettingsObserver = new ContentObserver(new Handler()) { // from class: com.android.settingslib.applications.ServiceListing.1
        @Override // android.database.ContentObserver
        public final void onChange(boolean z, Uri uri) {
            ServiceListing.this.reload();
        }
    };
    public final AnonymousClass2 mPackageReceiver = new BroadcastReceiver() { // from class: com.android.settingslib.applications.ServiceListing.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            ServiceListing.this.reload();
        }
    };

    /* loaded from: classes.dex */
    public interface Callback {
        void onServicesReloaded(ArrayList arrayList);
    }

    public final void reload() {
        this.mEnabledServices.clear();
        String string = Settings.Secure.getString(this.mContentResolver, this.mSetting);
        if (string != null && !"".equals(string)) {
            for (String str : string.split(":")) {
                ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
                if (unflattenFromString != null) {
                    this.mEnabledServices.add(unflattenFromString);
                }
            }
        }
        this.mServices.clear();
        int currentUser = ActivityManager.getCurrentUser();
        int i = 132;
        if (this.mAddDeviceLockedFlags) {
            i = 786564;
        }
        for (ResolveInfo resolveInfo : this.mContext.getPackageManager().queryIntentServicesAsUser(new Intent(this.mIntentAction), i, currentUser)) {
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (!this.mPermission.equals(serviceInfo.permission)) {
                String str2 = this.mTag;
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Skipping ");
                m.append(this.mNoun);
                m.append(" service ");
                m.append(serviceInfo.packageName);
                m.append("/");
                m.append(serviceInfo.name);
                m.append(": it does not require the permission ");
                m.append(this.mPermission);
                Slog.w(str2, m.toString());
            } else {
                this.mServices.add(serviceInfo);
            }
        }
        Iterator it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            ((Callback) it.next()).onServicesReloaded(this.mServices);
        }
    }

    public final void setListening(boolean z) {
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
                intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
                intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
                intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
                intentFilter.addDataScheme("package");
                this.mContext.registerReceiver(this.mPackageReceiver, intentFilter);
                this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor(this.mSetting), false, this.mSettingsObserver);
                return;
            }
            this.mContext.unregisterReceiver(this.mPackageReceiver);
            this.mContentResolver.unregisterContentObserver(this.mSettingsObserver);
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.settingslib.applications.ServiceListing$1] */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.settingslib.applications.ServiceListing$2] */
    public ServiceListing(Context context, String str, String str2, String str3, String str4, String str5, boolean z) {
        this.mContentResolver = context.getContentResolver();
        this.mContext = context;
        this.mTag = str;
        this.mSetting = str2;
        this.mIntentAction = str3;
        this.mPermission = str4;
        this.mNoun = str5;
        this.mAddDeviceLockedFlags = z;
    }
}
