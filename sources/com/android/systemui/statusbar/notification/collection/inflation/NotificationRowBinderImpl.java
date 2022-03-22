package com.android.systemui.statusbar.notification.collection.inflation;

import android.app.Notification;
import android.content.Context;
import android.widget.ImageView;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.util.NotificationMessagingUtil;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.InflationException;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.NotificationClicker;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.collection.legacy.LowPriorityInflationHelper;
import com.android.systemui.statusbar.notification.icon.IconManager;
import com.android.systemui.statusbar.notification.icon.IconPack;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.android.systemui.statusbar.notification.row.RowContentBindParams;
import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import com.android.systemui.statusbar.notification.row.RowInflaterTask;
import com.android.systemui.statusbar.notification.row.dagger.ExpandableNotificationRowComponent;
import com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.Objects;
import javax.inject.Provider;
import kotlin.Pair;
/* loaded from: classes.dex */
public final class NotificationRowBinderImpl implements NotificationRowBinder {
    public BindRowCallback mBindRowCallback;
    public final Context mContext;
    public final ExpandableNotificationRowComponent.Builder mExpandableNotificationRowComponentBuilder;
    public final IconManager mIconManager;
    public NotificationListContainer mListContainer;
    public final LowPriorityInflationHelper mLowPriorityInflationHelper;
    public final NotificationMessagingUtil mMessagingUtil;
    public final NotifBindPipeline mNotifBindPipeline;
    public final NotifPipelineFlags mNotifPipelineFlags;
    public NotificationClicker mNotificationClicker;
    public final NotificationLockscreenUserManager mNotificationLockscreenUserManager;
    public final NotificationRemoteInputManager mNotificationRemoteInputManager;
    public NotificationPresenter mPresenter;
    public final RowContentBindStage mRowContentBindStage;
    public final Provider<RowInflaterTask> mRowInflaterTaskProvider;

    /* loaded from: classes.dex */
    public interface BindRowCallback {
    }

    public final void inflateContentViews(NotificationEntry notificationEntry, NotifInflater.Params params, final ExpandableNotificationRow expandableNotificationRow, final NotificationRowContentBinder.InflationCallback inflationCallback) {
        final boolean z;
        NotificationMessagingUtil notificationMessagingUtil = this.mMessagingUtil;
        Objects.requireNonNull(notificationEntry);
        final boolean isImportantMessaging = notificationMessagingUtil.isImportantMessaging(notificationEntry.mSbn, notificationEntry.getImportance());
        if (params != null) {
            z = params.isLowPriority;
        } else {
            this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
            LowPriorityInflationHelper lowPriorityInflationHelper = this.mLowPriorityInflationHelper;
            Objects.requireNonNull(lowPriorityInflationHelper);
            lowPriorityInflationHelper.mNotifPipelineFlags.checkLegacyPipelineEnabled();
            if (!notificationEntry.isAmbient() || lowPriorityInflationHelper.mGroupManager.isChildInGroup(notificationEntry)) {
                z = false;
            } else {
                z = true;
            }
        }
        RowContentBindParams stageParams = this.mRowContentBindStage.getStageParams(notificationEntry);
        if (stageParams.mUseIncreasedHeight != isImportantMessaging) {
            stageParams.mDirtyContentViews = 1 | stageParams.mDirtyContentViews;
        }
        stageParams.mUseIncreasedHeight = isImportantMessaging;
        if (stageParams.mUseLowPriority != z) {
            stageParams.mDirtyContentViews |= 3;
        }
        stageParams.mUseLowPriority = z;
        expandableNotificationRow.setNeedsRedaction(this.mNotificationLockscreenUserManager.needsRedaction(notificationEntry));
        stageParams.mDirtyContentViews = stageParams.mContentViews;
        this.mRowContentBindStage.requestRebind(notificationEntry, new NotifBindPipeline.BindCallback() { // from class: com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.notification.row.NotifBindPipeline.BindCallback
            public final void onBindFinished(NotificationEntry notificationEntry2) {
                ExpandableNotificationRow expandableNotificationRow2 = ExpandableNotificationRow.this;
                boolean z2 = isImportantMessaging;
                boolean z3 = z;
                NotificationRowContentBinder.InflationCallback inflationCallback2 = inflationCallback;
                Objects.requireNonNull(expandableNotificationRow2);
                expandableNotificationRow2.mUseIncreasedCollapsedHeight = z2;
                expandableNotificationRow2.mIsLowPriority = z3;
                Objects.requireNonNull(expandableNotificationRow2.mPrivateLayout);
                NotificationChildrenContainer notificationChildrenContainer = expandableNotificationRow2.mChildrenContainer;
                if (notificationChildrenContainer != null) {
                    notificationChildrenContainer.mIsLowPriority = z3;
                    if (notificationChildrenContainer.mContainingNotification != null) {
                        notificationChildrenContainer.recreateLowPriorityHeader(null);
                        notificationChildrenContainer.updateHeaderVisibility(false);
                    }
                    boolean z4 = notificationChildrenContainer.mUserLocked;
                    if (z4) {
                        notificationChildrenContainer.setUserLocked(z4);
                    }
                }
                if (inflationCallback2 != null) {
                    inflationCallback2.onAsyncInflationFinished(notificationEntry2);
                }
            }
        });
    }

