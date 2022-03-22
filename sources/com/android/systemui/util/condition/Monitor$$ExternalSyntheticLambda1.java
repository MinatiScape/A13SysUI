package com.android.systemui.util.condition;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.util.Log;
import com.android.systemui.statusbar.connectivity.CallbackHandler;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.util.condition.Monitor;
import com.android.wm.shell.common.DisplayInsetsController;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Monitor$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ Monitor$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        String str;
        switch (this.$r8$classId) {
            case 0:
                Monitor monitor = (Monitor) this.f$0;
                Monitor.Callback callback = (Monitor.Callback) this.f$1;
                Objects.requireNonNull(monitor);
                if (Log.isLoggable("Monitor", 3)) {
                    Log.d("Monitor", "removing callback");
                }
                Iterator<Monitor.Callback> it = monitor.mCallbacks.iterator();
                while (it.hasNext()) {
                    Monitor.Callback next = it.next();
                    if (next == null || next == callback) {
                        it.remove();
                    }
                }
                if (monitor.mCallbacks.isEmpty() && monitor.mHaveConditionsStarted) {
                    if (Log.isLoggable("Monitor", 3)) {
                        Log.d("Monitor", "stopping all conditions");
                    }
                    monitor.mConditions.forEach(new Monitor$$ExternalSyntheticLambda2(monitor, 0));
                    monitor.mAllConditionsMet = false;
                    monitor.mHaveConditionsStarted = false;
                    return;
                }
                return;
            case 1:
                CallbackHandler callbackHandler = (CallbackHandler) this.f$0;
                MobileDataIndicators mobileDataIndicators = (MobileDataIndicators) this.f$1;
                SimpleDateFormat simpleDateFormat = CallbackHandler.SSDF;
                Objects.requireNonNull(callbackHandler);
                Iterator<SignalCallback> it2 = callbackHandler.mSignalCallbacks.iterator();
                while (it2.hasNext()) {
                    it2.next().setMobileDataIndicators(mobileDataIndicators);
                }
                return;
            case 2:
                DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl displayWindowInsetsControllerImpl = (DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl) this.f$0;
                int i = DisplayInsetsController.PerDisplay.DisplayWindowInsetsControllerImpl.$r8$clinit;
                Objects.requireNonNull(displayWindowInsetsControllerImpl);
                DisplayInsetsController.PerDisplay perDisplay = DisplayInsetsController.PerDisplay.this;
                Objects.requireNonNull(perDisplay);
                CopyOnWriteArrayList<DisplayInsetsController.OnInsetsChangedListener> copyOnWriteArrayList = DisplayInsetsController.this.mListeners.get(perDisplay.mDisplayId);
                if (copyOnWriteArrayList != null) {
                    Iterator<DisplayInsetsController.OnInsetsChangedListener> it3 = copyOnWriteArrayList.iterator();
                    while (it3.hasNext()) {
                        it3.next().topFocusedWindowChanged();
                    }
                    return;
                }
                return;
            default:
                ReverseChargingController reverseChargingController = (ReverseChargingController) this.f$0;
                Bundle bundle = (Bundle) this.f$1;
                boolean z = ReverseChargingController.DEBUG;
                Objects.requireNonNull(reverseChargingController);
                if (ReverseChargingController.DEBUG) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onDockPresentChangedOnMainThread(): rtx =");
                    m.append(reverseChargingController.mReverse ? 1 : 0);
                    m.append(" type=");
                    m.append((int) bundle.getByte("key_dock_present_type"));
                    m.append(" bundle=");
                    m.append(bundle.toString());
                    m.append(" this=");
                    m.append(reverseChargingController);
                    Log.d("ReverseChargingControl", m.toString());
                }
                if (bundle.getByte("key_dock_present_type") == 4) {
                    str = reverseChargingController.mContext.getString(2131953162);
                } else {
                    str = null;
                }
                reverseChargingController.mName = str;
                return;
        }
    }
}
