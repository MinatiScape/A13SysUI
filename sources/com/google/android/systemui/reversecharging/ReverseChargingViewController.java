package com.google.android.systemui.reversecharging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle;
import dagger.Lazy;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ReverseChargingViewController extends BroadcastReceiver implements LifecycleOwner, BatteryController.BatteryStateChangeCallback {
    public static final boolean DEBUG = Log.isLoggable("ReverseChargingViewCtrl", 3);
    public AmbientIndicationContainer mAmbientIndicationContainer;
    public final BatteryController mBatteryController;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public String mContentDescription;
    public final Context mContext;
    public final KeyguardIndicationControllerGoogle mKeyguardIndicationController;
    public int mLevel;
    public final LifecycleRegistry mLifecycle = new LifecycleRegistry(this, true);
    public final Executor mMainExecutor;
    public String mName;
    public boolean mProvidingBattery;
    public boolean mReverse;
    public String mReverseCharging;
    public String mSlotReverseCharging;
    public final StatusBarIconController mStatusBarIconController;
    public final Lazy<StatusBar> mStatusBarLazy;

    public final void loadStrings() {
        this.mReverseCharging = this.mContext.getString(2131952105);
        this.mSlotReverseCharging = this.mContext.getString(2131953317);
        this.mContentDescription = this.mContext.getString(2131953164);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public final void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        this.mReverse = this.mBatteryController.isReverseOn();
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onBatteryLevelChanged(): rtx=");
            m.append(this.mReverse ? 1 : 0);
            m.append(" level=");
            m.append(this.mLevel);
            m.append(" name=");
            m.append(this.mName);
            m.append(" this=");
            m.append(this);
            Log.d("ReverseChargingViewCtrl", m.toString());
        }
        this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda0(this, 13));
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public final void onReverseChanged(boolean z, int i, String str) {
        boolean z2;
        this.mReverse = z;
        this.mLevel = i;
        this.mName = str;
        if (!z || i < 0) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.mProvidingBattery = z2;
        if (DEBUG) {
            StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("onReverseChanged(): rtx=", z ? 1 : 0, " level=", i, " name=");
            m.append(str);
            m.append(" this=");
            m.append(this);
            Log.d("ReverseChargingViewCtrl", m.toString());
        }
        this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda0(this, 13));
    }

    public ReverseChargingViewController(Context context, BatteryController batteryController, Lazy<StatusBar> lazy, StatusBarIconController statusBarIconController, BroadcastDispatcher broadcastDispatcher, Executor executor, KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle) {
        this.mBatteryController = batteryController;
        this.mStatusBarIconController = statusBarIconController;
        this.mStatusBarLazy = lazy;
        this.mContext = context;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mMainExecutor = executor;
        this.mKeyguardIndicationController = keyguardIndicationControllerGoogle;
        loadStrings();
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.LOCALE_CHANGED")) {
            loadStrings();
            if (DEBUG) {
                Log.d("ReverseChargingViewCtrl", "onReceive(): ACTION_LOCALE_CHANGED this=" + this);
            }
            this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda0(this, 13));
        }
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public final Lifecycle getLifecycle() {
        return this.mLifecycle;
    }
}