    @Override // com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder
    public final void inflateViews(NotificationEntry notificationEntry, NotifInflater.Params params, NotificationRowContentBinder.InflationCallback inflationCallback) throws InflationException {
        StatusBarIconView statusBarIconView;
        StatusBarIcon statusBarIcon;
        if (params == null) {
            this.mNotifPipelineFlags.checkLegacyPipelineEnabled();
        }
        NotificationStackScrollLayout viewParentForNotification = this.mListContainer.getViewParentForNotification();
        if (notificationEntry.rowExists()) {
            this.mIconManager.updateIcons(notificationEntry);
            ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
            expandableNotificationRow.reset();
            updateRow(notificationEntry, expandableNotificationRow);
            inflateContentViews(notificationEntry, params, expandableNotificationRow, inflationCallback);
            return;
        }
        IconManager iconManager = this.mIconManager;
        Objects.requireNonNull(iconManager);
        StatusBarIconView createIconView = iconManager.iconBuilder.createIconView(notificationEntry);
        createIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        StatusBarIconView createIconView2 = iconManager.iconBuilder.createIconView(notificationEntry);
        createIconView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        createIconView2.setVisibility(4);
        StatusBarIconView createIconView3 = iconManager.iconBuilder.createIconView(notificationEntry);
        createIconView3.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        createIconView3.mIncreasedSize = true;
        createIconView3.maybeUpdateIconScaleDimens();
        if (notificationEntry.mSbn.getNotification().isMediaNotification()) {
            statusBarIconView = iconManager.iconBuilder.createIconView(notificationEntry);
            statusBarIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        } else {
            statusBarIconView = null;
        }
        StatusBarIcon iconDescriptor = iconManager.getIconDescriptor(notificationEntry, false);
        if (notificationEntry.mSensitive) {
            statusBarIcon = iconManager.getIconDescriptor(notificationEntry, true);
        } else {
            statusBarIcon = iconDescriptor;
        }
        Pair pair = new Pair(iconDescriptor, statusBarIcon);
        StatusBarIcon statusBarIcon2 = (StatusBarIcon) pair.component1();
        StatusBarIcon statusBarIcon3 = (StatusBarIcon) pair.component2();
        try {
            IconManager.setIcon(notificationEntry, statusBarIcon2, createIconView);
            IconManager.setIcon(notificationEntry, statusBarIcon3, createIconView2);
            IconManager.setIcon(notificationEntry, statusBarIcon3, createIconView3);
            if (statusBarIconView != null) {
                IconManager.setIcon(notificationEntry, statusBarIcon2, statusBarIconView);
            }
            notificationEntry.mIcons = new IconPack(true, createIconView, createIconView2, createIconView3, statusBarIconView, notificationEntry.mIcons);
            RowInflaterTask rowInflaterTask = this.mRowInflaterTaskProvider.mo144get();
            Context context = this.mContext;
            NotificationRowBinderImpl$$ExternalSyntheticLambda1 notificationRowBinderImpl$$ExternalSyntheticLambda1 = new NotificationRowBinderImpl$$ExternalSyntheticLambda1(this, notificationEntry, params, inflationCallback);
            Objects.requireNonNull(rowInflaterTask);
            rowInflaterTask.mInflateOrigin = new Throwable("inflate requested here");
            rowInflaterTask.mListener = notificationRowBinderImpl$$ExternalSyntheticLambda1;
            AsyncLayoutInflater asyncLayoutInflater = new AsyncLayoutInflater(context);
            rowInflaterTask.mEntry = notificationEntry;
            notificationEntry.abortTask();
            notificationEntry.mRunningTask = rowInflaterTask;
            asyncLayoutInflater.inflate(2131624525, viewParentForNotification, rowInflaterTask);
        } catch (InflationException e) {
            notificationEntry.mIcons = new IconPack(false, null, null, null, null, notificationEntry.mIcons);
            throw e;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0023, code lost:
        if (r7.smartReplies.equals(r6.smartReplies) == false) goto L_0x0027;
     */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0029  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x003c  */
    @Override // com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinder
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onNotificationRankingUpdated(com.android.systemui.statusbar.notification.collection.NotificationEntry r4, java.lang.Integer r5, com.android.systemui.statusbar.NotificationUiAdjustment r6, com.android.systemui.statusbar.NotificationUiAdjustment r7, com.android.systemui.statusbar.notification.NotificationEntryManager.AnonymousClass1 r8) {
        /*
            r3 = this;
            com.android.systemui.statusbar.notification.NotifPipelineFlags r0 = r3.mNotifPipelineFlags
            r0.checkLegacyPipelineEnabled()
            if (r6 != r7) goto L_0x0008
            goto L_0x0026
        L_0x0008:
            boolean r0 = r6.isConversation
            boolean r1 = r7.isConversation
            r2 = 1
            if (r0 == r1) goto L_0x0010
            goto L_0x0027
        L_0x0010:
            java.util.List<android.app.Notification$Action> r0 = r6.smartActions
            java.util.List<android.app.Notification$Action> r1 = r7.smartActions
            boolean r0 = com.android.systemui.statusbar.NotificationUiAdjustment.areDifferent(r0, r1)
            if (r0 == 0) goto L_0x001b
            goto L_0x0027
        L_0x001b:
            java.util.List<java.lang.CharSequence> r7 = r7.smartReplies
            java.util.List<java.lang.CharSequence> r6 = r6.smartReplies
            boolean r6 = r7.equals(r6)
            if (r6 != 0) goto L_0x0026
            goto L_0x0027
        L_0x0026:
            r2 = 0
        L_0x0027:
            if (r2 == 0) goto L_0x003c
            boolean r5 = r4.rowExists()
            if (r5 == 0) goto L_0x0061
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r5 = r4.row
            r5.reset()
            r3.updateRow(r4, r5)
            r6 = 0
            r3.inflateContentViews(r4, r6, r5, r8)
            goto L_0x0061
        L_0x003c:
            if (r5 == 0) goto L_0x0061
            int r3 = r4.getImportance()
            int r5 = r5.intValue()
            if (r3 == r5) goto L_0x0061
            boolean r3 = r4.rowExists()
            if (r3 == 0) goto L_0x0061
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r3 = r4.row
            java.util.Objects.requireNonNull(r3)
            com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin r4 = r3.mMenuRow
            if (r4 == 0) goto L_0x0061
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r3.mEntry
            java.util.Objects.requireNonNull(r3)
            android.service.notification.StatusBarNotification r3 = r3.mSbn
            r4.onNotificationUpdated(r3)
        L_0x0061:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.inflation.NotificationRowBinderImpl.onNotificationRankingUpdated(com.android.systemui.statusbar.notification.collection.NotificationEntry, java.lang.Integer, com.android.systemui.statusbar.NotificationUiAdjustment, com.android.systemui.statusbar.NotificationUiAdjustment, com.android.systemui.statusbar.notification.NotificationEntryManager$1):void");
    }

    public final void updateRow(NotificationEntry notificationEntry, ExpandableNotificationRow expandableNotificationRow) {
        boolean z;
        NotificationContentView[] notificationContentViewArr;
        int i = notificationEntry.targetSdk;
        if (i < 9 || i >= 21) {
            z = false;
        } else {
            z = true;
        }
        Objects.requireNonNull(expandableNotificationRow);
        for (NotificationContentView notificationContentView : expandableNotificationRow.mLayouts) {
            Objects.requireNonNull(notificationContentView);
            notificationContentView.mLegacy = z;
            notificationContentView.updateLegacy();
        }
        NotificationClicker notificationClicker = this.mNotificationClicker;
        Objects.requireNonNull(notificationClicker);
        Notification notification = notificationEntry.mSbn.getNotification();
        if (notification.contentIntent == null && notification.fullScreenIntent == null && !expandableNotificationRow.mEntry.isBubble()) {
            expandableNotificationRow.setOnClickListener(null);
            expandableNotificationRow.mOnDragSuccessListener = null;
            return;
        }
        expandableNotificationRow.setOnClickListener(notificationClicker);
        expandableNotificationRow.mOnDragSuccessListener = notificationClicker.mOnDragSuccessListener;
    }

    public NotificationRowBinderImpl(Context context, NotificationMessagingUtil notificationMessagingUtil, NotificationRemoteInputManager notificationRemoteInputManager, NotificationLockscreenUserManager notificationLockscreenUserManager, NotifBindPipeline notifBindPipeline, RowContentBindStage rowContentBindStage, Provider<RowInflaterTask> provider, ExpandableNotificationRowComponent.Builder builder, IconManager iconManager, LowPriorityInflationHelper lowPriorityInflationHelper, NotifPipelineFlags notifPipelineFlags) {
        this.mContext = context;
        this.mNotifBindPipeline = notifBindPipeline;
        this.mRowContentBindStage = rowContentBindStage;
        this.mMessagingUtil = notificationMessagingUtil;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mNotificationLockscreenUserManager = notificationLockscreenUserManager;
        this.mRowInflaterTaskProvider = provider;
        this.mExpandableNotificationRowComponentBuilder = builder;
        this.mIconManager = iconManager;
        this.mLowPriorityInflationHelper = lowPriorityInflationHelper;
        this.mNotifPipelineFlags = notifPipelineFlags;
    }
}
