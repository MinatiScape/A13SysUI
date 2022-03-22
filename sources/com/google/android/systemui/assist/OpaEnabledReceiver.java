package com.google.android.systemui.assist;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.PathInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.AssistUtils;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.util.Assert;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda15;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class OpaEnabledReceiver {
    public final Executor mBgExecutor;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final AssistantContentObserver mContentObserver;
    public final ContentResolver mContentResolver;
    public final Context mContext;
    public final Executor mFgExecutor;
    public boolean mIsAGSAAssistant;
    public boolean mIsLongPressHomeEnabled;
    public boolean mIsOpaEligible;
    public boolean mIsOpaEnabled;
    public final OpaEnabledSettings mOpaEnabledSettings;
    public final OpaEnabledBroadcastReceiver mBroadcastReceiver = new OpaEnabledBroadcastReceiver();
    public final ArrayList mListeners = new ArrayList();

    /* loaded from: classes.dex */
    public class AssistantContentObserver extends ContentObserver {
        public AssistantContentObserver(Context context) {
            super(new Handler(context.getMainLooper()));
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z, Uri uri) {
            OpaEnabledReceiver opaEnabledReceiver = OpaEnabledReceiver.this;
            Objects.requireNonNull(opaEnabledReceiver);
            opaEnabledReceiver.updateOpaEnabledState(true, null);
        }
    }

    /* loaded from: classes.dex */
    public class OpaEnabledBroadcastReceiver extends BroadcastReceiver {
        public OpaEnabledBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.google.android.systemui.OPA_ENABLED")) {
                boolean booleanExtra = intent.getBooleanExtra("OPA_ENABLED", false);
                OpaEnabledSettings opaEnabledSettings = OpaEnabledReceiver.this.mOpaEnabledSettings;
                Objects.requireNonNull(opaEnabledSettings);
                Assert.isNotMainThread();
                Settings.Secure.putIntForUser(opaEnabledSettings.mContext.getContentResolver(), "systemui.google.opa_enabled", booleanExtra ? 1 : 0, ActivityManager.getCurrentUser());
            } else if (intent.getAction().equals("com.google.android.systemui.OPA_USER_ENABLED")) {
                boolean booleanExtra2 = intent.getBooleanExtra("OPA_USER_ENABLED", false);
                OpaEnabledSettings opaEnabledSettings2 = OpaEnabledReceiver.this.mOpaEnabledSettings;
                Objects.requireNonNull(opaEnabledSettings2);
                Assert.isNotMainThread();
                try {
                    opaEnabledSettings2.mLockSettings.setBoolean("systemui.google.opa_user_enabled", booleanExtra2, ActivityManager.getCurrentUser());
                } catch (RemoteException e) {
                    Log.e("OpaEnabledSettings", "RemoteException on OPA_USER_ENABLED", e);
                }
            }
            OpaEnabledReceiver.this.updateOpaEnabledState(true, goAsync());
        }
    }

    public final void dispatchOpaEnabledState(Context context) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Dispatching OPA eligble = ");
        m.append(this.mIsOpaEligible);
        m.append("; AGSA = ");
        m.append(this.mIsAGSAAssistant);
        m.append("; OPA enabled = ");
        m.append(this.mIsOpaEnabled);
        Log.i("OpaEnabledReceiver", m.toString());
        for (int i = 0; i < this.mListeners.size(); i++) {
            ((OpaEnabledListener) this.mListeners.get(i)).onOpaEnabledReceived(context, this.mIsOpaEligible, this.mIsAGSAAssistant, this.mIsOpaEnabled, this.mIsLongPressHomeEnabled);
        }
    }

    public final void registerContentObserver() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assistant"), false, this.mContentObserver, -2);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assist_long_press_home_enabled"), false, this.mContentObserver, -2);
    }

    public final void registerEnabledReceiver(int i) {
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, new IntentFilter("com.google.android.systemui.OPA_ENABLED"), this.mBgExecutor, new UserHandle(i));
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, new IntentFilter("com.google.android.systemui.OPA_USER_ENABLED"), this.mBgExecutor, new UserHandle(i));
    }

    public final void updateOpaEnabledState(final boolean z, final BroadcastReceiver.PendingResult pendingResult) {
        this.mBgExecutor.execute(new Runnable() { // from class: com.google.android.systemui.assist.OpaEnabledReceiver$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                boolean z2;
                boolean z3;
                boolean z4;
                OpaEnabledReceiver opaEnabledReceiver = OpaEnabledReceiver.this;
                boolean z5 = z;
                BroadcastReceiver.PendingResult pendingResult2 = pendingResult;
                Objects.requireNonNull(opaEnabledReceiver);
                OpaEnabledSettings opaEnabledSettings = opaEnabledReceiver.mOpaEnabledSettings;
                Objects.requireNonNull(opaEnabledSettings);
                Assert.isNotMainThread();
                boolean z6 = false;
                if (Settings.Secure.getIntForUser(opaEnabledSettings.mContext.getContentResolver(), "systemui.google.opa_enabled", 0, ActivityManager.getCurrentUser()) != 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                opaEnabledReceiver.mIsOpaEligible = z2;
                OpaEnabledSettings opaEnabledSettings2 = opaEnabledReceiver.mOpaEnabledSettings;
                Objects.requireNonNull(opaEnabledSettings2);
                Assert.isNotMainThread();
                Context context = opaEnabledSettings2.mContext;
                PathInterpolator pathInterpolator = OpaUtils.INTERPOLATOR_40_40;
                ComponentName assistComponentForUser = new AssistUtils(context).getAssistComponentForUser(-2);
                if (assistComponentForUser == null || !"com.google.android.googlequicksearchbox/com.google.android.voiceinteraction.GsaVoiceInteractionService".equals(assistComponentForUser.flattenToString())) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                opaEnabledReceiver.mIsAGSAAssistant = z3;
                OpaEnabledSettings opaEnabledSettings3 = opaEnabledReceiver.mOpaEnabledSettings;
                Objects.requireNonNull(opaEnabledSettings3);
                Assert.isNotMainThread();
                try {
                    z4 = opaEnabledSettings3.mLockSettings.getBoolean("systemui.google.opa_user_enabled", false, ActivityManager.getCurrentUser());
                } catch (RemoteException e) {
                    Log.e("OpaEnabledSettings", "isOpaEnabled RemoteException", e);
                    z4 = false;
                }
                opaEnabledReceiver.mIsOpaEnabled = z4;
                OpaEnabledSettings opaEnabledSettings4 = opaEnabledReceiver.mOpaEnabledSettings;
                Objects.requireNonNull(opaEnabledSettings4);
                Assert.isNotMainThread();
                if (Settings.Secure.getInt(opaEnabledSettings4.mContext.getContentResolver(), "assist_long_press_home_enabled", opaEnabledSettings4.mContext.getResources().getBoolean(17891368) ? 1 : 0) != 0) {
                    z6 = true;
                }
                opaEnabledReceiver.mIsLongPressHomeEnabled = z6;
                if (z5) {
                    opaEnabledReceiver.mFgExecutor.execute(new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(opaEnabledReceiver, 9));
                }
                if (pendingResult2 != null) {
                    opaEnabledReceiver.mFgExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda15(pendingResult2, 10));
                }
            }
        });
    }

    public OpaEnabledReceiver(Context context, BroadcastDispatcher broadcastDispatcher, Executor executor, Executor executor2, OpaEnabledSettings opaEnabledSettings) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mContentObserver = new AssistantContentObserver(context);
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mFgExecutor = executor;
        this.mBgExecutor = executor2;
        this.mOpaEnabledSettings = opaEnabledSettings;
        updateOpaEnabledState(false, null);
        registerContentObserver();
        registerEnabledReceiver(-2);
    }

    @VisibleForTesting
    public BroadcastReceiver getBroadcastReceiver() {
        return this.mBroadcastReceiver;
    }
}
