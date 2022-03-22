package com.android.systemui.statusbar.phone;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.systemui.doze.DozeHost;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.DozeServiceHost;
import com.android.systemui.statusbar.phone.ScrimController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DozeScrimController implements StatusBarStateController.StateListener {
    public static final boolean DEBUG = Log.isLoggable("DozeScrimController", 3);
    public final DozeLog mDozeLog;
    public final DozeParameters mDozeParameters;
    public boolean mDozing;
    public boolean mFullyPulsing;
    public DozeHost.PulseCallback mPulseCallback;
    public int mPulseReason;
    public final Handler mHandler = new Handler();
    public final AnonymousClass1 mScrimCallback = new ScrimController.Callback() { // from class: com.android.systemui.statusbar.phone.DozeScrimController.1
        @Override // com.android.systemui.statusbar.phone.ScrimController.Callback
        public final void onCancelled() {
            DozeScrimController.this.pulseFinished();
        }

        @Override // com.android.systemui.statusbar.phone.ScrimController.Callback
        public final void onDisplayBlanked() {
            if (DozeScrimController.DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Pulse in, mDozing=");
                m.append(DozeScrimController.this.mDozing);
                m.append(" mPulseReason=");
                m.append(DozeLog.reasonToString(DozeScrimController.this.mPulseReason));
                Log.d("DozeScrimController", m.toString());
            }
            DozeScrimController dozeScrimController = DozeScrimController.this;
            if (dozeScrimController.mDozing) {
                Objects.requireNonNull(dozeScrimController);
                dozeScrimController.mDozeLog.tracePulseStart(dozeScrimController.mPulseReason);
                DozeHost.PulseCallback pulseCallback = dozeScrimController.mPulseCallback;
                if (pulseCallback != null) {
                    pulseCallback.onPulseStarted();
                }
            }
        }

        @Override // com.android.systemui.statusbar.phone.ScrimController.Callback
        public final void onFinished() {
            if (DozeScrimController.DEBUG) {
                KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Pulse in finished, mDozing="), DozeScrimController.this.mDozing, "DozeScrimController");
            }
            DozeScrimController dozeScrimController = DozeScrimController.this;
            if (dozeScrimController.mDozing) {
                int i = dozeScrimController.mPulseReason;
                if (!(i == 1 || i == 6)) {
                    Handler handler = dozeScrimController.mHandler;
                    AnonymousClass3 r3 = dozeScrimController.mPulseOut;
                    DozeParameters dozeParameters = dozeScrimController.mDozeParameters;
                    Objects.requireNonNull(dozeParameters);
                    handler.postDelayed(r3, dozeParameters.getInt("doze.pulse.duration.visible", 2131492918));
                    DozeScrimController dozeScrimController2 = DozeScrimController.this;
                    Handler handler2 = dozeScrimController2.mHandler;
                    AnonymousClass2 r32 = dozeScrimController2.mPulseOutExtended;
                    DozeParameters dozeParameters2 = dozeScrimController2.mDozeParameters;
                    Objects.requireNonNull(dozeParameters2);
                    handler2.postDelayed(r32, dozeParameters2.getInt("doze.pulse.duration.visible", 2131492918) * 2);
                }
                DozeScrimController.this.mFullyPulsing = true;
            }
        }
    };
    public final AnonymousClass2 mPulseOutExtended = new Runnable() { // from class: com.android.systemui.statusbar.phone.DozeScrimController.2
        @Override // java.lang.Runnable
        public final void run() {
            DozeScrimController dozeScrimController = DozeScrimController.this;
            dozeScrimController.mHandler.removeCallbacks(dozeScrimController.mPulseOut);
            DozeScrimController.this.mPulseOut.run();
        }
    };
    public final AnonymousClass3 mPulseOut = new AnonymousClass3();

    /* renamed from: com.android.systemui.statusbar.phone.DozeScrimController$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements Runnable {
        public AnonymousClass3() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            DozeScrimController dozeScrimController = DozeScrimController.this;
            dozeScrimController.mFullyPulsing = false;
            dozeScrimController.mHandler.removeCallbacks(dozeScrimController.mPulseOut);
            DozeScrimController dozeScrimController2 = DozeScrimController.this;
            dozeScrimController2.mHandler.removeCallbacks(dozeScrimController2.mPulseOutExtended);
            if (DozeScrimController.DEBUG) {
                KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Pulse out, mDozing="), DozeScrimController.this.mDozing, "DozeScrimController");
            }
            DozeScrimController dozeScrimController3 = DozeScrimController.this;
            if (dozeScrimController3.mDozing) {
                dozeScrimController3.pulseFinished();
            }
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozingChanged(boolean z) {
        if (this.mDozing != z) {
            this.mDozeLog.traceDozingChanged(z);
        }
        setDozing(z);
    }

    public final void pulse(DozeServiceHost.AnonymousClass1 r3, int i) {
        boolean z;
        if (!this.mDozing || this.mPulseCallback != null) {
            if (DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Pulse suppressed. Dozing: ");
                m.append(this.mDozeParameters);
                m.append(" had callback? ");
                if (this.mPulseCallback != null) {
                    z = true;
                } else {
                    z = false;
                }
                KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(m, z, "DozeScrimController");
            }
            r3.onPulseFinished();
            if (!this.mDozing) {
                this.mDozeLog.tracePulseDropped("device isn't dozing");
                return;
            }
            DozeLog dozeLog = this.mDozeLog;
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("already has pulse callback mPulseCallback=");
            m2.append(this.mPulseCallback);
            dozeLog.tracePulseDropped(m2.toString());
            return;
        }
        this.mPulseCallback = r3;
        this.mPulseReason = i;
    }

    public final void pulseFinished() {
        this.mDozeLog.tracePulseFinish();
        DozeHost.PulseCallback pulseCallback = this.mPulseCallback;
        if (pulseCallback != null) {
            pulseCallback.onPulseFinished();
            this.mPulseCallback = null;
        }
    }

    @VisibleForTesting
    public void setDozing(boolean z) {
        if (this.mDozing != z) {
            this.mDozing = z;
            if (!z && this.mPulseCallback != null) {
                if (DEBUG) {
                    Log.d("DozeScrimController", "Cancel pulsing");
                }
                this.mFullyPulsing = false;
                this.mHandler.removeCallbacks(this.mPulseOut);
                this.mHandler.removeCallbacks(this.mPulseOutExtended);
                pulseFinished();
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.phone.DozeScrimController$1] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.statusbar.phone.DozeScrimController$2] */
    public DozeScrimController(DozeParameters dozeParameters, DozeLog dozeLog, StatusBarStateController statusBarStateController) {
        this.mDozeParameters = dozeParameters;
        statusBarStateController.addCallback(this);
        this.mDozeLog = dozeLog;
    }
}
