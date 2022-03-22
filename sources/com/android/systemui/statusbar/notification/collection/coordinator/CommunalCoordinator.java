package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda2;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class CommunalCoordinator implements Coordinator {
    public final CommunalStateController mCommunalStateController;
    public final Executor mExecutor;
    public final NotificationEntryManager mNotificationEntryManager;
    public final NotificationLockscreenUserManager mNotificationLockscreenUserManager;
    public final AnonymousClass1 mFilter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.CommunalCoordinator.1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            CommunalStateController communalStateController = CommunalCoordinator.this.mCommunalStateController;
            Objects.requireNonNull(communalStateController);
            return communalStateController.mCommunalViewShowing;
        }
    };
    public final AnonymousClass2 mStateCallback = new AnonymousClass2();

    /* renamed from: com.android.systemui.statusbar.notification.collection.coordinator.CommunalCoordinator$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements CommunalStateController.Callback {
        public AnonymousClass2() {
        }

        @Override // com.android.systemui.communal.CommunalStateController.Callback
        public final void onCommunalViewShowingChanged() {
            CommunalCoordinator.this.mExecutor.execute(new TaskView$$ExternalSyntheticLambda2(this, 4));
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addPreGroupFilter(this.mFilter);
        this.mCommunalStateController.addCallback((CommunalStateController.Callback) this.mStateCallback);
        if (!notifPipeline.isNewPipelineEnabled) {
            this.mNotificationLockscreenUserManager.addKeyguardNotificationSuppressor(new NotificationLockscreenUserManager.KeyguardNotificationSuppressor() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.CommunalCoordinator$$ExternalSyntheticLambda0
                @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager.KeyguardNotificationSuppressor
                public final boolean shouldSuppressOnKeyguard(NotificationEntry notificationEntry) {
                    CommunalCoordinator communalCoordinator = CommunalCoordinator.this;
                    Objects.requireNonNull(communalCoordinator);
                    CommunalStateController communalStateController = communalCoordinator.mCommunalStateController;
                    Objects.requireNonNull(communalStateController);
                    return communalStateController.mCommunalViewShowing;
                }
            });
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.notification.collection.coordinator.CommunalCoordinator$1] */
    public CommunalCoordinator(Executor executor, NotificationEntryManager notificationEntryManager, NotificationLockscreenUserManager notificationLockscreenUserManager, CommunalStateController communalStateController) {
        this.mExecutor = executor;
        this.mNotificationEntryManager = notificationEntryManager;
        this.mNotificationLockscreenUserManager = notificationLockscreenUserManager;
        this.mCommunalStateController = communalStateController;
    }
}
