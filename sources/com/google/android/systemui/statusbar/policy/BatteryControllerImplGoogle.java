package com.google.android.systemui.statusbar.policy;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.settings.UserContentResolverProvider;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryControllerImpl;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BatteryControllerImplGoogle extends BatteryControllerImpl implements ReverseChargingController.ReverseChargingChangeCallback {
    public static final String EBS_STATE_AUTHORITY = "com.google.android.flipendo.api";
    public final ContentObserver mContentObserver;
    public final UserContentResolverProvider mContentResolverProvider;
    public boolean mExtremeSaver;
    public String mName;
    public boolean mReverse;
    public final ReverseChargingController mReverseChargingController;
    public int mRtxLevel;
    public static final Uri IS_EBS_ENABLED_OBSERVABLE_URI = Uri.parse("content://com.google.android.flipendo.api/get_flipendo_state");
    public static final boolean DEBUG = Log.isLoggable("BatteryControllerGoogle", 3);

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.systemui.statusbar.policy.BatteryControllerImpl
    public final void addCallback(BatteryController.BatteryStateChangeCallback batteryStateChangeCallback) {
        super.addCallback(batteryStateChangeCallback);
        batteryStateChangeCallback.onReverseChanged(this.mReverse, this.mRtxLevel, this.mName);
        batteryStateChangeCallback.onExtremeBatterySaverChanged(this.mExtremeSaver);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryControllerImpl, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("BatteryController state:");
        printWriter.print("  mLevel=");
        printWriter.println(this.mLevel);
        printWriter.print("  mPluggedIn=");
        printWriter.println(this.mPluggedIn);
        printWriter.print("  mCharging=");
        printWriter.println(this.mCharging);
        printWriter.print("  mCharged=");
        printWriter.println(this.mCharged);
        printWriter.print("  mPowerSave=");
        printWriter.println(this.mPowerSave);
        printWriter.print("  mStateUnknown=");
        printWriter.println(this.mStateUnknown);
        printWriter.print("  mReverse=");
        printWriter.println(this.mReverse);
        printWriter.print("  mExtremeSaver=");
        printWriter.println(this.mExtremeSaver);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public final boolean isReverseSupported() {
        return this.mReverseChargingController.isReverseSupported();
    }

    @Override // com.google.android.systemui.reversecharging.ReverseChargingController.ReverseChargingChangeCallback
    public final void onReverseChargingChanged(boolean z, int i, String str) {
        this.mReverse = z;
        this.mRtxLevel = i;
        this.mName = str;
        if (DEBUG) {
            StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("onReverseChargingChanged(): rtx=", z ? 1 : 0, " level=", i, " name=");
            m.append(str);
            m.append(" this=");
            m.append(this);
            Log.d("BatteryControllerGoogle", m.toString());
        }
        synchronized (this.mChangeCallbacks) {
            ArrayList arrayList = new ArrayList(this.mChangeCallbacks);
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((BatteryController.BatteryStateChangeCallback) arrayList.get(i2)).onReverseChanged(this.mReverse, this.mRtxLevel, this.mName);
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public final void setReverseState(boolean z) {
        ReverseChargingController reverseChargingController = this.mReverseChargingController;
        Objects.requireNonNull(reverseChargingController);
        if (reverseChargingController.isReverseSupported()) {
            if (ReverseChargingController.DEBUG) {
                ExifInterface$$ExternalSyntheticOutline1.m("setReverseState(): rtx=", z ? 1 : 0, "ReverseChargingControl");
            }
            reverseChargingController.mStopReverseAtAcUnplug = false;
            reverseChargingController.setReverseStateInternal(z, 2);
        }
    }

    public BatteryControllerImplGoogle(Context context, EnhancedEstimates enhancedEstimates, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, Handler handler, Handler handler2, UserContentResolverProvider userContentResolverProvider, ReverseChargingController reverseChargingController) {
        super(context, enhancedEstimates, powerManager, broadcastDispatcher, demoModeController, handler, handler2);
        this.mReverseChargingController = reverseChargingController;
        this.mContentResolverProvider = userContentResolverProvider;
        this.mContentObserver = new ContentObserver(handler2) { // from class: com.google.android.systemui.statusbar.policy.BatteryControllerImplGoogle.1
            @Override // android.database.ContentObserver
            public final void onChange(boolean z, Uri uri) {
                Bundle bundle;
                if (BatteryControllerImplGoogle.DEBUG) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Change in EBS value ");
                    m.append(uri.toSafeString());
                    Log.d("BatteryControllerGoogle", m.toString());
                }
                BatteryControllerImplGoogle batteryControllerImplGoogle = BatteryControllerImplGoogle.this;
                Objects.requireNonNull(batteryControllerImplGoogle);
                try {
                    bundle = batteryControllerImplGoogle.mContentResolverProvider.getUserContentResolver().call(BatteryControllerImplGoogle.EBS_STATE_AUTHORITY, "get_flipendo_state", (String) null, new Bundle());
                } catch (IllegalArgumentException unused) {
                    bundle = new Bundle();
                }
                boolean z2 = bundle.getBoolean("flipendo_state", false);
                Objects.requireNonNull(batteryControllerImplGoogle);
                if (z2 != batteryControllerImplGoogle.mExtremeSaver) {
                    batteryControllerImplGoogle.mExtremeSaver = z2;
                    synchronized (batteryControllerImplGoogle.mChangeCallbacks) {
                        int size = batteryControllerImplGoogle.mChangeCallbacks.size();
                        for (int i = 0; i < size; i++) {
                            batteryControllerImplGoogle.mChangeCallbacks.get(i).onExtremeBatterySaverChanged(batteryControllerImplGoogle.mExtremeSaver);
                        }
                    }
                }
            }
        };
    }

    @Override // com.android.systemui.statusbar.policy.BatteryControllerImpl
    public final void init() {
        super.init();
        this.mReverse = false;
        this.mRtxLevel = -1;
        this.mName = null;
        this.mReverseChargingController.init(this);
        ReverseChargingController reverseChargingController = this.mReverseChargingController;
        Objects.requireNonNull(reverseChargingController);
        synchronized (reverseChargingController.mChangeCallbacks) {
            reverseChargingController.mChangeCallbacks.add(this);
        }
        onReverseChargingChanged(reverseChargingController.mReverse, reverseChargingController.mRtxLevel, reverseChargingController.mName);
        try {
            ContentResolver userContentResolver = this.mContentResolverProvider.getUserContentResolver();
            Uri uri = IS_EBS_ENABLED_OBSERVABLE_URI;
            userContentResolver.registerContentObserver(uri, false, this.mContentObserver, -1);
            this.mContentObserver.onChange(false, uri);
        } catch (Exception e) {
            Log.w("BatteryControllerGoogle", "Couldn't register to observe provider", e);
        }
    }

    @Override // com.android.systemui.statusbar.policy.BatteryControllerImpl, android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        this.mReverseChargingController.handleIntentForReverseCharging(intent);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public final boolean isReverseOn() {
        return this.mReverse;
    }
}
