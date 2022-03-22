package com.google.android.systemui.elmyra.gates;

import android.app.ActivityManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import com.google.android.systemui.elmyra.UserContentObserver;
import com.google.android.systemui.elmyra.actions.Action;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardDeferredSetup extends Gate {
    public boolean mDeferredSetupComplete;
    public final ArrayList mExceptions;
    public final KeyguardVisibility mKeyguardGate;
    public final AnonymousClass1 mKeyguardGateListener;
    public final UserContentObserver mSettingsObserver;

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final boolean isBlocked() {
        for (int i = 0; i < this.mExceptions.size(); i++) {
            if (((Action) this.mExceptions.get(i)).isAvailable()) {
                return false;
            }
        }
        return !this.mDeferredSetupComplete && this.mKeyguardGate.isBlocking();
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onActivate() {
        this.mKeyguardGate.activate();
        boolean z = false;
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "assist_gesture_setup_complete", 0, -2) != 0) {
            z = true;
        }
        this.mDeferredSetupComplete = z;
        this.mSettingsObserver.activate();
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final void onDeactivate() {
        this.mKeyguardGate.deactivate();
        UserContentObserver userContentObserver = this.mSettingsObserver;
        Objects.requireNonNull(userContentObserver);
        userContentObserver.mContext.getContentResolver().unregisterContentObserver(userContentObserver);
        try {
            ActivityManager.getService().unregisterUserSwitchObserver(userContentObserver.mUserSwitchCallback);
        } catch (RemoteException e) {
            Log.e("Elmyra/UserContentObserver", "Failed to unregister user switch observer", e);
        }
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public final String toString() {
        return super.toString() + " [mDeferredSetupComplete -> " + this.mDeferredSetupComplete + "]";
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.elmyra.gates.KeyguardDeferredSetup$1, com.google.android.systemui.elmyra.gates.Gate$Listener] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public KeyguardDeferredSetup(android.content.Context r4, java.util.List<com.google.android.systemui.elmyra.actions.Action> r5) {
        /*
            r3 = this;
            r3.<init>(r4)
            com.google.android.systemui.elmyra.gates.KeyguardDeferredSetup$1 r0 = new com.google.android.systemui.elmyra.gates.KeyguardDeferredSetup$1
            r0.<init>()
            r3.mKeyguardGateListener = r0
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>(r5)
            r3.mExceptions = r1
            com.google.android.systemui.elmyra.gates.KeyguardVisibility r5 = new com.google.android.systemui.elmyra.gates.KeyguardVisibility
            r5.<init>(r4)
            r3.mKeyguardGate = r5
            r5.mListener = r0
            com.google.android.systemui.elmyra.UserContentObserver r5 = new com.google.android.systemui.elmyra.UserContentObserver
            java.lang.String r0 = "assist_gesture_setup_complete"
            android.net.Uri r0 = android.provider.Settings.Secure.getUriFor(r0)
            com.android.systemui.recents.OverviewProxyService$$ExternalSyntheticLambda4 r1 = new com.android.systemui.recents.OverviewProxyService$$ExternalSyntheticLambda4
            r2 = 2
            r1.<init>(r3, r2)
            r2 = 0
            r5.<init>(r4, r0, r1, r2)
            r3.mSettingsObserver = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.elmyra.gates.KeyguardDeferredSetup.<init>(android.content.Context, java.util.List):void");
    }
}
