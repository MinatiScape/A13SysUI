package com.google.android.systemui.statusbar.notification.voicereplies;

import androidx.mediarouter.R$dimen;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.SafeFlow;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerKt {
    /* JADX WARN: Removed duplicated region for block: B:10:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void access$sendHunStateChanges(com.android.systemui.statusbar.policy.HeadsUpManager r5, final kotlinx.coroutines.channels.Channel r6, kotlin.coroutines.Continuation r7) {
        /*
            boolean r0 = r7 instanceof com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$sendHunStateChanges$1
            if (r0 == 0) goto L_0x0013
            r0 = r7
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$sendHunStateChanges$1 r0 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$sendHunStateChanges$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0013
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x0018
        L_0x0013:
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$sendHunStateChanges$1 r0 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$sendHunStateChanges$1
            r0.<init>(r7)
        L_0x0018:
            java.lang.Object r7 = r0.result
            kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L_0x003b
            if (r2 == r3) goto L_0x002b
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L_0x002b:
            java.lang.Object r5 = r0.L$1
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$sendHunStateChanges$listener$1 r5 = (com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$sendHunStateChanges$listener$1) r5
            java.lang.Object r6 = r0.L$0
            com.android.systemui.statusbar.policy.HeadsUpManager r6 = (com.android.systemui.statusbar.policy.HeadsUpManager) r6
            kotlin.ResultKt.throwOnFailure(r7)     // Catch: all -> 0x0039
            r7 = r5
            r5 = r6
            goto L_0x005f
        L_0x0039:
            r7 = move-exception
            goto L_0x006a
        L_0x003b:
            kotlin.ResultKt.throwOnFailure(r7)
            com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$sendHunStateChanges$listener$1 r7 = new com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt$sendHunStateChanges$listener$1
            r7.<init>()
            r5.addListener(r7)
            r0.L$0 = r5     // Catch: all -> 0x0065
            r0.L$1 = r7     // Catch: all -> 0x0065
            r0.label = r3     // Catch: all -> 0x0065
            kotlinx.coroutines.CancellableContinuationImpl r6 = new kotlinx.coroutines.CancellableContinuationImpl     // Catch: all -> 0x0065
            kotlin.coroutines.Continuation r0 = androidx.preference.R$color.intercepted(r0)     // Catch: all -> 0x0065
            r6.<init>(r0, r3)     // Catch: all -> 0x0065
            r6.initCancellability()     // Catch: all -> 0x0065
            java.lang.Object r6 = r6.getResult()     // Catch: all -> 0x0065
            if (r6 != r1) goto L_0x005f
            return
        L_0x005f:
            kotlin.KotlinNothingValueException r6 = new kotlin.KotlinNothingValueException     // Catch: all -> 0x0065
            r6.<init>()     // Catch: all -> 0x0065
            throw r6     // Catch: all -> 0x0065
        L_0x0065:
            r6 = move-exception
            r4 = r6
            r6 = r5
            r5 = r7
            r7 = r4
        L_0x006a:
            java.util.Objects.requireNonNull(r6)
            com.android.systemui.util.ListenerSet<com.android.systemui.statusbar.policy.OnHeadsUpChangedListener> r6 = r6.mListeners
            r6.remove(r5)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManagerKt.access$sendHunStateChanges(com.android.systemui.statusbar.policy.HeadsUpManager, kotlinx.coroutines.channels.Channel, kotlin.coroutines.Continuation):void");
    }

    public static final <T> Object collectLatest(Flow<? extends T> flow, Function2<? super T, ? super Continuation<? super Unit>, ? extends Object> function2, Continuation<? super Unit> continuation) {
        Object coroutineScope = R$dimen.coroutineScope(new NotificationVoiceReplyManagerKt$collectLatest$2(flow, function2, null), continuation);
        if (coroutineScope == CoroutineSingletons.COROUTINE_SUSPENDED) {
            return coroutineScope;
        }
        return Unit.INSTANCE;
    }

    public static final SafeFlow distinctUntilChanged(Flow flow, Function2 function2) {
        return new SafeFlow(new NotificationVoiceReplyManagerKt$distinctUntilChanged$2(flow, function2, null));
    }
}
