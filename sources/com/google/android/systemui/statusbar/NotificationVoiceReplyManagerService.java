package com.google.android.systemui.statusbar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.android.systemui.R$anim;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyLogger;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import java.util.Objects;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.SupervisorJobImpl;
import kotlinx.coroutines.channels.AbstractChannel;
import kotlinx.coroutines.internal.ContextScope;
import kotlinx.coroutines.internal.MainDispatcherLoader;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerService extends Service {
    public final NotificationVoiceReplyLogger logger;
    public final NotificationVoiceReplyManager.Initializer managerInitializer;
    public final ContextScope scope;
    public NotificationVoiceReplyManager voiceReplyManager;
    public final AbstractChannel serializer = R$anim.Channel$default(0, 7);
    public final NotificationVoiceReplyManagerService$binder$1 binder = new NotificationVoiceReplyManagerService$binder$1(this);

    @Override // android.app.Service
    public final int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    @Override // android.app.Service
    public final void onDestroy() {
        ContextScope contextScope = this.scope;
        Objects.requireNonNull(contextScope);
        Job job = (Job) contextScope.coroutineContext.get(Job.Key.$$INSTANCE);
        if (job != null) {
            job.cancel(null);
            super.onDestroy();
            return;
        }
        throw new IllegalStateException(Intrinsics.stringPlus("Scope cannot be cancelled because it does not have a job: ", contextScope).toString());
    }

    public NotificationVoiceReplyManagerService(NotificationVoiceReplyManager.Initializer initializer, NotificationVoiceReplyLogger notificationVoiceReplyLogger) {
        this.managerInitializer = initializer;
        this.logger = notificationVoiceReplyLogger;
        SupervisorJobImpl supervisorJobImpl = new SupervisorJobImpl(null);
        ExecutorCoroutineDispatcher executorCoroutineDispatcher = Dispatchers.Default;
        this.scope = new ContextScope(CoroutineContext.DefaultImpls.plus(supervisorJobImpl, MainDispatcherLoader.dispatcher));
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0042 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final void access$ensureCallerIsAgsa(com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService r12) {
        /*
            java.util.Objects.requireNonNull(r12)
            android.content.pm.PackageManager r0 = r12.getPackageManager()
            int r1 = android.os.Binder.getCallingUid()
            java.lang.String[] r0 = r0.getPackagesForUid(r1)
            r1 = 0
            if (r0 != 0) goto L_0x0013
            goto L_0x0043
        L_0x0013:
            int r2 = r0.length
            r3 = 0
            r4 = r3
        L_0x0016:
            if (r4 >= r2) goto L_0x0043
            r5 = r0[r4]
            int r4 = r4 + 1
            java.lang.String r6 = "com.google.android.googlequicksearchbox"
            boolean r6 = kotlin.jvm.internal.Intrinsics.areEqual(r5, r6)
            r7 = 1
            if (r6 == 0) goto L_0x003f
            byte[][] r6 = com.google.android.systemui.statusbar.NotificationVoiceReplyManagerServiceKt.AGSA_CERTS
            int r8 = r6.length
            r9 = r3
        L_0x0029:
            if (r9 >= r8) goto L_0x003b
            r10 = r6[r9]
            int r9 = r9 + 1
            android.content.pm.PackageManager r11 = r12.getPackageManager()
            boolean r10 = r11.hasSigningCertificate(r5, r10, r7)
            if (r10 == 0) goto L_0x0029
            r6 = r7
            goto L_0x003c
        L_0x003b:
            r6 = r3
        L_0x003c:
            if (r6 == 0) goto L_0x003f
            goto L_0x0040
        L_0x003f:
            r7 = r3
        L_0x0040:
            if (r7 == 0) goto L_0x0016
            r1 = r5
        L_0x0043:
            if (r1 == 0) goto L_0x0046
            return
        L_0x0046:
            java.lang.SecurityException r12 = new java.lang.SecurityException
            java.lang.String r0 = "Caller is not allowlisted"
            r12.<init>(r0)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService.access$ensureCallerIsAgsa(com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0042  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005a  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:27:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0073 -> B:13:0x0030). Please submit an issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final java.lang.Object access$serializeIncomingIPCs(com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService r6, kotlin.coroutines.Continuation r7) {
        /*
            java.util.Objects.requireNonNull(r6)
            boolean r0 = r7 instanceof com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$serializeIncomingIPCs$1
            if (r0 == 0) goto L_0x0016
            r0 = r7
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$serializeIncomingIPCs$1 r0 = (com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$serializeIncomingIPCs$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L_0x0016
            int r1 = r1 - r2
            r0.label = r1
            goto L_0x001b
        L_0x0016:
            com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$serializeIncomingIPCs$1 r0 = new com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService$serializeIncomingIPCs$1
            r0.<init>(r6, r7)
        L_0x001b:
            java.lang.Object r7 = r0.result
            kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED
            int r2 = r0.label
            r3 = 2
            r4 = 1
            if (r2 == 0) goto L_0x0042
            if (r2 == r4) goto L_0x003a
            if (r2 != r3) goto L_0x0032
            java.lang.Object r6 = r0.L$0
            kotlinx.coroutines.channels.ChannelIterator r6 = (kotlinx.coroutines.channels.ChannelIterator) r6
            kotlin.ResultKt.throwOnFailure(r7)
        L_0x0030:
            r7 = r6
            goto L_0x004f
        L_0x0032:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "call to 'resume' before 'invoke' with coroutine"
            r6.<init>(r7)
            throw r6
        L_0x003a:
            java.lang.Object r6 = r0.L$0
            kotlinx.coroutines.channels.ChannelIterator r6 = (kotlinx.coroutines.channels.ChannelIterator) r6
            kotlin.ResultKt.throwOnFailure(r7)
            goto L_0x005d
        L_0x0042:
            kotlin.ResultKt.throwOnFailure(r7)
            kotlinx.coroutines.channels.AbstractChannel r6 = r6.serializer
            java.util.Objects.requireNonNull(r6)
            kotlinx.coroutines.channels.AbstractChannel$Itr r7 = new kotlinx.coroutines.channels.AbstractChannel$Itr
            r7.<init>(r6)
        L_0x004f:
            r0.L$0 = r7
            r0.label = r4
            java.lang.Object r6 = r7.hasNext(r0)
            if (r6 != r1) goto L_0x005a
            goto L_0x0078
        L_0x005a:
            r5 = r7
            r7 = r6
            r6 = r5
        L_0x005d:
            java.lang.Boolean r7 = (java.lang.Boolean) r7
            boolean r7 = r7.booleanValue()
            if (r7 == 0) goto L_0x0076
            java.lang.Object r7 = r6.next()
            kotlin.jvm.functions.Function1 r7 = (kotlin.jvm.functions.Function1) r7
            r0.L$0 = r6
            r0.label = r3
            java.lang.Object r7 = r7.invoke(r0)
            if (r7 != r1) goto L_0x0030
            goto L_0x0078
        L_0x0076:
            kotlin.Unit r1 = kotlin.Unit.INSTANCE
        L_0x0078:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService.access$serializeIncomingIPCs(com.google.android.systemui.statusbar.NotificationVoiceReplyManagerService, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public static final void access$serially(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService, Function1 function1) {
        Objects.requireNonNull(notificationVoiceReplyManagerService);
        BuildersKt.runBlocking$default(new NotificationVoiceReplyManagerService$serially$1(notificationVoiceReplyManagerService, function1, null));
    }

    @Override // android.app.Service
    public final void onCreate() {
        super.onCreate();
        this.voiceReplyManager = this.managerInitializer.connect(this.scope);
        BuildersKt.launch$default(this.scope, null, null, new NotificationVoiceReplyManagerService$onCreate$1(this, null), 3);
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return this.binder;
    }
}
