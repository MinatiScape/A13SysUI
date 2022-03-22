package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.os.RemoteException;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationClickNotifier$onNotificationActionClick$1;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
final class SmartActionInflaterImpl$onSmartActionClick$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Notification.Action $action;
    public final /* synthetic */ int $actionIndex;
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ SmartReplyView.SmartActions $smartActions;
    public final /* synthetic */ SmartActionInflaterImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SmartActionInflaterImpl$onSmartActionClick$1(SmartActionInflaterImpl smartActionInflaterImpl, NotificationEntry notificationEntry, int i, Notification.Action action, SmartReplyView.SmartActions smartActions) {
        super(0);
        this.this$0 = smartActionInflaterImpl;
        this.$entry = notificationEntry;
        this.$actionIndex = i;
        this.$action = action;
        this.$smartActions = smartActions;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        SmartReplyController smartReplyController = this.this$0.smartReplyController;
        NotificationEntry notificationEntry = this.$entry;
        int i = this.$actionIndex;
        Notification.Action action = this.$action;
        boolean z = this.$smartActions.fromAssistant;
        Objects.requireNonNull(smartReplyController);
        NotificationVisibility obtain = smartReplyController.mVisibilityProvider.obtain(notificationEntry, true);
        NotificationClickNotifier notificationClickNotifier = smartReplyController.mClickNotifier;
        Objects.requireNonNull(notificationEntry);
        String str = notificationEntry.mKey;
        Objects.requireNonNull(notificationClickNotifier);
        try {
            notificationClickNotifier.barService.onNotificationActionClick(str, i, action, obtain, z);
        } catch (RemoteException unused) {
        }
        notificationClickNotifier.mainExecutor.execute(new NotificationClickNotifier$onNotificationActionClick$1(notificationClickNotifier, str));
        return Unit.INSTANCE;
    }
}
