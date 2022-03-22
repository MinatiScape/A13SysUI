package com.android.systemui.statusbar.notification.collection.coordinator;

import android.content.Context;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.SectionClassifier;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderEntryListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NotifRowController;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RowAppearanceCoordinator.kt */
/* loaded from: classes.dex */
public final class RowAppearanceCoordinator implements Coordinator {
    public NotificationEntry entryToExpand;
    public final boolean mAlwaysExpandNonGroupedNotification;
    public AssistantFeedbackController mAssistantFeedbackController;
    public SectionClassifier mSectionClassifier;

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addOnBeforeRenderListListener(new OnBeforeRenderListListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RowAppearanceCoordinator$attach$1
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener
            public final void onBeforeRenderList(List<? extends ListEntry> list) {
                Object obj;
                NotificationEntry representativeEntry;
                NotifSection section;
                RowAppearanceCoordinator rowAppearanceCoordinator = RowAppearanceCoordinator.this;
                Objects.requireNonNull(rowAppearanceCoordinator);
                NotificationEntry notificationEntry = null;
                if (list.isEmpty()) {
                    obj = null;
                } else {
                    obj = list.get(0);
                }
                ListEntry listEntry = (ListEntry) obj;
                if (!(listEntry == null || (representativeEntry = listEntry.getRepresentativeEntry()) == null)) {
                    SectionClassifier sectionClassifier = rowAppearanceCoordinator.mSectionClassifier;
                    Intrinsics.checkNotNull(representativeEntry.getSection());
                    Objects.requireNonNull(sectionClassifier);
                    Set<? extends NotifSectioner> set = sectionClassifier.lowPrioritySections;
                    if (set == null) {
                        set = null;
                    }
                    if (!set.contains(section.sectioner)) {
                        notificationEntry = representativeEntry;
                    }
                }
                rowAppearanceCoordinator.entryToExpand = notificationEntry;
            }
        });
        OnAfterRenderEntryListener rowAppearanceCoordinator$attach$2 = new OnAfterRenderEntryListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RowAppearanceCoordinator$attach$2
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderEntryListener
            public final void onAfterRenderEntry(NotificationEntry notificationEntry, NotifRowController notifRowController) {
                boolean z;
                RowAppearanceCoordinator rowAppearanceCoordinator = RowAppearanceCoordinator.this;
                Objects.requireNonNull(rowAppearanceCoordinator);
                if (rowAppearanceCoordinator.mAlwaysExpandNonGroupedNotification || Intrinsics.areEqual(notificationEntry, rowAppearanceCoordinator.entryToExpand)) {
                    z = true;
                } else {
                    z = false;
                }
                notifRowController.setSystemExpanded(z);
                AssistantFeedbackController assistantFeedbackController = rowAppearanceCoordinator.mAssistantFeedbackController;
                Objects.requireNonNull(assistantFeedbackController);
                notifRowController.setFeedbackIcon(assistantFeedbackController.mIcons.get(assistantFeedbackController.getFeedbackStatus(notificationEntry)));
                Objects.requireNonNull(notificationEntry);
                notifRowController.setLastAudiblyAlertedMs(notificationEntry.mRanking.getLastAudiblyAlertedMillis());
            }
        };
        RenderStageManager renderStageManager = notifPipeline.mRenderStageManager;
        Objects.requireNonNull(renderStageManager);
        renderStageManager.onAfterRenderEntryListeners.add(rowAppearanceCoordinator$attach$2);
    }

    public RowAppearanceCoordinator(Context context, AssistantFeedbackController assistantFeedbackController, SectionClassifier sectionClassifier) {
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mSectionClassifier = sectionClassifier;
        this.mAlwaysExpandNonGroupedNotification = context.getResources().getBoolean(2131034117);
    }
}
