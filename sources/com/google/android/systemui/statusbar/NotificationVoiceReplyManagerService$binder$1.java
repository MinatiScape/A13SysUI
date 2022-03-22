package com.google.android.systemui.statusbar;

import android.os.Binder;
import android.os.UserHandle;
import kotlinx.coroutines.flow.SharedFlowImpl;
import kotlinx.coroutines.flow.SharedFlowKt;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* loaded from: classes.dex */
public final class NotificationVoiceReplyManagerService$binder$1 extends INotificationVoiceReplyService$Stub {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final /* synthetic */ NotificationVoiceReplyManagerService this$0;
    public final SharedFlowImpl setFeatureEnabledFlow = SharedFlowKt.MutableSharedFlow$default(0, 7);
    public final SharedFlowImpl startVoiceReplyFlow = SharedFlowKt.MutableSharedFlow$default(0, 7);
    public final SharedFlowImpl onVoiceAuthStateChangedFlow = SharedFlowKt.MutableSharedFlow$default(0, 7);

    public NotificationVoiceReplyManagerService$binder$1(NotificationVoiceReplyManagerService notificationVoiceReplyManagerService) {
        this.this$0 = notificationVoiceReplyManagerService;
    }

    public static int getUserId() {
        return UserHandle.getUserId(Binder.getCallingUid());
    }
}
