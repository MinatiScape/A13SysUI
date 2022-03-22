package com.android.systemui.tuner;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.util.ArrayUtils;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.systemui.DejankUtils;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.TrackedCollections;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class TunerServiceImpl extends TunerService {
    public static final String[] RESET_EXCEPTION_LIST = {"sysui_qs_tiles", "doze_always_on", "qs_media_resumption", "qs_media_recommend"};
    public ContentResolver mContentResolver;
    public final Context mContext;
    public int mCurrentUser;
    public UserTracker.Callback mCurrentUserTracker;
    public final DemoModeController mDemoModeController;
    public final LeakDetector mLeakDetector;
    public final HashSet<TunerService.Tunable> mTunables;
    public final ComponentName mTunerComponent;
    public UserTracker mUserTracker;
    public final Observer mObserver = new Observer();
    public final ArrayMap<Uri, String> mListeningUris = new ArrayMap<>();
    public final ConcurrentHashMap<String, Set<TunerService.Tunable>> mTunableLookup = new ConcurrentHashMap<>();

    /* loaded from: classes.dex */
    public class Observer extends ContentObserver {
        public Observer() {
            super(new Handler(Looper.getMainLooper()));
        }

        public final void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
            if (i2 == TunerServiceImpl.this.mUserTracker.getUserId()) {
                for (Uri uri : collection) {
                    TunerServiceImpl tunerServiceImpl = TunerServiceImpl.this;
                    Objects.requireNonNull(tunerServiceImpl);
                    String str = tunerServiceImpl.mListeningUris.get(uri);
                    Set<TunerService.Tunable> set = tunerServiceImpl.mTunableLookup.get(str);
                    if (set != null) {
                        String stringForUser = Settings.Secure.getStringForUser(tunerServiceImpl.mContentResolver, str, tunerServiceImpl.mCurrentUser);
                        for (TunerService.Tunable tunable : set) {
                            tunable.onTuningChanged(str, stringForUser);
                        }
                    }
                }
            }
        }
    }

    @Override // com.android.systemui.tuner.TunerService
    public final void addTunable(TunerService.Tunable tunable, String... strArr) {
        for (final String str : strArr) {
            if (!this.mTunableLookup.containsKey(str)) {
                this.mTunableLookup.put(str, new ArraySet());
            }
            this.mTunableLookup.get(str).add(tunable);
            if (LeakDetector.ENABLED) {
                this.mTunables.add(tunable);
                LeakDetector leakDetector = this.mLeakDetector;
                HashSet<TunerService.Tunable> hashSet = this.mTunables;
                Objects.requireNonNull(leakDetector);
                TrackedCollections trackedCollections = leakDetector.mTrackedCollections;
                if (trackedCollections != null) {
                    trackedCollections.track(hashSet, "TunerService.mTunables");
                }
            }
            Uri uriFor = Settings.Secure.getUriFor(str);
            if (!this.mListeningUris.containsKey(uriFor)) {
                this.mListeningUris.put(uriFor, str);
                this.mContentResolver.registerContentObserver(uriFor, false, this.mObserver, this.mCurrentUser);
            }
            tunable.onTuningChanged(str, (String) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.tuner.TunerServiceImpl$$ExternalSyntheticLambda2
                @Override // java.util.function.Supplier
                public final Object get() {
                    TunerServiceImpl tunerServiceImpl = TunerServiceImpl.this;
                    String str2 = str;
                    Objects.requireNonNull(tunerServiceImpl);
                    return Settings.Secure.getStringForUser(tunerServiceImpl.mContentResolver, str2, tunerServiceImpl.mCurrentUser);
                }
            }));
        }
    }

    @Override // com.android.systemui.tuner.TunerService
    public final String getValue(String str) {
        return Settings.Secure.getStringForUser(this.mContentResolver, str, this.mCurrentUser);
    }

    @Override // com.android.systemui.tuner.TunerService
    public final void setValue(String str, String str2) {
        Settings.Secure.putStringForUser(this.mContentResolver, str, str2, this.mCurrentUser);
    }

    @Override // com.android.systemui.tuner.TunerService
    public final void clearAll() {
        clearAllFromUser(this.mCurrentUser);
    }

    public final void clearAllFromUser(int i) {
        DemoModeController demoModeController = this.mDemoModeController;
        Objects.requireNonNull(demoModeController);
        demoModeController.globalSettings.putInt("sysui_tuner_demo_on", 0);
        DemoModeController demoModeController2 = this.mDemoModeController;
        Objects.requireNonNull(demoModeController2);
        demoModeController2.globalSettings.putInt("sysui_demo_allowed", 0);
        for (String str : this.mTunableLookup.keySet()) {
            if (!ArrayUtils.contains(RESET_EXCEPTION_LIST, str)) {
                Settings.Secure.putStringForUser(this.mContentResolver, str, null, i);
            }
        }
    }

    @Override // com.android.systemui.tuner.TunerService
    public final int getValue(String str, int i) {
        return Settings.Secure.getIntForUser(this.mContentResolver, str, i, this.mCurrentUser);
    }

    @Override // com.android.systemui.tuner.TunerService
    public final void removeTunable(TunerService.Tunable tunable) {
        for (Set<TunerService.Tunable> set : this.mTunableLookup.values()) {
            set.remove(tunable);
        }
        if (LeakDetector.ENABLED) {
            this.mTunables.remove(tunable);
        }
    }

    @Override // com.android.systemui.tuner.TunerService
    public final void setValue(String str, int i) {
        Settings.Secure.putIntForUser(this.mContentResolver, str, i, this.mCurrentUser);
    }

    @Override // com.android.systemui.tuner.TunerService
    public final void showResetRequest(final CarrierTextManager$$ExternalSyntheticLambda0 carrierTextManager$$ExternalSyntheticLambda0) {
        SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
        SystemUIDialog.setShowForAllUsers(systemUIDialog);
        systemUIDialog.setMessage(2131953154);
        systemUIDialog.setButton(-2, this.mContext.getString(2131952094), (DialogInterface.OnClickListener) null);
        systemUIDialog.setButton(-1, this.mContext.getString(2131953044), new DialogInterface.OnClickListener() { // from class: com.android.systemui.tuner.TunerServiceImpl$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                TunerServiceImpl tunerServiceImpl = TunerServiceImpl.this;
                Runnable runnable = carrierTextManager$$ExternalSyntheticLambda0;
                Objects.requireNonNull(tunerServiceImpl);
                tunerServiceImpl.mContext.sendBroadcast(new Intent("com.android.systemui.action.CLEAR_TUNER"));
                tunerServiceImpl.mUserTracker.getUserContext().getPackageManager().setComponentEnabledSetting(tunerServiceImpl.mTunerComponent, 2, 1);
                Settings.Secure.putInt(tunerServiceImpl.mContext.getContentResolver(), "seen_tuner_warning", 0);
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        systemUIDialog.show();
    }

    public TunerServiceImpl(Context context, Handler handler, LeakDetector leakDetector, DemoModeController demoModeController, UserTracker userTracker) {
        HashSet<TunerService.Tunable> hashSet;
        String value;
        if (LeakDetector.ENABLED) {
            hashSet = new HashSet<>();
        } else {
            hashSet = null;
        }
        this.mTunables = hashSet;
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mLeakDetector = leakDetector;
        this.mDemoModeController = demoModeController;
        this.mUserTracker = userTracker;
        this.mTunerComponent = new ComponentName(context, TunerActivity.class);
        for (UserInfo userInfo : UserManager.get(context).getUsers()) {
            this.mCurrentUser = userInfo.getUserHandle().getIdentifier();
            if (getValue("sysui_tuner_version", 0) != 4) {
                int value2 = getValue("sysui_tuner_version", 0);
                if (value2 < 1 && (value = getValue("icon_blacklist")) != null) {
                    ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(this.mContext, value);
                    iconHideList.add("rotate");
                    iconHideList.add("headset");
                    Settings.Secure.putStringForUser(this.mContentResolver, "icon_blacklist", TextUtils.join(",", iconHideList), this.mCurrentUser);
                }
                if (value2 < 2) {
                    this.mUserTracker.getUserContext().getPackageManager().setComponentEnabledSetting(this.mTunerComponent, 2, 1);
                }
                if (value2 < 4) {
                    final int i = this.mCurrentUser;
                    handler.postDelayed(new Runnable() { // from class: com.android.systemui.tuner.TunerServiceImpl$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            TunerServiceImpl tunerServiceImpl = TunerServiceImpl.this;
                            int i2 = i;
                            Objects.requireNonNull(tunerServiceImpl);
                            tunerServiceImpl.clearAllFromUser(i2);
                        }
                    }, 5000L);
                }
                setValue("sysui_tuner_version", 4);
            }
        }
        this.mCurrentUser = this.mUserTracker.getUserId();
        UserTracker.Callback callback = new UserTracker.Callback() { // from class: com.android.systemui.tuner.TunerServiceImpl.1
            @Override // com.android.systemui.settings.UserTracker.Callback
            public final void onUserChanged(int i2) {
                TunerServiceImpl tunerServiceImpl = TunerServiceImpl.this;
                tunerServiceImpl.mCurrentUser = i2;
                for (String str : tunerServiceImpl.mTunableLookup.keySet()) {
                    String stringForUser = Settings.Secure.getStringForUser(tunerServiceImpl.mContentResolver, str, tunerServiceImpl.mCurrentUser);
                    for (TunerService.Tunable tunable : tunerServiceImpl.mTunableLookup.get(str)) {
                        tunable.onTuningChanged(str, stringForUser);
                    }
                }
                TunerServiceImpl tunerServiceImpl2 = TunerServiceImpl.this;
                Objects.requireNonNull(tunerServiceImpl2);
                if (tunerServiceImpl2.mListeningUris.size() != 0) {
                    tunerServiceImpl2.mContentResolver.unregisterContentObserver(tunerServiceImpl2.mObserver);
                    for (Uri uri : tunerServiceImpl2.mListeningUris.keySet()) {
                        tunerServiceImpl2.mContentResolver.registerContentObserver(uri, false, tunerServiceImpl2.mObserver, tunerServiceImpl2.mCurrentUser);
                    }
                }
            }
        };
        this.mCurrentUserTracker = callback;
        this.mUserTracker.addCallback(callback, new HandlerExecutor(handler));
    }
}
