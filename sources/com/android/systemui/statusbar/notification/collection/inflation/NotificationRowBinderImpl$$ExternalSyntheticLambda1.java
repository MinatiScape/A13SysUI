package com.android.systemui.statusbar.notification.collection.inflation;

import android.view.View;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.NotifInflater;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRowDragController;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.notification.row.NotificationRowContentBinder;
import com.android.systemui.statusbar.notification.row.RowInflaterTask;
import com.android.systemui.statusbar.phone.StatusBarNotificationPresenter;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.Objects;
import java.util.function.BooleanSupplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationRowBinderImpl$$ExternalSyntheticLambda1 implements RowInflaterTask.RowInflationFinishedListener {
    public final /* synthetic */ NotificationRowBinderImpl f$0;
    public final /* synthetic */ NotificationEntry f$1;
    public final /* synthetic */ NotifInflater.Params f$2;
    public final /* synthetic */ NotificationRowContentBinder.InflationCallback f$3;

    public /* synthetic */ NotificationRowBinderImpl$$ExternalSyntheticLambda1(NotificationRowBinderImpl notificationRowBinderImpl, NotificationEntry notificationEntry, NotifInflater.Params params, NotificationRowContentBinder.InflationCallback inflationCallback) {
        this.f$0 = notificationRowBinderImpl;
        this.f$1 = notificationEntry;
        this.f$2 = params;
        this.f$3 = inflationCallback;
    }

    public final void onInflationFinished(ExpandableNotificationRow expandableNotificationRow) {
        NotificationRowBinderImpl notificationRowBinderImpl = this.f$0;
        NotificationEntry notificationEntry = this.f$1;
        NotifInflater.Params params = this.f$2;
        NotificationRowContentBinder.InflationCallback inflationCallback = this.f$3;
        Objects.requireNonNull(notificationRowBinderImpl);
        final ExpandableNotificationRowController expandableNotificationRowController = notificationRowBinderImpl.mExpandableNotificationRowComponentBuilder.mo137expandableNotificationRow(expandableNotificationRow).mo139notificationEntry(notificationEntry).onExpandClickListener(notificationRowBinderImpl.mPresenter).mo138listContainer(notificationRowBinderImpl.mListContainer).build().getExpandableNotificationRowController();
        Objects.requireNonNull(expandableNotificationRowController);
        expandableNotificationRowController.mActivatableNotificationViewController.init();
        expandableNotificationRowController.mView.initialize(notificationEntry, expandableNotificationRowController.mRemoteInputViewSubcomponentFactory, expandableNotificationRowController.mAppName, expandableNotificationRowController.mNotificationKey, expandableNotificationRowController.mExpansionLogger, expandableNotificationRowController.mKeyguardBypassController, expandableNotificationRowController.mGroupMembershipManager, expandableNotificationRowController.mGroupExpansionManager, expandableNotificationRowController.mHeadsUpManager, expandableNotificationRowController.mRowContentBindStage, expandableNotificationRowController.mOnExpandClickListener, expandableNotificationRowController.mOnFeedbackClickListener, expandableNotificationRowController.mFalsingManager, expandableNotificationRowController.mFalsingCollector, expandableNotificationRowController.mStatusBarStateController, expandableNotificationRowController.mPeopleNotificationIdentifier, expandableNotificationRowController.mOnUserInteractionCallback, expandableNotificationRowController.mBubblesManagerOptional, expandableNotificationRowController.mNotificationGutsManager);
        expandableNotificationRowController.mView.setDescendantFocusability(393216);
        if (expandableNotificationRowController.mAllowLongPress) {
            if (expandableNotificationRowController.mFeatureFlags.isEnabled(Flags.NOTIFICATION_DRAG_TO_CONTENTS)) {
                ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRowController.mView;
                ExpandableNotificationRowDragController expandableNotificationRowDragController = expandableNotificationRowController.mDragController;
                Objects.requireNonNull(expandableNotificationRow2);
                expandableNotificationRow2.mDragController = expandableNotificationRowDragController;
            }
            ExpandableNotificationRow expandableNotificationRow3 = expandableNotificationRowController.mView;
            ExpandableNotificationRowController$$ExternalSyntheticLambda0 expandableNotificationRowController$$ExternalSyntheticLambda0 = new ExpandableNotificationRowController$$ExternalSyntheticLambda0(expandableNotificationRowController);
            Objects.requireNonNull(expandableNotificationRow3);
            expandableNotificationRow3.mLongPressListener = expandableNotificationRowController$$ExternalSyntheticLambda0;
        }
        if (NotificationRemoteInputManager.ENABLE_REMOTE_INPUT) {
            expandableNotificationRowController.mView.setDescendantFocusability(131072);
        }
        expandableNotificationRowController.mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController.1
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
                ExpandableNotificationRow expandableNotificationRow4 = expandableNotificationRowController.mView;
                Objects.requireNonNull(expandableNotificationRow4);
                NotificationEntry notificationEntry2 = expandableNotificationRow4.mEntry;
                long elapsedRealtime = expandableNotificationRowController.mClock.elapsedRealtime();
                Objects.requireNonNull(notificationEntry2);
                if (notificationEntry2.initializationTime == -1) {
                    notificationEntry2.initializationTime = elapsedRealtime;
                }
                ExpandableNotificationRowController expandableNotificationRowController2 = expandableNotificationRowController;
                boolean z = false;
                expandableNotificationRowController2.mPluginManager.addPluginListener((PluginListener) expandableNotificationRowController2.mView, NotificationMenuRowPlugin.class, false);
                ExpandableNotificationRowController expandableNotificationRowController3 = expandableNotificationRowController;
                ExpandableNotificationRow expandableNotificationRow5 = expandableNotificationRowController3.mView;
                if (expandableNotificationRowController3.mStatusBarStateController.getState() == 1) {
                    z = true;
                }
                expandableNotificationRow5.setOnKeyguard(z);
                ExpandableNotificationRowController expandableNotificationRowController4 = expandableNotificationRowController;
                expandableNotificationRowController4.mStatusBarStateController.addCallback(expandableNotificationRowController4.mStatusBarStateListener);
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
                ExpandableNotificationRowController expandableNotificationRowController2 = expandableNotificationRowController;
                expandableNotificationRowController2.mPluginManager.removePluginListener(expandableNotificationRowController2.mView);
                ExpandableNotificationRowController expandableNotificationRowController3 = expandableNotificationRowController;
                expandableNotificationRowController3.mStatusBarStateController.removeCallback(expandableNotificationRowController3.mStatusBarStateListener);
            }
        });
        Objects.requireNonNull(notificationEntry);
        notificationEntry.mRowController = expandableNotificationRowController;
        notificationRowBinderImpl.mListContainer.bindRow(expandableNotificationRow);
        NotificationRemoteInputManager notificationRemoteInputManager = notificationRowBinderImpl.mNotificationRemoteInputManager;
        Objects.requireNonNull(notificationRemoteInputManager);
        RemoteInputController remoteInputController = notificationRemoteInputManager.mRemoteInputController;
        Objects.requireNonNull(expandableNotificationRow);
        NotificationContentView notificationContentView = expandableNotificationRow.mPrivateLayout;
        Objects.requireNonNull(notificationContentView);
        notificationContentView.mRemoteInputController = remoteInputController;
        expandableNotificationRow.mOnActivatedListener = notificationRowBinderImpl.mPresenter;
        notificationEntry.row = expandableNotificationRow;
        notificationRowBinderImpl.mNotifBindPipeline.manageRow(notificationEntry, expandableNotificationRow);
        StatusBarNotificationPresenter statusBarNotificationPresenter = (StatusBarNotificationPresenter) notificationRowBinderImpl.mBindRowCallback;
        Objects.requireNonNull(statusBarNotificationPresenter);
        expandableNotificationRow.mAboveShelfChangedListener = statusBarNotificationPresenter.mAboveShelfObserver;
        final KeyguardStateController keyguardStateController = statusBarNotificationPresenter.mKeyguardStateController;
        Objects.requireNonNull(keyguardStateController);
        expandableNotificationRow.mSecureStateProvider = new BooleanSupplier() { // from class: com.android.systemui.statusbar.phone.StatusBarNotificationPresenter$$ExternalSyntheticLambda2
            @Override // java.util.function.BooleanSupplier
            public final boolean getAsBoolean() {
                return KeyguardStateController.this.canDismissLockScreen();
            }
        };
        notificationRowBinderImpl.updateRow(notificationEntry, expandableNotificationRow);
        notificationRowBinderImpl.inflateContentViews(notificationEntry, params, expandableNotificationRow, inflationCallback);
    }
}
