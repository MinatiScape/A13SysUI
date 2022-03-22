package com.android.systemui.statusbar.policy;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.os.RemoteException;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
final class SmartReplyInflaterImpl$onSmartReplyClick$1 extends Lambda implements Function0<Boolean> {
    public final /* synthetic */ Button $button;
    public final /* synthetic */ CharSequence $choice;
    public final /* synthetic */ NotificationEntry $entry;
    public final /* synthetic */ int $replyIndex;
    public final /* synthetic */ SmartReplyView.SmartReplies $smartReplies;
    public final /* synthetic */ SmartReplyView $smartReplyView;
    public final /* synthetic */ SmartReplyInflaterImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SmartReplyInflaterImpl$onSmartReplyClick$1(SmartReplyInflaterImpl smartReplyInflaterImpl, SmartReplyView.SmartReplies smartReplies, Button button, CharSequence charSequence, int i, NotificationEntry notificationEntry, SmartReplyView smartReplyView) {
        super(0);
        this.this$0 = smartReplyInflaterImpl;
        this.$smartReplies = smartReplies;
        this.$button = button;
        this.$choice = charSequence;
        this.$replyIndex = i;
        this.$entry = notificationEntry;
        this.$smartReplyView = smartReplyView;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Boolean invoke() {
        boolean z;
        SmartReplyConstants smartReplyConstants = this.this$0.constants;
        int editChoicesBeforeSending = this.$smartReplies.remoteInput.getEditChoicesBeforeSending();
        Objects.requireNonNull(smartReplyConstants);
        if (editChoicesBeforeSending == 1) {
            z = false;
        } else if (editChoicesBeforeSending != 2) {
            z = smartReplyConstants.mEditChoicesBeforeSending;
        } else {
            z = true;
        }
        if (z) {
            NotificationRemoteInputManager notificationRemoteInputManager = this.this$0.remoteInputManager;
            Button button = this.$button;
            SmartReplyView.SmartReplies smartReplies = this.$smartReplies;
            RemoteInput remoteInput = smartReplies.remoteInput;
            PendingIntent pendingIntent = smartReplies.pendingIntent;
            NotificationEntry.EditedSuggestionInfo editedSuggestionInfo = new NotificationEntry.EditedSuggestionInfo(this.$choice, this.$replyIndex);
            Objects.requireNonNull(notificationRemoteInputManager);
            notificationRemoteInputManager.activateRemoteInput(button, new RemoteInput[]{remoteInput}, remoteInput, pendingIntent, editedSuggestionInfo, null, null);
        } else {
            SmartReplyController smartReplyController = this.this$0.smartReplyController;
            NotificationEntry notificationEntry = this.$entry;
            int i = this.$replyIndex;
            CharSequence text = this.$button.getText();
            int metricsEventEnum = NotificationLogger.getNotificationLocation(this.$entry).toMetricsEventEnum();
            Objects.requireNonNull(smartReplyController);
            smartReplyController.mCallback.onSmartReplySent(notificationEntry, text);
            ArraySet arraySet = smartReplyController.mSendingKeys;
            Objects.requireNonNull(notificationEntry);
            arraySet.add(notificationEntry.mKey);
            try {
                smartReplyController.mBarService.onNotificationSmartReplySent(notificationEntry.mSbn.getKey(), i, text, metricsEventEnum, false);
            } catch (RemoteException unused) {
            }
            NotificationEntry notificationEntry2 = this.$entry;
            Objects.requireNonNull(notificationEntry2);
            notificationEntry2.hasSentReply = true;
            try {
                this.$smartReplies.pendingIntent.send(this.this$0.context, 0, SmartReplyInflaterImpl.access$createRemoteInputIntent(this.this$0, this.$smartReplies, this.$choice));
            } catch (PendingIntent.CanceledException e) {
                Log.w("SmartReplyViewInflater", "Unable to send smart reply", e);
            }
            SmartReplyView smartReplyView = this.$smartReplyView;
            Objects.requireNonNull(smartReplyView);
            View view = smartReplyView.mSmartReplyContainer;
            if (view != null) {
                view.setVisibility(8);
            }
        }
        return Boolean.FALSE;
    }
}
