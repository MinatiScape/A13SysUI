package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Invalidator;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda8;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Assert;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.sequences.FilteringSequence$iterator$1;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: SensitiveContentCoordinator.kt */
/* loaded from: classes.dex */
public final class SensitiveContentCoordinatorImpl extends Invalidator implements SensitiveContentCoordinator, DynamicPrivacyController.Listener, OnBeforeRenderListListener {
    public final DynamicPrivacyController dynamicPrivacyController;
    public final KeyguardStateController keyguardStateController;
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    public final NotificationLockscreenUserManager lockscreenUserManager;
    public final StatusBarStateController statusBarStateController;

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        DynamicPrivacyController dynamicPrivacyController = this.dynamicPrivacyController;
        Objects.requireNonNull(dynamicPrivacyController);
        dynamicPrivacyController.mListeners.add(this);
        notifPipeline.addOnBeforeRenderListListener(this);
        ShadeListBuilder shadeListBuilder = notifPipeline.mShadeListBuilder;
        Objects.requireNonNull(shadeListBuilder);
        Assert.isMainThread();
        shadeListBuilder.mPipelineState.requireState();
        this.mListener = new StatusBar$$ExternalSyntheticLambda8(shadeListBuilder);
    }

    @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener
    public final void onBeforeRenderList(List<? extends ListEntry> list) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        if (this.keyguardStateController.isKeyguardGoingAway()) {
            return;
        }
        if (this.statusBarStateController.getState() != 1 || !this.keyguardUpdateMonitor.getUserUnlockedWithBiometricAndIsBypassing(KeyguardUpdateMonitor.getCurrentUser())) {
            int currentUserId = this.lockscreenUserManager.getCurrentUserId();
            boolean isLockscreenPublicMode = this.lockscreenUserManager.isLockscreenPublicMode(currentUserId);
            if (!isLockscreenPublicMode || this.lockscreenUserManager.userAllowsPrivateNotificationsInPublic(currentUserId)) {
                z = false;
            } else {
                z = true;
            }
            boolean isDynamicallyUnlocked = this.dynamicPrivacyController.isDynamicallyUnlocked();
            FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.filter(SequencesKt___SequencesKt.flatMap(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(list), SensitiveContentCoordinatorKt$extractAllRepresentativeEntries$1.INSTANCE), SensitiveContentCoordinatorImpl$onBeforeRenderList$1.INSTANCE));
            while (filteringSequence$iterator$1.hasNext()) {
                NotificationEntry notificationEntry = (NotificationEntry) filteringSequence$iterator$1.next();
                Objects.requireNonNull(notificationEntry);
                int identifier = notificationEntry.mSbn.getUser().getIdentifier();
                if (isLockscreenPublicMode || this.lockscreenUserManager.isLockscreenPublicMode(identifier)) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    if (!isDynamicallyUnlocked) {
                        z3 = true;
                    } else if (!(identifier == currentUserId || identifier == -1)) {
                        z3 = this.lockscreenUserManager.needsSeparateWorkChallenge(identifier);
                    }
                    boolean needsRedaction = this.lockscreenUserManager.needsRedaction(notificationEntry);
                    if (z3 || !needsRedaction) {
                        z4 = false;
                    } else {
                        z4 = true;
                    }
                    notificationEntry.setSensitive(z4, z);
                }
                z3 = false;
                boolean needsRedaction2 = this.lockscreenUserManager.needsRedaction(notificationEntry);
                if (z3) {
                }
                z4 = false;
                notificationEntry.setSensitive(z4, z);
            }
        }
    }

    public SensitiveContentCoordinatorImpl(DynamicPrivacyController dynamicPrivacyController, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardUpdateMonitor keyguardUpdateMonitor, StatusBarStateController statusBarStateController, KeyguardStateController keyguardStateController) {
        this.dynamicPrivacyController = dynamicPrivacyController;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        this.statusBarStateController = statusBarStateController;
        this.keyguardStateController = keyguardStateController;
    }

    @Override // com.android.systemui.statusbar.notification.DynamicPrivacyController.Listener
    public final void onDynamicPrivacyChanged() {
        invalidateList();
    }
}
