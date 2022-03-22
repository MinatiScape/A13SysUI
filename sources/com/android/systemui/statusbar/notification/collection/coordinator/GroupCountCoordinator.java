package com.android.systemui.statusbar.notification.collection.coordinator;

import android.util.ArrayMap;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderGroupListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener;
import com.android.systemui.statusbar.notification.collection.render.NotifGroupController;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.FilteringSequence$iterator$1;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: GroupCountCoordinator.kt */
/* loaded from: classes.dex */
public final class GroupCountCoordinator implements Coordinator {
    public final ArrayMap<GroupEntry, Integer> untruncatedChildCounts = new ArrayMap<>();

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addOnBeforeFinalizeFilterListener(new OnBeforeFinalizeFilterListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.GroupCountCoordinator$attach$1
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeFinalizeFilterListener
            public final void onBeforeFinalizeFilter(List<? extends ListEntry> list) {
                GroupCountCoordinator groupCountCoordinator = GroupCountCoordinator.this;
                Objects.requireNonNull(groupCountCoordinator);
                groupCountCoordinator.untruncatedChildCounts.clear();
                FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.filter(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(list), GroupCountCoordinator$onBeforeFinalizeFilter$$inlined$filterIsInstance$1.INSTANCE));
                while (filteringSequence$iterator$1.hasNext()) {
                    GroupEntry groupEntry = (GroupEntry) filteringSequence$iterator$1.next();
                    ArrayMap<GroupEntry, Integer> arrayMap = groupCountCoordinator.untruncatedChildCounts;
                    Objects.requireNonNull(groupEntry);
                    arrayMap.put(groupEntry, Integer.valueOf(groupEntry.mUnmodifiableChildren.size()));
                }
            }
        });
        OnAfterRenderGroupListener groupCountCoordinator$attach$2 = new OnAfterRenderGroupListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.GroupCountCoordinator$attach$2
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderGroupListener
            public final void onAfterRenderGroup(GroupEntry groupEntry, NotifGroupController notifGroupController) {
                GroupCountCoordinator groupCountCoordinator = GroupCountCoordinator.this;
                Objects.requireNonNull(groupCountCoordinator);
                Integer num = groupCountCoordinator.untruncatedChildCounts.get(groupEntry);
                if (num != null) {
                    notifGroupController.setUntruncatedChildCount(num.intValue());
                    return;
                }
                throw new IllegalStateException(Intrinsics.stringPlus("No untruncated child count for group: ", groupEntry.mKey).toString());
            }
        };
        RenderStageManager renderStageManager = notifPipeline.mRenderStageManager;
        Objects.requireNonNull(renderStageManager);
        renderStageManager.onAfterRenderGroupListeners.add(groupCountCoordinator$attach$2);
    }
}
