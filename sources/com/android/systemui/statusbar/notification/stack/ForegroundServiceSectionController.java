package com.android.systemui.statusbar.notification.stack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.R$styleable;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.statusbar.NotificationRemoveInterceptor;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.icon.IconPack;
import com.android.systemui.statusbar.notification.row.DungeonRow;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.util.Assert;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ForegroundServiceSectionController.kt */
/* loaded from: classes.dex */
public final class ForegroundServiceSectionController {
    public final LinkedHashSet entries = new LinkedHashSet();
    public View entriesView;
    public final NotificationEntryManager entryManager;

    public ForegroundServiceSectionController(NotificationEntryManager notificationEntryManager, ForegroundServiceDismissalFeatureController foregroundServiceDismissalFeatureController) {
        this.entryManager = notificationEntryManager;
        if (foregroundServiceDismissalFeatureController.isForegroundServiceDismissalEnabled()) {
            NotificationRemoveInterceptor notificationRemoveInterceptor = new NotificationRemoveInterceptor() { // from class: com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController.1
                @Override // com.android.systemui.statusbar.NotificationRemoveInterceptor
                public final boolean onNotificationRemoveRequested(NotificationEntry notificationEntry, int i) {
                    boolean z;
                    boolean z2;
                    boolean z3;
                    ForegroundServiceSectionController foregroundServiceSectionController = ForegroundServiceSectionController.this;
                    Objects.requireNonNull(foregroundServiceSectionController);
                    Assert.isMainThread();
                    if (i == 3) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (i == 2 || i == 1) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (i == 12) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (notificationEntry != null) {
                        if (z2 && !notificationEntry.mSbn.isClearable()) {
                            Assert.isMainThread();
                            if (!foregroundServiceSectionController.entries.contains(notificationEntry)) {
                                Assert.isMainThread();
                                foregroundServiceSectionController.entries.add(notificationEntry);
                                foregroundServiceSectionController.update();
                            }
                            foregroundServiceSectionController.entryManager.updateNotifications("FgsSectionController.onNotificationRemoveRequested");
                            return true;
                        } else if ((z || z3) && !notificationEntry.mSbn.isClearable()) {
                            return true;
                        } else {
                            Assert.isMainThread();
                            if (foregroundServiceSectionController.entries.contains(notificationEntry)) {
                                Assert.isMainThread();
                                foregroundServiceSectionController.entries.remove(notificationEntry);
                                foregroundServiceSectionController.update();
                            }
                        }
                    }
                    return false;
                }
            };
            Objects.requireNonNull(notificationEntryManager);
            notificationEntryManager.mRemoveInterceptors.add(notificationRemoveInterceptor);
            notificationEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController.2
                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public final void onPostEntryUpdated(NotificationEntry notificationEntry) {
                    if (ForegroundServiceSectionController.this.entries.contains(notificationEntry)) {
                        ForegroundServiceSectionController foregroundServiceSectionController = ForegroundServiceSectionController.this;
                        Objects.requireNonNull(foregroundServiceSectionController);
                        Assert.isMainThread();
                        foregroundServiceSectionController.entries.remove(notificationEntry);
                        ForegroundServiceSectionController foregroundServiceSectionController2 = ForegroundServiceSectionController.this;
                        Objects.requireNonNull(foregroundServiceSectionController2);
                        Assert.isMainThread();
                        foregroundServiceSectionController2.entries.add(notificationEntry);
                        ForegroundServiceSectionController.this.update();
                    }
                }
            });
        }
    }

    public final void update() {
        String str;
        IconPack iconPack;
        StatusBarIconView statusBarIconView;
        ExpandableNotificationRow expandableNotificationRow;
        Assert.isMainThread();
        View view = this.entriesView;
        if (view != null) {
            View findViewById = view.findViewById(2131427935);
            Objects.requireNonNull(findViewById, "null cannot be cast to non-null type android.widget.LinearLayout");
            LinearLayout linearLayout = (LinearLayout) findViewById;
            linearLayout.removeAllViews();
            for (final NotificationEntry notificationEntry : CollectionsKt___CollectionsKt.sortedWith(this.entries, new Comparator() { // from class: com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController$update$lambda-2$$inlined$sortedBy$1
                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    NotificationEntry notificationEntry2 = (NotificationEntry) t;
                    Objects.requireNonNull(notificationEntry2);
                    Integer valueOf = Integer.valueOf(notificationEntry2.mRanking.getRank());
                    NotificationEntry notificationEntry3 = (NotificationEntry) t2;
                    Objects.requireNonNull(notificationEntry3);
                    return R$styleable.compareValues(valueOf, Integer.valueOf(notificationEntry3.mRanking.getRank()));
                }
            })) {
                StatusBarIcon statusBarIcon = null;
                View inflate = LayoutInflater.from(linearLayout.getContext()).inflate(2131624102, (ViewGroup) null);
                Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.systemui.statusbar.notification.row.DungeonRow");
                final DungeonRow dungeonRow = (DungeonRow) inflate;
                dungeonRow.entry = notificationEntry;
                View findViewById2 = dungeonRow.findViewById(2131427505);
                Objects.requireNonNull(findViewById2, "null cannot be cast to non-null type android.widget.TextView");
                TextView textView = (TextView) findViewById2;
                NotificationEntry notificationEntry2 = dungeonRow.entry;
                if (notificationEntry2 == null || (expandableNotificationRow = notificationEntry2.row) == null) {
                    str = null;
                } else {
                    str = expandableNotificationRow.mAppName;
                }
                textView.setText(str);
                View findViewById3 = dungeonRow.findViewById(2131428102);
                Objects.requireNonNull(findViewById3, "null cannot be cast to non-null type com.android.systemui.statusbar.StatusBarIconView");
                StatusBarIconView statusBarIconView2 = (StatusBarIconView) findViewById3;
                NotificationEntry notificationEntry3 = dungeonRow.entry;
                if (!(notificationEntry3 == null || (iconPack = notificationEntry3.mIcons) == null || (statusBarIconView = iconPack.mStatusBarIcon) == null)) {
                    statusBarIcon = statusBarIconView.mIcon;
                }
                statusBarIconView2.set(statusBarIcon);
                dungeonRow.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController$update$1$2$1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        ForegroundServiceSectionController foregroundServiceSectionController = ForegroundServiceSectionController.this;
                        DungeonRow dungeonRow2 = dungeonRow;
                        Objects.requireNonNull(dungeonRow2);
                        NotificationEntry notificationEntry4 = dungeonRow2.entry;
                        Intrinsics.checkNotNull(notificationEntry4);
                        Objects.requireNonNull(foregroundServiceSectionController);
                        Assert.isMainThread();
                        foregroundServiceSectionController.entries.remove(notificationEntry4);
                        ForegroundServiceSectionController.this.update();
                        NotificationEntry notificationEntry5 = notificationEntry;
                        Objects.requireNonNull(notificationEntry5);
                        ExpandableNotificationRow expandableNotificationRow2 = notificationEntry5.row;
                        Objects.requireNonNull(expandableNotificationRow2);
                        expandableNotificationRow2.mDismissed = false;
                        NotificationEntry notificationEntry6 = notificationEntry;
                        Objects.requireNonNull(notificationEntry6);
                        notificationEntry6.row.resetTranslation();
                        ForegroundServiceSectionController foregroundServiceSectionController2 = ForegroundServiceSectionController.this;
                        Objects.requireNonNull(foregroundServiceSectionController2);
                        foregroundServiceSectionController2.entryManager.updateNotifications("ForegroundServiceSectionController.onClick");
                    }
                });
                linearLayout.addView(dungeonRow);
            }
            if (this.entries.isEmpty()) {
                View view2 = this.entriesView;
                if (view2 != null) {
                    view2.setVisibility(8);
                    return;
                }
                return;
            }
            View view3 = this.entriesView;
            if (view3 != null) {
                view3.setVisibility(0);
                return;
            }
            return;
        }
        throw new IllegalStateException("ForegroundServiceSectionController is trying to show dismissed fgs notifications without having been initialized!");
    }
}
