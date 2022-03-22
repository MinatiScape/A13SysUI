package com.google.android.systemui.statusbar.notification.voicereplies;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.os.PowerManager;
import android.widget.Button;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import java.util.List;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class VoiceReplyTarget {
    public final PendingIntent actionIntent;
    public final Notification.Builder builder;
    public final NotificationEntry entry;
    public final Button expandedViewReplyButton;
    public final RemoteInput freeformInput;
    public final List<NotificationVoiceReplyHandler> handlers;
    public final NotificationVoiceReplyLogger logger;
    public final String notifKey;
    public final NotificationShadeWindowController notifShadeWindowController;
    public final NotificationRemoteInputManager notificationRemoteInputManager;
    public final long postTime;
    public final PowerManager powerManager;
    public final RemoteInput[] remoteInputs;
    public final LockscreenShadeTransitionController shadeTransitionController;
    public final StatusBar statusBar;
    public final StatusBarKeyguardViewManager statusBarKeyguardViewManager;
    public final SysuiStatusBarStateController statusBarStateController;
    public final int userId;

    /* JADX WARN: Multi-variable type inference failed */
    public VoiceReplyTarget(NotificationEntry notificationEntry, Notification.Builder builder, long j, List<? extends NotificationVoiceReplyHandler> list, PendingIntent pendingIntent, RemoteInput[] remoteInputArr, RemoteInput remoteInput, Button button, NotificationRemoteInputManager notificationRemoteInputManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, StatusBar statusBar, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationVoiceReplyLogger notificationVoiceReplyLogger, NotificationShadeWindowController notificationShadeWindowController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, PowerManager powerManager) {
        this.entry = notificationEntry;
        this.builder = builder;
        this.postTime = j;
        this.handlers = list;
        this.actionIntent = pendingIntent;
        this.remoteInputs = remoteInputArr;
        this.freeformInput = remoteInput;
        this.expandedViewReplyButton = button;
        this.notificationRemoteInputManager = notificationRemoteInputManager;
        this.shadeTransitionController = lockscreenShadeTransitionController;
        this.statusBar = statusBar;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.logger = notificationVoiceReplyLogger;
        this.notifShadeWindowController = notificationShadeWindowController;
        this.statusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.powerManager = powerManager;
        this.notifKey = notificationEntry.mKey;
        this.userId = notificationEntry.mSbn.getUser().getIdentifier();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitFocusState$2$listener$1, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.Object awaitFocusState(final com.android.systemui.statusbar.policy.RemoteInputView r2, final boolean r3, kotlin.coroutines.Continuation r4) {
        /*
            com.android.systemui.statusbar.policy.RemoteInputView$RemoteEditText r0 = r2.mEditText
            r1 = 1
            if (r0 == 0) goto L_0x000d
            boolean r0 = r0.hasFocus()
            if (r0 == 0) goto L_0x000d
            r0 = r1
            goto L_0x000e
        L_0x000d:
            r0 = 0
        L_0x000e:
            if (r0 != r3) goto L_0x0013
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            return r2
        L_0x0013:
            kotlinx.coroutines.CancellableContinuationImpl r0 = new kotlinx.coroutines.CancellableContinuationImpl
            kotlin.coroutines.Continuation r4 = androidx.preference.R$color.intercepted(r4)
            r0.<init>(r4, r1)
            r0.initCancellability()
            java.util.concurrent.atomic.AtomicBoolean r4 = new java.util.concurrent.atomic.AtomicBoolean
            r4.<init>(r1)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitFocusState$2$listener$1 r1 = new com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitFocusState$2$listener$1
            r1.<init>()
            java.util.ArrayList<android.view.View$OnFocusChangeListener> r3 = r2.mEditTextFocusChangeListeners
            r3.add(r1)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitFocusState$2$1 r3 = new com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitFocusState$2$1
            r3.<init>(r2, r1)
            r0.invokeOnCancellation(r3)
            java.lang.Object r2 = r0.getResult()
            kotlin.coroutines.intrinsics.CoroutineSingletons r3 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            if (r2 != r3) goto L_0x003f
            return r2
        L_0x003f:
            kotlin.Unit r2 = kotlin.Unit.INSTANCE
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget.awaitFocusState(com.android.systemui.statusbar.policy.RemoteInputView, boolean, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0040  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:40:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v1, types: [java.lang.Object, com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$awaitKeyguardReset$2$callback$1] */
    /* JADX WARN: Type inference failed for: r7v0, types: [com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$awaitStateChange$2$cb$1, com.android.systemui.statusbar.phone.StatusBarWindowCallback] */
    /* JADX WARN: Unknown variable types count: 2 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:19:0x0073 -> B:20:0x0078). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final java.lang.Object access$awaitKeyguardNotOccluded(final com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r9, kotlin.coroutines.Continuation r10) {
        /*
            Method dump skipped, instructions count: 255
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget.access$awaitKeyguardNotOccluded(com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$3$callback$1, com.android.systemui.plugins.statusbar.StatusBarStateController$StateListener] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$2$callback$1, com.android.systemui.plugins.statusbar.StatusBarStateController$StateListener] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final java.lang.Object access$expandShade(final com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget r6, kotlin.coroutines.Continuation r7) {
        /*
            java.util.Objects.requireNonNull(r6)
            com.android.systemui.log.LogLevel r0 = com.android.systemui.log.LogLevel.DEBUG
            com.android.systemui.statusbar.SysuiStatusBarStateController r1 = r6.statusBarStateController
            int r1 = r1.getState()
            r2 = 1
            java.lang.String r3 = "NotifVoiceReply"
            if (r1 == 0) goto L_0x0084
            r4 = 2
            if (r1 == r2) goto L_0x0034
            if (r1 == r4) goto L_0x00da
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r6 = r6.logger
            java.util.Objects.requireNonNull(r6)
            com.android.systemui.log.LogBuffer r6 = r6.logBuffer
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStatic$2 r7 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStatic$2
            java.lang.String r1 = "Unknown state, cancelling"
            r7.<init>(r1)
            java.util.Objects.requireNonNull(r6)
            boolean r1 = r6.frozen
            if (r1 != 0) goto L_0x0031
            com.android.systemui.log.LogMessageImpl r7 = r6.obtain(r3, r0, r7)
            r6.push(r7)
        L_0x0031:
            r2 = 0
            goto L_0x00da
        L_0x0034:
            kotlinx.coroutines.CancellableContinuationImpl r1 = new kotlinx.coroutines.CancellableContinuationImpl
            kotlin.coroutines.Continuation r7 = androidx.preference.R$color.intercepted(r7)
            r1.<init>(r7, r2)
            r1.initCancellability()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r7 = r6.logger
            java.util.Objects.requireNonNull(r7)
            com.android.systemui.log.LogBuffer r7 = r7.logBuffer
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStatic$2 r4 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStatic$2
            java.lang.String r5 = "On keyguard, waiting for SHADE_LOCKED state"
            r4.<init>(r5)
            java.util.Objects.requireNonNull(r7)
            boolean r5 = r7.frozen
            if (r5 != 0) goto L_0x005c
            com.android.systemui.log.LogMessageImpl r0 = r7.obtain(r3, r0, r4)
            r7.push(r0)
        L_0x005c:
            java.util.concurrent.atomic.AtomicBoolean r7 = new java.util.concurrent.atomic.AtomicBoolean
            r7.<init>(r2)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$2$callback$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$2$callback$1
            r0.<init>()
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$2$1 r7 = new com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$2$1
            r7.<init>(r6, r0)
            r1.invokeOnCancellation(r7)
            com.android.systemui.statusbar.SysuiStatusBarStateController r7 = r6.statusBarStateController
            r7.addCallback(r0)
            com.android.systemui.statusbar.LockscreenShadeTransitionController r7 = r6.shadeTransitionController
            com.android.systemui.statusbar.notification.collection.NotificationEntry r6 = r6.entry
            java.util.Objects.requireNonNull(r6)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r6 = r6.row
            r7.goToLockedShade(r6, r2)
            java.lang.Object r6 = r1.getResult()
            goto L_0x00de
        L_0x0084:
            com.android.systemui.statusbar.SysuiStatusBarStateController r1 = r6.statusBarStateController
            boolean r1 = r1.isExpanded()
            if (r1 != 0) goto L_0x00da
            kotlinx.coroutines.CancellableContinuationImpl r1 = new kotlinx.coroutines.CancellableContinuationImpl
            kotlin.coroutines.Continuation r7 = androidx.preference.R$color.intercepted(r7)
            r1.<init>(r7, r2)
            r1.initCancellability()
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger r7 = r6.logger
            java.util.Objects.requireNonNull(r7)
            com.android.systemui.log.LogBuffer r7 = r7.logBuffer
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStatic$2 r4 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger$logStatic$2
            java.lang.String r5 = "Shade collapsed, waiting for expansion"
            r4.<init>(r5)
            java.util.Objects.requireNonNull(r7)
            boolean r5 = r7.frozen
            if (r5 != 0) goto L_0x00b4
            com.android.systemui.log.LogMessageImpl r0 = r7.obtain(r3, r0, r4)
            r7.push(r0)
        L_0x00b4:
            java.util.concurrent.atomic.AtomicBoolean r7 = new java.util.concurrent.atomic.AtomicBoolean
            r7.<init>(r2)
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$3$callback$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$3$callback$1
            r0.<init>()
            com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$3$1 r7 = new com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$expandShade$3$1
            r7.<init>(r6, r0)
            r1.invokeOnCancellation(r7)
            com.android.systemui.statusbar.SysuiStatusBarStateController r7 = r6.statusBarStateController
            r7.addCallback(r0)
            com.android.systemui.statusbar.phone.StatusBar r6 = r6.statusBar
            java.util.Objects.requireNonNull(r6)
            com.android.systemui.statusbar.phone.StatusBarCommandQueueCallbacks r6 = r6.mCommandQueueCallbacks
            r6.animateExpandNotificationsPanel()
            java.lang.Object r6 = r1.getResult()
            goto L_0x00de
        L_0x00da:
            java.lang.Boolean r6 = java.lang.Boolean.valueOf(r2)
        L_0x00de:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget.access$expandShade(com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
