package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import kotlin.collections.CollectionsKt___CollectionsKt;
/* loaded from: classes.dex */
public final class RankingCoordinator implements Coordinator {
    public final NodeController mAlertingHeaderController;
    public boolean mHasMinimizedEntries;
    public boolean mHasSilentEntries;
    public final HighPriorityProvider mHighPriorityProvider;
    public final SectionClassifier mSectionClassifier;
    public final SectionHeaderController mSilentHeaderController;
    public final NodeController mSilentNodeController;
    public final StatusBarStateController mStatusBarStateController;
    public final AnonymousClass1 mAlertingNotifSectioner = new NotifSectioner() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final NodeController getHeaderNodeController() {
            return null;
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final boolean isInSection(ListEntry listEntry) {
            return RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry);
        }
    };
    public final AnonymousClass2 mSilentNotifSectioner = new NotifSectioner() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.2
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final NodeController getHeaderNodeController() {
            return RankingCoordinator.this.mSilentNodeController;
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final boolean isInSection(ListEntry listEntry) {
            if (RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry) || listEntry.getRepresentativeEntry().isAmbient()) {
                return false;
            }
            return true;
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final void onEntriesUpdated(ArrayList arrayList) {
            int i = 0;
            RankingCoordinator.this.mHasSilentEntries = false;
            while (true) {
                if (i >= arrayList.size()) {
                    break;
                }
                NotificationEntry representativeEntry = ((ListEntry) arrayList.get(i)).getRepresentativeEntry();
                Objects.requireNonNull(representativeEntry);
                if (representativeEntry.mSbn.isClearable()) {
                    RankingCoordinator.this.mHasSilentEntries = true;
                    break;
                }
                i++;
            }
            RankingCoordinator rankingCoordinator = RankingCoordinator.this;
            rankingCoordinator.mSilentHeaderController.setClearSectionEnabled(rankingCoordinator.mHasMinimizedEntries | rankingCoordinator.mHasSilentEntries);
        }
    };
    public final AnonymousClass3 mMinimizedNotifSectioner = new NotifSectioner() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.3
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final NodeController getHeaderNodeController() {
            return RankingCoordinator.this.mSilentNodeController;
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final boolean isInSection(ListEntry listEntry) {
            if (RankingCoordinator.this.mHighPriorityProvider.isHighPriority(listEntry) || !listEntry.getRepresentativeEntry().isAmbient()) {
                return false;
            }
            return true;
        }

        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
        public final void onEntriesUpdated(ArrayList arrayList) {
            int i = 0;
            RankingCoordinator.this.mHasMinimizedEntries = false;
            while (true) {
                if (i >= arrayList.size()) {
                    break;
                }
                NotificationEntry representativeEntry = ((ListEntry) arrayList.get(i)).getRepresentativeEntry();
                Objects.requireNonNull(representativeEntry);
                if (representativeEntry.mSbn.isClearable()) {
                    RankingCoordinator.this.mHasMinimizedEntries = true;
                    break;
                }
                i++;
            }
            RankingCoordinator rankingCoordinator = RankingCoordinator.this;
            rankingCoordinator.mSilentHeaderController.setClearSectionEnabled(rankingCoordinator.mHasMinimizedEntries | rankingCoordinator.mHasSilentEntries);
        }
    };
    public final AnonymousClass4 mSuspendedFilter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.4
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            Objects.requireNonNull(notificationEntry);
            return notificationEntry.mRanking.isSuspended();
        }
    };
    public final AnonymousClass5 mDndVisualEffectsFilter = new NotifFilter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.5
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifFilter
        public final boolean shouldFilterOut(NotificationEntry notificationEntry, long j) {
            if (RankingCoordinator.this.mStatusBarStateController.isDozing()) {
                Objects.requireNonNull(notificationEntry);
                if (notificationEntry.shouldSuppressVisualEffect(128)) {
                    return true;
                }
            }
            if (!RankingCoordinator.this.mStatusBarStateController.isDozing()) {
                Objects.requireNonNull(notificationEntry);
                if (notificationEntry.shouldSuppressVisualEffect(256)) {
                    return true;
                }
            }
            return false;
        }
    };
    public final AnonymousClass6 mStatusBarStateCallback = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator.6
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozingChanged(boolean z) {
            invalidateList();
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        this.mStatusBarStateController.addCallback(this.mStatusBarStateCallback);
        SectionClassifier sectionClassifier = this.mSectionClassifier;
        Set singleton = Collections.singleton(this.mMinimizedNotifSectioner);
        Objects.requireNonNull(sectionClassifier);
        sectionClassifier.lowPrioritySections = CollectionsKt___CollectionsKt.toSet(singleton);
        notifPipeline.addPreGroupFilter(this.mSuspendedFilter);
        notifPipeline.addPreGroupFilter(this.mDndVisualEffectsFilter);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator$2] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator$3] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator$4] */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator$5] */
    /* JADX WARN: Type inference failed for: r0v5, types: [com.android.systemui.statusbar.notification.collection.coordinator.RankingCoordinator$6] */
    public RankingCoordinator(StatusBarStateController statusBarStateController, HighPriorityProvider highPriorityProvider, SectionClassifier sectionClassifier, NodeController nodeController, SectionHeaderController sectionHeaderController, NodeController nodeController2) {
        this.mStatusBarStateController = statusBarStateController;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mSectionClassifier = sectionClassifier;
        this.mAlertingHeaderController = nodeController;
        this.mSilentNodeController = nodeController2;
        this.mSilentHeaderController = sectionHeaderController;
    }
}
