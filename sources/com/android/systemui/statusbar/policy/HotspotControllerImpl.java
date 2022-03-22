package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.Context;
import android.net.TetheringManager;
import android.net.wifi.WifiClient;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.util.ConcurrentUtils;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda6;
import com.android.systemui.statusbar.policy.HotspotController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class HotspotControllerImpl implements HotspotController, WifiManager.SoftApCallback {
    public static final boolean DEBUG = Log.isLoggable("HotspotController", 3);
    public final Context mContext;
    public int mHotspotState;
    public final Handler mMainHandler;
    public volatile int mNumConnectedDevices;
    public final TetheringManager mTetheringManager;
    public boolean mWaitingForTerminalState;
    public final WifiManager mWifiManager;
    public final ArrayList<HotspotController.Callback> mCallbacks = new ArrayList<>();
    public volatile boolean mIsTetheringSupported = true;
    public volatile boolean mHasTetherableWifiRegexs = true;
    public AnonymousClass1 mTetheringCallback = new TetheringManager.TetheringEventCallback() { // from class: com.android.systemui.statusbar.policy.HotspotControllerImpl.1
        public final void onTetheringSupported(boolean z) {
            if (HotspotControllerImpl.this.mIsTetheringSupported != z) {
                HotspotControllerImpl.this.mIsTetheringSupported = z;
                HotspotControllerImpl.m114$$Nest$mfireHotspotAvailabilityChanged(HotspotControllerImpl.this);
            }
        }

        public final void onTetherableInterfaceRegexpsChanged(TetheringManager.TetheringInterfaceRegexps tetheringInterfaceRegexps) {
            boolean z;
            if (tetheringInterfaceRegexps.getTetherableWifiRegexs().size() != 0) {
                z = true;
            } else {
                z = false;
            }
            if (HotspotControllerImpl.this.mHasTetherableWifiRegexs != z) {
                HotspotControllerImpl.this.mHasTetherableWifiRegexs = z;
                HotspotControllerImpl.m114$$Nest$mfireHotspotAvailabilityChanged(HotspotControllerImpl.this);
            }
        }
    };

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(HotspotController.Callback callback) {
        HotspotController.Callback callback2 = callback;
        synchronized (this.mCallbacks) {
            if (callback2 != null) {
                if (!this.mCallbacks.contains(callback2)) {
                    if (DEBUG) {
                        Log.d("HotspotController", "addCallback " + callback2);
                    }
                    this.mCallbacks.add(callback2);
                    if (this.mWifiManager != null) {
                        if (this.mCallbacks.size() == 1) {
                            this.mWifiManager.registerSoftApCallback(new HandlerExecutor(this.mMainHandler), this);
                        } else {
                            this.mMainHandler.post(new ScreenshotController$$ExternalSyntheticLambda6(this, callback2, 2));
                        }
                    }
                }
            }
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        printWriter.println("HotspotController state:");
        printWriter.print("  available=");
        printWriter.println(isHotspotSupported());
        printWriter.print("  mHotspotState=");
        switch (this.mHotspotState) {
            case 10:
                str = "DISABLING";
                break;
            case QSTileImpl.H.STALE /* 11 */:
                str = "DISABLED";
                break;
            case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                str = "ENABLING";
                break;
            case QS.VERSION /* 13 */:
                str = "ENABLED";
                break;
            case 14:
                str = "FAILED";
                break;
            default:
                str = null;
                break;
        }
        printWriter.println(str);
        printWriter.print("  mNumConnectedDevices=");
        printWriter.println(this.mNumConnectedDevices);
        printWriter.print("  mWaitingForTerminalState=");
        printWriter.println(this.mWaitingForTerminalState);
    }

    public final void fireHotspotChangedCallback() {
        ArrayList arrayList;
        synchronized (this.mCallbacks) {
            arrayList = new ArrayList(this.mCallbacks);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((HotspotController.Callback) it.next()).onHotspotChanged(isHotspotEnabled(), this.mNumConnectedDevices);
        }
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public final boolean isHotspotEnabled() {
        if (this.mHotspotState == 13) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public final boolean isHotspotSupported() {
        if (!this.mIsTetheringSupported || !this.mHasTetherableWifiRegexs || !UserManager.get(this.mContext).isUserAdmin(ActivityManager.getCurrentUser())) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public final boolean isHotspotTransient() {
        if (this.mWaitingForTerminalState || this.mHotspotState == 12) {
            return true;
        }
        return false;
    }

    public final void maybeResetSoftApState() {
        if (this.mWaitingForTerminalState) {
            int i = this.mHotspotState;
            if (!(i == 11 || i == 13)) {
                if (i == 14) {
                    this.mTetheringManager.stopTethering(0);
                } else {
                    return;
                }
            }
            this.mWaitingForTerminalState = false;
        }
    }

    public final void onStateChanged(int i, int i2) {
        this.mHotspotState = i;
        maybeResetSoftApState();
        if (!isHotspotEnabled()) {
            this.mNumConnectedDevices = 0;
        }
        fireHotspotChangedCallback();
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(HotspotController.Callback callback) {
        WifiManager wifiManager;
        HotspotController.Callback callback2 = callback;
        if (callback2 != null) {
            if (DEBUG) {
                Log.d("HotspotController", "removeCallback " + callback2);
            }
            synchronized (this.mCallbacks) {
                this.mCallbacks.remove(callback2);
                if (this.mCallbacks.isEmpty() && (wifiManager = this.mWifiManager) != null) {
                    wifiManager.unregisterSoftApCallback(this);
                }
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public final void setHotspotEnabled(boolean z) {
        if (this.mWaitingForTerminalState) {
            if (DEBUG) {
                Log.d("HotspotController", "Ignoring setHotspotEnabled; waiting for terminal state.");
            }
        } else if (z) {
            this.mWaitingForTerminalState = true;
            if (DEBUG) {
                Log.d("HotspotController", "Starting tethering");
            }
            this.mTetheringManager.startTethering(new TetheringManager.TetheringRequest.Builder(0).setShouldShowEntitlementUi(false).build(), ConcurrentUtils.DIRECT_EXECUTOR, new TetheringManager.StartTetheringCallback() { // from class: com.android.systemui.statusbar.policy.HotspotControllerImpl.2
                public final void onTetheringFailed(int i) {
                    if (HotspotControllerImpl.DEBUG) {
                        Log.d("HotspotController", "onTetheringFailed");
                    }
                    HotspotControllerImpl.this.maybeResetSoftApState();
                    HotspotControllerImpl.this.fireHotspotChangedCallback();
                }
            });
        } else {
            this.mTetheringManager.stopTethering(0);
        }
    }

    /* renamed from: -$$Nest$mfireHotspotAvailabilityChanged  reason: not valid java name */
    public static void m114$$Nest$mfireHotspotAvailabilityChanged(HotspotControllerImpl hotspotControllerImpl) {
        ArrayList arrayList;
        Objects.requireNonNull(hotspotControllerImpl);
        synchronized (hotspotControllerImpl.mCallbacks) {
            arrayList = new ArrayList(hotspotControllerImpl.mCallbacks);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ((HotspotController.Callback) it.next()).onHotspotAvailabilityChanged(hotspotControllerImpl.isHotspotSupported());
        }
    }

    public HotspotControllerImpl(Context context, Handler handler, Handler handler2, DumpManager dumpManager) {
        this.mContext = context;
        TetheringManager tetheringManager = (TetheringManager) context.getSystemService(TetheringManager.class);
        this.mTetheringManager = tetheringManager;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mMainHandler = handler;
        tetheringManager.registerTetheringEventCallback(new HandlerExecutor(handler2), this.mTetheringCallback);
        dumpManager.registerDumpable("HotspotControllerImpl", this);
    }

    public final void onConnectedClientsChanged(List<WifiClient> list) {
        this.mNumConnectedDevices = list.size();
        fireHotspotChangedCallback();
    }

    @Override // com.android.systemui.statusbar.policy.HotspotController
    public final int getNumConnectedDevices() {
        return this.mNumConnectedDevices;
    }
}
