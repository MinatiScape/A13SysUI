package com.google.android.systemui.elmyra.actions;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Bundle;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.tuner.TunerService;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.assist.OpaEnabledReceiver;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LaunchOpa extends Action implements TunerService.Tunable {
    public final AssistManager mAssistManager;
    public boolean mEnableForAnyAssistant;
    public boolean mIsGestureEnabled;
    public boolean mIsOpaEnabled;
    public final KeyguardManager mKeyguardManager = (KeyguardManager) this.mContext.getSystemService("keyguard");
    public final AnonymousClass1 mOpaEnabledListener;
    public final StatusBar mStatusBar;

    /* loaded from: classes.dex */
    public static class Builder {
        public final Context mContext;
        public ArrayList mFeedbackEffects = new ArrayList();
        public final StatusBar mStatusBar;

        public Builder(Context context, StatusBar statusBar) {
            this.mContext = context;
            this.mStatusBar = statusBar;
        }
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final boolean isAvailable() {
        if (!this.mIsGestureEnabled || !this.mIsOpaEnabled) {
            return false;
        }
        return true;
    }

    public final void launchOpa(long j) {
        int i;
        Bundle bundle = new Bundle();
        if (this.mKeyguardManager.isKeyguardLocked()) {
            i = 14;
        } else {
            i = 13;
        }
        bundle.putInt("triggered_by", i);
        bundle.putLong("latency_id", j);
        bundle.putInt("invocation_type", 2);
        this.mAssistManager.startAssist(bundle);
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        long j;
        this.mStatusBar.collapseShade();
        triggerFeedbackEffects(detectionProperties);
        if (detectionProperties != null) {
            j = detectionProperties.mActionId;
        } else {
            j = 0;
        }
        launchOpa(j);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        if ("assist_gesture_any_assistant".equals(str)) {
            this.mEnableForAnyAssistant = "1".equals(str2);
            AssistManagerGoogle assistManagerGoogle = (AssistManagerGoogle) this.mAssistManager;
            Objects.requireNonNull(assistManagerGoogle);
            OpaEnabledReceiver opaEnabledReceiver = assistManagerGoogle.mOpaEnabledReceiver;
            Objects.requireNonNull(opaEnabledReceiver);
            opaEnabledReceiver.dispatchOpaEnabledState(opaEnabledReceiver.mContext);
        }
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final String toString() {
        return super.toString() + " [mIsGestureEnabled -> " + this.mIsGestureEnabled + "; mIsOpaEnabled -> " + this.mIsOpaEnabled + "]";
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v1, types: [com.google.android.systemui.assist.OpaEnabledListener, com.google.android.systemui.elmyra.actions.LaunchOpa$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public LaunchOpa(android.content.Context r7, com.android.systemui.statusbar.phone.StatusBar r8, java.util.ArrayList r9) {
        /*
            r6 = this;
            r6.<init>(r7, r9)
            com.google.android.systemui.elmyra.actions.LaunchOpa$1 r7 = new com.google.android.systemui.elmyra.actions.LaunchOpa$1
            r7.<init>()
            r6.mOpaEnabledListener = r7
            r6.mStatusBar = r8
            java.lang.Class<com.android.systemui.assist.AssistManager> r8 = com.android.systemui.assist.AssistManager.class
            java.lang.Object r8 = com.android.systemui.Dependency.get(r8)
            com.android.systemui.assist.AssistManager r8 = (com.android.systemui.assist.AssistManager) r8
            r6.mAssistManager = r8
            android.content.Context r9 = r6.mContext
            java.lang.String r0 = "keyguard"
            java.lang.Object r9 = r9.getSystemService(r0)
            android.app.KeyguardManager r9 = (android.app.KeyguardManager) r9
            r6.mKeyguardManager = r9
            android.content.Context r9 = r6.mContext
            android.content.ContentResolver r9 = r9.getContentResolver()
            r0 = -2
            java.lang.String r1 = "assist_gesture_enabled"
            r2 = 1
            int r9 = android.provider.Settings.Secure.getIntForUser(r9, r1, r2, r0)
            r0 = 0
            if (r9 == 0) goto L_0x0035
            r9 = r2
            goto L_0x0036
        L_0x0035:
            r9 = r0
        L_0x0036:
            r6.mIsGestureEnabled = r9
            com.google.android.systemui.elmyra.UserContentObserver r9 = new com.google.android.systemui.elmyra.UserContentObserver
            android.content.Context r3 = r6.mContext
            android.net.Uri r1 = android.provider.Settings.Secure.getUriFor(r1)
            com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda10 r4 = new com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda10
            r5 = 3
            r4.<init>(r6, r5)
            r9.<init>(r3, r1, r4, r2)
            java.lang.Class<com.android.systemui.tuner.TunerService> r9 = com.android.systemui.tuner.TunerService.class
            java.lang.Object r9 = com.android.systemui.Dependency.get(r9)
            com.android.systemui.tuner.TunerService r9 = (com.android.systemui.tuner.TunerService) r9
            java.lang.String r1 = "assist_gesture_any_assistant"
            java.lang.String[] r3 = new java.lang.String[]{r1}
            r9.addTunable(r6, r3)
            android.content.Context r9 = r6.mContext
            android.content.ContentResolver r9 = r9.getContentResolver()
            int r9 = android.provider.Settings.Secure.getInt(r9, r1, r0)
            if (r9 != r2) goto L_0x0067
            goto L_0x0068
        L_0x0067:
            r2 = r0
        L_0x0068:
            r6.mEnableForAnyAssistant = r2
            com.google.android.systemui.assist.AssistManagerGoogle r8 = (com.google.android.systemui.assist.AssistManagerGoogle) r8
            r8.addOpaEnabledListener(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.elmyra.actions.LaunchOpa.<init>(android.content.Context, com.android.systemui.statusbar.phone.StatusBar, java.util.ArrayList):void");
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final void onProgress(float f, int i) {
        updateFeedbackEffects(f, i);
    }
}
