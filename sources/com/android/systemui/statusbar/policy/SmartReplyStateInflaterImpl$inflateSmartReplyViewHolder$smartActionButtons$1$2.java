package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$2 extends Lambda implements Function2<Integer, Notification.Action, Button> {
    public final /* synthetic */ boolean $delayOnClickListener;
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ SmartReplyView.SmartActions $smartActions;
    public final /* synthetic */ SmartReplyView $smartReplyView;
    public final /* synthetic */ ContextThemeWrapper $themedPackageContext;
    public final /* synthetic */ SmartReplyStateInflaterImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$2(SmartReplyStateInflaterImpl smartReplyStateInflaterImpl, SmartReplyView smartReplyView, NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, boolean z, ContextThemeWrapper contextThemeWrapper) {
        super(2);
        this.this$0 = smartReplyStateInflaterImpl;
        this.$smartReplyView = smartReplyView;
        this.$entry = notificationEntry;
        this.$smartActions = smartActions;
        this.$delayOnClickListener = z;
        this.$themedPackageContext = contextThemeWrapper;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Button invoke(Integer num, Notification.Action action) {
        return this.this$0.smartActionsInflater.inflateActionButton(this.$smartReplyView, this.$entry, this.$smartActions, num.intValue(), action, this.$delayOnClickListener, this.$themedPackageContext);
    }
}
