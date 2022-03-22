package com.android.systemui.statusbar.notification.collection.render;

import android.os.Trace;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderEntryListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderGroupListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.FilteringSequence$iterator$1;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: RenderStageManager.kt */
/* loaded from: classes.dex */
public final class RenderStageManager {
    public NotifViewRenderer viewRenderer;
    public final ArrayList onAfterRenderListListeners = new ArrayList();
    public final ArrayList onAfterRenderGroupListeners = new ArrayList();
    public final ArrayList onAfterRenderEntryListeners = new ArrayList();

    public final void dispatchOnAfterRenderEntries(NotifViewRenderer notifViewRenderer, List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.dispatchOnAfterRenderEntries");
        try {
            if (!this.onAfterRenderEntryListeners.isEmpty()) {
                for (ListEntry listEntry : list) {
                    if (listEntry instanceof NotificationEntry) {
                        NotificationEntry notificationEntry = (NotificationEntry) listEntry;
                        NotifViewController rowController = notifViewRenderer.getRowController(notificationEntry);
                        Iterator it = this.onAfterRenderEntryListeners.iterator();
                        while (it.hasNext()) {
                            ((OnAfterRenderEntryListener) it.next()).onAfterRenderEntry(notificationEntry, rowController);
                        }
                    } else if (listEntry instanceof GroupEntry) {
                        GroupEntry groupEntry = (GroupEntry) listEntry;
                        Objects.requireNonNull(groupEntry);
                        NotificationEntry notificationEntry2 = groupEntry.mSummary;
                        if (notificationEntry2 != null) {
                            NotifViewController rowController2 = notifViewRenderer.getRowController(notificationEntry2);
                            Iterator it2 = this.onAfterRenderEntryListeners.iterator();
                            while (it2.hasNext()) {
                                ((OnAfterRenderEntryListener) it2.next()).onAfterRenderEntry(notificationEntry2, rowController2);
                            }
                            GroupEntry groupEntry2 = (GroupEntry) listEntry;
                            Objects.requireNonNull(groupEntry2);
                            for (NotificationEntry notificationEntry3 : groupEntry2.mUnmodifiableChildren) {
                                NotifViewController rowController3 = notifViewRenderer.getRowController(notificationEntry3);
                                Iterator it3 = this.onAfterRenderEntryListeners.iterator();
                                while (it3.hasNext()) {
                                    ((OnAfterRenderEntryListener) it3.next()).onAfterRenderEntry(notificationEntry3, rowController3);
                                }
                            }
                        } else {
                            throw new IllegalStateException(Intrinsics.stringPlus("No Summary: ", groupEntry).toString());
                        }
                    } else {
                        throw new IllegalStateException(Intrinsics.stringPlus("Unhandled entry: ", listEntry).toString());
                    }
                }
            }
        } finally {
            Trace.endSection();
        }
    }

    public final void dispatchOnAfterRenderGroups(NotifViewRenderer notifViewRenderer, List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.dispatchOnAfterRenderGroups");
        try {
            if (!this.onAfterRenderGroupListeners.isEmpty()) {
                FilteringSequence$iterator$1 filteringSequence$iterator$1 = new FilteringSequence$iterator$1(SequencesKt___SequencesKt.filter(new CollectionsKt___CollectionsKt$asSequence$$inlined$Sequence$1(list), RenderStageManager$dispatchOnAfterRenderGroups$lambda5$$inlined$filterIsInstance$1.INSTANCE));
                while (filteringSequence$iterator$1.hasNext()) {
                    GroupEntry groupEntry = (GroupEntry) filteringSequence$iterator$1.next();
                    NotifViewController groupController = notifViewRenderer.getGroupController(groupEntry);
                    Iterator it = this.onAfterRenderGroupListeners.iterator();
                    while (it.hasNext()) {
                        ((OnAfterRenderGroupListener) it.next()).onAfterRenderGroup(groupEntry, groupController);
                    }
                }
            }
        } finally {
            Trace.endSection();
        }
    }
}
