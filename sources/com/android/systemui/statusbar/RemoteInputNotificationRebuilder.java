package com.android.systemui.statusbar;

import android.app.Notification;
import android.app.RemoteInputHistoryItem;
import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.service.notification.StatusBarNotification;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.people.PeopleTileViewHelper$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public final class RemoteInputNotificationRebuilder {
    public final Context mContext;

    @VisibleForTesting
    public StatusBarNotification rebuildWithRemoteInputInserted(NotificationEntry notificationEntry, CharSequence charSequence, boolean z, String str, Uri uri) {
        RemoteInputHistoryItem remoteInputHistoryItem;
        Objects.requireNonNull(notificationEntry);
        StatusBarNotification statusBarNotification = notificationEntry.mSbn;
        Notification.Builder recoverBuilder = Notification.Builder.recoverBuilder(this.mContext, statusBarNotification.getNotification().clone());
        if (!(charSequence == null && uri == null)) {
            if (uri != null) {
                remoteInputHistoryItem = new RemoteInputHistoryItem(str, uri, charSequence);
            } else {
                remoteInputHistoryItem = new RemoteInputHistoryItem(charSequence);
            }
            Parcelable[] parcelableArray = statusBarNotification.getNotification().extras.getParcelableArray("android.remoteInputHistoryItems");
            recoverBuilder.setRemoteInputHistory(parcelableArray != null ? (RemoteInputHistoryItem[]) Stream.concat(Stream.of(remoteInputHistoryItem), Arrays.stream(parcelableArray).map(PeopleTileViewHelper$$ExternalSyntheticLambda2.INSTANCE$1)).toArray(RemoteInputNotificationRebuilder$$ExternalSyntheticLambda0.INSTANCE) : new RemoteInputHistoryItem[]{remoteInputHistoryItem});
        }
        recoverBuilder.setShowRemoteInputSpinner(z);
        recoverBuilder.setHideSmartReplies(true);
        Notification build = recoverBuilder.build();
        build.contentView = statusBarNotification.getNotification().contentView;
        build.bigContentView = statusBarNotification.getNotification().bigContentView;
        build.headsUpContentView = statusBarNotification.getNotification().headsUpContentView;
        return new StatusBarNotification(statusBarNotification.getPackageName(), statusBarNotification.getOpPkg(), statusBarNotification.getId(), statusBarNotification.getTag(), statusBarNotification.getUid(), statusBarNotification.getInitialPid(), build, statusBarNotification.getUser(), statusBarNotification.getOverrideGroupKey(), statusBarNotification.getPostTime());
    }

    public RemoteInputNotificationRebuilder(Context context) {
        this.mContext = context;
    }
}
