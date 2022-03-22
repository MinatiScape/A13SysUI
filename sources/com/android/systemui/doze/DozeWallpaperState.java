package com.android.systemui.doze;

import android.app.IWallpaperManager;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public final class DozeWallpaperState implements DozeMachine.Part {
    public static final boolean DEBUG = Log.isLoggable("DozeWallpaperState", 3);
    public final BiometricUnlockController mBiometricUnlockController;
    public final DozeParameters mDozeParameters;
    public boolean mIsAmbientMode;
    public final IWallpaperManager mWallpaperManagerService;

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void dump(PrintWriter printWriter) {
        boolean z;
        StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(LockIconView$$ExternalSyntheticOutline0.m(printWriter, "DozeWallpaperState:", " isAmbientMode: "), this.mIsAmbientMode, printWriter, " hasWallpaperService: ");
        if (this.mWallpaperManagerService != null) {
            z = true;
        } else {
            z = false;
        }
        m.append(z);
        printWriter.println(m.toString());
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x003f, code lost:
        if (r7 != false) goto L_0x0041;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0041, code lost:
        if (r6 != false) goto L_0x0043;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0043, code lost:
        r2 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0044, code lost:
        r6 = r2;
     */
    @Override // com.android.systemui.doze.DozeMachine.Part
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void transitionTo(com.android.systemui.doze.DozeMachine.State r6, com.android.systemui.doze.DozeMachine.State r7) {
        /*
            r5 = this;
            java.lang.String r0 = "DozeWallpaperState"
            int r1 = r7.ordinal()
            r2 = 0
            r3 = 1
            switch(r1) {
                case 2: goto L_0x000d;
                case 3: goto L_0x000d;
                case 4: goto L_0x000d;
                case 5: goto L_0x000d;
                case 6: goto L_0x000b;
                case 7: goto L_0x000d;
                case 8: goto L_0x000b;
                case 9: goto L_0x000d;
                case 10: goto L_0x000d;
                case 11: goto L_0x000d;
                default: goto L_0x000b;
            }
        L_0x000b:
            r1 = r2
            goto L_0x000e
        L_0x000d:
            r1 = r3
        L_0x000e:
            if (r1 == 0) goto L_0x0018
            com.android.systemui.statusbar.phone.DozeParameters r6 = r5.mDozeParameters
            java.util.Objects.requireNonNull(r6)
            boolean r6 = r6.mControlScreenOffAnimation
            goto L_0x0045
        L_0x0018:
            com.android.systemui.doze.DozeMachine$State r4 = com.android.systemui.doze.DozeMachine.State.DOZE_PULSING
            if (r6 != r4) goto L_0x0022
            com.android.systemui.doze.DozeMachine$State r6 = com.android.systemui.doze.DozeMachine.State.FINISH
            if (r7 != r6) goto L_0x0022
            r6 = r3
            goto L_0x0023
        L_0x0022:
            r6 = r2
        L_0x0023:
            com.android.systemui.statusbar.phone.DozeParameters r7 = r5.mDozeParameters
            boolean r7 = r7.getDisplayNeedsBlanking()
            r7 = r7 ^ r3
            if (r7 == 0) goto L_0x0041
            com.android.systemui.statusbar.phone.BiometricUnlockController r7 = r5.mBiometricUnlockController
            java.util.Objects.requireNonNull(r7)
            boolean r4 = r7.isWakeAndUnlock()
            if (r4 != 0) goto L_0x003e
            boolean r7 = r7.mFadedAwayAfterWakeAndUnlock
            if (r7 == 0) goto L_0x003c
            goto L_0x003e
        L_0x003c:
            r7 = r2
            goto L_0x003f
        L_0x003e:
            r7 = r3
        L_0x003f:
            if (r7 == 0) goto L_0x0043
        L_0x0041:
            if (r6 == 0) goto L_0x0044
        L_0x0043:
            r2 = r3
        L_0x0044:
            r6 = r2
        L_0x0045:
            boolean r7 = r5.mIsAmbientMode
            if (r1 == r7) goto L_0x0092
            r5.mIsAmbientMode = r1
            android.app.IWallpaperManager r7 = r5.mWallpaperManagerService
            if (r7 == 0) goto L_0x0092
            if (r6 == 0) goto L_0x0054
            r6 = 500(0x1f4, double:2.47E-321)
            goto L_0x0056
        L_0x0054:
            r6 = 0
        L_0x0056:
            boolean r1 = com.android.systemui.doze.DozeWallpaperState.DEBUG     // Catch: RemoteException -> 0x0080
            if (r1 == 0) goto L_0x0078
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: RemoteException -> 0x0080
            r1.<init>()     // Catch: RemoteException -> 0x0080
            java.lang.String r2 = "AOD wallpaper state changed to: "
            r1.append(r2)     // Catch: RemoteException -> 0x0080
            boolean r2 = r5.mIsAmbientMode     // Catch: RemoteException -> 0x0080
            r1.append(r2)     // Catch: RemoteException -> 0x0080
            java.lang.String r2 = ", animationDuration: "
            r1.append(r2)     // Catch: RemoteException -> 0x0080
            r1.append(r6)     // Catch: RemoteException -> 0x0080
            java.lang.String r1 = r1.toString()     // Catch: RemoteException -> 0x0080
            android.util.Log.i(r0, r1)     // Catch: RemoteException -> 0x0080
        L_0x0078:
            android.app.IWallpaperManager r1 = r5.mWallpaperManagerService     // Catch: RemoteException -> 0x0080
            boolean r2 = r5.mIsAmbientMode     // Catch: RemoteException -> 0x0080
            r1.setInAmbientMode(r2, r6)     // Catch: RemoteException -> 0x0080
            goto L_0x0092
        L_0x0080:
            java.lang.String r6 = "Cannot notify state to WallpaperManagerService: "
            java.lang.StringBuilder r6 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r6)
            boolean r5 = r5.mIsAmbientMode
            r6.append(r5)
            java.lang.String r5 = r6.toString()
            android.util.Log.w(r0, r5)
        L_0x0092:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeWallpaperState.transitionTo(com.android.systemui.doze.DozeMachine$State, com.android.systemui.doze.DozeMachine$State):void");
    }

    public DozeWallpaperState(IWallpaperManager iWallpaperManager, BiometricUnlockController biometricUnlockController, DozeParameters dozeParameters) {
        this.mWallpaperManagerService = iWallpaperManager;
        this.mBiometricUnlockController = biometricUnlockController;
        this.mDozeParameters = dozeParameters;
    }
}
