package com.android.settingslib.applications;

import android.app.Application;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.ModuleInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.format.Formatter;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.util.SparseArray;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.R$dimen;
import com.android.settingslib.Utils;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda20;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda0;
import java.io.File;
import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
/* loaded from: classes.dex */
public final class ApplicationsState {
    public static ApplicationsState sInstance;
    public final int mAdminRetrieveFlags;
    public final BackgroundHandler mBackgroundHandler;
    public final Application mContext;
    public String mCurComputingSizePkg;
    public int mCurComputingSizeUserId;
    public UUID mCurComputingSizeUuid;
    public final IPackageManager mIpm;
    public final PackageManager mPm;
    public final int mRetrieveFlags;
    public final StorageStatsManager mStats;
    public final UserManager mUm;
    public static final Object sLock = new Object();
    public static final AnonymousClass1 ALPHA_COMPARATOR = new Comparator<AppEntry>() { // from class: com.android.settingslib.applications.ApplicationsState.1
        public final Collator sCollator = Collator.getInstance();

        @Override // java.util.Comparator
        public final int compare(AppEntry appEntry, AppEntry appEntry2) {
            ApplicationInfo applicationInfo;
            int compare;
            AppEntry appEntry3 = appEntry;
            AppEntry appEntry4 = appEntry2;
            int compare2 = this.sCollator.compare(appEntry3.label, appEntry4.label);
            if (compare2 != 0) {
                return compare2;
            }
            ApplicationInfo applicationInfo2 = appEntry3.info;
            if (applicationInfo2 == null || (applicationInfo = appEntry4.info) == null || (compare = this.sCollator.compare(applicationInfo2.packageName, applicationInfo.packageName)) == 0) {
                return appEntry3.info.uid - appEntry4.info.uid;
            }
            return compare;
        }
    };
    public static final AnonymousClass8 FILTER_DOWNLOADED_AND_LAUNCHER = new Object() { // from class: com.android.settingslib.applications.ApplicationsState.8
    };
    public static final AnonymousClass17 FILTER_GAMES = new Object() { // from class: com.android.settingslib.applications.ApplicationsState.17
    };
    public static final AnonymousClass18 FILTER_AUDIO = new Object() { // from class: com.android.settingslib.applications.ApplicationsState.18
    };
    public static final AnonymousClass19 FILTER_MOVIES = new Object() { // from class: com.android.settingslib.applications.ApplicationsState.19
    };
    public static final AnonymousClass20 FILTER_PHOTOS = new Object() { // from class: com.android.settingslib.applications.ApplicationsState.20
    };
    public final ArrayList<Session> mSessions = new ArrayList<>();
    public final ArrayList<Session> mRebuildingSessions = new ArrayList<>();
    public InterestingConfigChanges mInterestingConfigChanges = new InterestingConfigChanges(-2147474940);
    public final SparseArray<HashMap<String, AppEntry>> mEntriesMap = new SparseArray<>();
    public final ArrayList<AppEntry> mAppEntries = new ArrayList<>();
    public ArrayList mApplications = new ArrayList();
    public long mCurId = 1;
    public final HashMap<String, Boolean> mSystemModules = new HashMap<>();
    public final ArrayList<WeakReference<Session>> mActiveSessions = new ArrayList<>();
    public final MainHandler mMainHandler = new MainHandler(Looper.getMainLooper());

    /* loaded from: classes.dex */
    public static class AppEntry extends SizeInfo {
        public final File apkFile;
        public long externalSize;
        public boolean hasLauncherEntry;
        public Drawable icon;
        public ApplicationInfo info;
        public long internalSize;
        public boolean isHomeApp;
        public String label;
        public String labelDescription;
        public boolean mounted;
        public long sizeLoadStart;
        public long size = -1;
        public boolean sizeStale = true;

        public final boolean ensureIconLocked(Application application) {
            Object obj = ApplicationsState.sLock;
            if (ThemeOverlayApplier.SETTINGS_PACKAGE.equals(application.getPackageName())) {
                return false;
            }
            if (this.icon == null) {
                if (this.apkFile.exists()) {
                    this.icon = Utils.getBadgedIcon(application, this.info);
                    return true;
                }
                this.mounted = false;
                this.icon = application.getDrawable(17303679);
            } else if (!this.mounted && this.apkFile.exists()) {
                this.mounted = true;
                this.icon = Utils.getBadgedIcon(application, this.info);
                return true;
            }
            return false;
        }

