package com.android.systemui.statusbar.notification.collection;

import android.os.Trace;
import com.android.systemui.util.Assert;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptyList;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function0;
/* compiled from: NotifLiveDataStoreImpl.kt */
/* loaded from: classes.dex */
public final class NotifLiveDataStoreImpl implements NotifLiveDataStore {
    public final NotifLiveDataImpl activeNotifCount;
    public final NotifLiveDataImpl<Integer> activeNotifCountPrivate;
    public final NotifLiveDataImpl activeNotifList;
    public final NotifLiveDataImpl<List<NotificationEntry>> activeNotifListPrivate;
    public final NotifLiveDataImpl hasActiveNotifs;
    public final NotifLiveDataImpl<Boolean> hasActiveNotifsPrivate;
    public final Executor mainExecutor;

    public final void setActiveNotifList(List<NotificationEntry> list) {
        Trace.beginSection("NotifLiveDataStore.setActiveNotifList");
        try {
            Assert.isMainThread();
            List<NotificationEntry> unmodifiableList = Collections.unmodifiableList(CollectionsKt___CollectionsKt.toList(list));
            Function0[] function0Arr = new Function0[3];
            boolean z = false;
            function0Arr[0] = this.activeNotifListPrivate.setValueAndProvideDispatcher(unmodifiableList);
            function0Arr[1] = this.activeNotifCountPrivate.setValueAndProvideDispatcher(Integer.valueOf(unmodifiableList.size()));
            NotifLiveDataImpl<Boolean> notifLiveDataImpl = this.hasActiveNotifsPrivate;
            if (!unmodifiableList.isEmpty()) {
                z = true;
            }
            function0Arr[2] = notifLiveDataImpl.setValueAndProvideDispatcher(Boolean.valueOf(z));
            for (Function0 function0 : SetsKt__SetsKt.listOf(function0Arr)) {
                function0.invoke();
            }
        } finally {
            Trace.endSection();
        }
    }

    public NotifLiveDataStoreImpl(Executor executor) {
        this.mainExecutor = executor;
        NotifLiveDataImpl<Boolean> notifLiveDataImpl = new NotifLiveDataImpl<>("hasActiveNotifs", Boolean.FALSE, executor);
        this.hasActiveNotifsPrivate = notifLiveDataImpl;
        NotifLiveDataImpl<Integer> notifLiveDataImpl2 = new NotifLiveDataImpl<>("activeNotifCount", 0, executor);
        this.activeNotifCountPrivate = notifLiveDataImpl2;
        NotifLiveDataImpl<List<NotificationEntry>> notifLiveDataImpl3 = new NotifLiveDataImpl<>("activeNotifList", EmptyList.INSTANCE, executor);
        this.activeNotifListPrivate = notifLiveDataImpl3;
        this.hasActiveNotifs = notifLiveDataImpl;
        this.activeNotifCount = notifLiveDataImpl2;
        this.activeNotifList = notifLiveDataImpl3;
    }

    @Override // com.android.systemui.statusbar.notification.collection.NotifLiveDataStore
    public final NotifLiveDataImpl getActiveNotifCount() {
        return this.activeNotifCount;
    }

    @Override // com.android.systemui.statusbar.notification.collection.NotifLiveDataStore
    public final NotifLiveDataImpl getActiveNotifList() {
        return this.activeNotifList;
    }

    @Override // com.android.systemui.statusbar.notification.collection.NotifLiveDataStore
    public final NotifLiveDataImpl getHasActiveNotifs() {
        return this.hasActiveNotifs;
    }
}
