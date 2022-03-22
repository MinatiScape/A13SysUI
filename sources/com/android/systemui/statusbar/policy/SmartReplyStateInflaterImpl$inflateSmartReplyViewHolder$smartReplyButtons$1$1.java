package com.android.systemui.statusbar.policy;

import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1 extends Lambda implements Function2<Integer, CharSequence, Button> {
    public final /* synthetic */ boolean $delayOnClickListener;
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ SmartReplyView.SmartReplies $smartReplies;
    public final /* synthetic */ SmartReplyView $smartReplyView;
    public final /* synthetic */ SmartReplyStateInflaterImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1(SmartReplyStateInflaterImpl smartReplyStateInflaterImpl, SmartReplyView smartReplyView, NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, boolean z) {
        super(2);
        this.this$0 = smartReplyStateInflaterImpl;
        this.$smartReplyView = smartReplyView;
        this.$entry = notificationEntry;
        this.$smartReplies = smartReplies;
        this.$delayOnClickListener = z;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Button invoke(Integer num, CharSequence charSequence) {
        return this.this$0.smartRepliesInflater.inflateReplyButton(this.$smartReplyView, this.$entry, this.$smartReplies, num.intValue(), charSequence, this.$delayOnClickListener);
    }
}
