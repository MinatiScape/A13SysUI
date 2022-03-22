package com.android.systemui.statusbar.notification;

import android.app.Notification;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import com.android.internal.widget.ConversationLayout;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.inflation.BindEventManager;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import kotlin.Function;
import kotlin.Pair;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.jvm.internal.FunctionAdapter;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.EmptySequence;
import kotlin.sequences.FilteringSequence;
import kotlin.sequences.FilteringSequence$iterator$1;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: ConversationNotifications.kt */
/* loaded from: classes.dex */
public final class ConversationNotificationManager {
    public final Context context;
    public final NotifPipelineFlags featureFlags;
    public final Handler mainHandler;
    public final CommonNotifCollection notifCollection;
    public final NotificationGroupManagerLegacy notificationGroupManager;
    public final ConcurrentHashMap<String, ConversationState> states = new ConcurrentHashMap<>();
    public boolean notifPanelCollapsed = true;

    /* compiled from: ConversationNotifications.kt */
    /* renamed from: com.android.systemui.statusbar.notification.ConversationNotificationManager$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public /* synthetic */ class AnonymousClass2 implements BindEventManager.Listener, FunctionAdapter {
        public AnonymousClass2() {
        }

        public final boolean equals(Object obj) {
            if (!(obj instanceof BindEventManager.Listener) || !(obj instanceof FunctionAdapter)) {
                return false;
            }
            return Intrinsics.areEqual(getFunctionDelegate(), ((FunctionAdapter) obj).getFunctionDelegate());
        }

        @Override // kotlin.jvm.internal.FunctionAdapter
        public final Function<?> getFunctionDelegate() {
            return new FunctionReferenceImpl(1, ConversationNotificationManager.this, ConversationNotificationManager.class, "onEntryViewBound", "onEntryViewBound(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)V", 0);
        }

        public final int hashCode() {
            return getFunctionDelegate().hashCode();
        }

        @Override // com.android.systemui.statusbar.notification.collection.inflation.BindEventManager.Listener
        public final void onViewBound(final NotificationEntry notificationEntry) {
            final ConversationNotificationManager conversationNotificationManager = ConversationNotificationManager.this;
            Objects.requireNonNull(conversationNotificationManager);
            if (notificationEntry.mRanking.isConversation()) {
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                if (expandableNotificationRow != null) {
                    expandableNotificationRow.mExpansionChangedListener = new ExpandableNotificationRow.OnExpansionChangedListener() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$onEntryViewBound$1
                        @Override // com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.OnExpansionChangedListener
                        public final void onExpansionChanged(final boolean z) {
                            NotificationEntry notificationEntry2 = NotificationEntry.this;
                            Objects.requireNonNull(notificationEntry2);
                            ExpandableNotificationRow expandableNotificationRow2 = notificationEntry2.row;
                            boolean z2 = true;
                            if (expandableNotificationRow2 == null || !expandableNotificationRow2.isShown()) {
                                z2 = false;
                            }
                            if (!z2 || !z) {
                                ConversationNotificationManager.onEntryViewBound$updateCount(conversationNotificationManager, NotificationEntry.this, z);
                                return;
                            }
                            NotificationEntry notificationEntry3 = NotificationEntry.this;
                            Objects.requireNonNull(notificationEntry3);
                            ExpandableNotificationRow expandableNotificationRow3 = notificationEntry3.row;
                            final ConversationNotificationManager conversationNotificationManager2 = conversationNotificationManager;
                            final NotificationEntry notificationEntry4 = NotificationEntry.this;
                            Runnable runnable = new Runnable() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$onEntryViewBound$1.1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ConversationNotificationManager.onEntryViewBound$updateCount(conversationNotificationManager2, notificationEntry4, z);
                                }
                            };
                            Objects.requireNonNull(expandableNotificationRow3);
                            expandableNotificationRow3.mOnIntrinsicHeightReachedRunnable = runnable;
                            if (expandableNotificationRow3.mActualHeight == expandableNotificationRow3.getIntrinsicHeight()) {
                                expandableNotificationRow3.mOnIntrinsicHeightReachedRunnable.run();
                                expandableNotificationRow3.mOnIntrinsicHeightReachedRunnable = null;
                            }
                        }
                    };
                }
                boolean z = true;
                if (expandableNotificationRow == null || !expandableNotificationRow.isExpanded(false)) {
                    z = false;
                }
                ConversationNotificationManager.onEntryViewBound$updateCount(conversationNotificationManager, notificationEntry, z);
            }
        }
    }

    /* compiled from: ConversationNotifications.kt */
    /* loaded from: classes.dex */
    public static final class ConversationState {
        public final Notification notification;
        public final int unreadCount;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConversationState)) {
                return false;
            }
            ConversationState conversationState = (ConversationState) obj;
            return this.unreadCount == conversationState.unreadCount && Intrinsics.areEqual(this.notification, conversationState.notification);
        }

        public final int hashCode() {
            return this.notification.hashCode() + (Integer.hashCode(this.unreadCount) * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ConversationState(unreadCount=");
            m.append(this.unreadCount);
            m.append(", notification=");
            m.append(this.notification);
            m.append(')');
            return m.toString();
        }

        public ConversationState(int i, Notification notification) {
            this.unreadCount = i;
            this.notification = notification;
        }
    }

    public static final void onEntryViewBound$updateCount(ConversationNotificationManager conversationNotificationManager, NotificationEntry notificationEntry, boolean z) {
        boolean z2;
        if (z) {
            if (conversationNotificationManager.notifPanelCollapsed) {
                Objects.requireNonNull(notificationEntry);
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                boolean z3 = false;
                if (expandableNotificationRow != null) {
                    if (!expandableNotificationRow.mIsPinned) {
                        z2 = false;
                    } else {
                        z2 = expandableNotificationRow.mExpandedWhenPinned;
                    }
                    if (z2) {
                        z3 = true;
                    }
                }
                if (!z3) {
                    return;
                }
            }
            Objects.requireNonNull(notificationEntry);
            conversationNotificationManager.states.compute(notificationEntry.mKey, ConversationNotificationManager$resetCount$1.INSTANCE);
            ExpandableNotificationRow expandableNotificationRow2 = notificationEntry.row;
            if (expandableNotificationRow2 != null) {
                resetBadgeUi(expandableNotificationRow2);
            }
        }
    }

    public final void onNotificationPanelExpandStateChanged(boolean z) {
        this.notifPanelCollapsed = z;
        if (!z) {
            FilteringSequence mapNotNull = SequencesKt___SequencesKt.mapNotNull(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(this.states.entrySet()), new ConversationNotificationManager$onNotificationPanelExpandStateChanged$expanded$1(this));
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(mapNotNull);
            while (filteringSequence$iterator$1.hasNext()) {
                Pair pair = (Pair) filteringSequence$iterator$1.next();
                linkedHashMap.put(pair.component1(), pair.component2());
            }
            final Map optimizeReadOnlyMap = MapsKt___MapsKt.optimizeReadOnlyMap(linkedHashMap);
            this.states.replaceAll(new BiFunction() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$onNotificationPanelExpandStateChanged$1
                @Override // java.util.function.BiFunction
                public final Object apply(Object obj, Object obj2) {
                    ConversationNotificationManager.ConversationState conversationState = (ConversationNotificationManager.ConversationState) obj2;
                    if (optimizeReadOnlyMap.containsKey((String) obj)) {
                        return new ConversationNotificationManager.ConversationState(0, conversationState.notification);
                    }
                    return conversationState;
                }
            });
            FilteringSequence$iterator$1 filteringSequence$iterator$12 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.mapNotNull(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(optimizeReadOnlyMap.values()), ConversationNotificationManager$onNotificationPanelExpandStateChanged$2.INSTANCE));
            while (filteringSequence$iterator$12.hasNext()) {
                resetBadgeUi((ExpandableNotificationRow) filteringSequence$iterator$12.next());
            }
        }
    }

    public ConversationNotificationManager(BindEventManager bindEventManager, NotificationGroupManagerLegacy notificationGroupManagerLegacy, Context context, CommonNotifCollection commonNotifCollection, NotifPipelineFlags notifPipelineFlags, Handler handler) {
        this.notificationGroupManager = notificationGroupManagerLegacy;
        this.context = context;
        this.notifCollection = commonNotifCollection;
        this.featureFlags = notifPipelineFlags;
        this.mainHandler = handler;
        commonNotifCollection.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager.1
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                ConversationNotificationManager conversationNotificationManager = ConversationNotificationManager.this;
                Objects.requireNonNull(conversationNotificationManager);
                conversationNotificationManager.states.remove(notificationEntry.mKey);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public final void onRankingUpdate(NotificationListenerService.RankingMap rankingMap) {
                ConversationNotificationManager conversationNotificationManager = ConversationNotificationManager.this;
                Objects.requireNonNull(conversationNotificationManager);
                NotificationListenerService.Ranking ranking = new NotificationListenerService.Ranking();
                FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.mapNotNull(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(conversationNotificationManager.states.keySet()), new ConversationNotificationManager$updateNotificationRanking$activeConversationEntries$1(conversationNotificationManager)));
                while (filteringSequence$iterator$1.hasNext()) {
                    NotificationEntry notificationEntry = (NotificationEntry) filteringSequence$iterator$1.next();
                    Objects.requireNonNull(notificationEntry);
                    if (rankingMap.getRanking(notificationEntry.mSbn.getKey(), ranking) && ranking.isConversation()) {
                        final boolean isImportantConversation = ranking.getChannel().isImportantConversation();
                        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                        boolean z = false;
                        if (expandableNotificationRow != null) {
                            NotificationContentView[] notificationContentViewArr = expandableNotificationRow.mLayouts;
                            NotificationContentView[] notificationContentViewArr2 = (NotificationContentView[]) Arrays.copyOf(notificationContentViewArr, notificationContentViewArr.length);
                            if (notificationContentViewArr2 != null) {
                                FilteringSequence$iterator$1 filteringSequence$iterator$12 = new FilteringSequence$iterator$1(new FilteringSequence(SequencesKt___SequencesKt.mapNotNull(SequencesKt___SequencesKt.flatMap(ArraysKt___ArraysKt.asSequence(notificationContentViewArr2), ConversationNotificationManager$updateNotificationRanking$1.INSTANCE), ConversationNotificationManager$updateNotificationRanking$2.INSTANCE), false, new ConversationNotificationManager$updateNotificationRanking$3(isImportantConversation)));
                                boolean z2 = false;
                                while (filteringSequence$iterator$12.hasNext()) {
                                    final ConversationLayout conversationLayout = (ConversationLayout) filteringSequence$iterator$12.next();
                                    if (!isImportantConversation || !notificationEntry.mIsMarkedForUserTriggeredMovement) {
                                        conversationLayout.setIsImportantConversation(isImportantConversation, false);
                                    } else {
                                        conversationNotificationManager.mainHandler.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.notification.ConversationNotificationManager$updateNotificationRanking$4$1
                                            @Override // java.lang.Runnable
                                            public final void run() {
                                                conversationLayout.setIsImportantConversation(isImportantConversation, true);
                                            }
                                        }, 960L);
                                    }
                                    z2 = true;
                                }
                                z = z2;
                            }
                        }
                        if (z && !conversationNotificationManager.featureFlags.isNewPipelineEnabled()) {
                            conversationNotificationManager.notificationGroupManager.updateIsolation(notificationEntry);
                        }
                    }
                }
            }
        });
        AnonymousClass2 r2 = new AnonymousClass2();
        Objects.requireNonNull(bindEventManager);
        bindEventManager.listeners.addIfAbsent(r2);
    }

    public static void resetBadgeUi(ExpandableNotificationRow expandableNotificationRow) {
        Sequence sequence;
        Objects.requireNonNull(expandableNotificationRow);
        NotificationContentView[] notificationContentViewArr = expandableNotificationRow.mLayouts;
        NotificationContentView[] notificationContentViewArr2 = (NotificationContentView[]) Arrays.copyOf(notificationContentViewArr, notificationContentViewArr.length);
        if (notificationContentViewArr2 == null) {
            sequence = null;
        } else {
            sequence = ArraysKt___ArraysKt.asSequence(notificationContentViewArr2);
        }
        if (sequence == null) {
            sequence = EmptySequence.INSTANCE;
        }
        FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.mapNotNull(SequencesKt___SequencesKt.flatMap(sequence, ConversationNotificationManager$resetBadgeUi$1.INSTANCE), ConversationNotificationManager$resetBadgeUi$2.INSTANCE));
        while (filteringSequence$iterator$1.hasNext()) {
            ((ConversationLayout) filteringSequence$iterator$1.next()).setUnreadCount(0);
        }
    }
}
