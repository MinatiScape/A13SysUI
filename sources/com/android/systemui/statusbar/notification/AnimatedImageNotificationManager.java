package com.android.systemui.statusbar.notification;

import android.graphics.drawable.AnimatedImageDrawable;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.Arrays;
import java.util.Objects;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.sequences.EmptySequence;
import kotlin.sequences.FilteringSequence$iterator$1;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class AnimatedImageNotificationManager {
    public final BindEventManager bindEventManager;
    public final HeadsUpManager headsUpManager;
    public boolean isStatusBarExpanded;
    public final CommonNotifCollection notifCollection;
    public final StatusBarStateController statusBarStateController;

    public AnimatedImageNotificationManager(CommonNotifCollection commonNotifCollection, BindEventManager bindEventManager, HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController) {
        this.notifCollection = commonNotifCollection;
        this.bindEventManager = bindEventManager;
        this.headsUpManager = headsUpManager;
        this.statusBarStateController = statusBarStateController;
    }

    public final Unit updateAnimatedImageDrawables(NotificationEntry notificationEntry) {
        boolean z;
        Objects.requireNonNull(notificationEntry);
        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
        Sequence sequence = null;
        if (expandableNotificationRow == null) {
            return null;
        }
        if (expandableNotificationRow.mIsHeadsUp || this.isStatusBarExpanded) {
            z = true;
        } else {
            z = false;
        }
        NotificationContentView[] notificationContentViewArr = expandableNotificationRow.mLayouts;
        NotificationContentView[] notificationContentViewArr2 = (NotificationContentView[]) Arrays.copyOf(notificationContentViewArr, notificationContentViewArr.length);
        if (notificationContentViewArr2 != null) {
            sequence = ArraysKt___ArraysKt.asSequence(notificationContentViewArr2);
        }
        if (sequence == null) {
            sequence = EmptySequence.INSTANCE;
        }
        FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.mapNotNull(SequencesKt___SequencesKt.flatMap(SequencesKt___SequencesKt.flatMap(SequencesKt___SequencesKt.flatMap(sequence, AnimatedImageNotificationManager$updateAnimatedImageDrawables$2.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$3.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$4.INSTANCE), AnimatedImageNotificationManager$updateAnimatedImageDrawables$5.INSTANCE));
        while (filteringSequence$iterator$1.hasNext()) {
            AnimatedImageDrawable animatedImageDrawable = (AnimatedImageDrawable) filteringSequence$iterator$1.next();
            if (z) {
                animatedImageDrawable.start();
            } else {
                animatedImageDrawable.stop();
            }
        }
        return Unit.INSTANCE;
    }
}
