package com.android.systemui.statusbar.events;

import android.content.Context;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescer;
import com.android.systemui.statusbar.notification.collection.coalescer.GroupCoalescerLogger;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SystemEventChipAnimationController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider locationPublisherProvider;
    public final Provider statusBarWindowControllerProvider;

    public /* synthetic */ SystemEventChipAnimationController_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.statusBarWindowControllerProvider = provider2;
        this.locationPublisherProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new SystemEventChipAnimationController((Context) this.contextProvider.mo144get(), (StatusBarWindowController) this.statusBarWindowControllerProvider.mo144get(), (StatusBarLocationPublisher) this.locationPublisherProvider.mo144get());
            default:
                return new GroupCoalescer((DelayableExecutor) this.contextProvider.mo144get(), (SystemClock) this.statusBarWindowControllerProvider.mo144get(), (GroupCoalescerLogger) this.locationPublisherProvider.mo144get());
        }
    }
}
