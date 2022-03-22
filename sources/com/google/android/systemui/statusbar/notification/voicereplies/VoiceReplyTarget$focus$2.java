package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.policy.RemoteInputViewController;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.MutableSharedFlow;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationVoiceReplyManager.kt */
@DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$focus$2", f = "NotificationVoiceReplyManager.kt", l = {793, 801, 805, 829, 834, 837}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class VoiceReplyTarget$focus$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ AuthStateRef $authState;
    public final /* synthetic */ MutableSharedFlow<Pair<String, RemoteInputViewController>> $remoteInputViewActivatedForVoiceReply;
    public final /* synthetic */ String $userMessageContent;
    private /* synthetic */ Object L$0;
    public int label;
    public final /* synthetic */ VoiceReplyTarget this$0;

    /* compiled from: NotificationVoiceReplyManager.kt */
    @DebugMetadata(c = "com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$focus$2$1", f = "NotificationVoiceReplyManager.kt", l = {}, m = "invokeSuspend")
    /* renamed from: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$focus$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends SuspendLambda implements Function2<Boolean, Continuation<? super Boolean>, Object> {
        public /* synthetic */ boolean Z$0;
        public int label;

        public AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            AnonymousClass1 r0 = new AnonymousClass1(continuation);
            r0.Z$0 = ((Boolean) obj).booleanValue();
            return r0;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(Boolean bool, Continuation<? super Boolean> continuation) {
            return ((AnonymousClass1) create(Boolean.valueOf(bool.booleanValue()), continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            if (this.label == 0) {
                ResultKt.throwOnFailure(obj);
                return Boolean.valueOf(!this.Z$0);
            }
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public VoiceReplyTarget$focus$2(VoiceReplyTarget voiceReplyTarget, String str, MutableSharedFlow<Pair<String, RemoteInputViewController>> mutableSharedFlow, AuthStateRef authStateRef, Continuation<? super VoiceReplyTarget$focus$2> continuation) {
        super(2, continuation);
        this.this$0 = voiceReplyTarget;
        this.$userMessageContent = str;
        this.$remoteInputViewActivatedForVoiceReply = mutableSharedFlow;
        this.$authState = authStateRef;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        VoiceReplyTarget$focus$2 voiceReplyTarget$focus$2 = new VoiceReplyTarget$focus$2(this.this$0, this.$userMessageContent, this.$remoteInputViewActivatedForVoiceReply, this.$authState, continuation);
        voiceReplyTarget$focus$2.L$0 = obj;
        return voiceReplyTarget$focus$2;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((VoiceReplyTarget$focus$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x02cd  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02ce A[Catch: all -> 0x00ae, TRY_LEAVE, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:105:0x02d2 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0082 A[Catch: all -> 0x00ae, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0088 A[Catch: all -> 0x00ae, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00c2 A[Catch: all -> 0x00ae, TRY_ENTER, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x013d A[Catch: all -> 0x00ae, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0151 A[Catch: all -> 0x00ae, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0193 A[Catch: all -> 0x00ae, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x01a6 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01af A[Catch: all -> 0x00ae, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:77:0x01e5 A[Catch: all -> 0x00ae, TRY_ENTER, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0283 A[Catch: all -> 0x00ae, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x029a  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x029b A[Catch: all -> 0x00ae, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x029f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x02b7 A[Catch: all -> 0x00ae, TryCatch #0 {all -> 0x00ae, blocks: (B:6:0x001b, B:8:0x0024, B:10:0x002d, B:12:0x0036, B:15:0x0041, B:17:0x004a, B:19:0x0056, B:21:0x0060, B:29:0x006f, B:32:0x0082, B:34:0x0088, B:36:0x00a6, B:39:0x00b1, B:42:0x00c2, B:44:0x00cc, B:46:0x00e3, B:47:0x00ea, B:51:0x0126, B:53:0x013d, B:55:0x0146, B:57:0x0151, B:59:0x0168, B:60:0x016f, B:63:0x017c, B:65:0x0193, B:66:0x019a, B:69:0x01a7, B:71:0x01af, B:73:0x01cd, B:74:0x01d4, B:77:0x01e5, B:79:0x01fc, B:80:0x0203, B:82:0x0224, B:85:0x0248, B:88:0x026c, B:90:0x0283, B:91:0x028a, B:94:0x029b, B:97:0x02a0, B:99:0x02b7, B:100:0x02be, B:103:0x02ce), top: B:110:0x0010 }] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r20) {
        /*
            Method dump skipped, instructions count: 774
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.statusbar.notification.voicereplies.VoiceReplyTarget$focus$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
