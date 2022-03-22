package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifLiveDataStoreImpl;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DataStoreCoordinator.kt */
/* loaded from: classes.dex */
public final class DataStoreCoordinator implements Coordinator {
    public final NotifLiveDataStoreImpl notifLiveDataStoreImpl;

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        OnAfterRenderListListener dataStoreCoordinator$attach$1 = new OnAfterRenderListListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.DataStoreCoordinator$attach$1
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener
            public final void onAfterRenderList(List<ListEntry> list, NotifStackController notifStackController) {
                DataStoreCoordinator dataStoreCoordinator = DataStoreCoordinator.this;
                Objects.requireNonNull(dataStoreCoordinator);
                ArrayList arrayList = new ArrayList();
                for (ListEntry listEntry : list) {
                    if (listEntry instanceof NotificationEntry) {
                        arrayList.add(listEntry);
                    } else if (listEntry instanceof GroupEntry) {
                        GroupEntry groupEntry = (GroupEntry) listEntry;
                        Objects.requireNonNull(groupEntry);
                        NotificationEntry notificationEntry = groupEntry.mSummary;
                        if (notificationEntry != null) {
                            arrayList.add(notificationEntry);
                            arrayList.addAll(groupEntry.mUnmodifiableChildren);
                        } else {
                            throw new IllegalStateException(Intrinsics.stringPlus("No Summary: ", groupEntry).toString());
                        }
                    } else {
                        throw new IllegalStateException(Intrinsics.stringPlus("Unexpected entry ", listEntry).toString());
                    }
                }
                dataStoreCoordinator.notifLiveDataStoreImpl.setActiveNotifList(arrayList);
            }
        };
        Objects.requireNonNull(notifPipeline);
        RenderStageManager renderStageManager = notifPipeline.mRenderStageManager;
        Objects.requireNonNull(renderStageManager);
        renderStageManager.onAfterRenderListListeners.add(dataStoreCoordinator$attach$1);
    }

    public DataStoreCoordinator(NotifLiveDataStoreImpl notifLiveDataStoreImpl) {
        this.notifLiveDataStoreImpl = notifLiveDataStoreImpl;
    }
}