        public AppEntry(Context context, ApplicationInfo applicationInfo, long j) {
            String str;
            File file = new File(applicationInfo.sourceDir);
            this.apkFile = file;
            this.info = applicationInfo;
            if (this.label == null || !this.mounted) {
                if (!file.exists()) {
                    this.mounted = false;
                    this.label = this.info.packageName;
                } else {
                    this.mounted = true;
                    CharSequence loadLabel = this.info.loadLabel(context.getPackageManager());
                    if (loadLabel != null) {
                        str = loadLabel.toString();
                    } else {
                        str = this.info.packageName;
                    }
                    this.label = str;
                }
            }
            if (this.labelDescription == null) {
                R$dimen.postOnBackgroundThread(new StatusBar$$ExternalSyntheticLambda20(this, context, 1));
            }
        }
    }

    /* loaded from: classes.dex */
    public class BackgroundHandler extends Handler {
        public static final /* synthetic */ int $r8$clinit = 0;
        public boolean mRunning;
        public final AnonymousClass1 mStatsObserver = new AnonymousClass1();

        /* renamed from: com.android.settingslib.applications.ApplicationsState$BackgroundHandler$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass1 extends IPackageStatsObserver.Stub {
            public AnonymousClass1() {
            }

            public final void onGetStatsCompleted(PackageStats packageStats, boolean z) {
                PackageStats packageStats2;
                boolean z2;
                long j;
                AnonymousClass1 r0 = this;
                if (z) {
                    synchronized (ApplicationsState.this.mEntriesMap) {
                        HashMap<String, AppEntry> hashMap = ApplicationsState.this.mEntriesMap.get(packageStats.userHandle);
                        if (hashMap != null) {
                            AppEntry appEntry = hashMap.get(packageStats.packageName);
                            if (appEntry != null) {
                                synchronized (appEntry) {
                                    appEntry.sizeStale = false;
                                    appEntry.sizeLoadStart = 0L;
                                    long j2 = packageStats.externalCodeSize + packageStats.externalObbSize;
                                    long j3 = packageStats.externalDataSize + packageStats.externalMediaSize;
                                    Objects.requireNonNull(ApplicationsState.this);
                                    long j4 = packageStats.codeSize;
                                    long j5 = packageStats.dataSize;
                                    long j6 = packageStats.cacheSize;
                                    long j7 = j2 + j3 + ((j4 + j5) - j6);
                                    if (appEntry.size == j7 && appEntry.cacheSize == j6 && appEntry.codeSize == j4 && appEntry.dataSize == j5 && appEntry.externalCodeSize == j2 && appEntry.externalDataSize == j3) {
                                        packageStats2 = packageStats;
                                        j = j3;
                                        if (appEntry.externalCacheSize == packageStats2.externalCacheSize) {
                                            z2 = false;
                                            r0 = this;
                                        }
                                    } else {
                                        packageStats2 = packageStats;
                                        j = j3;
                                    }
                                    appEntry.size = j7;
                                    appEntry.cacheSize = j6;
                                    appEntry.codeSize = j4;
                                    appEntry.dataSize = j5;
                                    appEntry.externalCodeSize = j2;
                                    appEntry.externalDataSize = j;
                                    appEntry.externalCacheSize = packageStats2.externalCacheSize;
                                    r0 = this;
                                    ApplicationsState.m21$$Nest$mgetSizeStr(ApplicationsState.this, j7);
                                    Objects.requireNonNull(ApplicationsState.this);
                                    long j8 = (packageStats2.codeSize + packageStats2.dataSize) - packageStats2.cacheSize;
                                    appEntry.internalSize = j8;
                                    ApplicationsState.m21$$Nest$mgetSizeStr(ApplicationsState.this, j8);
                                    Objects.requireNonNull(ApplicationsState.this);
                                    long j9 = packageStats2.externalCodeSize + packageStats2.externalDataSize + packageStats2.externalCacheSize + packageStats2.externalMediaSize + packageStats2.externalObbSize;
                                    appEntry.externalSize = j9;
                                    ApplicationsState.m21$$Nest$mgetSizeStr(ApplicationsState.this, j9);
                                    z2 = true;
                                }
                                if (z2) {
                                    ApplicationsState.this.mMainHandler.sendMessage(ApplicationsState.this.mMainHandler.obtainMessage(4, packageStats2.packageName));
                                }
                            } else {
                                packageStats2 = packageStats;
                            }
                            String str = ApplicationsState.this.mCurComputingSizePkg;
                            if (str != null && str.equals(packageStats2.packageName)) {
                                BackgroundHandler backgroundHandler = BackgroundHandler.this;
                                ApplicationsState applicationsState = ApplicationsState.this;
                                if (applicationsState.mCurComputingSizeUserId == packageStats2.userHandle) {
                                    applicationsState.mCurComputingSizePkg = null;
                                    backgroundHandler.sendEmptyMessage(7);
                                }
                            }
                        }
                    }
                }
            }
        }

        public BackgroundHandler(Looper looper) {
            super(looper);
        }

        public final void getCombinedSessionFlags(ArrayList arrayList) {
            synchronized (ApplicationsState.this.mEntriesMap) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Objects.requireNonNull((Session) it.next());
                }
            }
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            ArrayList arrayList;
            int i;
            String str;
            int i2;
            synchronized (ApplicationsState.this.mRebuildingSessions) {
                if (ApplicationsState.this.mRebuildingSessions.size() > 0) {
                    arrayList = new ArrayList(ApplicationsState.this.mRebuildingSessions);
                    ApplicationsState.this.mRebuildingSessions.clear();
                } else {
                    arrayList = null;
                }
            }
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Objects.requireNonNull((Session) it.next());
                }
            }
            getCombinedSessionFlags(ApplicationsState.this.mSessions);
            int i3 = message.what;
            int i4 = 0;
            boolean z = true;
            switch (i3) {
                case 2:
                    synchronized (ApplicationsState.this.mEntriesMap) {
                        i = 0;
                        for (int i5 = 0; i5 < ApplicationsState.this.mApplications.size() && i < 6; i5++) {
                            if (!this.mRunning) {
                                this.mRunning = true;
                                ApplicationsState.this.mMainHandler.sendMessage(ApplicationsState.this.mMainHandler.obtainMessage(6, 1));
                            }
                            ApplicationInfo applicationInfo = (ApplicationInfo) ApplicationsState.this.mApplications.get(i5);
                            int userId = UserHandle.getUserId(applicationInfo.uid);
                            if (ApplicationsState.this.mEntriesMap.get(userId).get(applicationInfo.packageName) == null) {
                                i++;
                                ApplicationsState.m20$$Nest$mgetEntryLocked(ApplicationsState.this, applicationInfo);
                            }
                            if (userId != 0) {
                                if (ApplicationsState.this.mEntriesMap.indexOfKey(0) >= 0) {
                                    AppEntry appEntry = ApplicationsState.this.mEntriesMap.get(0).get(applicationInfo.packageName);
                                    if (appEntry != null && !ApplicationsState.hasFlag(appEntry.info.flags, 8388608)) {
                                        ApplicationsState.this.mEntriesMap.get(0).remove(applicationInfo.packageName);
                                        ApplicationsState.this.mAppEntries.remove(appEntry);
                                    }
                                }
                            }
                        }
                    }
                    if (i >= 6) {
                        sendEmptyMessage(2);
                        return;
                    }
                    if (!ApplicationsState.this.mMainHandler.hasMessages(8)) {
                        ApplicationsState.this.mMainHandler.sendEmptyMessage(8);
                    }
                    sendEmptyMessage(3);
                    return;
                case 3:
                    if (ApplicationsState.hasFlag(0, 1)) {
                        ArrayList arrayList2 = new ArrayList();
                        ApplicationsState.this.mPm.getHomeActivities(arrayList2);
                        synchronized (ApplicationsState.this.mEntriesMap) {
                            int size = ApplicationsState.this.mEntriesMap.size();
                            for (int i6 = 0; i6 < size; i6++) {
                                HashMap<String, AppEntry> valueAt = ApplicationsState.this.mEntriesMap.valueAt(i6);
                                Iterator it2 = arrayList2.iterator();
                                while (it2.hasNext()) {
                                    AppEntry appEntry2 = valueAt.get(((ResolveInfo) it2.next()).activityInfo.packageName);
                                    if (appEntry2 != null) {
                                        appEntry2.isHomeApp = true;
                                    }
                                }
                            }
                        }
                    }
                    sendEmptyMessage(4);
                    return;
                case 4:
                case 5:
                    if ((i3 == 4 && ApplicationsState.hasFlag(0, 8)) || (message.what == 5 && ApplicationsState.hasFlag(0, 16))) {
                        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
                        if (message.what == 4) {
                            str = "android.intent.category.LAUNCHER";
                        } else {
                            str = "android.intent.category.LEANBACK_LAUNCHER";
                        }
                        intent.addCategory(str);
                        int i7 = 0;
                        while (i7 < ApplicationsState.this.mEntriesMap.size()) {
                            int keyAt = ApplicationsState.this.mEntriesMap.keyAt(i7);
                            List queryIntentActivitiesAsUser = ApplicationsState.this.mPm.queryIntentActivitiesAsUser(intent, 786944, keyAt);
                            synchronized (ApplicationsState.this.mEntriesMap) {
                                HashMap<String, AppEntry> valueAt2 = ApplicationsState.this.mEntriesMap.valueAt(i7);
                                int size2 = queryIntentActivitiesAsUser.size();
                                int i8 = i4;
                                while (i8 < size2) {
                                    ResolveInfo resolveInfo = (ResolveInfo) queryIntentActivitiesAsUser.get(i8);
                                    String str2 = resolveInfo.activityInfo.packageName;
                                    AppEntry appEntry3 = valueAt2.get(str2);
                                    if (appEntry3 != null) {
                                        appEntry3.hasLauncherEntry = z;
                                        boolean z2 = resolveInfo.activityInfo.enabled;
                                    } else {
                                        Log.w("ApplicationsState", "Cannot find pkg: " + str2 + " on user " + keyAt);
                                    }
                                    i8++;
                                    z = true;
                                }
                            }
                            i7++;
                            i4 = 0;
                            z = true;
                        }
                        if (!ApplicationsState.this.mMainHandler.hasMessages(7)) {
                            ApplicationsState.this.mMainHandler.sendEmptyMessage(7);
                        }
                    }
                    if (message.what == 4) {
                        sendEmptyMessage(5);
                        return;
                    } else {
                        sendEmptyMessage(6);
                        return;
                    }
                case FalsingManager.VERSION /* 6 */:
                    if (ApplicationsState.hasFlag(0, 2)) {
                        synchronized (ApplicationsState.this.mEntriesMap) {
                            i2 = 0;
                            while (i4 < ApplicationsState.this.mAppEntries.size() && i2 < 2) {
                                AppEntry appEntry4 = ApplicationsState.this.mAppEntries.get(i4);
                                if (appEntry4.icon == null || !appEntry4.mounted) {
                                    synchronized (appEntry4) {
                                        if (appEntry4.ensureIconLocked(ApplicationsState.this.mContext)) {
                                            if (!this.mRunning) {
                                                this.mRunning = true;
                                                ApplicationsState.this.mMainHandler.sendMessage(ApplicationsState.this.mMainHandler.obtainMessage(6, 1));
                                            }
                                            i2++;
                                        }
                                    }
                                }
                                i4++;
                            }
                        }
                        if (i2 > 0 && !ApplicationsState.this.mMainHandler.hasMessages(3)) {
                            ApplicationsState.this.mMainHandler.sendEmptyMessage(3);
                        }
                        if (i2 >= 2) {
                            sendEmptyMessage(6);
                            return;
                        }
                    }
                    sendEmptyMessage(7);
                    return;
                case 7:
                    if (ApplicationsState.hasFlag(0, 4)) {
                        synchronized (ApplicationsState.this.mEntriesMap) {
                            if (ApplicationsState.this.mCurComputingSizePkg == null) {
                                long uptimeMillis = SystemClock.uptimeMillis();
                                for (int i9 = 0; i9 < ApplicationsState.this.mAppEntries.size(); i9++) {
                                    AppEntry appEntry5 = ApplicationsState.this.mAppEntries.get(i9);
                                    if (ApplicationsState.hasFlag(appEntry5.info.flags, 8388608) && (appEntry5.size == -1 || appEntry5.sizeStale)) {
                                        long j = appEntry5.sizeLoadStart;
                                        if (j == 0 || j < uptimeMillis - 20000) {
                                            if (!this.mRunning) {
                                                this.mRunning = true;
                                                ApplicationsState.this.mMainHandler.sendMessage(ApplicationsState.this.mMainHandler.obtainMessage(6, 1));
                                            }
                                            appEntry5.sizeLoadStart = uptimeMillis;
                                            ApplicationsState applicationsState = ApplicationsState.this;
                                            ApplicationInfo applicationInfo2 = appEntry5.info;
                                            applicationsState.mCurComputingSizeUuid = applicationInfo2.storageUuid;
                                            applicationsState.mCurComputingSizePkg = applicationInfo2.packageName;
                                            applicationsState.mCurComputingSizeUserId = UserHandle.getUserId(applicationInfo2.uid);
                                            ApplicationsState.this.mBackgroundHandler.post(new WMShell$7$$ExternalSyntheticLambda0(this, 1));
                                        }
                                        return;
                                    }
                                }
                                if (!ApplicationsState.this.mMainHandler.hasMessages(5)) {
                                    ApplicationsState.this.mMainHandler.sendEmptyMessage(5);
                                    this.mRunning = false;
                                    ApplicationsState.this.mMainHandler.sendMessage(ApplicationsState.this.mMainHandler.obtainMessage(6, 0));
                                }
                                return;
                            }
                            return;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* loaded from: classes.dex */
    public class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            ApplicationsState applicationsState = ApplicationsState.this;
            Objects.requireNonNull(applicationsState);
            synchronized (applicationsState.mEntriesMap) {
            }
            switch (message.what) {
                case 1:
                    Session session = (Session) message.obj;
                    Iterator<WeakReference<Session>> it = ApplicationsState.this.mActiveSessions.iterator();
                    while (it.hasNext()) {
                        Session session2 = it.next().get();
                        if (session2 != null && session2 == session) {
                            Objects.requireNonNull(session);
                            throw null;
                        }
                    }
                    return;
                case 2:
                    Iterator<WeakReference<Session>> it2 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it2.hasNext()) {
                        if (it2.next().get() != null) {
                            throw null;
                        }
                    }
                    return;
                case 3:
                    Iterator<WeakReference<Session>> it3 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it3.hasNext()) {
                        if (it3.next().get() != null) {
                            throw null;
                        }
                    }
                    return;
                case 4:
                    Iterator<WeakReference<Session>> it4 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it4.hasNext()) {
                        if (it4.next().get() != null) {
                            String str = (String) message.obj;
                            throw null;
                        }
                    }
                    return;
                case 5:
                    Iterator<WeakReference<Session>> it5 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it5.hasNext()) {
                        if (it5.next().get() != null) {
                            throw null;
                        }
                    }
                    return;
                case FalsingManager.VERSION /* 6 */:
                    Iterator<WeakReference<Session>> it6 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it6.hasNext()) {
                        if (it6.next().get() != null) {
                            throw null;
                        }
                    }
                    return;
                case 7:
                    Iterator<WeakReference<Session>> it7 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it7.hasNext()) {
                        if (it7.next().get() != null) {
                            throw null;
                        }
                    }
                    return;
                case 8:
                    Iterator<WeakReference<Session>> it8 = ApplicationsState.this.mActiveSessions.iterator();
                    while (it8.hasNext()) {
                        if (it8.next().get() != null) {
                            throw null;
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class SizeInfo {
        public long cacheSize;
        public long codeSize;
        public long dataSize;
        public long externalCacheSize;
        public long externalCodeSize;
        public long externalDataSize;
    }

    public static boolean hasFlag(int i, int i2) {
        return (i & i2) != 0;
    }

    public void clearEntries() {
        for (int i = 0; i < this.mEntriesMap.size(); i++) {
            this.mEntriesMap.valueAt(i).clear();
        }
        this.mAppEntries.clear();
    }

    /* loaded from: classes.dex */
    public class Session implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        public void onDestroy() {
            onPause();
            throw null;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        public void onPause() {
            throw null;
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        public void onResume() {
            throw null;
        }
    }

    /* renamed from: -$$Nest$mgetSizeStr  reason: not valid java name */
    public static String m21$$Nest$mgetSizeStr(ApplicationsState applicationsState, long j) {
        if (j >= 0) {
            return Formatter.formatFileSize(applicationsState.mContext, j);
        }
        Objects.requireNonNull(applicationsState);
        return null;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.settingslib.applications.ApplicationsState$1] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.settingslib.applications.ApplicationsState$8] */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.settingslib.applications.ApplicationsState$17] */
    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.settingslib.applications.ApplicationsState$18] */
    /* JADX WARN: Type inference failed for: r0v6, types: [com.android.settingslib.applications.ApplicationsState$19] */
    /* JADX WARN: Type inference failed for: r0v7, types: [com.android.settingslib.applications.ApplicationsState$20] */
    static {
        Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    }

    public static ApplicationsState getInstance(Application application, IPackageManager iPackageManager) {
        ApplicationsState applicationsState;
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new ApplicationsState(application, iPackageManager);
            }
            applicationsState = sInstance;
        }
        return applicationsState;
    }

    /* renamed from: -$$Nest$mgetEntryLocked  reason: not valid java name */
    public static AppEntry m20$$Nest$mgetEntryLocked(ApplicationsState applicationsState, ApplicationInfo applicationInfo) {
        boolean z;
        Objects.requireNonNull(applicationsState);
        int userId = UserHandle.getUserId(applicationInfo.uid);
        AppEntry appEntry = applicationsState.mEntriesMap.get(userId).get(applicationInfo.packageName);
        if (appEntry == null) {
            Boolean bool = applicationsState.mSystemModules.get(applicationInfo.packageName);
            if (bool == null) {
                z = false;
            } else {
                z = bool.booleanValue();
            }
            if (z) {
                return null;
            }
            Application application = applicationsState.mContext;
            long j = applicationsState.mCurId;
            applicationsState.mCurId = 1 + j;
            appEntry = new AppEntry(application, applicationInfo, j);
            applicationsState.mEntriesMap.get(userId).put(applicationInfo.packageName, appEntry);
            applicationsState.mAppEntries.add(appEntry);
        } else if (appEntry.info != applicationInfo) {
            appEntry.info = applicationInfo;
        }
        return appEntry;
    }

    public ApplicationsState(Application application, IPackageManager iPackageManager) {
        this.mContext = application;
        this.mPm = application.getPackageManager();
        IconDrawableFactory.newInstance(application);
        this.mIpm = iPackageManager;
        UserManager userManager = (UserManager) application.getSystemService(UserManager.class);
        this.mUm = userManager;
        this.mStats = (StorageStatsManager) application.getSystemService(StorageStatsManager.class);
        for (int i : userManager.getProfileIdsWithDisabled(UserHandle.myUserId())) {
            this.mEntriesMap.put(i, new HashMap<>());
        }
        HandlerThread handlerThread = new HandlerThread("ApplicationsState.Loader");
        handlerThread.start();
        this.mBackgroundHandler = new BackgroundHandler(handlerThread.getLooper());
        this.mAdminRetrieveFlags = 4227584;
        this.mRetrieveFlags = 33280;
        for (ModuleInfo moduleInfo : this.mPm.getInstalledModules(0)) {
            this.mSystemModules.put(moduleInfo.getPackageName(), Boolean.valueOf(moduleInfo.isHidden()));
        }
        synchronized (this.mEntriesMap) {
            try {
                this.mEntriesMap.wait(1L);
            } catch (InterruptedException unused) {
            }
        }
    }

    public void setInterestingConfigChanges(InterestingConfigChanges interestingConfigChanges) {
        this.mInterestingConfigChanges = interestingConfigChanges;
    }
}
