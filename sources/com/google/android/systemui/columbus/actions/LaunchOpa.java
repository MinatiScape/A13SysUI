package com.google.android.systemui.columbus.actions;

import android.app.KeyguardManager;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
/* compiled from: LaunchOpa.kt */
/* loaded from: classes.dex */
public final class LaunchOpa extends UserAction {
    public final AssistManagerGoogle assistManager;
    public boolean enableForAnyAssistant;
    public boolean isGestureEnabled;
    public boolean isOpaEnabled;
    public final Lazy<KeyguardManager> keyguardManager;
    public final LaunchOpa$opaEnabledListener$1 opaEnabledListener;
    public final StatusBar statusBar;
    public final String tag = "Columbus/LaunchOpa";
    public final LaunchOpa$tunable$1 tunable;
    public final UiEventLogger uiEventLogger;

    @Override // com.google.android.systemui.columbus.actions.UserAction
    public final boolean availableOnLockscreen() {
        return true;
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        long j;
        int i;
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_ASSISTANT);
        this.statusBar.collapseShade();
        if (detectionProperties == null) {
            j = 0;
        } else {
            j = detectionProperties.actionId;
        }
        Bundle bundle = new Bundle();
        if (this.keyguardManager.get().isKeyguardLocked()) {
            i = 120;
        } else {
            i = 119;
        }
        bundle.putInt("triggered_by", i);
        bundle.putLong("latency_id", j);
        bundle.putInt("invocation_type", 2);
        AssistManagerGoogle assistManagerGoogle = this.assistManager;
        if (assistManagerGoogle != null) {
            assistManagerGoogle.startAssist(bundle);
        }
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" [isGestureEnabled -> ");
        sb.append(this.isGestureEnabled);
        sb.append("; isOpaEnabled -> ");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.isOpaEnabled, ']');
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r14v1, types: [com.android.systemui.tuner.TunerService$Tunable, com.google.android.systemui.columbus.actions.LaunchOpa$tunable$1] */
    /* JADX WARN: Type inference failed for: r9v3, types: [com.google.android.systemui.columbus.actions.LaunchOpa$opaEnabledListener$1, com.google.android.systemui.assist.OpaEnabledListener] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public LaunchOpa(android.content.Context r8, com.android.systemui.statusbar.phone.StatusBar r9, java.util.Set<com.google.android.systemui.columbus.feedback.FeedbackEffect> r10, com.android.systemui.assist.AssistManager r11, dagger.Lazy<android.app.KeyguardManager> r12, com.android.systemui.tuner.TunerService r13, com.google.android.systemui.columbus.ColumbusContentObserver.Factory r14, com.android.internal.logging.UiEventLogger r15) {
        /*
            r7 = this;
            r7.<init>(r8, r10)
            r7.statusBar = r9
            r7.keyguardManager = r12
            r7.uiEventLogger = r15
            java.lang.String r9 = "Columbus/LaunchOpa"
            r7.tag = r9
            boolean r9 = r11 instanceof com.google.android.systemui.assist.AssistManagerGoogle
            if (r9 == 0) goto L_0x0014
            com.google.android.systemui.assist.AssistManagerGoogle r11 = (com.google.android.systemui.assist.AssistManagerGoogle) r11
            goto L_0x0015
        L_0x0014:
            r11 = 0
        L_0x0015:
            r7.assistManager = r11
            com.google.android.systemui.columbus.actions.LaunchOpa$opaEnabledListener$1 r9 = new com.google.android.systemui.columbus.actions.LaunchOpa$opaEnabledListener$1
            r9.<init>()
            r7.opaEnabledListener = r9
            java.lang.String r10 = "assist_gesture_enabled"
            android.net.Uri r2 = android.provider.Settings.Secure.getUriFor(r10)
            com.google.android.systemui.columbus.actions.LaunchOpa$settingsObserver$1 r3 = new com.google.android.systemui.columbus.actions.LaunchOpa$settingsObserver$1
            r3.<init>(r7)
            java.util.Objects.requireNonNull(r14)
            com.google.android.systemui.columbus.ColumbusContentObserver r12 = new com.google.android.systemui.columbus.ColumbusContentObserver
            com.google.android.systemui.columbus.ContentResolverWrapper r1 = r14.contentResolver
            com.android.systemui.settings.UserTracker r4 = r14.userTracker
            java.util.concurrent.Executor r5 = r14.executor
            android.os.Handler r6 = r14.handler
            r0 = r12
            r0.<init>(r1, r2, r3, r4, r5, r6)
            com.google.android.systemui.columbus.actions.LaunchOpa$tunable$1 r14 = new com.google.android.systemui.columbus.actions.LaunchOpa$tunable$1
            r14.<init>()
            r7.tunable = r14
            android.content.ContentResolver r15 = r8.getContentResolver()
            r0 = -2
            r1 = 1
            int r10 = android.provider.Settings.Secure.getIntForUser(r15, r10, r1, r0)
            r15 = 0
            if (r10 == 0) goto L_0x0050
            r10 = r1
            goto L_0x0051
        L_0x0050:
            r10 = r15
        L_0x0051:
            r7.isGestureEnabled = r10
            android.content.ContentResolver r8 = r8.getContentResolver()
            java.lang.String r10 = "assist_gesture_any_assistant"
            int r8 = android.provider.Settings.Secure.getInt(r8, r10, r15)
            if (r8 != r1) goto L_0x0061
            r8 = r1
            goto L_0x0062
        L_0x0061:
            r8 = r15
        L_0x0062:
            r7.enableForAnyAssistant = r8
            com.android.systemui.settings.UserTracker r8 = r12.userTracker
            com.google.android.systemui.columbus.ColumbusContentObserver$userTrackerCallback$1 r0 = r12.userTrackerCallback
            java.util.concurrent.Executor r2 = r12.executor
            r8.addCallback(r0, r2)
            r12.updateContentObserver()
            java.lang.String[] r8 = new java.lang.String[]{r10}
            r13.addTunable(r14, r8)
            if (r11 != 0) goto L_0x007a
            goto L_0x007d
        L_0x007a:
            r11.addOpaEnabledListener(r9)
        L_0x007d:
            boolean r8 = r7.isGestureEnabled
            if (r8 == 0) goto L_0x0086
            boolean r8 = r7.isOpaEnabled
            if (r8 == 0) goto L_0x0086
            goto L_0x0087
        L_0x0086:
            r1 = r15
        L_0x0087:
            r7.setAvailable(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.columbus.actions.LaunchOpa.<init>(android.content.Context, com.android.systemui.statusbar.phone.StatusBar, java.util.Set, com.android.systemui.assist.AssistManager, dagger.Lazy, com.android.systemui.tuner.TunerService, com.google.android.systemui.columbus.ColumbusContentObserver$Factory, com.android.internal.logging.UiEventLogger):void");
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}
