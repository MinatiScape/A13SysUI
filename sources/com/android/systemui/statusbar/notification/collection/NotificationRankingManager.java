package com.android.systemui.statusbar.notification.collection;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.NotificationEntryManagerLogger;
import com.android.systemui.statusbar.notification.NotificationFilter;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.Lazy;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.FilteringSequence;
import kotlin.sequences.SequencesKt___SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt$sortedWith$1;
/* compiled from: NotificationRankingManager.kt */
/* loaded from: classes.dex */
public final class NotificationRankingManager implements LegacyNotificationRanker {
    public final NotificationGroupManagerLegacy groupManager;
    public final HeadsUpManager headsUpManager;
    public final HighPriorityProvider highPriorityProvider;
    public final NotificationEntryManager.KeyguardEnvironment keyguardEnvironment;
    public final NotificationEntryManagerLogger logger;
    public final Lazy<NotificationMediaManager> mediaManagerLazy;
    public final NotificationFilter notifFilter;
    public final PeopleNotificationIdentifier peopleNotificationIdentifier;
    public NotificationListenerService.RankingMap rankingMap;
    public final NotificationSectionsFeatureManager sectionsFeatureManager;
    public final kotlin.Lazy mediaManager$delegate = LazyKt__LazyJVMKt.lazy(new NotificationRankingManager$mediaManager$2(this));
    public final NotificationRankingManager$rankingComparator$1 rankingComparator = new Comparator() { // from class: com.android.systemui.statusbar.notification.collection.NotificationRankingManager$rankingComparator$1
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            NotificationEntry notificationEntry = (NotificationEntry) obj;
            NotificationEntry notificationEntry2 = (NotificationEntry) obj2;
            Objects.requireNonNull(notificationEntry);
            StatusBarNotification statusBarNotification = notificationEntry.mSbn;
            Objects.requireNonNull(notificationEntry2);
            StatusBarNotification statusBarNotification2 = notificationEntry2.mSbn;
            int rank = notificationEntry.mRanking.getRank();
            int rank2 = notificationEntry2.mRanking.getRank();
            boolean access$isColorizedForegroundService = CloseableKt.access$isColorizedForegroundService(notificationEntry);
            boolean access$isColorizedForegroundService2 = CloseableKt.access$isColorizedForegroundService(notificationEntry2);
            boolean access$isImportantCall = CloseableKt.access$isImportantCall(notificationEntry);
            boolean access$isImportantCall2 = CloseableKt.access$isImportantCall(notificationEntry2);
            NotificationRankingManager notificationRankingManager = NotificationRankingManager.this;
            Objects.requireNonNull(notificationRankingManager);
            int peopleNotificationType = notificationRankingManager.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry);
            NotificationRankingManager notificationRankingManager2 = NotificationRankingManager.this;
            Objects.requireNonNull(notificationRankingManager2);
            int peopleNotificationType2 = notificationRankingManager2.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry2);
            boolean isImportantMedia = NotificationRankingManager.this.isImportantMedia(notificationEntry);
            boolean isImportantMedia2 = NotificationRankingManager.this.isImportantMedia(notificationEntry2);
            boolean access$isSystemMax = CloseableKt.access$isSystemMax(notificationEntry);
            boolean access$isSystemMax2 = CloseableKt.access$isSystemMax(notificationEntry2);
            boolean isRowHeadsUp = notificationEntry.isRowHeadsUp();
            boolean isRowHeadsUp2 = notificationEntry2.isRowHeadsUp();
            NotificationRankingManager notificationRankingManager3 = NotificationRankingManager.this;
            Objects.requireNonNull(notificationRankingManager3);
            boolean isHighPriority = notificationRankingManager3.highPriorityProvider.isHighPriority(notificationEntry);
            NotificationRankingManager notificationRankingManager4 = NotificationRankingManager.this;
            Objects.requireNonNull(notificationRankingManager4);
            boolean isHighPriority2 = notificationRankingManager4.highPriorityProvider.isHighPriority(notificationEntry2);
            if (isRowHeadsUp != isRowHeadsUp2) {
                if (!isRowHeadsUp) {
                    return 1;
                }
            } else if (isRowHeadsUp) {
                return NotificationRankingManager.this.headsUpManager.compare(notificationEntry, notificationEntry2);
            } else {
                if (access$isColorizedForegroundService != access$isColorizedForegroundService2) {
                    if (!access$isColorizedForegroundService) {
                        return 1;
                    }
                } else if (access$isImportantCall == access$isImportantCall2) {
                    NotificationRankingManager notificationRankingManager5 = NotificationRankingManager.this;
                    Objects.requireNonNull(notificationRankingManager5);
                    if (notificationRankingManager5.sectionsFeatureManager.isFilteringEnabled() && peopleNotificationType != peopleNotificationType2) {
                        return NotificationRankingManager.this.peopleNotificationIdentifier.compareTo(peopleNotificationType, peopleNotificationType2);
                    }
                    if (isImportantMedia != isImportantMedia2) {
                        if (!isImportantMedia) {
                            return 1;
                        }
                    } else if (access$isSystemMax != access$isSystemMax2) {
                        if (!access$isSystemMax) {
                            return 1;
                        }
                    } else if (isHighPriority != isHighPriority2) {
                        return Intrinsics.compare(isHighPriority ? 1 : 0, isHighPriority2 ? 1 : 0) * (-1);
                    } else {
                        if (rank != rank2) {
                            return rank - rank2;
                        }
                        int i = (statusBarNotification2.getNotification().when > statusBarNotification.getNotification().when ? 1 : (statusBarNotification2.getNotification().when == statusBarNotification.getNotification().when ? 0 : -1));
                        if (i >= 0) {
                            if (i == 0) {
                                return 0;
                            }
                            return 1;
                        }
                    }
                } else if (!access$isImportantCall) {
                    return 1;
                }
            }
            return -1;
        }
    };

    public final List<NotificationEntry> filterAndSortLocked(Collection<NotificationEntry> collection, String str) {
        int i;
        boolean z;
        this.logger.logFilterAndSort(str);
        List<NotificationEntry> list = SequencesKt___SequencesKt.toList(new SequencesKt___SequencesKt$sortedWith$1(new FilteringSequence(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(collection), false, new NotificationRankingManager$filterAndSortLocked$filtered$1(this)), this.rankingComparator));
        for (NotificationEntry notificationEntry : collection) {
            boolean access$isImportantCall = CloseableKt.access$isImportantCall(notificationEntry);
            boolean isRowHeadsUp = notificationEntry.isRowHeadsUp();
            boolean isImportantMedia = isImportantMedia(notificationEntry);
            boolean access$isSystemMax = CloseableKt.access$isSystemMax(notificationEntry);
            if (CloseableKt.access$isColorizedForegroundService(notificationEntry) || access$isImportantCall) {
                i = 3;
            } else {
                if (this.sectionsFeatureManager.isFilteringEnabled()) {
                    if (this.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry) != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        i = 4;
                    }
                }
                if (isRowHeadsUp || isImportantMedia || access$isSystemMax || this.highPriorityProvider.isHighPriority(notificationEntry)) {
                    i = 5;
                } else {
                    i = 6;
                }
            }
            notificationEntry.mBucket = i;
        }
        return list;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public final boolean isNotificationForCurrentProfiles(NotificationEntry notificationEntry) {
        return this.keyguardEnvironment.isNotificationForCurrentProfiles(notificationEntry.mSbn);
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public final List<NotificationEntry> updateRanking(NotificationListenerService.RankingMap rankingMap, Collection<NotificationEntry> collection, String str) {
        List<NotificationEntry> filterAndSortLocked;
        if (rankingMap != null) {
            this.rankingMap = rankingMap;
            synchronized (collection) {
                for (NotificationEntry notificationEntry : collection) {
                    NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                    Objects.requireNonNull(notificationEntry);
                    if (rankingMap.getRanking(notificationEntry.mKey, ranking)) {
                        notificationEntry.setRanking(ranking);
                        String overrideGroupKey = ranking.getOverrideGroupKey();
                        if (!Objects.equals(notificationEntry.mSbn.getOverrideGroupKey(), overrideGroupKey)) {
                            String groupKey = notificationEntry.mSbn.getGroupKey();
                            boolean isGroup = notificationEntry.mSbn.isGroup();
                            boolean isGroupSummary = notificationEntry.mSbn.getNotification().isGroupSummary();
                            notificationEntry.mSbn.setOverrideGroupKey(overrideGroupKey);
                            this.groupManager.onEntryUpdated(notificationEntry, groupKey, isGroup, isGroupSummary);
                        }
                    }
                }
            }
        }
        synchronized (this) {
            filterAndSortLocked = filterAndSortLocked(collection, str);
        }
        return filterAndSortLocked;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.statusbar.notification.collection.NotificationRankingManager$rankingComparator$1] */
    public NotificationRankingManager(Lazy<NotificationMediaManager> lazy, NotificationGroupManagerLegacy notificationGroupManagerLegacy, HeadsUpManager headsUpManager, NotificationFilter notificationFilter, NotificationEntryManagerLogger notificationEntryManagerLogger, NotificationSectionsFeatureManager notificationSectionsFeatureManager, PeopleNotificationIdentifier peopleNotificationIdentifier, HighPriorityProvider highPriorityProvider, NotificationEntryManager.KeyguardEnvironment keyguardEnvironment) {
        this.mediaManagerLazy = lazy;
        this.groupManager = notificationGroupManagerLegacy;
        this.headsUpManager = headsUpManager;
        this.notifFilter = notificationFilter;
        this.logger = notificationEntryManagerLogger;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.peopleNotificationIdentifier = peopleNotificationIdentifier;
        this.highPriorityProvider = highPriorityProvider;
        this.keyguardEnvironment = keyguardEnvironment;
    }

    public final boolean isImportantMedia(NotificationEntry notificationEntry) {
        Objects.requireNonNull(notificationEntry);
        String str = notificationEntry.mKey;
        NotificationMediaManager notificationMediaManager = (NotificationMediaManager) this.mediaManager$delegate.getValue();
        Objects.requireNonNull(notificationMediaManager);
        if (!Intrinsics.areEqual(str, notificationMediaManager.mMediaNotificationKey) || notificationEntry.getImportance() <= 1) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationRanker
    public final NotificationListenerService.RankingMap getRankingMap() {
        return this.rankingMap;
    }
}
